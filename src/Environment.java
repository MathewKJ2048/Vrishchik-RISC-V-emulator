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
    // ecall codes
    public static final int READ_WORD = 1;
    public static final int READ_CHAR = 2;
    public static final int READ_STRING = 3;
    public static final int WRITE_WORD = 4;
    public static final int WRITE_CHAR = 5;
    public static final int WRITE_STRING = 6;
    public static final int EXIT = 0;

    public static void process()
    {
        //System.out.println("Process called");
        if(Processor.register(code_reg) == READ_WORD)
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
    }
}
