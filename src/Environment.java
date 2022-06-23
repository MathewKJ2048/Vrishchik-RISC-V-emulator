import compiler.Binary;
import compiler.Syntax;
import processor.Processor;

public class Environment
{
    public static volatile String input = null;
    public static volatile String output = null;
    public static volatile boolean to_exit = false;
    public static void reset()
    {
        input = null;
        output = null;
        to_exit = false;
    }
    // registers
    public static final int code_reg = 12;
    public static final int rv1_reg = 10;
    public static final int rv2_reg = 11;
    public static final int arg1_reg = 10;
    public static final int arg2_reg = 11;
    public static final int arg3_reg = 13;
    // ecall codes
    public static final int EXIT = 0;
    public static final int READ_WORD = 1;
    public static final int READ_CHAR = 2;
    public static final int READ_STRING = 3;
    public static final int WRITE_REG = 4;
    public static final int WRITE_CHAR = 5;
    public static final int WRITE_BYTE = 6;
    public static final int WRITE_HALF = 7;
    public static final int WRITE_WORD = 8;
    public static final int WRITE_STRING = 9;

    public static void process()
    {
        int code = Processor.register(code_reg);
        int arg1 = Processor.register(arg1_reg);
        int arg2 = Processor.register(arg2_reg);
        int arg3 = Processor.register(arg3_reg);
        if(code == READ_WORD)
        {
            if(input!=null)
            {
                try
                {
                    long value = compiler.Parser.parseLong(input);
                    input = null;
                    if(compiler.Binary.belongs_in_range(value,32,true))
                    {
                        Processor.set_register(rv1_reg,(int)value);
                    }
                    else throw new Exception("value out of range for 32 bit signed number");
                    Processor.thaw();
                }
                catch(Exception e)
                {
                    output = "\n"+e.getMessage()+"\nInvalid input, try again\n";
                    input = null;
                }
            }
        }
        else if(code == READ_CHAR)
        {
            if(input!=null)
            {
                try
                {
                    if(input.length()!=1)throw new Exception("Multiple characters entered");
                    char ch = input.charAt(0);
                    input = null;
                    Processor.set_register(rv1_reg,ch);
                    Processor.thaw();
                }
                catch(Exception e)
                {
                    output = "\n"+e.getMessage()+"\nInvalid input, try again\n";
                    input = null;
                }
            }
        }
        else if(code == READ_STRING)
        {
            if(input!=null)
            {
                try
                {
                    if(input.length()>arg2)throw new Exception("String is too long for buffer size "+arg2);
                    for(int i = 0; i<input.length();i++)
                    {
                        char d = input.charAt(i);
                        if((byte)d != d)throw new Exception("Unsupported character");
                        Processor.set_memory(arg1+i,(byte)d);
                    }
                    input = null;
                    Processor.thaw();
                }
                catch(Exception e)
                {
                    output = "\n"+e.getMessage()+"\nInvalid input, try again\n";
                    input = null;
                }
            }
        }
        else if(code == EXIT)
        {
            Processor.skip_to_end();
            Processor.thaw();
        }
        else if(code == WRITE_REG)
        {
            try
            {
                int base = arg2;
                if(base==0)base=10;
                output = ""+compiler.Binary.convert(arg1,arg3==0,base,32);
            }
            catch(Exception e)
            {
                output = "\n"+e.getMessage()+"\nUnable to print\n";
            }
            Processor.thaw();
        }
        else if(code == WRITE_CHAR)
        {
            int value = arg1;
            if(value!=(byte)value)
            {
                output = "\nvalue in register too large to be treated as an ASCII character\n";
            }else output = ""+((char)value);
            Processor.thaw();
        }
        else if(code == WRITE_STRING)
        {
            try
            {
                int length = arg2;
                int start = arg1;
                StringBuilder b = new StringBuilder();
                if(length==-1)
                {
                    for(int i=start;Processor.memory(i)!=0;i++)b.append((char)Processor.memory(i));
                }
                else
                {
                    for(int i=start;i<length;i++)b.append((char)Processor.memory(i));
                }
                output=b.toString();
            }
            catch(Exception ex)
            {
                output = "\nUnable to print string\n";
            }
            Processor.thaw(); // thaw is essential here bc it is printing
        }
        else if(code == WRITE_BYTE || code == WRITE_HALF || code == WRITE_WORD)
        {
            int address = arg1;
            int base = arg2;
            int size = 0;
            boolean base_unspecified = false;
            if(code == WRITE_BYTE) size = 1;
            else if(code == WRITE_HALF) size = 2;
            else if(code == WRITE_WORD) size = 4;
            if(base == 0)
            {
                base_unspecified=true;
                base=10;
            }
            try
            {
                StringBuilder bytes = new StringBuilder();
                for(int j=0;j<size;j++)
                {
                    byte b=processor.Processor.memory(address+j);
                    bytes.append(Binary.to_binary_signed(b,8));
                }
                long value = Binary.from_binary_signed(bytes.toString());
                String s = Binary.convert(value,arg3==0,base,size*8);
                if(!base_unspecified)s+="_"+ Syntax.get_id_of_base(base);
                output = ""+s;
            }
            catch(Exception e)
            {
                output = "\n"+e.getMessage()+"\nUnable to print"+size+" byte(s) at address:"+address+" with base +"+base+"\n";
            }
            Processor.thaw();
        }
        else
        {
            output = "\nunrecognized ecall code\n";
            Processor.thaw();
        }
    }
}
