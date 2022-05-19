import javax.swing.*;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws Exception {

        //System.out.println("start");
        GUI_check();
    }
    public static void BIN_check()
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            int val = sc.nextInt();
            System.out.println("SIGNED:");
            System.out.println("bin:"+GUI_RISCV.convert(val,true,2));
            System.out.println("oct:"+GUI_RISCV.convert(val,true,8));
            System.out.println("dec:"+GUI_RISCV.convert(val,true,10));
            System.out.println("hex:"+GUI_RISCV.convert(val,true,16));
            System.out.println("UNSIGNED:");
            System.out.println("bin:"+GUI_RISCV.convert(val,false,2));
            System.out.println("oct:"+GUI_RISCV.convert(val,false,8));
            System.out.println("dec:"+GUI_RISCV.convert(val,false,10));
            System.out.println("hex:"+GUI_RISCV.convert(val,false,16));
        }
    }
    public static void GUI_check() throws Exception
    {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        //UIManager.getLookAndFeelDefaults().put("defaultFont",new Font("Monospace",Font.PLAIN,10));
        JFrame r = new GUI_RISCV("RISC-V emulator");
    }
    public static void bin_check() throws Exception
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            String in = sc.next();
            if(in.equals("quit"))break;
            //System.out.println(Syntax.is_valid_label(in));
            //System.out.println(Syntax.is_label(in));


            try{
            System.out.println(compiler.Binary.to_binary_unsigned(Integer.parseInt(in),5));
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            try{
                System.out.println(compiler.Binary.to_binary_signed(Integer.parseInt(in),12));
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void full_check()
    {
        compiler.Compiler c = new compiler.Compiler(0,0);
        String error = "";
        try
        {
            c.compile(Paths.get("test.s"),Paths.get("test.bin"));
        }
        catch(Exception e)
        {
            error = "ERROR: "+e.getMessage();
        }
        System.out.println(c.get_transcript().get_binary());



        try{processor.Processor.Read(Paths.get("test.bin"),false);}catch(Exception e){}
        System.out.println(processor.Processor.execute_all());

        for(int i=0;i<32;i++)
        {
            System.out.println("Register x"+i+": "+processor.Processor.get_register(i));
        }
    }
    public static void parse_check()
    {

        compiler.Parser p = new compiler.Parser(" \" a, , bc \" , , \"efg\" ");
        while(p.hasNext())
        {
            try{
                System.out.println("|"+p.next()+"|"+p.start()+"|"+p.end()+"|"+p.is_argument()+"|"+p.is_next_argument());}
            catch(Exception e)
            {
                System.out.println("Exception encountered");
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}
