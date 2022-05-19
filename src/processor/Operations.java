package processor;

import java.util.ArrayList;
import java.util.List;

class IF_BUFF{
    int isEmpty=0;
    String S=new String();
}
class IDRF_BUFF{
    int isEmpty=0;
    int hold=0;
    int[] arr={0,0,0,0,0,0};
    String S=new String();
}
class EXE_BUFF{
    int isEmpty=0;
    int exe1;
    int[] arr= {0,0,0,0};
}
class MEM_BUFF{
    int cont=0;
    int isEmpty=0;
    int mem1;
    int[] arr= {0,0,0,0};
}
class Operations{
    public static class Clock_cycle
    {
        public boolean b[];
        public Clock_cycle()
        {
            this.b = new boolean[5];
        }
    }
    boolean[] OpFlags = new boolean[5];

    public List<Clock_cycle> cc = new ArrayList<>();
    int count=0,k=0;
    Database D = Processor.D;
    int conti=0;
    Instruction_SET I = Processor.I;
    Extract_Reg R = Processor.R;
    IF_BUFF IF_BUFF = new IF_BUFF();
    IDRF_BUFF IDRF_BUFF= new IDRF_BUFF();
    EXE_BUFF EXE_BUFF=new EXE_BUFF();
    MEM_BUFF MEM_BUFF=new MEM_BUFF();
   // String IF_BUFF;
    //int EXE_BUFF,MEM_BUFF;
    //int[] IDRF_BUFF= new int[6];
    int[] Reg = new int[3];
    public boolean IF(){
        if(D.PC<Processor.i && IF_BUFF.isEmpty==0) {
            OpFlags[0]=true;
            System.out.println("IF:"+Processor.clk);
            //this.cc.get(Processor.clk-1).b[4] = true;
            IF_BUFF.S = new String();
            for (int i = 3; i >= 0; i--) {
                for (int j = 0; j < 8; j++) {
                    if (((D.InstrMem[D.PC + i] >> j) & 1) == 1) {
                        IF_BUFF.S = '1' + IF_BUFF.S;
                    } else {
                        IF_BUFF.S = '0' + IF_BUFF.S;
                    }
                }
            }
                IF_BUFF.isEmpty=1;
        }
        else{
            OpFlags[0]=false;
        }

        if(OpFlags[0]==false && OpFlags[1]==false && OpFlags[2]==false && OpFlags[3]==false && OpFlags[4]==false){
            Processor.END=false;
        }
        return false;
    }
    public boolean IDRF(){

        if(IF_BUFF.isEmpty!=0 && IDRF_BUFF.isEmpty==0 && IDRF_BUFF.hold==0) {
            OpFlags[1]=true;
            System.out.println("IDRF:"+Processor.clk);
            //this.cc.get(Processor.clk-1).b[3] = true;
            String S = IF_BUFF.S;
            String Type = D.Inst_Type(S.substring(25, 32));
            IDRF_BUFF.arr[0] = I.Find_Inst(S, Type);
            R.Get_Reg(S, Type, Reg);
            IDRF_BUFF.arr[1] = Reg[0];//rd
            IDRF_BUFF.arr[2] = Reg[1];// rs2
            IDRF_BUFF.arr[3] = Reg[2];// rs1
            IDRF_BUFF.arr[4] = UTIL.toDecimal(S.substring(0, 12));
            IDRF_BUFF.arr[5] = UTIL.toDecimal(S.substring(0, 7) + S.substring(20, 25));
            IDRF_BUFF.S=S;

            if(Type=="R") {
                //System.out.println(Processor.Reg_Flags[Reg[1]]);
                //System.out.println(Processor.Reg_Flags[Reg[2]]);
                if(Processor.DataFwd==1 && Processor.Reg_Flags[Reg[1]] != 0 && Processor.Reg_Flags[Reg[2]] == 0){
                    D.R[IDRF_BUFF.arr[2]]=EXE_BUFF.exe1;
                    IDRF_BUFF.isEmpty=1;
                    IF_BUFF.isEmpty=0;
                }
                else if(Processor.DataFwd==1 && Processor.Reg_Flags[Reg[1]] == 0 && Processor.Reg_Flags[Reg[2]] != 0){
                    D.R[IDRF_BUFF.arr[3]]=EXE_BUFF.exe1;
                    D.R[Reg[0]]=EXE_BUFF.exe1;
                    IDRF_BUFF.isEmpty=1;
                    IF_BUFF.isEmpty=0;
                }

               else if (Processor.Reg_Flags[Reg[1]] == 0 && Processor.Reg_Flags[Reg[2]] == 0) {
                    IDRF_BUFF.isEmpty=1;
                    IF_BUFF.isEmpty=0;

                }
                else {
                    count=4;
                    IDRF_BUFF.isEmpty=0;
                    IDRF_BUFF.hold=1;
                    IF_BUFF.isEmpty=0;
                }
            }
            else if(Type=="I") {
                if(Processor.DataFwd==1 && Processor.Reg_Flags[Reg[2]] != 0){
                    D.R[IDRF_BUFF.arr[3]]=EXE_BUFF.exe1;
                    IDRF_BUFF.isEmpty=1;
                    IF_BUFF.isEmpty=0;
                }
                else if (Processor.Reg_Flags[Reg[2]] == 0) {

                    IDRF_BUFF.isEmpty=1;
                    IF_BUFF.isEmpty=0;

                }
                else {
                    count=3;
                    IDRF_BUFF.isEmpty=0;
                    IDRF_BUFF.hold=1;
                    IF_BUFF.isEmpty=0;

                }
            }
            else if(Type=="S") {
                if ((Processor.Reg_Flags[Reg[1]] == 0 && Processor.Reg_Flags[Reg[2]] == 0)||
                (Processor.Reg_Flags[Reg[1]] != 0 && Processor.Reg_Flags[Reg[2]] == 0)) {
                    IDRF_BUFF.isEmpty=1;
                    IF_BUFF.isEmpty=0;

                }
                else {
                    count=3;
                    IDRF_BUFF.isEmpty=0;
                    IDRF_BUFF.hold=1;
                }
                Processor.Reg_Flags[Reg[0]]--;
            }
           else if(Type=="B") {
                if (Processor.Reg_Flags[Reg[1]] == 0 && Processor.Reg_Flags[Reg[2]] == 0) {
                    IDRF_BUFF.isEmpty=1;
                    IF_BUFF.isEmpty=0;

                }
                else {
                    count=2;
                    IDRF_BUFF.isEmpty=0;
                    IDRF_BUFF.hold=1;
                    IF_BUFF.isEmpty=1;
                    D.PC=D.PC-4;
                }
                Processor.Reg_Flags[Reg[0]]--;
            }
            else if(Type!="B"){
                IDRF_BUFF.isEmpty=1;
                IF_BUFF.isEmpty=0;
            }
            Processor.Reg_Flags[Reg[0]]++;
            int op = IDRF_BUFF.arr[0];
            int rs1 = D.R[IDRF_BUFF.arr[3]];
            int rs2 = D.R[IDRF_BUFF.arr[2]];
            int Bimm = UTIL.SignToDecimal(S.substring(0, 1) + S.substring(24, 25) + S.substring(1, 7) + S.substring(20, 24));
            if (op == 27 && count==0) {
                if (rs1 == rs2) {
                    D.PC = D.PC + Bimm;
                } else {
                    D.PC = D.PC + 4;
                }
                Processor.stall++;
                return true;

            } else if (op == 28 && count==0) {
                if (rs1 != rs2) {
                    D.PC = D.PC + Bimm;
                } else {
                    D.PC = D.PC + 4;
                }
                Processor.stall++;
                return true;
            } else if (op == 29 && count==0) {
                if (rs1 < rs2) {
                    D.PC = D.PC + Bimm;
                } else {
                    D.PC = D.PC + 4;
                }
                Processor.stall++;
                return true;
            } else if (op == 30 && count==0) {
                if (rs1 >= rs2) {
                    D.PC = D.PC + Bimm;
                } else {
                    D.PC = D.PC + 4;
                }
                Processor.stall++;
                return true;
            } else if (op == 31 && count==0) {
                if (rs1 < rs2) {
                    D.PC = D.PC + Bimm;
                } else {
                    D.PC = D.PC + 4;
                }
                Processor.stall++;
                return true;
            } else if (op == 32 && count==0) {
                if (rs1 >= rs2) {
                    D.PC = D.PC + Bimm;
                } else {
                    D.PC = D.PC + 4;

                }
                Processor.stall++;
                return true;
            } else if (op == 34) {
                D.R[IDRF_BUFF.arr[1]] = D.PC + 4;
                D.PC = D.PC + UTIL.SignToDecimal(S.charAt(0) + S.substring(12, 20) + S.charAt(11) + S.substring(1, 11));
            } else if (op == 35) {
                D.PC = D.PC + UTIL.SignToDecimal(S.charAt(0) + S.substring(10, 21) + S.charAt(9) + S.substring(1, 9) + "000000000000");
            } else {
                D.PC = D.PC + 4;
            }
        }
        else {
            OpFlags[1] = false;
        }
        if(D.PC==Processor.i){
            IF_BUFF.isEmpty=0;
        }
    return false;
    }
    public boolean EXE(){

        if(IDRF_BUFF.isEmpty!=0 && EXE_BUFF.isEmpty==0) {
            OpFlags[2]=true;
            System.out.println("EXE:"+Processor.clk);
            //this.cc.get(Processor.clk-1).b[2] = true;
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
                EXE_BUFF.exe1 = rs1 << (rs2) / 32;
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
                EXE_BUFF.exe1 = rs1 >> (rs2) / 32;
                //D.R[IDRF_BUFF[1]]=rs1>>(rs2)/32;

            } else if (op == 7) {
                if (D.R[IDRF_BUFF.arr[1]] > 0) {
                    EXE_BUFF.exe1 = rs1 >> (rs2) / 32;
                    //D.R[IDRF_BUFF[1]]=rs1>>(rs2)/32;

                } else {
                    EXE_BUFF.exe1 = -(rs1 >> (rs2) / 32);
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

            } else if (op == 33) {
                EXE_BUFF.exe1 = UTIL.SignToDecimal(S.substring(0, 20));
                // D.R[IDRF_BUFF[1]]=UTIL.SignToDecimal(S.substring(0,20));

            }
            EXE_BUFF.isEmpty=1;
            IDRF_BUFF.isEmpty=0;
        }
        else {
            OpFlags[2] = false;
        }
        return false;
    }
    public boolean Mem(){

        if(EXE_BUFF.isEmpty!=0 && MEM_BUFF.isEmpty==0) {
            OpFlags[3]=true;
            System.out.println("MEM:"+Processor.clk);
            //this.cc.get(Processor.clk-1).b[1] = true;
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
            EXE_BUFF.isEmpty=0;
            MEM_BUFF.isEmpty=1;
        }
        else {
            OpFlags[3] = false;
        }
        if(MEM_BUFF.cont==1){
            MEM_BUFF.cont=0;
            conti=1;
            return true;
        }
        return false;
    }
    public boolean WB() {
       // if(D.PC==Processor.i){
         //   Processor.END=false;
           // return true;
        //}
        if (MEM_BUFF.isEmpty!=0) {
            OpFlags[4]=true;
            System.out.println("WB:"+Processor.clk);
            //this.cc.get(Processor.clk-1).b[0] = true;
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
            Processor.Reg_Flags[MEM_BUFF.arr[1]]--;
            MEM_BUFF.isEmpty=0;
            if(count!=0){
                if(count==1){
                    Processor.clk++;
                    Processor.stall=Processor.stall+4;
                    count=0;
                }
                else if(count==2){
                    IF_BUFF.isEmpty=1;
                    IDRF_BUFF.hold=0;
                    Processor.clk++;
                    Processor.stall=Processor.stall+3;
                    count=0;
                }
                else if(count==3){
                    IDRF_BUFF.isEmpty=1;
                    IDRF_BUFF.hold=0;
                    Processor.clk++;
                    Processor.stall=Processor.stall+2;
                    count=0;
                }
                else if(count==4){

                    IDRF_BUFF.isEmpty=1;
                    IDRF_BUFF.hold=0;
                    MEM_BUFF.cont=1;
                    Processor.stall=Processor.stall+2;
                    count=0;
                }
                else if(count==5){

                    Processor.clk++;
                    Processor.stall=Processor.stall+0;
                    count=0;
                }


            }

        }
        else {
            OpFlags[4] = false;
        }
        if(conti==1){
            conti=0;
            return true;
        }
        return false;
    }
}
