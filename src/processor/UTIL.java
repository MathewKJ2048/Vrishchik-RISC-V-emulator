package processor;

import compiler.Compiler;

public class UTIL{
    Database D = Processor.D;
    static int toDecimal(String s){
        int sum=0;
        for(int i=0;i<s.length();i++){
            sum=sum*2+s.charAt(i)-'0';
        }
        return sum;
    }
    static int SignToDecimal(String s){
        int sum=0;
        int sum1;
        for(int i=1;i<s.length();i++){
            sum=sum*2+s.charAt(i)-'0';
        }
        sum1=sum - (s.charAt(0)-'0')*(int)Math.pow(2,s.length()-1);
        return sum1;
    }
    static void StoreMem(int mem,int value){
        Processor.D.Mem[mem]=(byte)(value>>24);
        Processor.D.Mem[mem+1]=(byte)(value>>16);
        Processor.D.Mem[mem+2]=(byte)(value>>8);
        Processor.D.Mem[mem+3]=(byte)(value);
    }
    public void check(int i){
        System.out.println(i+" "+Instruction_SET.i);
        if(i== Instruction_SET.i){
            Code.BubbleSort();
            Processor.i=68;
            for(int j=9;j>0;j--){
                D.R[8]=0;
                Processor.E.check2(j);
            }
            Processor.END=false;
        }
    }
}
