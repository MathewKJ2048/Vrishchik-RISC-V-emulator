package processor;

import java.util.*;
import java.io.*;

public class Processor{
    static Database D = new Database();
    static Instruction_SET I = new Instruction_SET();
    static Extract_Reg R=new Extract_Reg();
    static Operations O = new Operations();
    static int j,i;
    /*
    public static void main(String[] args) throws FileNotFoundException {
        j=0;
        i=Read("Test.txt");
        Arrays.fill(D.Mem,(byte)0);
        Programs.BubbleSort();
        System.out.println("Before Bubble sort");
        for(int k=0;k<32;k++){
            System.out.println("R"+k+"="+D.R[k]);
        }
        for(int k=0;k<400;k=k+4){
            int z=(D.Mem[k]&0xFF)<<24|(D.Mem[k+1]&0xFF)<<16|(D.Mem[k+2]&0xFF)<<8|(D.Mem[k+3]&0xFF);
            System.out.println("Memory["+k+":"+(k+3)+"] = "+z);
        }
        System.out.println("-------------------------------------------------------------\nAfter Bubble sort");
        execute_all();
        for(int k=0;k<32;k++){
            System.out.println("R"+k+"="+D.R[k]);
        }
        for(int k=0;k<400;k=k+4){
            int z=(D.Mem[k]&0xFF)<<24|(D.Mem[k+1]&0xFF)<<16|(D.Mem[k+2]&0xFF)<<8|(D.Mem[k+3]&0xFF);
            System.out.println("Memory["+k+":"+(k+3)+"] = "+z);
        }

    }*/
    public static void execute_all(){
        for(j=0;j<(i/4);j=D.PC/4)
        {
            System.out.println("line "+j+" executed");
            O.IF();
            O.IDRF();
            O.EXE();
            O.Mem();
            O.WB();
        }
        System.out.println("Execution complete");
        for(int i=0;i<D.Mem.length;i++)
        {
            if(D.Mem[i]!=null && D.Mem[i].byteValue()!=0)System.out.println("non-0 memory");
        }
        System.out.println("Memory check complete");
    }
    public static void execute_step(){
        O.IF();
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
    public static boolean is_over(){
        if(D.PC>=i/4){
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
    public static void reset_instruction(){
        int index;
        for(index=0;index<1024;index++){
            D.InstrMem[index]=0;
        }
    }
    public static void Read(byte[] binary) throws Exception
    {
        if(binary.length%4!=0)throw new Exception("unexpected length for binary file");
        for(int i=0;i<binary.length;i++)D.InstrMem[i]=binary[i];
        System.out.println("Reading complete");
        for(int i=0;i<binary.length;i++)System.out.println("Instruction "+i+" :"+Integer.toBinaryString((binary[i] & 0xFF) + 0x100).substring(1));
        D.PC = 0;
        Processor.i = binary.length;
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

