package processor;
import java.util.ArrayList;
import java.util.List;

class IF_BUFF{
    String S=new String();
}
class IDRF_BUFF{
    int[] arr={0,0,0,0,0,0};
    String S=new String();
}
class EXE_BUFF{
    int exe1;
    int[] arr= {0,0,0,0};
}
class MEM_BUFF{
    int mem1;
    int[] arr= {0,0,0,0};
}

class Operations{
    Database D = Processor.D;
    Instruction_SET I = Processor.I;
    Extract_Reg R = Processor.R;
    IF_BUFF IF_BUFF = new IF_BUFF();
    IDRF_BUFF IDRF_BUFF= new IDRF_BUFF();
    EXE_BUFF EXE_BUFF=new EXE_BUFF();
    MEM_BUFF MEM_BUFF=new MEM_BUFF();
    int[] Reg = new int[3];
    String S;
    public void IF(){

        IF_BUFF.S= new String();
        for(int i=3;i>=0;i--){
            for(int j=0;j<8;j++){
                if(((D.InstrMem[D.PC+i]>>j)&1)==1){
                    IF_BUFF.S='1'+IF_BUFF.S;
                }
                else {
                    IF_BUFF.S = '0' + IF_BUFF.S;
                }
            }
        }

    }
    public void IDRF(){
        String S = IF_BUFF.S;
        String Type = D.Inst_Type(S.substring(25,32));
        IDRF_BUFF.arr[0] = I.Find_Inst(S, Type);
        R.Get_Reg(S,Type,Reg);
        IDRF_BUFF.arr[1] = Reg[0];//rd
        IDRF_BUFF.arr[2] = Reg[1];// rs2
        IDRF_BUFF.arr[3] = Reg[2];// rs1
        IDRF_BUFF.arr[4] = UTIL.toDecimal(S.substring(0, 12));
        IDRF_BUFF.arr[5] = UTIL.toDecimal(S.substring(0, 7) + S.substring(20, 25));
        IDRF_BUFF.S=S;
        int op = IDRF_BUFF.arr[0];
        int rs1 = D.R[IDRF_BUFF.arr[3]];
        int rs2 = D.R[IDRF_BUFF.arr[2]];
        int Bimm = UTIL.SignToDecimal(S.substring(0, 1) + S.substring(24, 25) + S.substring(1, 7) + S.substring(20, 24));
        if (op == 27) {
            if (rs1 == rs2) {
                D.PC = D.PC + Bimm;
            } else {
                D.PC = D.PC + 4;
            }
        } else if (op == 28) {
            if (rs1 != rs2) {
                D.PC = D.PC + Bimm;
            } else {
                D.PC = D.PC + 4;
            }

        } else if (op == 29) {
            if (rs1 < rs2) {
                D.PC = D.PC + Bimm;
            } else {
                D.PC = D.PC + 4;
            }
        } else if (op == 30) {
            if (rs1 >= rs2) {
                D.PC = D.PC + Bimm;
            } else {
                D.PC = D.PC + 4;
            }
        } else if (op == 31) {
            if (rs1 < rs2) {
                D.PC = D.PC + Bimm;
            } else {
                D.PC = D.PC + 4;
            }
        } else if (op == 32) {
            if (rs1 >= rs2) {
                D.PC = D.PC + Bimm;
            } else {
                D.PC = D.PC + 4;

            }
        } else if (op == 34) {
            D.R[IDRF_BUFF.arr[1]] = D.PC + 4;
            D.PC = D.PC + UTIL.SignToDecimal(S.charAt(0) + S.substring(12, 20) + S.charAt(11) + S.substring(1, 11));
        } else if (op == 35) {
            D.PC = D.PC + UTIL.SignToDecimal(S.charAt(0) + S.substring(10, 21) + S.charAt(9) + S.substring(1, 9) + "000000000000");
        } else {
            D.PC = D.PC + 4;
        }
    }
    public void EXE(){
        int op = IDRF_BUFF.arr[0];
        int rs1 = D.R[IDRF_BUFF.arr[3]];
        int rs2 = D.R[IDRF_BUFF.arr[2]];
        int offset = IDRF_BUFF.arr[4];
        int offset2 = IDRF_BUFF.arr[5];
        String S = IDRF_BUFF.S;
        EXE_BUFF.arr[0] = IDRF_BUFF.arr[0];
        EXE_BUFF.arr[1] = IDRF_BUFF.arr[1];
        EXE_BUFF.arr[2] = IDRF_BUFF.arr[2];
        EXE_BUFF.arr[3] = IDRF_BUFF.arr[3];
        if (op == 0) {
            EXE_BUFF.exe1 = rs1 + rs2;
            //D.R[IDRF_BUFF[1]]=rs1+rs2;

        } else if (op == 1) {
            EXE_BUFF.exe1 = rs1 - rs2;
            //D.R[IDRF_BUFF[1]]=rs1-rs2;

        } else if (op == 2) {
            EXE_BUFF.exe1 = rs1 << (rs2);
            //D.R[IDRF_BUFF[1]]=rs1<<(rs2)/32;

        } else if (op == 3) {
            if (rs1 < rs2) {
                EXE_BUFF.exe1 = 1;
                //D.R[IDRF_BUFF[1]]=1;

            } else {
                EXE_BUFF.exe1 = 0;
                //D.R[IDRF_BUFF[1]]=0;

            }
        } else if (op == 4) {
            if (rs1 < rs2) {
                EXE_BUFF.exe1 = 1;
                //D.R[IDRF_BUFF[1]]=1;

            } else {
                EXE_BUFF.exe1 = 0;
                // D.R[IDRF_BUFF[1]]=0;

            }
        } else if (op == 5) {
            EXE_BUFF.exe1 = rs1 ^ rs2;
            //D.R[IDRF_BUFF[1]] = rs1^rs2;

        } else if (op == 6) {
            EXE_BUFF.exe1 = rs1 >> (rs2);
            //D.R[IDRF_BUFF[1]]=rs1>>(rs2)/32;

        } else if (op == 7) {
            if (D.R[IDRF_BUFF.arr[1]] > 0) {
                EXE_BUFF.exe1 = rs1 >> (rs2);
                //D.R[IDRF_BUFF[1]]=rs1>>(rs2)/32;

            } else {
                EXE_BUFF.exe1 = -(rs1 >> (rs2));
                //D.R[IDRF_BUFF[1]]=-(rs1>>(rs2)/32);
            }

        } else if (op == 8) {
            EXE_BUFF.exe1 = rs1 | rs2;
            //D.R[IDRF_BUFF[1]]=rs1|rs2;

        } else if (op == 9) {
            EXE_BUFF.exe1 = rs1 & rs2;
            // D.R[IDRF_BUFF[1]]=rs1&rs2;

        } else if (op == 10) {
            EXE_BUFF.exe1 = rs1 + UTIL.SignToDecimal(S.substring(0, 12));

            //D.R[IDRF_BUFF[1]]=rs1+UTIL.SignToDecimal(S.substring(0,12));

        } else if (op == 11) {
            if (rs1 < UTIL.SignToDecimal(S.substring(0, 12))) {
                EXE_BUFF.exe1 = 1;
                //D.R[IDRF_BUFF[1]]=1;
            } else {
                EXE_BUFF.exe1 = 0;
                //D.R[IDRF_BUFF[1]]=0;
            }

        } else if (op == 12) {
            if (rs1 < UTIL.toDecimal(S.substring(0, 12))) {
                EXE_BUFF.exe1 = 1;
                //D.R[IDRF_BUFF[1]]=1;
            } else {
                EXE_BUFF.exe1 = 0;
                //D.R[IDRF_BUFF[1]]=0;
            }

        } else if (op == 13) {
            EXE_BUFF.exe1 = rs1 ^ Integer.parseInt(S.substring(0, 12));
            //D.R[IDRF_BUFF[1]]= rs1^Integer.parseInt(S.substring(0,12));

        } else if (op == 14) {
            EXE_BUFF.exe1 = rs1 | Integer.parseInt(S.substring(0, 12));
            //D.R[IDRF_BUFF[1]]= rs1|Integer.parseInt(S.substring(0,12));

        } else if (op == 15) {
            EXE_BUFF.exe1 = rs1 & Integer.parseInt(S.substring(0, 12));
            //D.R[IDRF_BUFF[1]]= rs1&Integer.parseInt(S.substring(0,12));

        } else if (op == 16) {
            EXE_BUFF.exe1 = rs1 << UTIL.toDecimal(S.substring(7, 12));
            // D.R[IDRF_BUFF[1]]= rs1<<UTIL.toDecimal(S.substring(7,12));

        } else if (op == 17) {
            EXE_BUFF.exe1 = rs1 >> UTIL.toDecimal(S.substring(7, 12));
            //D.R[IDRF_BUFF[1]]= rs1>>UTIL.toDecimal(S.substring(7,12));

        } else if (op == 18) {
            if (D.R[IDRF_BUFF.arr[1]] > 0) {
                EXE_BUFF.exe1 = rs1 >> UTIL.toDecimal(S.substring(7, 12));
                //D.R[IDRF_BUFF[1]]=rs1>>UTIL.toDecimal(S.substring(7,12));
            } else {
                EXE_BUFF.exe1 = -(rs1 >> UTIL.toDecimal(S.substring(7, 12)));
                //D.R[IDRF_BUFF[1]]=-(rs1>>UTIL.toDecimal(S.substring(7,12)));
            }

        } else if (op == 19) {
            EXE_BUFF.exe1 = offset + rs1;

        } else if (op == 20) {
            EXE_BUFF.exe1 = offset + rs1;

        } else if (op == 21) {
            EXE_BUFF.exe1 = offset + rs1;

        } else if (op == 22) {
            EXE_BUFF.exe1 = offset + rs1;

        } else if (op == 23) {
            EXE_BUFF.exe1 = offset + rs1;

        } else if (op == 24) {
            EXE_BUFF.exe1 = offset2 + rs1;

        } else if (op == 25) {
            EXE_BUFF.exe1 = offset2 + rs1;

        } else if (op == 26) {
            EXE_BUFF.exe1 = offset2 + rs1;

        } else if (op == 33) { // TODO look into this, this is LUI
            EXE_BUFF.exe1 = UTIL.SignToDecimal(S.substring(0, 20)) << (12);
            // D.R[IDRF_BUFF[1]]=UTIL.SignToDecimal(S.substring(0,20));

        }
    }
    public void Mem(){
        int op = EXE_BUFF.arr[0];
        int rs1 = D.R[EXE_BUFF.arr[3]];
        int rs2 = D.R[EXE_BUFF.arr[2]];
        MEM_BUFF.arr[0] = EXE_BUFF.arr[0];
        MEM_BUFF.arr[1] = EXE_BUFF.arr[1];
        MEM_BUFF.arr[2] = EXE_BUFF.arr[2];
        MEM_BUFF.arr[3] = EXE_BUFF.arr[3];
        if (op == 0) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
            //D.R[IDRF_BUFF[1]]=rs1+rs2;
        } else if (op == 1) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 2) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;

        } else if (op == 3) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 4) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 5) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 6) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 7) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 8) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 9) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 10) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 11) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 12) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 13) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 14) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 15) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 16) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 17) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 18) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;
        } else if (op == 19) {
            if (D.Mem[EXE_BUFF.exe1] > 0) {
                MEM_BUFF.mem1 = D.Mem[EXE_BUFF.exe1];
                //D.R[IDRF_BUFF[1]] = D.Mem[EXE_BUFF];
            } else {
                MEM_BUFF.mem1 = D.Mem[EXE_BUFF.exe1];
                MEM_BUFF.mem1 = -MEM_BUFF.mem1;
            }

        } else if (op == 20) {
            if (D.Mem[EXE_BUFF.exe1] > 0) {
                MEM_BUFF.mem1 = (D.Mem[EXE_BUFF.exe1] & 0xFF) << 8 | D.Mem[EXE_BUFF.exe1 + 1] & 0xFF;
            } else {
                MEM_BUFF.mem1 = (D.Mem[EXE_BUFF.exe1] & 0xFF) << 8 | D.Mem[EXE_BUFF.exe1 + 1] & 0xFF;
                MEM_BUFF.mem1 = -MEM_BUFF.mem1;
            }

        } else if (op == 21) {
            MEM_BUFF.mem1 = (D.Mem[EXE_BUFF.exe1] & 0xFF) << 24 | (D.Mem[EXE_BUFF.exe1 + 1] & 0xFF) << 16 | (D.Mem[EXE_BUFF.exe1 + 2] & 0xFF) << 8 | D.Mem[EXE_BUFF.exe1 + 3] & 0xFF;

        } else if (op == 22) {
            MEM_BUFF.mem1 = D.Mem[EXE_BUFF.exe1];

        } else if (op == 23) {
            MEM_BUFF.mem1 = (D.Mem[EXE_BUFF.exe1] & 0xFF) << 8 | D.Mem[EXE_BUFF.exe1 + 1];

        } else if (op == 24) {
            D.Mem[EXE_BUFF.exe1] = (byte) rs2;

        } else if (op == 25) {
            D.Mem[EXE_BUFF.exe1] = (byte) (rs2 >> 8);
            D.Mem[EXE_BUFF.exe1 + 1] = (byte) (rs2);

        } else if (op == 26) {
            D.Mem[EXE_BUFF.exe1] = (byte) (rs2 >> 24);
            D.Mem[EXE_BUFF.exe1 + 1] = (byte) (rs2 >> 16);
            D.Mem[EXE_BUFF.exe1 + 2] = (byte) (rs2 >> 8);
            D.Mem[EXE_BUFF.exe1 + 3] = (byte) (rs2);

        } else if (op == 33) {
            MEM_BUFF.mem1 = EXE_BUFF.exe1;

        }
    }
    public void WB(){
        int op = MEM_BUFF.arr[0];
        if (op == 0) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 1) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 2) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 3) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 4) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 5) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 6) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 7) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 8) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 9) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 10) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 11) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 12) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 13) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 14) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 15) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 16) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 17) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 18) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 19) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 20) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 21) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 22) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 23) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 33) {
            D.R[MEM_BUFF.arr[1]] = MEM_BUFF.mem1;
        } else if (op == 36) {
            D.R[MEM_BUFF.arr[2]] = D.R[MEM_BUFF.arr[3]];
        }
        D.R[0]=0;
    }
}
