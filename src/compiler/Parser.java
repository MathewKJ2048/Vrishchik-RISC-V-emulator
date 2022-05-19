package compiler;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser // similar to Scanner class
{
    private int s = -1;
    private int e = -1;
    private boolean is_argument = false;
    final private Matcher m;
    final private String contents;
    private final Pattern p;
    public Parser(String contents)
    {
        this.contents = Syntax.WHITESPACE+contents;
        this.p = Pattern.compile(Syntax.DELIMITER_REGEX+Syntax.TOKEN_REGEX);
        // the regex used by default is:  ((( *, *)|( +))(([^, \\\"])+|(\"([^\\\"]|(\\.))*\")))
        this.m = p.matcher(this.contents);
    }
    public String next() throws IOException
    {
        if(m.find())
        {
            String s = contents.substring(m.start(),m.end());
            if(s.contains(Syntax.ARGUMENT_SEPARATOR))is_argument = true;
            // idexof ith char of s is m.start()+i
            Matcher r = Pattern.compile(Syntax.DELIMITER_REGEX).matcher(s);
            r.find();
            this.s = m.start()+r.end();
            this.e = m.start()+s.length();
            return contents.substring(this.s,this.e);
        }
        else throw new IOException();
    }
    public boolean hasNext()
    {
        try{return p.matcher(this.contents).find(this.e==-1?0:this.e);}
        catch(Exception e){return false;}
    }
    public int start()
    {
        return this.s-1; // -1 bc of whitespace
    }
    public int end()
    {
        return this.e-2;  //  -1 bc of whitespace, -1 bc of offset
    }
    public long nextLong() throws IOException, NumberFormatException
    {
        String s = next();
        return Long.parseLong(s);
    }

    public boolean hasNextLong()
    {
        try
        {
            Matcher r = p.matcher(this.contents);
            if(!r.find(this.e==-1?0:this.e))return false;
            String temp = contents.substring(r.start(),r.end()); // next d+t pair
            Matcher s = Pattern.compile(Syntax.DELIMITER_REGEX).matcher(temp);
            if(!s.find())return false;
            String value = temp.substring(s.end());
            Long.parseLong(value);
            return true;
        }
        catch(Exception e){return false;}
    }
    public boolean is_argument() // returns true if token is preceded by ,
    {
        return this.is_argument;
    }
    public boolean is_next_argument() // returns true if token is succeeded by ,
    {
        Matcher r = Pattern.compile(Syntax.DELIMITER_REGEX).matcher(contents);
        if(r.find(this.e==-1?0:this.e))
        {
            return contents.substring(r.start(),r.end()).contains(Syntax.ARGUMENT_SEPARATOR);
        }
        else return false;
    }
    public static long parseLong(String s) throws NumberFormatException
    {
        //capital letters represent digits after 9
        //get rid of underscores
        s = s.replace(Syntax.IMMEDIATE_SEPARATOR,"");
        /*
        the string can have the marks of base anywhere, multiple times
        the marks are removed
        the string is processed
         */
        int base = Syntax.get_base_of(s);
        String id[] = new String[]{};
        for(int i=0;i<Syntax.bases.size();i++)if(Syntax.bases.get(i).b==base)id=Syntax.bases.get(i).id;
        for(int i=0;i<id.length;i++)s=s.replace(id[i],"");
        return Long.parseLong(s,base);
    }
    public static String processString(String s) throws Exception
    {
        s = s.substring(1,s.length()-1);
        StringBuilder v = new StringBuilder();
        for(int i=0;i<s.length();i++)
        {
            char d = s.charAt(i);
            if(d==Syntax.STRING_ESCAPE_CHAR)
            {
                i++;
                d = s.charAt(i);
                if(d == Syntax.NEWLINE)v.append(System.lineSeparator());
                else if(d == Syntax.ASCII_CHAR)
                {
                    char h = s.charAt(++i);
                    char t = s.charAt(++i);
                    char o = s.charAt(++i);
                    if(!(Syntax.is_number(h)&&Syntax.is_number(t)&&Syntax.is_number(o)))throw new Exception("Number expected in ascii escape sequence");
                    v.append(""+(100*h+10*t+o));
                }
                else v.append(Syntax.get_value_of(d));
            }
            else
            {
                v.append(d);
            }
        }
        return v.toString();
    }
}



