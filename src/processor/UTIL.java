package processor;

class UTIL{
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
}
