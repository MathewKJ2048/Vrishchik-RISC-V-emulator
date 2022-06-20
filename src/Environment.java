import processor.Processor;

public class Environment
{
    public static volatile String input;
    public static volatile String output;
    public static void process()
    {
        // right now, it simply takes input and regurgitates it
        if(input == null)return;
        else
        {
            output = input;
        }
        Processor.thaw();
    }
}
