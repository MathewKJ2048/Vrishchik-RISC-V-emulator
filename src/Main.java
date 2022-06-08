import compiler.Binary;
import compiler.Compiler;
import compiler.Decompiler;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;



public class Main
{
    public static void main(String[] args) throws Exception {

        GUI_check();
    }
    public static void GUI_check() throws Exception
    {

        GUI_RISCV.load_preferences();
        UIManager.setLookAndFeel(GUI_RISCV.get_look_and_feel_location(GUI_RISCV.get_look_and_feel()));
        JFrame r = new GUI_RISCV("Vrishchik");
    }
    public static void limit_check()
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            System.out.println("Enter number of bits");
            int n = sc.nextInt();
            if(n<=1)break;
            System.out.println("Enter number:");
            long m = sc.nextLong();
            System.out.println("signed:"+compiler.Binary.belongs_in_range(m,n,true));
            System.out.println("unsigned:"+compiler.Binary.belongs_in_range(m,n,false));

        }
    }
    public static void check() // TODO rewrite
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get("test.s"));
            compiler.Compiler.compile(lines);
        }
        catch (Exception e)
        {
            System.out.println(compiler.Compiler.get_transcript().get_compilation());
            System.out.println("--------------------------------------------------------");
            System.out.println(compiler.Compiler.get_transcript().get_labels());
            System.out.println("---------------------------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("Error in compilation");
            return;
        }

        System.out.println("compilation complete");
        try
        {
           //compiler.Decompiler.decompile(Paths.get("test2.s"), Paths.get("test.bin"),true,10);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Error in decompilation");
            return;
        }
        String separator = "----------------";
        System.out.println(separator+"code:\n"+ Compiler.get_transcript().get_code());
        System.out.println(separator+"Scrubbed code:\n"+ Compiler.get_transcript().get_scrubbed_code());
        System.out.println(separator+"labels:\n"+ Compiler.get_transcript().get_labels());
        System.out.println(separator+"binary:\n"+ Compiler.get_transcript().get_binary());
        System.out.println(separator+"compilation:\n"+ Compiler.get_transcript().get_compilation());
        System.out.println("\nDecompiled:\n");
        System.out.println(Decompiler.get_source());
        try
        {
            //compiler.Compiler.compile(Paths.get("test2.s"), Paths.get("test2.bin"),true);
        }
        catch(Exception e)
        {
            System.out.println("decompiler code unable to be compiled into test2");
            e.printStackTrace();
        }
        try
        {
            //compiler.Decompiler.decompile(Paths.get("test3.s"), Paths.get("test2.bin"),true, 10);
        }
        catch(Exception e)
        {
            System.out.println("Unable to decompile test2.bin");
            e.printStackTrace();
        }
        try
        {
            byte[] byte_code = Files.readAllBytes(Paths.get("test.bin"));
            byte[] byte_code2 = Files.readAllBytes(Paths.get("test2.bin"));
            if(byte_code2.length!=byte_code.length)throw new Exception("length mismatch in byte code "+byte_code.length+" vs "+byte_code2.length);
            for(int i=0;i<byte_code.length;i++)
            {
                if(byte_code[i]!=byte_code2[i])throw new Exception("byte code is different");
            }
            System.out.println("byte codes verified to be same");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            byte[] source_code = Files.readAllBytes(Paths.get("test.bin"));
            byte[] source_code2 = Files.readAllBytes(Paths.get("test2.bin"));
            if(source_code2.length!=source_code.length)throw new Exception("length mismatch in source code");
            for(int i=0;i<source_code.length;i++)
            {
                if(source_code[i]!=source_code2[i])throw new Exception("source code is different");
            }
            System.out.println("source codes verified to be same");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    /*
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
    }*/
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
    public static void BIN_check()
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            int val = sc.nextInt();
            System.out.println("SIGNED:");
            /*
            System.out.println("bin:"+compiler.Binary.convert(val,true,2));
            System.out.println("oct:"+compiler.Binary.convert(val,true,8));
            System.out.println("dec:"+compiler.Binary.convert(val,true,10));
            System.out.println("hex:"+compiler.Binary.convert(val,true,16));
            System.out.println("UNSIGNED:");
            System.out.println("bin:"+compiler.Binary.convert(val,false,2));
            System.out.println("oct:"+compiler.Binary.convert(val,false,8));
            System.out.println("dec:"+compiler.Binary.convert(val,false,10));
            System.out.println("hex:"+compiler.Binary.convert(val,false,16));
            */

        }
    }
    public static void from_bin_check()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("start");
        while(true)
        {
            String s = sc.next();
            if(s.equals("quit"))break;
            System.out.println("number is:"+Binary.from_binary_signed(s));
        }
        System.out.println("end");
    }
}
