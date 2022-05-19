package compiler;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Syntax
{
    //keywords
    static class Keyword
    {
        public String[] words;
        public Keyword(String[] w)
        {
            words = w;
        }
        public boolean contains(String token)
        {
            for (String word : words) if (word.equals(token)) return true;
            return false;
        }
        public int start_of_first_instance_in(String line)
        {
            int m_id = -1;
            for (String word : words) {
                int id = line.indexOf(word);
                if (id != -1) {
                    if (m_id == -1) m_id = id;
                    else if (m_id > id) m_id = id;
                }
            }
            return m_id;
        }
        public int end_of_first_instance_in(String line)
        {
            int m_id = -1;
            int length = 0;
            for (String word : words) {
                int id = line.indexOf(word);
                if (id != -1) {
                    if (length == 0 || m_id > id) {
                        length = word.length();
                        m_id = id;
                    }
                }
            }
            return m_id+length;
        }
    }
    //
    public static final String WHITESPACE = " ";
    public static final String MULTI_LINE_START = "#*";
    public static final String MULTI_LINE_END = "*#";
    public static final String SINGLE_LINE = "#";
    public static final String LABEL_TERMINATOR = ":";
    public static final String DATATYPE_INITIATOR = ".";
    public static final String ARGUMENT_SEPARATOR = ",";
    public static final String IMMEDIATE_OPEN = "(";
    public static final String IMMEDIATE_CLOSE = ")";
    public static final String IMMEDIATE_SEPARATOR = "_";
    //
    public static final Keyword DATA = new Keyword(new String[]{DATATYPE_INITIATOR+"data"});
    public static final Keyword CODE = new Keyword(new String[]{DATATYPE_INITIATOR+"code",DATATYPE_INITIATOR+"text"});
    public static final Keyword MAIN = new Keyword(new String[]{"main"});
    //datatypes
    public static final Keyword WORD = new Keyword(new String[]{"word","int"});
    public static final Keyword SHORT = new Keyword(new String[]{"half","short"});
    public static final Keyword BYTE = new Keyword(new String[]{"byte","quarter"});
    public static final Keyword SPACE = new Keyword(new String[]{"space"});
    public static final Keyword ALIGN = new Keyword(new String[]{"align"});
    public static final Keyword ASCII = new Keyword(new String[]{"ascii"});
    public static final Keyword ASCIIZ = new Keyword(new String[]{"asciiz"});
    //command types
    public static final char R_TYPE = 'R';
    public static final char I_TYPE = 'I';
    public static final char S_TYPE = 'S';
    public static final char J_TYPE = 'J';
    public static final char U_TYPE = 'U';
    public static final char B_TYPE = 'B';
    public static final char PSEUDO_TYPE = 'P';
    //input types
    public static final char RRR = 0; // three registers
    public static final char RR = 1;  // two registers
    public static final char R = 2;   // single register
    public static final char RRi = 3; // two registers and one value
    public static final char Ri_R_ = 4; // one compulsory register, one compulsory value and one optional register
    public static final char Ri = 5; // one register and one value
    public static final char i = 6;  // one value
    public static final char $ = 7;  // no input
    public static final char POLY = 8;  // polymorphic commands
    //commands
    public static final Keyword ADD = new Keyword(new String[]{"add"});
    public static final Keyword SUB = new Keyword(new String[]{"sub"});
    public static final Keyword AND = new Keyword(new String[]{"and"});
    public static final Keyword OR = new Keyword(new String[]{"or"});
    public static final Keyword SRA = new Keyword(new String[]{"sra"});
    public static final Keyword SRL = new Keyword(new String[]{"srl"});
    public static final Keyword XOR = new Keyword(new String[]{"xor"});
    public static final Keyword SLTU = new Keyword(new String[]{"sltu"});
    public static final Keyword SLT = new Keyword(new String[]{"slt"});
    public static final Keyword SGTU = new Keyword(new String[]{"sgtu"});
    public static final Keyword SGT = new Keyword(new String[]{"sgt"});
    public static final Keyword SLL = new Keyword(new String[]{"sll","sla"});
    public static final Keyword ANDI = new Keyword(new String[]{"andi"});
    public static final Keyword ORI = new Keyword(new String[]{"ori"});
    public static final Keyword XORI = new Keyword(new String[]{"xori"});
    public static final Keyword SLTI = new Keyword(new String[]{"slti"});
    public static final Keyword SLTIU = new Keyword(new String[]{"sltiu"});
    public static final Keyword ADDI = new Keyword(new String[]{"addi"});
    public static final Keyword SUBI = new Keyword(new String[]{"subi"});
    public static final Keyword SLLI = new Keyword(new String[]{"slli","slai"});
    public static final Keyword SRLI = new Keyword(new String[]{"srli"});
    public static final Keyword SRAI = new Keyword(new String[]{"srai"});
    public static final Keyword LW = new Keyword(new String[]{"lw"});
    public static final Keyword LH = new Keyword(new String[]{"lh"});
    public static final Keyword LB = new Keyword(new String[]{"lb"});
    public static final Keyword LHU = new Keyword(new String[]{"lhu"});
    public static final Keyword LBU = new Keyword(new String[]{"lbu"});
    public static final Keyword SW = new Keyword(new String[]{"sw"});
    public static final Keyword SB = new Keyword(new String[]{"sb"});
    public static final Keyword SH = new Keyword(new String[]{"sh"});
    public static final Keyword BEQ = new Keyword(new String[]{"beq"});
    public static final Keyword BNE = new Keyword(new String[]{"bne"});
    public static final Keyword BLT = new Keyword(new String[]{"blt"});
    public static final Keyword BGE = new Keyword(new String[]{"bge"});
    public static final Keyword BLTU = new Keyword(new String[]{"bltu"});
    public static final Keyword BGEU = new Keyword(new String[]{"bgeu"});
    public static final Keyword BGT = new Keyword(new String[]{"bgt"});
    public static final Keyword BLE = new Keyword(new String[]{"ble"});
    public static final Keyword BGTU = new Keyword(new String[]{"bgtu"});
    public static final Keyword BLEU = new Keyword(new String[]{"bleu"});
    public static final Keyword LI = new Keyword(new String[]{"li"});
    public static final Keyword MV = new Keyword(new String[]{"mv","mov"});
    public static final Keyword SWP = new Keyword(new String[]{"swp"});
    public static final Keyword DEC = new Keyword(new String[]{"dec"});
    public static final Keyword INC = new Keyword(new String[]{"inc"});
    public static final Keyword NEG = new Keyword(new String[]{"neg"});
    public static final Keyword SEQZ = new Keyword(new String[]{"seqz"});
    public static final Keyword SNEZ = new Keyword(new String[]{"snez"});
    public static final Keyword SLTZ = new Keyword(new String[]{"sltz"});
    public static final Keyword SGTZ = new Keyword(new String[]{"sgtz"});
    public static final Keyword INCI = new Keyword(new String[]{"inci"});
    public static final Keyword DECI = new Keyword(new String[]{"deci"});
    public static final Keyword CLR = new Keyword(new String[]{"clr"});
    public static final Keyword NOT = new Keyword(new String[]{"not"});
    public static final Keyword NOTI = new Keyword(new String[]{"inv"});
    public static final Keyword J = new Keyword(new String[]{"j,jump"});
    public static final Keyword JR = new Keyword(new String[]{"jr"});
    public static final Keyword RET = new Keyword(new String[]{"ret"});
    public static final Keyword NOP = new Keyword(new String[]{"nop"});
    public static final Keyword BEQZ = new Keyword(new String[]{"beq"});
    public static final Keyword BNEZ = new Keyword(new String[]{"bnez"});
    public static final Keyword BLTZ = new Keyword(new String[]{"bltz"});
    public static final Keyword BGTZ = new Keyword(new String[]{"bgtz"});
    public static final Keyword BLEZ = new Keyword(new String[]{"blez"});
    public static final Keyword BGEZ = new Keyword(new String[]{"bgez"});
    public static final Keyword LUI = new Keyword(new String[]{"lui"});
    public static final Keyword AUIPC = new Keyword(new String[]{"auipc"});
    public static final Keyword JAL = new Keyword(new String[]{"jal"});
    public static final Keyword JALR = new Keyword(new String[]{"jalr"});
    //public static final Keyword  = new Keyword(new String[]{""});
    //
    static class Command
    {
        final Keyword name; //name of command
        final char input_type; // type of input in syntax
        final char type;    //type of command
        final int n;        //number of fundamental instructions the command decomposes into
        public Command(Keyword name, char type, char input_type)//used for instructions which are already fundamental
        {
            this.name = name;
            this.type = type;
            this.n = 1;
            this.input_type = input_type;
        }
        public Command(Keyword name, char type,char input_type, int n)//used for instructions which are not necessarily fundamental
        {
            this.name = name;
            this.type = type;
            this.n = n;
            this.input_type = input_type;
        }
    }
    public static final List<Command> commands = get_commands();
    private static List<Command> get_commands()
    {
        List<Command> l = new ArrayList<>();
        //RRR
        l.add(new Command(ADD,R_TYPE,RRR));
        l.add(new Command(SUB,R_TYPE,RRR));
        l.add(new Command(AND,R_TYPE,RRR));
        l.add(new Command(OR,R_TYPE,RRR));
        l.add(new Command(XOR,R_TYPE,RRR));
        l.add(new Command(SLL,R_TYPE,RRR));
        l.add(new Command(SRL,R_TYPE,RRR));
        l.add(new Command(SRA,R_TYPE,RRR));
        l.add(new Command(SLT,R_TYPE,RRR));
        l.add(new Command(SLTU,R_TYPE,RRR));
        l.add(new Command(SGT,PSEUDO_TYPE,RRR));
        l.add(new Command(SGTU,PSEUDO_TYPE,RRR));
        //RRi
        l.add(new Command(ADDI,I_TYPE,RRi));
        l.add(new Command(SUBI,I_TYPE,RRi));
        l.add(new Command(XORI,I_TYPE,RRi));
        l.add(new Command(ANDI,I_TYPE,RRi));
        l.add(new Command(ORI,I_TYPE,RRi));
        l.add(new Command(SLTIU,I_TYPE,RRi));
        l.add(new Command(SLTI,I_TYPE,RRi));
        l.add(new Command(SLLI,I_TYPE,RRi));
        l.add(new Command(SRLI,I_TYPE,RRi));
        l.add(new Command(SRAI,I_TYPE,RRi));
        l.add(new Command(BEQ,B_TYPE,RRi));
        l.add(new Command(BNE,B_TYPE,RRi));
        l.add(new Command(BLT,B_TYPE,RRi));
        l.add(new Command(BGE,B_TYPE,RRi));
        l.add(new Command(BGEU,B_TYPE,RRi));
        l.add(new Command(BLTU,B_TYPE,RRi));
        l.add(new Command(BGT,PSEUDO_TYPE,RRi));
        l.add(new Command(BLE,PSEUDO_TYPE,RRi));
        l.add(new Command(BLEU,PSEUDO_TYPE,RRi));
        l.add(new Command(BGTU,PSEUDO_TYPE,RRi));
        //Ri_R_
        l.add(new Command(LW,I_TYPE,Ri_R_));
        l.add(new Command(LB,I_TYPE,Ri_R_));
        l.add(new Command(LH,I_TYPE,Ri_R_));
        l.add(new Command(LBU,I_TYPE,Ri_R_));
        l.add(new Command(LHU,I_TYPE,Ri_R_));
        l.add(new Command(SW,S_TYPE,Ri_R_));
        l.add(new Command(SB,S_TYPE,Ri_R_));
        l.add(new Command(SH,S_TYPE,Ri_R_));
        //RR
        l.add(new Command(INC,PSEUDO_TYPE,RR));
        l.add(new Command(DEC,PSEUDO_TYPE,RR));
        l.add(new Command(MV,PSEUDO_TYPE,RR));
        l.add(new Command(SWP,PSEUDO_TYPE,RR,3));
        l.add(new Command(NOT,PSEUDO_TYPE,R));
        l.add(new Command(NEG,PSEUDO_TYPE,R));
        l.add(new Command(SEQZ,PSEUDO_TYPE,R));
        l.add(new Command(SNEZ,PSEUDO_TYPE,R));
        l.add(new Command(SGTZ,PSEUDO_TYPE,R));
        l.add(new Command(SLTZ,PSEUDO_TYPE,R));
        //Ri
        l.add(new Command(LUI,U_TYPE,Ri));
        l.add(new Command(AUIPC,U_TYPE,Ri));
        l.add(new Command(INCI,PSEUDO_TYPE,Ri));
        l.add(new Command(DECI,PSEUDO_TYPE,Ri));
        l.add(new Command(LI,PSEUDO_TYPE,Ri));
        l.add(new Command(BEQZ,PSEUDO_TYPE,Ri));
        l.add(new Command(BNEZ,PSEUDO_TYPE,Ri));
        l.add(new Command(BLTZ,PSEUDO_TYPE,Ri));
        l.add(new Command(BLEZ,PSEUDO_TYPE,Ri));
        l.add(new Command(BGTZ,PSEUDO_TYPE,Ri));
        l.add(new Command(BGEZ,PSEUDO_TYPE,Ri));
        //R
        l.add(new Command(CLR,PSEUDO_TYPE,R));
        l.add(new Command(NOTI,PSEUDO_TYPE,R));
        l.add(new Command(JR,PSEUDO_TYPE,R));
        //i
        l.add(new Command(J,PSEUDO_TYPE,i));
        //$
        l.add(new Command(RET,PSEUDO_TYPE,$));
        l.add(new Command(NOP,PSEUDO_TYPE,$));
        //poly
        l.add(new Command(JAL,J_TYPE,POLY));
        l.add(new Command(JALR,I_TYPE,POLY));

        return l;
    }
    public static boolean is_command(String s)
    {
        for (Command command : commands) if (command.name.contains(s)) return true;
        return false;
    }
    public static char get_input_type_of_command(String command)
    {
        for (Command value : commands) if (value.name.contains(command)) return value.input_type;
        return 0;
    }
    public static int get_n_of_command(String command)
    {
        for (Command value : commands) if (value.name.contains(command)) return value.n;
        return 0;
    }
    
    
    static class Register
    {
        final List<String> names;
        final int address;
        public Register(String name, int address)
        {
            this.names = new ArrayList<>();
            this.names.add(name);
            this.address = address;
        }
        public Register(String[] names, int address)
        {
            this.names = new ArrayList<>();
            this.names.addAll(Arrays.asList(names));
            this.address = address;
        }
        public boolean is(String name)
        {
            for (String s : names) if (s.equals(name)) return true;
            return false;
        }
    }
    public static List<Register> registers = get_registers();
    private static List<Register> get_registers()
    {
        List<Register> r = new ArrayList<>();
        r.add(new Register("$zero",0));
        r.add(new Register("$ra",1));
        r.add(new Register("$sp",2));
        r.add(new Register("$gp",3));
        r.add(new Register("$tp",4));
        r.add(new Register("$t0",5));
        r.add(new Register("$t1",6));
        r.add(new Register("$t2",7));
        r.add(new Register(new String[]{"$s0","$fp"},8));
        r.add(new Register("$s1",9));
        r.add(new Register("$a0",10));
        r.add(new Register("$a1",11));
        r.add(new Register("$a2",12));
        r.add(new Register("$a3",13));
        r.add(new Register("$a4",14));
        r.add(new Register("$a5",15));
        r.add(new Register("$a6",16));
        r.add(new Register("$a7",17));
        r.add(new Register("$s2",18));
        r.add(new Register("$s3",19));
        r.add(new Register("$s4",20));
        r.add(new Register("$s5",21));
        r.add(new Register("$s6",22));
        r.add(new Register("$s7",23));
        r.add(new Register("$s8",24));
        r.add(new Register("$s9",25));
        r.add(new Register("$s10",26));
        r.add(new Register("$s11",27));
        r.add(new Register("$t3",28));
        r.add(new Register("$t4",29));
        r.add(new Register("$t5",30));
        r.add(new Register("$t6",31));
        return r;
    }
    public static int address_of_register(String register)
    {
        for (Register value : registers) if (value.is(register)) return value.address;
        return -1;
    }
    public static String name_of_register(int address)
    {
        for (Register value:registers)if(value.address == address)return value.names.get(0);
        return "";
    }

    public static class Base
    {
        final int b;
        final String[] id;
        public Base(int b, String[] id)
        {
            this.b = b;
            this.id = id;
        }
        public boolean contains_id(String id)
        {
            for(String i : this.id)if(id.contains(i))return true;
            return false;
        }
    }
    public static final Base DEFAULT_BASE = new Base(10,new String[]{"d"});
    public static final List<Base> bases = get_bases();
    private static List<Base> get_bases()
    {
        List<Base> l = new ArrayList<>(); // capital letters represent digits after 9
        l.add(new Base(2,new String[]{"b"}));
        l.add(new Base(8,new String[]{"o","q"}));
        l.add(new Base(16,new String[]{"h","x"}));
        l.add(DEFAULT_BASE);
        return l;
    }
    public static int get_base_of(String num)
    {
        for(Base b: bases)if(b.contains_id(num))return b.b;
        return DEFAULT_BASE.b;
    }

    /*
    rules for identifiers:
    cannot be a literal
    must be atleast 1 character long
    can start with VALID_SPECIAL_CHARACTER or alphabet
    every other character must be VALID_SPECIAL_CHARACTER or alphabet or number
     */
    public static final char[] VALID_SPECIAL_CHARACTERS = new char[]{'_','$'};
    public static boolean is_number(char ch)
    {
        return '0' <= ch && ch <= '9';
    }
    public static boolean is_alphabet(char ch)
    {
        return 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z';
    }
    public static boolean is_identifier(String s)
    {
        try {Parser.parseLong(s);return false;}
        catch(Exception ignored){} // ensures that s is not a literal
        if(s.length() == 0)return false;
        if(is_number(s.charAt(0)))return false;
        for(int i=0;i<s.length();i++)
        {
            char d = s.charAt(i);
            for(char ch : VALID_SPECIAL_CHARACTERS)if(ch==d)continue;   // checks if d is a valid special character
            if(!(is_number(d)||is_alphabet(d)))return false;
        }
        return true;
    }
    public static boolean is_label(String s)
    {
        return LABEL_TERMINATOR.equals(s.charAt(s.length() - 1) + "");
    }
    public static boolean is_valid_label(String s)
    {
        return is_label(s) && is_identifier(s.substring(0, s.length() - 1));
    }
    public static boolean is_data_type(String s)
    {
        return DATATYPE_INITIATOR.equals(""+s.charAt(0));
    }

    public static final String STRING_TERMINATOR= "\\\""; // terminator when used in regex represents a "
    public static final char STRING_TERMINATOR_CHAR = '"';
    public static final String STRING_ESCAPE = "\\\\"; // escape when used in regex represents a \
    public static final char STRING_ESCAPE_CHAR = '\\';
    public static final String STRING_REGEX = STRING_TERMINATOR+"([^"+STRING_ESCAPE+STRING_TERMINATOR+"]|("+STRING_ESCAPE+".))*"+STRING_TERMINATOR;
    public static final String DELIMITER_REGEX = "((("+Syntax.WHITESPACE+")*"+Syntax.ARGUMENT_SEPARATOR+"("+Syntax.WHITESPACE+")*)|("+Syntax.WHITESPACE+")+)";
    public static final String TOKEN_REGEX = "((([^"+Syntax.WHITESPACE+Syntax.ARGUMENT_SEPARATOR+STRING_ESCAPE+STRING_TERMINATOR+"])+)|("+Syntax.STRING_REGEX+"))";
    // string = \"([^\\\"]|(\\.))*\"
    // delimiter = (( )*,( )*)|( )+
    // token = (([^ ,\\\"])*)|(string)
    // " opens and closes the String
    // / functions as an escape sequence indicator
    public static final char NEWLINE = 'n';
    public static final char ASCII_CHAR = 'a';
    record Escape_sequence(char sequence,int ascii)
    {
    }
    public static final List<Escape_sequence> es = get_escape_sequences();
    private static List<Escape_sequence> get_escape_sequences()
    {
        List<Escape_sequence> l = new ArrayList<>();
        l.add(new Escape_sequence('0',0));//null character
        l.add(new Escape_sequence('\\',92));//backslash
        l.add(new Escape_sequence('"',34));//double quote
        l.add(new Escape_sequence('b',40));//backspace
        l.add(new Escape_sequence('t',41));//horizontal tab
        l.add(new Escape_sequence('\'',39));//single quote
        l.add(new Escape_sequence('r',13));//carriage return
        l.add(new Escape_sequence('f',12));//formfeed
        l.add(new Escape_sequence('v',11));//vertical tab
        l.add(new Escape_sequence('?',63));//question mark
        l.add(new Escape_sequence('a',7));//bell
        //l.add(new Escape_sequence('',));
        return l;
    }
    public static int get_value_of(char sequence) throws Exception
    {
        for(Escape_sequence e : es)if(e.sequence == sequence)return e.ascii;
        throw new Exception("Unidentified escape sequence");
    }
    public static boolean is_printable_ASCII(char ch)
    {
        return 32<=ch && ch<=126;
    }
}
