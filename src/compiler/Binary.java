package compiler;
/*
The purpose of this class is only to generate binary strings for the given command
the required registers and immediates are passed as arguments to the function corresponding to the command required
the 32 bit binary string representing the command is returned
 */
public class Binary
{
    public static String ecall()
    {
        return "00000000000000000000000001110011";
    }
    private static String U_type(int destination_register_address, long value, String opcode) throws Exception
    {
        String immediate = ""; // this allows for the use of unsigned values as well
        try
        {
            immediate = to_binary_signed(value,20);
        }
        catch (Exception e)
        {
            immediate = to_binary_unsigned(value,20);
        }
        String destination = to_binary_unsigned(destination_register_address,5);
        return immediate+destination+opcode;
    }
    public static String lui(int destination_register_address, long value) throws Exception {
        return U_type(destination_register_address, value,"0110111");
    }
    public static String auipc(int destination_register_address, long value) throws Exception {
        return U_type(destination_register_address, value,"0010111");
    }

    private static String I_type(int source_register_address, int destination_register_address, long value, String funct3, String opcode) throws Exception {
        String immediate = to_binary_signed(value,12);
        String source = to_binary_unsigned(source_register_address,5);
        String destination = to_binary_unsigned(destination_register_address,5);
        return immediate+source+funct3+destination+opcode;
    }
    public static String addi(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"000","0010011");
    }
    public static String ori(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"110","0010011");
    }
    public static String andi(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"111","0010011");
    }
    public static String xori(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"100","0010011");
    }
    public static String slti(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"010","0010011");
    }
    public static String sltiu(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"011","0010011");
    }
    public static String lw(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"010","0000011");
    }
    public static String lh(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"001","0000011");
    }
    public static String lb(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"000","0000011");
    }
    public static String lhu(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"101","0000011");
    }
    public static String lbu(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"100","0000011");
    }
    public static String jalr(int source_register_address, int destination_register_address, long value) throws Exception {
        return I_type(source_register_address,destination_register_address,value,"000","1100111");
    }
    //
    private static String I_TYPE_shamt(int source_register_address, int destination_register_address, long value, String funct3, String funct7)throws Exception
    {
        String opcode = "0010011";
        String source = to_binary_unsigned(source_register_address,5);
        String destination = to_binary_unsigned(destination_register_address,5);
        String shamt = to_binary_unsigned(value,5);
        return funct7+shamt+source+funct3+destination+opcode;
    }
    public static String slli(int source_register_address, int destination_register_address, long value) throws Exception
    {
        return I_TYPE_shamt(source_register_address, destination_register_address, value, "001","0000000");
    }
    public static String srli(int source_register_address, int destination_register_address, long value) throws Exception
    {
        return I_TYPE_shamt(source_register_address, destination_register_address, value, "101","0000000");
    }
    public static String srai(int source_register_address, int destination_register_address, long value) throws Exception
    {
        return I_TYPE_shamt(source_register_address, destination_register_address, value, "101","0100000");
    }

    private static String R_type(int source_register_address_1,int source_register_address_2, int destination_register_address, String funct3, String funct7) throws Exception {
        String opcode = "0110011";
        String source_1 = to_binary_unsigned(source_register_address_1,5);
        String source_2 = to_binary_unsigned(source_register_address_2,5);
        String destination = to_binary_unsigned(destination_register_address,5);
        return funct7+source_2+source_1+funct3+destination+opcode;
    }
    public static String add(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"000","0000000");
    }
    public static String sub(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception
    {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"000","0100000");
    }
    public static String or(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"110","0000000");
    }
    public static String and(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"111","0000000");
    }
    public static String sra(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"101","0100000");
    }
    public static String srl(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"101","0000000");
    }
    public static String xor(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"100","0000000");
    }
    public static String sltu(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"011","0000000");
    }
    public static String slt(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"010","0000000");
    }
    public static String sll(int source_register_address_1,int source_register_address_2, int destination_register_address) throws Exception {
        return R_type(source_register_address_1,source_register_address_2,destination_register_address,"001","0000000");
    }
    
    private static String B_type(int source_register_address_1,int source_register_address_2, long value, String funct3) throws Exception {
        String opcode = "1100011";
        String source_1 = to_binary_unsigned(source_register_address_1,5);
        String source_2 = to_binary_unsigned(source_register_address_2,5);
        String immediate = to_binary_signed(value,12);
        return extract_bits(12,12,immediate,12,1)+extract_bits(10,5,immediate,12,1)
                    +source_2+source_1+funct3+extract_bits(4,1,immediate,12,1)+extract_bits(11,11,immediate,12,1)+opcode;
    }
    public static String beq(int source_register_address_1,int source_register_address_2, long value) throws Exception {
        return B_type(source_register_address_1,source_register_address_2,value,"000");
    }
    public static String bne(int source_register_address_1,int source_register_address_2, long value) throws Exception {
        return B_type(source_register_address_1,source_register_address_2,value,"001");
    }
    public static String blt(int source_register_address_1,int source_register_address_2, long value) throws Exception {
        return B_type(source_register_address_1,source_register_address_2,value,"100");
    }
    public static String bge(int source_register_address_1,int source_register_address_2, long value) throws Exception {
        return B_type(source_register_address_1,source_register_address_2,value,"101");
    }
    public static String bltu(int source_register_address_1,int source_register_address_2, long value) throws Exception {
        return B_type(source_register_address_1,source_register_address_2,value,"110");
    }
    public static String bgeu(int source_register_address_1,int source_register_address_2, long value) throws Exception {
        return B_type(source_register_address_1,source_register_address_2,value,"111");
    }
    
    private static String S_type(int source_register_1_address, int source_register_2_address, long offset, String funct3) throws Exception {
        String opcode = "0100011";
        String immediate = to_binary_signed(offset,12);
        String source_1 = to_binary_unsigned(source_register_1_address,5);
        String source_2 = to_binary_unsigned(source_register_2_address,5);
        return extract_bits(11,5,immediate,11,0)+source_2+source_1+funct3+extract_bits(4,0,immediate,11,0)+opcode;
    }
    public static String sw(int source_register_1_address, int source_register_2_address, long offset) throws Exception {
        return S_type(source_register_1_address,source_register_2_address,offset,"010");
    }
    public static String sb(int source_register_1_address, int source_register_2_address, long offset) throws Exception {
        return S_type(source_register_1_address,source_register_2_address,offset,"000");
    }
    public static String sh(int source_register_1_address, int source_register_2_address, long offset) throws Exception {
        return S_type(source_register_1_address,source_register_2_address,offset,"001");
    }
    //sh 001 sb 000

    
    public static String jal(int destination_register_address, long offset) throws Exception {
        String opcode = "1101111";
        String destination = to_binary_unsigned(destination_register_address,5);
        String immediate = to_binary_signed(offset,20);
        return extract_bits(20,20,immediate,20,1)+extract_bits(10,1,immediate,20,1)+extract_bits(11,11,immediate,20,1)+extract_bits(19,12,immediate,20,1)
                    +destination+opcode;
    }
    
    public static long from_binary_unsigned(String binary)
    {
        long answer=0;
        for(int i=0;i<binary.length();i++)
        {
            if(binary.charAt(i)=='1')answer+= 1L <<(binary.length()-i-1);
        }
        return answer;
    }
    public static long from_binary_signed(String binary)
    {
        if(binary.length()==0)return 0;
        long answer=0;
        for(int i=1;i<binary.length();i++)
        {
            if(binary.charAt(i)=='1')answer+= (1L<<(binary.length()-i-1));
        }
        if(binary.charAt(0)=='1')answer-=(1L <<(binary.length()-1));
        return answer;
    }
    public static String to_binary_signed(long n, int length) throws Exception
    {
        if(!belongs_in_range(n,length,true))throw new Exception ("Number out of range (signed)");
        long p = 1L<<length;  // 2^l
        long N = (p + n)%p;
        String bin = Long.toBinaryString(N);
        if(length<bin.length())throw new Exception("number too large (signed)");
        return "0".repeat(Math.max(0, length - bin.length()))+bin;
    }
    public static String to_binary_unsigned(long n,int length) throws Exception
    {
        if(!belongs_in_range(n,length,false))throw new Exception ("Number out of range (unsigned)");
        String bin = Long.toBinaryString(n);
        return "0".repeat(Math.max(0, length - bin.length()))+bin;
    }
    public static String to_binary(long value, int length, boolean signed)
    {
        try{
            if(signed) return compiler.Binary.to_binary_signed(value,length);
            else return compiler.Binary.to_binary_unsigned(value,length);
        }
        catch (Exception e)
        {
            return "-".repeat(length);
        }
    }
    public static String to_decimal(String binary)
    {
        if(binary.charAt(0)=='-')return "-";
        long answer = 0;
        for(int i=0;i<binary.length();i++)
        {
            answer = answer<<1;
            answer+=binary.charAt(i)-'0';
        }
        return answer+"";
    }
    public static String to_octal(String binary)
    {
        if(binary.charAt(0)=='-')return "-";
        int l = binary.length();
        int off = l%3;
        binary = "0".repeat(off!=0?(3-off):0)+binary;
        StringBuilder answer = new StringBuilder();
        for(int i=0;i<binary.length()/3;i++)
        {
            String triad = binary.substring(3*i,3*i+3);
            answer.append(""+to_decimal(triad));
        }
        return answer.toString();
    }
    public static String to_hexadecimal(String binary)
    {
        if(binary.charAt(0)=='-')return "-";
        int l = binary.length();
        int off = l%4;
        binary = "0".repeat(off!=0?(4-off):0)+binary;
        StringBuilder answer = new StringBuilder();
        for(int i=0;i<binary.length()/4;i++)
        {
            String tetrad = binary.substring(4*i,4*i+4);
            String dec = to_decimal(tetrad);
            if(dec.length()>=2)answer.append(""+(char)(dec.charAt(1)-'0'+'A'));
            else answer.append(dec);
        }
        return answer.toString();
    }
    public static String convert(long value,boolean signed,int base,int length)
    {
        String answer = "";
        String bin;


        if(signed&&value<0){value=-value;answer+="-";}

        bin = to_binary(value,length,true);
        if(base==2)answer+=bin;
        else if(base == 10)answer+=to_decimal(bin);
        else if(base == 8)answer+=to_octal(bin);
        else if(base == 16)answer+=to_hexadecimal(bin);
        return answer;
    }
    public static String extract_bits( int l, int r, String b, int MSB_index, int LSB_index)// returns b[l:r] where b is b[MSBi:LSBi]
    {
        StringBuilder sb = new StringBuilder();
        for(int i=l;i>=r;i--)
        {
            sb.append(b.charAt(MSB_index - i));
        }
        return sb.toString();
        /*
        charAt(0) gives MSBi
        charAt(i) gives MSBi - i
        charAt(j) gives x
         x=MSbi - j
         j = MSBi-x
         thus CharAt(MSBi-x) gives xth char
        */
    }


    public static String get_command(String instruction,int base,int code_current) throws Exception //TODO look into error thrown for getting comment
    {
        if(instruction.length()!=32)throw new Exception("Error: size mismatch");
        StringBuilder command = new StringBuilder("");
        String OPCODE = extract_bits(6,0,instruction,31,0);

        if(OPCODE.equals("0110011"))//R-type
        {
            String RD = extract_bits(11,7,instruction,31,0);
            String RS1 = extract_bits(19,15,instruction,31,0);
            String RS2 = extract_bits(24,20,instruction,31,0);
            String funct3 = extract_bits(14,12,instruction,31,0);
            String funct7 = extract_bits(31,25,instruction,31,0);
            String word = "";
            if(funct7.equals("0000000"))
            {
                if(funct3.equals("000"))word=Syntax.ADD.words[0];
                else if(funct3.equals("001"))word=Syntax.SLL.words[0];
                else if(funct3.equals("010"))word=Syntax.SLT.words[0];
                else if(funct3.equals("011"))word=Syntax.SLTU.words[0];
                else if(funct3.equals("100"))word=Syntax.XOR.words[0];
                else if(funct3.equals("101"))word=Syntax.SRL.words[0];
                else if(funct3.equals("110"))word=Syntax.OR.words[0];
                else if(funct3.equals("111"))word=Syntax.AND.words[0];
                else throw new Exception("Unrecognized funct3 ("+funct3+") for R-type instruction: "+instruction);
            }
            else if(funct7.equals("0100000"))
            {
                if(funct3.equals("000"))word=Syntax.SUB.words[0];
                else if(funct3.equals("101"))word=Syntax.SRA.words[0];
                else throw new Exception("Unrecognized funct3 ("+funct3+") for R-type instruction: "+instruction);
            }
            else throw new Exception("Unrecognized funct7 ("+funct7+") for R-type instruction: "+instruction);
            String RD_name = Syntax.name_of_register((int)from_binary_unsigned(RD));
            String RS1_name = Syntax.name_of_register((int)from_binary_unsigned(RS1));
            String RS2_name = Syntax.name_of_register((int)from_binary_unsigned(RS2));
            command.append(word+" "+RD_name+","+RS1_name+","+RS2_name);
        }
        else if(OPCODE.equals("0010011")||OPCODE.equals("0000011")||OPCODE.equals("1100111"))//I-type
        {
            String RD = extract_bits(11,7,instruction,31,0);
            String RS1 = extract_bits(19,15,instruction,31,0);
            String funct3 = extract_bits(14,12,instruction,31,0);
            String word = "";
            if(OPCODE.equals("0010011"))
            {
                if(funct3.equals("000"))word=Syntax.ADDI.words[0];
                else if(funct3.equals("010"))word=Syntax.SLTI.words[0];
                else if(funct3.equals("011"))word=Syntax.SLTIU.words[0];
                else if(funct3.equals("100"))word=Syntax.XORI.words[0];
                else if(funct3.equals("110"))word=Syntax.ORI.words[0];
                else if(funct3.equals("111"))word=Syntax.ANDI.words[0];
                else if(funct3.equals("001"))word=Syntax.SLLI.words[0];
                else if(funct3.equals("101"))
                {
                    if(instruction.charAt(1)=='1')word=Syntax.SRAI.words[0];
                    else word=Syntax.SRLI.words[0];
                }
                else throw new Exception("Unrecognized funct3 ("+funct3+") for I-type instruction: "+instruction);
            }
            else if(OPCODE.equals("0000011"))
            {
                if(funct3.equals("000"))word=Syntax.LB.words[0];
                else if(funct3.equals("001"))word=Syntax.LH.words[0];
                else if(funct3.equals("010"))word=Syntax.LW.words[0];
                else if(funct3.equals("100"))word=Syntax.LBU.words[0];
                else if(funct3.equals("101"))word=Syntax.LHU.words[0];
                else throw new Exception("Unrecognized funct3 ("+funct3+") for I-type instruction: "+instruction);
            }
            else
            {
                if(funct3.equals("000"))word=Syntax.JALR.words[0];
                else throw new Exception("Unrecognized funct3 ("+funct3+") for I-type instruction: "+instruction);
            }
            String RD_name = Syntax.name_of_register((int)from_binary_unsigned(RD));
            String RS1_name = Syntax.name_of_register((int)from_binary_unsigned(RS1));
            String immediate = extract_bits(31,20,instruction,31,0);
            String val;
            if(OPCODE.equals("0010011") && (funct3.equals("001")|| funct3.equals("101"))) //shamt type
                val=convert(from_binary_unsigned(immediate.substring(7)),false,base,32);//TODO why 32?
            else val=convert(from_binary_signed(immediate),true,base,32);// TODO why 32?
            val+="_"+Syntax.get_id_of_base(base);
            command.append(word).append(" ").append(RD_name).append(",").append(RS1_name).append(",").append(val);

        }
        else if(OPCODE.equals("0100011"))//S-type
        {
            String funct3 = extract_bits(14,12,instruction,31,0);
            String word = "";
            //System.out.println("funct3 is:"+funct3);
            if(funct3.equals("000"))word = Syntax.SB.words[0];
            else if(funct3.equals("001"))word = Syntax.SH.words[0];
            else if(funct3.equals("010"))word = Syntax.SW.words[0];
            else throw new Exception("Unrecognized funct3 ("+funct3+") for S-type instruction: "+instruction);

            //System.out.println("Word is:"+word);
            String RS1 = extract_bits(19,15,instruction,31,0);
            String RS2 = extract_bits(24,20,instruction,31,0);
            String RS1_name = Syntax.name_of_register((int)from_binary_unsigned(RS1));
            String RS2_name = Syntax.name_of_register((int)from_binary_unsigned(RS2));
            String immediate = extract_bits(31,25,instruction,31,0)+extract_bits(11,25,instruction,31,0);
            String imm_val = convert(from_binary_unsigned(immediate),false,base,32)+"_"+Syntax.get_id_of_base(base);
            command.append(word+" "+RS1_name+","+imm_val+"("+RS2_name+")");
        }
        else if(OPCODE.equals("1100011"))//B-type
        {
            String RS1 = extract_bits(19,15,instruction,31,0);
            String RS2 = extract_bits(24,20,instruction,31,0);
            String funct3 = extract_bits(14,12,instruction,31,0);
            String word = "";
            if(funct3.equals("000"))word=Syntax.BEQ.words[0];
            else if(funct3.equals("001"))word=Syntax.BNE.words[0];
            else if(funct3.equals("100"))word=Syntax.BLT.words[0];
            else if(funct3.equals("101"))word=Syntax.BGE.words[0];
            else if(funct3.equals("110"))word=Syntax.BLTU.words[0];
            else if(funct3.equals("111"))word=Syntax.BGEU.words[0];
            else throw new Exception("Unrecognized funct3 ("+funct3+") for I-type instruction: "+instruction);
            String RS2_name = Syntax.name_of_register((int)from_binary_unsigned(RS2));
            String RS1_name = Syntax.name_of_register((int)from_binary_unsigned(RS1));
            String immediate = extract_bits(31,31,instruction,31,0)+
                    extract_bits(7,7,instruction,31,0)+
                    extract_bits(30,25,instruction,31,0)+
                    extract_bits(11,8,instruction,31,0);
            String val = convert(from_binary_signed(immediate)+code_current,true,base,32)+"_"+Syntax.get_id_of_base(base);
            command.append(word+" "+RS1_name+","+RS2_name+","+val);
        }
        else if(OPCODE.equals("0110111")||OPCODE.equals("0010111")) // TODO debug this
        {
            //U_TYPE;
            String RD = extract_bits(11,7,instruction,31,0);
            String RD_name = Syntax.name_of_register((int)from_binary_unsigned(RD));
            String immediate = extract_bits(31,12,instruction,31,0);
            String value = convert(from_binary_signed(immediate)+code_current,true,base,32)+"_"+Syntax.get_id_of_base(base);
            String word = OPCODE.equals("0110111")?Syntax.LUI.words[0]:Syntax.AUIPC.words[0];
            command.append(word+" "+RD_name+","+value);
        }
        else if(OPCODE.equals("1101111"))
        {
            //J_TYPE;
            String RD = extract_bits(11,7,instruction,31,0);
            String RD_name = Syntax.name_of_register((int)from_binary_unsigned(RD));
            String immediate = extract_bits(31,31,instruction,31,0)+
                    extract_bits(19,12,instruction,31,0)+
                    extract_bits(20,20,instruction,31,0)+
                    extract_bits(30,21,instruction,31,0);
            String value = convert(from_binary_signed(immediate)+code_current,true,base,32)+"_"+Syntax.get_id_of_base(base);
            command.append(Syntax.JAL.words[0]+" "+RD_name+","+value);
        }
        else throw new Exception("unrecognized opcode");
        return command.toString();
    }
    public static boolean belongs_in_range(long value, int number_of_bits, boolean signed)
    {
        /*
        checks if value can be represented in an n bit system, where
        n = number of bits
        using two's complement representation
        ranges:
        unsigned: 0 to 2^n - 1              both inclusive
        signed: -(2^(n-1)) to 2^(n-1) - 1   both inclusive
        1<<n = 2^n
         */
        long min = signed?-(1L<<(number_of_bits-1)):0;
        long max = (1L<<(number_of_bits-(signed?1:0))) - 1;
        if(value>max || value<min)return false;
        else return true;
    }
}
