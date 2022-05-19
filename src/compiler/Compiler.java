package compiler;

import java.nio.file.*;
import java.util.*;

public class Compiler
{
    private int memory_start_data;
    private int data_current;
    private int memory_start_code;
    private int code_current;
    public int get_memory_start_data(){return memory_start_data;}
    public int get_memory_start_code(){return memory_start_code;}
    private List<Line> l;
    //private StringBuilder transcript;
    
    //private static final String separator = "-----------------------------------------------------------------";

    public static class Transcript
    {
        StringBuilder labels;
        StringBuilder code;
        StringBuilder scrubbed_code;
        StringBuilder compilation;
        StringBuilder binary;
        public Transcript()
        {
            this.labels = new StringBuilder();
            this.code = new StringBuilder();
            this.compilation = new StringBuilder();
            this.scrubbed_code = new StringBuilder();
            this.binary = new StringBuilder();
        }
        public String get_labels()
        {
            return this.labels.toString();
        }
        public String get_scrubbed_code()
        {
            return this.scrubbed_code.toString();
        }
        public String get_binary()
        {
            return this.binary.toString();
        }
        public String get_compilation()
        {
            return this.compilation.toString();
        }
        public String get_code()
        {
            return this.code.toString();
        }
    }
    Transcript transcript;
    
    private static class Label
    {
        String name;
        int address;
        Label(String name, int address)
        {
            this.name = name;
            this.address = address;
        }
    }
    public int get_address_code(String name)
    {
        for (Label label : l_lc) if (label.name.equals(name)) return label.address;
        return -1;
    }
    public int get_address_data(String name)
    {
        for (Label label : l_ld) if (label.name.equals(name)) return label.address;
        return -1;
    }
    private List<Label> l_ld;
    private List<Label> l_lc;
    private static class Instruction
    {
        String contents;
        int address;
        String comment;
        Instruction(String contents, int address, String comment)
        {
            this.contents = contents;
            this.address = address;
            this.comment = comment;
        }
    }
    private List<Instruction> l_pc;
    
    private static class Line
    {
        String contents;
        int number;
    }
    
    private class Source_stream
    {
        private final StringBuilder stream;
        private final List<Integer> numbers;
        Source_stream()
        {
            stream = new StringBuilder();
            numbers = new ArrayList<>();
        }
        void append(String s, int n)
        {
            stream.append(s);
            for(int i=0;i<s.length();i++)numbers.add(n);
        }
        String stream()
        {
            return this.stream.toString();
        }
        int get_number(int i)
        {
            return numbers.get(i);
        }
        void log(StringBuilder s)
        {
            if(stream.length() == 0)return;
            s.append(numbers.get(0)).append("|");
            s.append((stream.charAt(0)));
            for(int i=1;i<stream.length();i++)
            {
                if(numbers.get(i-1)!=numbers.get(i)) s.append("\n").append(numbers.get(i)).append("|");
                s.append((stream.charAt(i)));
            }
        }
    }
    private Source_stream code_section;
    private Source_stream data_section;
    
    public Compiler(int memory_start_data, int memory_start_code)
    {
        set(memory_start_data, memory_start_code);
    }
    private void set(int memory_start_data, int memory_start_code)
    {
        this.transcript = new Transcript();
        this.memory_start_data = memory_start_data;
        this.memory_start_code = memory_start_code;
        this.data_current = memory_start_data;
        this.code_current = memory_start_code;
        this.l = new ArrayList<>();
        this.code_section = new Source_stream();
        this.data_section = new Source_stream();
        this.l_ld = new ArrayList<>();
        this.l_lc = new ArrayList<>();
        this.l_pc = new ArrayList<>();
    }
    private void check_clashing_labels()throws Exception
    {
        for(int i=0;i<l_lc.size();i++)
        {
            for(int j=i+1;j<l_lc.size();j++) // within code
            {
                if(l_lc.get(i).name.equals(l_lc.get(j).name))throw new Exception("Label name reused: "+l_lc.get(i).name+" (code section)");
            }
            for(int j=0;j<l_ld.size();j++)  // between code and data
            {
                if(l_lc.get(i).name.equals(l_ld.get(j).name))throw new Exception("Label name reused: "+l_lc.get(i).name+" (code and data section)");
            }
        }
        for(int i=0;i<l_ld.size();i++) //within data
        {
            for(int j=i+1;j<l_ld.size();j++)
            {
                if(l_ld.get(i).name.equals(l_ld.get(j).name))throw new Exception("Label name reused: "+l_ld.get(i).name+" (data section)");
            }
        }
    }
    public Transcript get_transcript()
    {
        return this.transcript;
    }

