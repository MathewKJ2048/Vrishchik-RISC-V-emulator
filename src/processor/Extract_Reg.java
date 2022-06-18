package processor;

class Extract_Reg{
    public void Get_Reg(String s,String s1,int[] Reg){
        switch (s1) {
            case "R" -> Reg_R(Reg, s);
            case "I" -> Reg_I(Reg, s);
            case "S" -> Reg_S(Reg, s);
            case "B" -> Reg_B(Reg, s);
            case "U" -> Reg_U(Reg, s);
            case "P" -> Reg_P(Reg,s);
            default -> Reg_J(Reg, s);
        }
    }
    public void Reg_P(int[] Reg,String s){
        Reg[1] = UTIL.toDecimal(s.substring(7,12));
        Reg[2] = UTIL.toDecimal(s.substring(12,17));

    }
    public void Reg_R(int[] Reg,String s){
        Reg[0] = UTIL.toDecimal(s.substring(20,25));
        Reg[1] = UTIL.toDecimal(s.substring(7,12));
        Reg[2] = UTIL.toDecimal(s.substring(12,17));
    }
    public void Reg_I(int[] Reg,String s){
        Reg[0] = UTIL.toDecimal(s.substring(20,25));
        Reg[2] = UTIL.toDecimal(s.substring(12,17));
    }
    public void Reg_S(int[] Reg,String s){
        Reg[1] = UTIL.toDecimal(s.substring(7,12));
        Reg[2] = UTIL.toDecimal(s.substring(12,17));
    }
    public void Reg_B(int[] Reg,String s){
        Reg[1] = UTIL.toDecimal(s.substring(7,12));
        Reg[2] = UTIL.toDecimal(s.substring(12,17));
    }
    public void Reg_U(int[] Reg,String s){
        Reg[0] = UTIL.toDecimal(s.substring(20,25));
    }
    public void Reg_J(int[] Reg,String s){
        Reg[0] = UTIL.toDecimal(s.substring(20,25));
        Reg[2] = UTIL.toDecimal(s.substring(12,17));
    }
}