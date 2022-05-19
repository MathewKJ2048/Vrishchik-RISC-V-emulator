package compiler;
/*
The purpose of this class is only to generate binary strings for the given command
the required registers and immediates are passed as arguments to the function corresponding to the command required
the 32 bit binary string representing the command is returned
 */
public class Binary
{
    private static String U_type(int destination_register_address, long value, String opcode) throws Exception {
        String immediate = to_binary_signed(value,20);
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
    
    
    public static String to_binary_signed(long n, int length) throws Exception
    {
        long lower_limit = -(1L<<(length-1)); //-(2^(l-1))
        long upper_limit = -lower_limit-1;    //2^(l-1)-1
        if(!(lower_limit <= n && n<=upper_limit))throw new Exception ("Number out of range (signed)");
        long p = 1L<<length;  // 2^l
        long N = (p + n)%p;
        String bin = Long.toBinaryString(N);
        if(length<bin.length())throw new Exception("number too large (signed)");
        return "0".repeat(Math.max(0, length - bin.length()))+bin;
    }
    public static String to_binary_unsigned(long n,int length) throws Exception
    {
        long lower_limit = 0;
        long upper_limit = (1L<<length) - 1; //2^l-1
        if(!(lower_limit <= n && n<=upper_limit))throw new Exception ("Number out of range (unsigned)");
        String bin = Long.toBinaryString(n);
        if(length<bin.length())throw new Exception("number too large (unsigned) ");
        return "0".repeat(Math.max(0, length - bin.length()))+bin;
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
}
