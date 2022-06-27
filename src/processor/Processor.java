package processor;

import java.util.*;
import java.io.*;

public class Processor{
    private static boolean is_frozen = false;
    public static boolean is_frozen()
    {
        return is_frozen;
    }
    public static void freeze()
    {
        is_frozen = true;
    }
    public static void thaw()
    {
        is_frozen = false;
    }
    static Database D = new Database();
    static Instruction_SET I = new Instruction_SET();
    static Extract_Reg R=new Extract_Reg();
    static Operations O = new Operations();
    static int j,i;
    public static void execute_step()
    {
        if(is_frozen())return;
        O.IF();
        if(O.IF_BUFF.S.equals("00000000000000000000000001110011")) // ECALL code
        {
            freeze();
            D.PC+=4;
            j++;
            return;
        }
        O.IDRF();
        O.EXE();
        O.Mem();
        O.WB();
        j=D.PC/4;
    }
    public static byte memory(int index){
        return D.Mem[index]==null?0:D.Mem[index];
    }
    public static int register(int index){
        return D.R[index];
    }
    public static int PC(){
        return D.PC;
    }
    public static void set_register(int index, int value)
    {
        D.R[index] = value;
    }
    public static void set_memory(int index, byte value)
    {
        D.Mem[index] = value;
    }
    public static void skip_to_end()
    {
        D.PC = i;
    }
    public static boolean is_over(){
        if(D.PC>=i){
            return true;
        }
        return false;
    }
    public static void reset_registers(){
        int index;
        for(index=0;index<32;index++){
            D.R[index]=0;
        }
    }
    public static void reset_memory(){
        int index;
        for(index=0;index<5000;index++){
            D.Mem[index]=0;
        }
    }
    private static boolean has_instructions = false;
    public static boolean has_instructions()
    {
        return has_instructions;
    }
    public static void reset_instruction()
    {
        int index;
        for(index=0;index<1024;index++){
            D.InstrMem[index]=0;
        }
        has_instructions = false;
    }
    public static int get_max_PC_value()
    {
        return i-1;
    }
    public static void Read(byte[] binary) throws Exception
    {
        if(binary.length%4!=0)throw new Exception("unexpected length for binary file");
        for(int i=0;i<binary.length;i++)D.InstrMem[i]=binary[i];
        System.out.println("Reading complete");
        for(int i=0;i<binary.length;i++)System.out.println("Instruction "+i+" :"+Integer.toBinaryString((binary[i] & 0xFF) + 0x100).substring(1));
        D.PC = 0;
        Processor.i = binary.length;
        has_instructions = true;
        /*
        int i=0;
        File filescan = new File(addr);
        Scanner scan = new Scanner(filescan);
        String s;
        while(scan.hasNext()){
            s=scan.next();
            int k= UTIL.toDecimal(s);
            D.InstrMem[i]=(byte)(k>>24);
            D.InstrMem[i+1]=(byte)(k>>16);
            D.InstrMem[i+2]=(byte)(k>>8);
            D.InstrMem[i+3]=(byte)(k);
            i=i+4;
        }
        D.PC=0;
         */
    }
    public static int get_memory_size() // returns size of memory in bytes
    {
        return D.Mem.length;
    }
}

