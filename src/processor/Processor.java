package processor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.io.*;

public class Processor{
    static Database D = new Database();
    static Instruction_SET I = new Instruction_SET();
    static Extract_Reg R=new Extract_Reg();
    static Operations O = new Operations();
    static UTIL U = new UTIL();
    static Extract_Reg E = new Extract_Reg();
    static int i;
    //static Scanner scan= new Scanner(System.in);
   // static boolean Flag_IF;
    //static boolean Flag_IDRF;
   // static boolean Flag_EXE;
    //static boolean Flag_Mem;
    //static boolean Flag_WB;
    static boolean END=true;
    static int clk=0;
    static int stall=0;
    static int[] Reg_Flags = new int[32];
    static int DataFwd;

    public static int get_register(int index)
    {
        return D.R[index];
    }
    public static String execute_all()
    {   //Flag_EXE=Flag_IDRF=Flag_Mem=Flag_WB=false;
        //Flag_IF=true;
        StringBuilder transcript = new StringBuilder();
        for( int f=0;f<32;f++){
            Reg_Flags[f]=0;
        }
        for(int k=0;k<40;k=k+4){
            int z=(D.Mem[k]&0xFF)<<24|(D.Mem[k+1]&0xFF)<<16|(D.Mem[k+2]&0xFF)<<8|(D.Mem[k+3]&0xFF);
            transcript.append("\nMemory["+k+":"+(k+3)+"] = "+z);
        }
       while(END)
        {
            /*O.IF();
            O.IDRF();
            O.EXE();
            O.Mem();
            O.WB();*/
            clk++;
            //O.cc.add(new Operations.Clock_cycle());
            //O.cc.add(new Operations.Clock_cycle());
            //O.cc.add(new Operations.Clock_cycle());
            //O.cc.add(new Operations.Clock_cycle());
            //O.cc.add(new Operations.Clock_cycle());
            if(O.WB()){
                continue;
            }
            if(O.Mem()){
                continue;
            }
            if(O.EXE()){
                continue;
            }
            if(O.IDRF()){
                continue;
            }
            if(O.IF()) {
                continue;
            }
        }
       O.IF_BUFF.isEmpty=O.IDRF_BUFF.isEmpty=O.EXE_BUFF.isEmpty=O.MEM_BUFF.isEmpty=0;
       O.IDRF_BUFF.hold=0;
       clk--;
       transcript.append("\nClock: "+clk);
       transcript.append("\nStalls: "+stall);
       //print_matrix();
        for(int k=0;k<40;k=k+4){
            int z=(D.Mem[k]&0xFF)<<24|(D.Mem[k+1]&0xFF)<<16|(D.Mem[k+2]&0xFF)<<8|(D.Mem[k+3]&0xFF);
            transcript.append("\nMemory["+k+":"+(k+3)+"] = "+z);
        }
        return transcript.toString();
    }
    static void print_matrix()
    {
        //O.cc.remove(O.cc.size()-1);
        System.out.println("IF\tID\tEXE\tMEM\tWB");
        for(int i=0;i<clk;i++)
        {
            Operations.Clock_cycle c = O.cc.get(i);
            System.out.println((c.b[4]?"1":"0")+"\t"+(c.b[3]?"1":"0")+"\t"+(c.b[2]?"1":"0")+"\t"+(c.b[1]?"1":"0")+"\t"+(c.b[0]?"1":"0")+"\t");
        }
    }
    public static void Read(Path binary,boolean forwarding) throws Exception
    {
        byte[] binary_file = Files.readAllBytes(binary);
        for(int i=0;i<binary_file.length;i++) {D.InstrMem[i] = binary_file[i];}
        D.PC=0;
        Processor.i = binary_file.length;
        for(int k=0;k<5000;k++){
            D.Mem[k]=0;
        }
        //System.out.println("Do you want to enable Data Forwarding?\n1) Yes\n2) No");
        DataFwd=forwarding?1:2;//scan.nextInt();
        U.check(i);
    }
    public static void clear_registers()
    {
        for(int i=0;i<D.R.length;i++)D.R[i]=0;
    }
}





