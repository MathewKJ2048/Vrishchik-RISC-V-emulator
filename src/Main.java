

import compiler.Binary;

import javax.swing.*;
import java.util.Scanner;



public class Main
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            GUI();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void convert_check()
    {
        Scanner sc = new Scanner(System.in);
        while(true)
        {
            int n = sc.nextInt();
            if(n<0)break;
            long value = -(1L<<n);
            System.out.println("value:"+value+":"+Binary.belongs_in_range(value,32,true));
            System.out.println(Binary.convert(value,true,2,32,false));
        }
    }
    public static void GUI() throws Exception
    {
        GUI_RISCV.load_preferences();
        UIManager.setLookAndFeel(GUI_RISCV.get_look_and_feel_location(GUI_RISCV.get_look_and_feel()));
        UIManager.put("TextArea.font",GUI_RISCV.get_console_font());
        JFrame r = new GUI_RISCV();
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
