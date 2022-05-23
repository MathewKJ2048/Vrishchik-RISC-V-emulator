package compiler;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Decompiler
{
    private static List<String> source;
    public static void reset()
    {
        source = new ArrayList<>();
    }
    public static int size()
    {
        return source.size();
    }
    public static String get_source()
    {
        StringBuilder src = new StringBuilder();
        for(String s : source)src.append(s+"\n");
        return src.toString();
    }
    public static void decompile(Path source, Path binary, boolean write, int base) throws Exception
    {
        reset();
        byte[] binary_file = Files.readAllBytes(binary);
        int n = binary_file.length/4;  // 32 bits per instruction -> 4 bytes per instruction
        Decompiler.source.add(Syntax.CODE.words[0]);
        for(int i=0;i<n;i++)
        {
            StringBuilder Instruction = new StringBuilder();
            for(int j=0;j<4;j++)Instruction.append(Binary.to_binary_unsigned((binary_file[4*i+j]+256)%256,8));
            Decompiler.source.add(Binary.get_command(Instruction.toString(),base,4*i));
        }
        if(write)
        {
            Files.writeString(source, Decompiler.get_source());
        }
    }


}
