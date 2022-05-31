package processor;

class Instruction_SET{
    public int Find_Inst(String s,String s1){
        return switch (s1) {
            case "R" -> Set_R(s);
            case "I" -> Set_I(s);
            case "S" -> Set_S(s);
            case "B" -> Set_B(s);
            case "U" -> Set_U(s);
            case "P" -> Set_P(s);
            default -> Set_J(s);
        };
    }
    public int Set_P(String s){
        return 36;
    }
    public int Set_R(String s){
        String sub =s.substring(0,7) +s.substring(17,20);
        return switch (sub) {
            case "0000000000" -> 0;
            case "0100000000" -> 1;
            case "0000000001" -> 2;
            case "0000000010" -> 3;
            case "0000000011" -> 4;
            case "0000000100" -> 5;
            case "0000000101" -> 6;
            case "0100000101" -> 7;
            case "0000000110" -> 8;
            default -> 9;
        };
    }

    public int Set_I(String s){
        String sub =  s.substring(17,20);
        if(s.startsWith("0010011", 25)) {
            switch (sub) {
                case "000":
                    return 10;
                case "010":
                    return 11;
                case "011":
                    return 12;
                case "100":
                    return 13;
                case "110":
                    return 14;
                case "111":
                    return 15;
                case "001":
                    return 16;
                default:
                    if (s.startsWith("000000")) {
                        return 17;
                    } else {
                        return 18;
                    }
            }
        }
        else {
            return switch (sub) {
                case "000" -> 19;
                case "001" -> 20;
                case "010" -> 21;
                case "100" -> 22;
                default -> 23;
            };

        }

    }
    public int Set_S(String s){
        String sub=s.substring(17,20);
        if(sub.equals("000")){
            return 24;
        }
        else if(sub.equals("001")){
            return 25;
        }
        else{
            return 26;
        }

    }
    public int Set_B(String s){
        String sub=s.substring(17,20);
        return switch (sub) {
            case "000" -> 27;
            case "001" -> 28;
            case "100" -> 29;
            case "101" -> 30;
            case "110" -> 31;
            default -> 32;
        };

    }
    public int Set_U(String s){
        return 33;

    }
    public int Set_J(String s){
        String sub=s.substring(17,20);
        if(sub.equals("000")){
            return 34;
        }
        else{
            return 35;
        }
    }
}