    public void compile(Path source, Path binary) throws Exception
    {
        List<String> l_raw = Files.readAllLines(source);
        this.l = new ArrayList<>();
        for(int i = 0;i< l_raw.size();i++)
        {
            Line li = new Line();
            li.number = i+1;
            li.contents = l_raw.get(i);
            l.add(li);
        }
        log(this.transcript.code);
        scrub();
        log(this.transcript.scrubbed_code);
        locate_blocs();
        process_data(this.transcript.compilation);
        process_code(this.transcript.compilation);
        this.transcript.labels.append("\ndata labels:\n");
        for (Label label : l_ld) {
            this.transcript.labels.append("\n").append(label.name).append(" refers to data location: ").append(label.address);
        }
        this.transcript.labels.append("\ncode labels:\n");
        for (Label label : l_lc) {
            this.transcript.labels.append("\n").append(label.name).append(" refers to PC location: ").append(label.address);
        }
        this.transcript.binary.append("\nPC\t|              code              |purpose");

        for (Instruction instruction : l_pc) {
            this.transcript.binary.append("\n").append(instruction.address).append("\t|").append(instruction.contents).append("|").append(instruction.comment);
        }
        //writing binary file
        byte[] b_array = new byte[l_pc.size()*4];
        for(int i=0;i<l_pc.size();i++)
        {
            for(int j=0;j<4;j++)
            {
                String s = l_pc.get(i).contents.substring(8*j,8*(j+1));
                byte b = 0;
                for(int k=0;k<8;k++)if(s.charAt(k)=='1')b=(byte)(b|1<<(7-k));
                b_array[4*i+j]=b;
            }
        }
        Files.write(binary, b_array);
    }
    public static String foo(byte b)
    {
        String s = "";
        for(int i=7;i>=0;i--)
        {
            int n = (1<<i)&((int)b);
            if(n==0)s+='0';
            else s+='1';
        }
        return s;
    }
    private void scrub()
    {
        remove_multi_line_comments();
        remove_single_line_comments();
        replace_tab_with_space();
        trim();
        remove_empty_lines();
    }
    private void remove_multi_line_comments()
    {
        boolean f = false;
        for(int i = 0; i<l.size(); i++)
        {
            String c = l.get(i).contents;
            int start = c.indexOf(Syntax.MULTI_LINE_START);
            int end = c.indexOf(Syntax.MULTI_LINE_END);
            int size_start = Syntax.MULTI_LINE_START.length();
            int size_end = Syntax.MULTI_LINE_END.length();
            if(f && end==-1)
            {
                l.get(i).contents = "";
            }
            if(start == -1 && end == -1)
            {
                continue;   
            }
            else if(start != -1 && end == -1)
            {
                f = true;
                l.get(i).contents = c.substring(0,start);
            }
            else if(start == -1 && end != -1)
            {
                f = false;
                l.get(i).contents = c.substring(end +size_end);
            }
            else
            {
                int next_end = c.indexOf(Syntax.MULTI_LINE_END, start);
                if(next_end != -1)
                {
                    l.get(i).contents = c.substring(0,start)+c.substring(end+size_end);
                    i--;
                }
                else
                {
                    f=true;
                    l.get(i).contents = c.substring(end +size_end,start);
                }
            }
        }
    }
    private void remove_single_line_comments()
    {
        for (Line line : l) {
            String c = line.contents;
            int start = c.indexOf(Syntax.SINGLE_LINE);
            if (start != -1) {
                line.contents = c.substring(0, start);
            }
        }
    }
    private void remove_empty_lines()
    {
        for(int i=0;i<l.size();i++)
        {
            if(l.get(i).contents.equals(""))
            {
                l.remove(i);
                i--;
            }
        }
    }
    private void replace_tab_with_space()
    {
        for (Line line : l) {
            StringBuilder s = new StringBuilder();
            String c = line.contents;
            for (int j = 0; j < c.length(); j++) {
                char d = c.charAt(j);
                if (d == '\t') s.append(Syntax.WHITESPACE);
                else s.append(d);
            }
            line.contents = s.toString();
        }
    }
    private void trim()
    {
        for (Line line : l) {
            String c = line.contents;
            c = c.trim();
            line.contents = c;
        }
    }
    public void log(StringBuilder transcript)
    {
        for (Line line : l) {
            transcript.append("\n").append(line.number).append("\t|").append(line.contents).append("|");
        }
    }
    private void locate_blocs() throws Exception
    {
        Source_stream source_full = new Source_stream();
        for (Line line : l) {
            source_full.append(line.contents + Syntax.WHITESPACE, line.number);
        }
        
        int data_start = Syntax.DATA.start_of_first_instance_in(source_full.stream());
        int data_end = Syntax.DATA.end_of_first_instance_in(source_full.stream());
        int code_start = Syntax.CODE.start_of_first_instance_in(source_full.stream());
        int code_end = Syntax.CODE.end_of_first_instance_in(source_full.stream());
        if(code_start == -1)throw new Exception("missing code");
        if(data_start == -1)//no data segment
        {
            for(int i=code_end;i<source_full.stream.length();i++)code_section.append(source_full.stream.charAt(i)+"",source_full.numbers.get(i));
        }
        else
        {
            if(data_start < code_start)//data section before code section
            {
                for(int i=data_end;i<code_start;i++)data_section.append(source_full.stream.charAt(i)+"",source_full.numbers.get(i));
                for(int i=code_end;i<source_full.stream.length();i++)code_section.append(source_full.stream.charAt(i)+"",source_full.numbers.get(i));
            }
            else if(code_start < data_start)// code section before data section
            {
                for(int i=code_end;i<data_start;i++)code_section.append(source_full.stream.charAt(i)+"",source_full.numbers.get(i));
                for(int i=data_end;i<source_full.stream.length();i++)data_section.append(source_full.stream.charAt(i)+"",source_full.numbers.get(i));
            }
            else throw new Exception("Coinciding section error: unable to tell data and code apart");
        }
    }
    private void process_data(StringBuilder transcript) throws Exception
    {
        transcript.append("\n"+"data section: ");
        if(data_section.stream.length()==0)return;
            Parser sc = new Parser(data_section.stream());
            while(sc.hasNext())
            {
                String t = sc.next();
                transcript.append("\n>").append(t);
                if(Syntax.is_label(t))
                {
                    if(!Syntax.is_valid_label(t))throw new Exception("Incorrect identifier '"+t.substring(0,t.length()-1)+"' for label in line "+data_section.get_number(sc.start()));
                    transcript.append("\n" + "label identified: ").append(t);
                    this.l_ld.add(new Label(t, data_current));
                }
                else if(Syntax.is_data_type(t))
                {
                    String type = t.substring(1);// leading . is removed
                    if(Syntax.WORD.contains(type))
                    {
                        if(data_current%4!=0)throw new Exception("misaligned memory in line"+data_section.get_number(sc.start()));
                        transcript.append("\n").append(Syntax.WORD.words[0]).append(" allocated");
                        if(sc.hasNextLong())
                        {
                            long initial_value = sc.nextLong();
                            l_pc.add(new Instruction(Binary.addi(0, 5, initial_value), code_current, Syntax.WORD.words[0] + " " + Syntax.ADDI.words[0]));
                            code_current+=4;
                            l_pc.add(new Instruction(Binary.sw(5, 0, data_current), code_current, Syntax.WORD.words[0] + " " + Syntax.SW.words[0]));
                            code_current+=4;
                            l_pc.add(new Instruction(Binary.andi(0, 5, 0), code_current, Syntax.WORD.words[0] + " " + Syntax.ANDI.words[0]));
                            code_current+=4;
                            transcript.append("\n" + "value initialized as: ").append(initial_value);
                        }
                        data_current+=4;
                    }
                    else if(Syntax.BYTE.contains(type))
                    {
                        //memory is always aligned for BYTE since it is byte addressable memory
                        transcript.append("\n").append(Syntax.BYTE.words[0]).append(" allocated");
                        if(sc.hasNextLong())
                        {
                            long initial_value = sc.nextLong();
                            l_pc.add(new Instruction(Binary.addi(0, 5, initial_value), code_current, Syntax.BYTE.words[0] + " " + Syntax.ADDI.words[0]));
                            code_current+=4;
                            l_pc.add(new Instruction(Binary.sb(5, 0, data_current), code_current, Syntax.BYTE.words[0] + " " + Syntax.SB.words[0]));
                            code_current+=4;
                            l_pc.add(new Instruction(Binary.andi(0, 5, 0), code_current, Syntax.BYTE.words[0] + " " + Syntax.ANDI.words[0]));
                            code_current+=4;
                            transcript.append("\n" + "value initialized as: ").append(initial_value);
                        }
                        data_current+=1;
                    }
                    else if(Syntax.SHORT.contains(type))
                    {
                        if(data_current%2!=0)throw new Exception("misaligned memory in line "+data_section.get_number(sc.start()));
                        transcript.append("\n").append(Syntax.SHORT.words[0]).append(" allocated");
                        if(sc.hasNextLong())
                        {
                            long initial_value = sc.nextLong();
                            l_pc.add(new Instruction(Binary.addi(0, 5, initial_value), code_current, Syntax.SHORT.words[0] + " " + Syntax.ADDI.words[0]));
                            code_current+=4;
                            l_pc.add(new Instruction(Binary.sh(5, 0, data_current), code_current, Syntax.SHORT.words[0] + " " + Syntax.SH.words[0]));
                            code_current+=4;
                            l_pc.add(new Instruction(Binary.andi(0, 5, 0), code_current, Syntax.SHORT.words[0] + " " + Syntax.ANDI.words[0]));
                            code_current+=4;
                            transcript.append("\n" + "value initialized as: ").append(initial_value);
                        }
                        data_current+=2;
                    }
                    else if(Syntax.SPACE.contains(type))
                    {
                        try
                        {
                            int n = (int)sc.nextLong();
                            data_current+=n;
                            transcript.append("\n").append(n).append(" bytes of space in memory allocated");
                        }
                        catch(Exception e)
                        {
                            throw new Exception(e.getMessage()+"\nliteral missing in line "+data_section.get_number(sc.start()));
                        }
                    }
                    else if(Syntax.ALIGN.contains(type))
                    {
                        try
                        {
                            int n = (int)sc.nextLong();
                            int p = (int)Math.pow(2,n);
                            while(data_current%p!=0)data_current++;
                            transcript.append("\n" + "data pointer aligned to nearest multiple of 2^").append(n);
                        }
                        catch(Exception e)
                        {
                            throw new Exception(e.getMessage()+"\nliteral missing in line "+data_section.get_number(sc.start()));
                        }
                    }
                    else if(Syntax.ASCII.contains(type)||Syntax.ASCIIZ.contains(type))
                    {
                        try
                        {
                            String s = sc.next();
                            System.out.println("input string"+"|"+s+"|");
                            String value = Parser.processString(s);
                            if(Syntax.ASCIIZ.contains(type))value+='\u0000';
                            int n = value.length();
                            for(int i=0;i<n;i++)
                            {
                                char d = value.charAt(i);
                                l_pc.add(new Instruction(Binary.addi(0, 5, d), code_current, (Syntax.is_printable_ASCII(d)?("'"+d+"'"):(""+((int)d))) + " " + Syntax.ADDI.words[0]));
                                code_current+=4;
                                l_pc.add(new Instruction(Binary.sb(5, 0, data_current), code_current, (Syntax.is_printable_ASCII(d)?("'"+d+"'"):(""+((int)d))) + " " + Syntax.SB.words[0]));
                                code_current+=4;
                                l_pc.add(new Instruction(Binary.andi(0, 5, 0), code_current, (Syntax.is_printable_ASCII(d)?("'"+d+"'"):(""+((int)d))) + " " + Syntax.ANDI.words[0]));
                                code_current+=4;
                                data_current+=1;
                            }
                            transcript.append("\n" + "space allocated and string initialized as: ").append(s);
                            if(Syntax.ASCIIZ.contains(type))transcript.append(" with null character termination");
                        }
                        catch(Exception e)
                        {
                            throw new Exception(e.getMessage()+"\nstring not rendered correctly in line "+data_section.get_number(sc.start()));
                        }
                    }
                    else throw new Exception("unrecognized type in line "+data_section.get_number(sc.start()));
                }
                else throw new Exception("Unrecognized token in line "+data_section.get_number(sc.start()));
            }
        transcript.append("\n");
    }
    private void identify_code_labels(StringBuilder transcript) throws Exception
    {
        Parser sc = new Parser(code_section.stream());
        int PC = code_current;
        while(sc.hasNext())
        {
            String token = sc.next();
            if(Syntax.is_command(token))
            {
                PC+=Syntax.get_n_of_command(token)*4;
            }
            else if(Syntax.is_label(token))
            {
                if(!Syntax.is_valid_label(token))throw new Exception("Incorrect identifier for label in line "+code_section.get_number(sc.start()));
                l_lc.add(new Label(token.substring(0, token.length() - 1), PC));
                transcript.append("\n" + "label identified: ").append(token);
            }
        }
    }
    private void jump_to_main(StringBuilder transcript) throws Exception {
        boolean main_present = false;
        for (Label item : l_lc) {
            if (Syntax.MAIN.contains(item.name)) {
                main_present = true;
                for(Label item_ : l_lc)item_.address+=4; //inserting jump to main requires all other code labels to shift up
                l_pc.add(new Instruction(Binary.jal(1, item.address), code_current, Syntax.JAL.words[0] + " " + Syntax.MAIN.words[0]));
                code_current+=4;
            }
        }
        if(!main_present)transcript.append("\nWARNING: no main label found");
    }
    private long process_immediate(String imm,Parser sc,StringBuilder transcript) throws Exception
    {
        long value;
        try
        {
            value = Parser.parseLong(imm);
            transcript.append("\nimmediate value: ").append(imm);
        }
        catch(Exception e)
        {
            transcript.append("\n" + "target label: ").append(imm);
            int value_c = get_address_code(imm);
            int value_d = get_address_data(imm);
            if(value_c == -1 && value_d == -1)throw new Exception("unidentified label '"+imm+"' in line "+code_section.numbers.get(sc.start()));
            else if(value_c == -1)
            {
                transcript.append(" (data)");
                value = value_d;
            }
            else if(value_d == -1)
            {
                transcript.append(" (code)");
                value = value_c;
            }
            else throw new Exception("multiple labels matching in line "+code_section.numbers.get(sc.start()));//should be unreachable
        }
        return value;
    }
    private int process_register(String r, Parser sc, String desc, StringBuilder transcript) throws  Exception
    {
        transcript.append("\n"+desc+" register: ").append(r);
        int r_add = Syntax.address_of_register(r);
        if(r_add == -1)throw new Exception("register '"+r+"' in line "+code_section.numbers.get(sc.start())+" does not exist");
        return r_add;
    }
    private void process_code(StringBuilder transcript) throws Exception
    {
        transcript.append("\n"+"code section: ");
        identify_code_labels(transcript);
        check_clashing_labels();
        jump_to_main(transcript);
        Parser sc = new Parser(code_section.stream());
        while(sc.hasNext())
        {
            String token = sc.next();
            transcript.append("\n").append(">").append(token);
            if(Syntax.is_command(token))
            {
                //command cannot be followed by ,
                if(sc.is_next_argument())throw new Exception("unexpected '"+Syntax.ARGUMENT_SEPARATOR+"' in line "+code_section.numbers.get(sc.start()));
                if(Syntax.get_input_type_of_command(token)==Syntax.RRR)
                {
                    try
                    {
                        int dest_add = process_register(sc.next(),sc,"destination",transcript); // destination
                        if(!sc.is_next_argument())throw new Exception("missing argument in line "+code_section.numbers.get(sc.start()));
                        int src1_add = process_register(sc.next(),sc,"first source",transcript); // source 1
                        if(!sc.is_next_argument())throw new Exception("missing argument in line "+code_section.numbers.get(sc.start()));
                        int src2_add = process_register(sc.next(),sc,"second source",transcript); //source 2
                        if(sc.is_next_argument())throw new Exception("too many arguments for "+token+" in line "+code_section.numbers.get(sc.start()));
                        // add sub and or xor sll (sla) srl sra slt sltu sgt sgtu
                        if(Syntax.ADD.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.add(src1_add, src2_add, dest_add), code_current, Syntax.ADD.words[0]));
                        }
                        else if(Syntax.SUB.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sub(src1_add, src2_add, dest_add), code_current, Syntax.SUB.words[0]));
                        }
                        else if(Syntax.AND.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.and(src1_add, src2_add, dest_add), code_current, Syntax.AND.words[0]));
                        }
                        else if(Syntax.OR.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.or(src1_add, src2_add, dest_add), code_current, Syntax.OR.words[0]));
                        }
                        else if(Syntax.XOR.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.xor(src1_add, src2_add, dest_add), code_current, Syntax.XOR.words[0]));
                        }
                        else if(Syntax.SLL.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sll(src1_add, src2_add, dest_add), code_current, Syntax.SLL.words[0]));
                        }
                        else if(Syntax.SRL.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.srl(src1_add, src2_add, dest_add), code_current, Syntax.SRL.words[0]));
                        }
                        else if(Syntax.SRA.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sra(src1_add, src2_add, dest_add), code_current, Syntax.SRA.words[0]));
                        }
                        else if(Syntax.SLT.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.slt(src1_add, src2_add, dest_add), code_current, Syntax.SLT.words[0]));
                        }
                        else if(Syntax.SLTU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sltu(src1_add, src2_add, dest_add), code_current, Syntax.SLTU.words[0]));
                        }
                        else if(Syntax.SGT.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.slt(src2_add, src1_add, dest_add), code_current, Syntax.SGT.words[0]+" "+Syntax.SLT.words[0]));
                        }
                        else if(Syntax.SGTU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sltu(src2_add, src1_add, dest_add), code_current, Syntax.SGTU.words[0]+" "+Syntax.SLTU.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.RRi)
                {
                    try
                    {
                        int src1_add = process_register(sc.next(),sc,"first source",transcript);
                        if(!sc.is_next_argument())throw new Exception("missing argument in line "+code_section.numbers.get(sc.start()));
                        int src2_add = process_register(sc.next(),sc,"second source",transcript);
                        if(!sc.is_next_argument())throw new Exception("missing argument in line "+code_section.numbers.get(sc.start()));
                        long value = process_immediate(sc.next(),sc,transcript);
                        if(sc.is_next_argument())throw new Exception("too many arguments for "+token+" in line "+code_section.numbers.get(sc.start()));
                        // (addi subi xori ori andi) (slli slri srai (slai)) (slti sltiu) (beq bne blt bge bltu bgeu ble bgt bleu bgtu)
                        if(Syntax.ADDI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.addi(src2_add, src1_add, value), code_current, Syntax.ADDI.words[0]));
                        }
                        else if(Syntax.SUBI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.addi(src2_add, src1_add, -value), code_current, Syntax.SUBI.words[0]+" "+Syntax.ADDI.words[0]));
                        }
                        else if(Syntax.ANDI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.andi(src2_add, src1_add, value), code_current, Syntax.ANDI.words[0]));
                        }
                        else if(Syntax.ORI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.ori(src2_add, src1_add, value), code_current, Syntax.ORI.words[0]));
                        }
                        else if(Syntax.XORI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.xori(src2_add, src1_add, value), code_current, Syntax.XORI.words[0]));
                        }
                        else if(Syntax.SLTI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.slti(src2_add, src1_add, value), code_current, Syntax.SLTI.words[0]));
                        }
                        else if(Syntax.SLTIU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sltiu(src2_add, src1_add, value), code_current, Syntax.SLTIU.words[0]));
                        }
                        else if(Syntax.SLLI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.slli(src2_add, src1_add, value), code_current, Syntax.SLLI.words[0]));
                        }
                        else if(Syntax.SRLI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.srli(src2_add, src1_add, value), code_current, Syntax.SRLI.words[0]));
                        }
                        else if(Syntax.SRAI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.srai(src2_add, src1_add, value), code_current, Syntax.SRAI.words[0]));
                        }
                        else if(Syntax.BEQ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.beq(src1_add, src2_add, value-code_current), code_current, Syntax.BEQ.words[0]));
                        }
                        else if(Syntax.BNE.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bne(src1_add, src2_add, value-code_current), code_current, Syntax.BNE.words[0]));
                        }
                        else if(Syntax.BLT.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.blt(src1_add, src2_add, value-code_current), code_current, Syntax.BLT.words[0]));
                        }
                        else if(Syntax.BLTU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bltu(src1_add, src2_add, value-code_current), code_current, Syntax.BLTU.words[0]));
                        }
                        else if(Syntax.BGE.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bge(src1_add, src2_add, value-code_current), code_current, Syntax.BGE.words[0]));
                        }
                        else if(Syntax.BGEU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bgeu(src1_add, src2_add, value-code_current), code_current, Syntax.BGEU.words[0]));
                        }
                        else if(Syntax.BLE.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bge(src2_add, src1_add, value-code_current), code_current, Syntax.BLE.words[0]+" "+Syntax.BGE.words[0]));
                        }
                        else if(Syntax.BGT.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.blt(src2_add, src1_add, value-code_current), code_current, Syntax.BGT.words[0]+" "+Syntax.BLT.words[0]));
                        }
                        else if(Syntax.BLEU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bgeu(src2_add, src1_add, value-code_current), code_current, Syntax.BLEU.words[0]+" "+Syntax.BGEU.words[0]));
                        }
                        else if(Syntax.BGTU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bltu(src2_add, src1_add, value-code_current), code_current, Syntax.BGTU.words[0]+" "+Syntax.BLTU.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.Ri_R_)
                {
                    try
                    {
                        int r_c_add = process_register(sc.next(),sc,"destination",transcript); //compulsory register
                        if(!sc.is_next_argument())throw new Exception("missing argument in line "+code_section.numbers.get(sc.start()));
                        String next = sc.next();
                        long immediate;
                        int r_o_add = 0; //optional register
                        if(sc.is_next_argument())throw new Exception("too many arguments for "+token+" in line "+code_section.numbers.get(sc.start()));
                        if(next.contains(Syntax.IMMEDIATE_CLOSE))
                        {
                            immediate = process_immediate(next.substring(0,next.indexOf(Syntax.IMMEDIATE_OPEN)),sc,transcript);
                            r_o_add = process_register(next.substring(next.indexOf(Syntax.IMMEDIATE_OPEN)+1,next.indexOf(Syntax.IMMEDIATE_CLOSE)),sc,"source",transcript);
                        }
                        else
                        {
                            immediate = process_immediate(next,sc,transcript);
                        }
                        // (lw lb lh lhu lbu) (sw sb sh)
                        if(Syntax.LW.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.lw(r_o_add,r_c_add,immediate), code_current, Syntax.LW.words[0]));
                        }
                        else if(Syntax.LB.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.lb(r_o_add,r_c_add,immediate), code_current, Syntax.LB.words[0]));
                        }
                        else if(Syntax.LH.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.lh(r_o_add, r_c_add,immediate), code_current, Syntax.LH.words[0]));
                        }
                        else if(Syntax.LBU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.lbu(r_o_add, r_c_add,immediate), code_current, Syntax.LBU.words[0]));
                        }
                        else if(Syntax.LHU.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.lhu(r_o_add, r_c_add,immediate), code_current, Syntax.LHU.words[0]));
                        }
                        else if(Syntax.SW.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sw(r_o_add, r_c_add, immediate), code_current, Syntax.SW.words[0]));
                        }
                        else if(Syntax.SB.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sw(r_o_add, r_c_add, immediate), code_current, Syntax.SB.words[0]));
                        }
                        else if(Syntax.SH.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sw(r_o_add, r_c_add, immediate), code_current, Syntax.SH.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        throw new Exception(e.getMessage()+"\nimproper arguments in line"+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.RR)
                {
                    try
                    {
                        int dest_add = process_register(sc.next(),sc,"destination",transcript); // destination
                        if(!sc.is_next_argument())throw new Exception("missing argument in line "+code_section.numbers.get(sc.start()));
                        int src_add = process_register(sc.next(),sc,"source",transcript); // source
                        if(sc.is_next_argument())throw new Exception("too many arguments for "+token+" in line "+code_section.numbers.get(sc.start()));
                        // (inc dec) (mv swp) (not neg) (seqz snez sltz sgtz)
                        if(Syntax.INC.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.add(src_add, dest_add, dest_add), code_current,Syntax.INC.words[0]+" "+Syntax.ADD.words[0]));
                        }
                        else if(Syntax.DEC.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sub(src_add, dest_add, dest_add), code_current,Syntax.DEC.words[0]+" "+Syntax.SUB.words[0]));
                        }
                        else if(Syntax.MV.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.add(src_add, 0, dest_add), code_current,Syntax.MV.words[0]+" "+Syntax.ADD.words[0]));
                        }
                        else if(Syntax.NOT.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.xori(src_add, dest_add, -1), code_current,Syntax.NOT.words[0]+" "+Syntax.XORI.words[0]));
                        }
                        else if(Syntax.NEG.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sub(0, src_add, dest_add), code_current,Syntax.NEG.words[0]+" "+Syntax.SUB.words[0]));
                        }
                        else if(Syntax.SEQZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sltiu(src_add, dest_add, 1), code_current,Syntax.SEQZ.words[0]+" "+Syntax.SLTIU.words[0]));
                        }
                        else if(Syntax.SNEZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.sltu(0, src_add, dest_add), code_current,Syntax.SNEZ.words[0]+" "+Syntax.SLTU.words[0]));
                        }
                        else if(Syntax.SLTZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.slt(src_add, 0, dest_add), code_current,Syntax.SLTZ.words[0]+" "+Syntax.SLT.words[0]));
                        }
                        else if(Syntax.SGTZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.slt(0, src_add, dest_add), code_current,Syntax.SGTZ.words[0]+" "+Syntax.SLT.words[0]));
                        }
                        else if(Syntax.SWP.contains(token))
                        {
                            /* src=a, dest = b, * is bitwise xor
                            src = src * dest        //src = a*b
                            dest = dest * src       //dest=b*(a*b)=a
                            src = src * dest        //src=(a*b)*a=b
                             */
                            l_pc.add(new Instruction(Binary.xor(dest_add, src_add, src_add), code_current,Syntax.SWP.words[0]+" "+Syntax.XOR.words[0]));
                            l_pc.add(new Instruction(Binary.xor(src_add, dest_add, dest_add), code_current+4,Syntax.SWP.words[0]+" "+Syntax.XOR.words[0]));
                            l_pc.add(new Instruction(Binary.xor(dest_add, src_add, src_add), code_current+8,Syntax.SWP.words[0]+" "+Syntax.XOR.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.Ri)
                {
                    try
                    {
                        int src_add = process_register(sc.next(),sc,"",transcript);
                        if(!sc.is_next_argument())throw new Exception("missing argument in line "+code_section.numbers.get(sc.start()));
                        long value = process_immediate(sc.next(),sc,transcript);
                        if(sc.is_next_argument())throw new Exception("too many arguments for "+token+" in line "+code_section.numbers.get(sc.start()));
                        // (inci deci) li (beqz bnez bltz bgez blez bgtz) (lui auipc)
                        if(Syntax.LI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.addi(0,src_add,value), code_current,Syntax.LI.words[0]+" "+Syntax.ADDI.words[0]));
                        }
                        else if(Syntax.INCI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.addi(src_add,src_add,value), code_current,Syntax.INCI.words[0]+" "+Syntax.ADDI.words[0]));
                        }
                        else if(Syntax.DECI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.addi(src_add,src_add,-value), code_current,Syntax.DECI.words[0]+" "+Syntax.ADDI.words[0]));
                        }
                        else if(Syntax.BEQZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.beq(0,src_add,value-code_current), code_current,Syntax.BEQZ.words[0]+" "+Syntax.BEQ.words[0]));
                        }
                        else if(Syntax.BNEZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bne(0,src_add,value-code_current), code_current,Syntax.BNEZ.words[0]+" "+Syntax.BNE.words[0]));
                        }
                        else if(Syntax.BLTZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.blt(src_add,0,value-code_current), code_current,Syntax.BLTZ.words[0]+" "+Syntax.BLT.words[0]));
                        }
                        else if(Syntax.BGTZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.blt(0,src_add,value-code_current), code_current,Syntax.BGTZ.words[0]+" "+Syntax.BLT.words[0]));
                        }
                        else if(Syntax.BLEZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bge(0,src_add,value-code_current), code_current,Syntax.BLEZ.words[0]+" "+Syntax.BGE.words[0]));
                        }
                        else if(Syntax.BGEZ.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.bge(src_add,0,value-code_current), code_current,Syntax.BGEZ.words[0]+" "+Syntax.BGE.words[0]));
                        }
                        else if(Syntax.LUI.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.lui(src_add,value), code_current,Syntax.LUI.words[0]));
                        }
                        else if(Syntax.AUIPC.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.auipc(src_add,value), code_current,Syntax.AUIPC.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        transcript.append("\nERROR: ").append(e.getMessage());
                        throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.R)
                {
                    try
                    {
                        int r_add = process_register(sc.next(),sc,"",transcript);
                        if(sc.is_next_argument())throw new Exception("too many arguments for "+token+" in line "+code_section.numbers.get(sc.start()));
                        // clr noti jr
                        if(Syntax.CLR.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.add(0,0,r_add), code_current,Syntax.CLR.words[0]+" "+Syntax.AND.words[0]));
                        }
                        else if(Syntax.NOTI.contains(token))  // feeding in -1 makes every bit of value 1, a xor 1 = ~a
                        {
                            l_pc.add(new Instruction(Binary.xori(r_add,r_add,-1), code_current,Syntax.NOTI.words[0]+" "+Syntax.XORI.words[0]));
                        }
                        else if(Syntax.JR.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.jalr(r_add,0,0), code_current,Syntax.JR.words[0]+" "+Syntax.JALR.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.i)
                {
                    try
                    {
                        long value = process_immediate(sc.next(),sc,transcript);
                        if(sc.is_next_argument())throw new Exception("too many arguments for "+token+" in line "+code_section.numbers.get(sc.start()));
                        if(Syntax.J.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.jal(0,value-code_current), code_current,Syntax.J.words[0]+" "+Syntax.JAL.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.$)
                {
                    try
                    {
                        // nop ret
                        if(Syntax.NOP.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.addi(0,0,0), code_current,Syntax.NOP.words[0]+" "+Syntax.ADDI.words[0]));
                        }
                        else if(Syntax.RET.contains(token))
                        {
                            l_pc.add(new Instruction(Binary.jalr(1,0,0), code_current,Syntax.RET.words[0]+" "+Syntax.JALR.words[0]));
                        }
                        else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                        code_current+=4*Syntax.get_n_of_command(token);
                    }
                    catch(Exception e)
                    {
                        throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                    }
                }
                else if(Syntax.get_input_type_of_command(token)==Syntax.POLY)
                {
                    if(Syntax.JAL.contains(token))
                    {
                        // r,i or i
                        try
                        {
                            int dest_add = 1;  // $ra by default
                            long immediate;
                            String arg1 = sc.next();
                            if(sc.is_next_argument())
                            {
                                String arg2 = sc.next();
                                dest_add = process_register(arg1,sc,"destination",transcript);
                                immediate = process_immediate(arg2,sc,transcript);
                            }
                            else
                            {
                                immediate = process_immediate(arg1,sc,transcript);
                            }
                            l_pc.add(new Instruction(Binary.jal(dest_add,immediate-code_current), code_current,Syntax.JAL.words[0]));
                            code_current+=4*Syntax.get_n_of_command(token);
                        }
                        catch(Exception e)
                        {
                            throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                        }
                    }
                    else if(Syntax.JALR.contains(token))
                    {
                        // r or r,i(r)
                        try
                        {
                            int dest_add = 1; // $ra by default
                            int src_add = 0;
                            long immediate = 0;
                            int r_c_add = process_register(sc.next(),sc,"first",transcript);
                            if(sc.is_next_argument())
                            {
                                String next = sc.next();
                                if(next.contains(Syntax.IMMEDIATE_CLOSE))
                                {
                                    immediate = process_immediate(next.substring(0,next.indexOf(Syntax.IMMEDIATE_OPEN)),sc,transcript);
                                    src_add = process_register(next.substring(next.indexOf(Syntax.IMMEDIATE_OPEN)+1,next.indexOf(Syntax.IMMEDIATE_CLOSE)),sc,"source",transcript);
                                }
                                else
                                {
                                    immediate = process_immediate(next,sc,transcript);
                                }
                            }
                            else
                            {
                                src_add = r_c_add;
                            }
                            l_pc.add(new Instruction(Binary.jalr(src_add,dest_add,immediate-code_current), code_current,Syntax.JALR.words[0]));
                            code_current+=4*Syntax.get_n_of_command(token);
                        }
                        catch(Exception e)
                        {
                            throw new Exception(e.getMessage()+"\nimproper arguments in line "+code_section.numbers.get(sc.start()));
                        }
                    }
                    else throw new Exception("command type mismatch in line "+code_section.numbers.get(sc.start()));
                    code_current+=4*Syntax.get_n_of_command(token);
                }
            }
            else if(Syntax.is_label(token))transcript.append("\ncode label");
            else throw new Exception("unrecognized token in line "+code_section.numbers.get(sc.start()));
        }
    }

}
