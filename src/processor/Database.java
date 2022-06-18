package processor;

class Database{
    int[] R=new int[32];
    int PC;
    Byte[] InstrMem=new Byte[1024];
    Byte[] Mem = new Byte[5000];
    String Inst_Type(String s){
        return switch (s) {
            case "0110011" -> "R";
            case "0010011", "0000011" -> "I";
            case "0100011" -> "S";
            case "1100011" -> "B";
            case "0110111" -> "U";
            case "1111111" -> "P";
            case "1101111" -> "JAL";
            case "0010111" -> "AUIPC";
            default -> "JALR";
        };
    }
}