# Commands

## An overview:


The following conventions hold:  

- __Rn__ stands for a register, where n is anumber
- __imm__ stands for an immediate value, which may be a literal, a code label or a data label.

---

## Arithmetic commands:

| syntax            | meaning       |
| ------            | -------       |
| add R1, R2, R3    | R1 = R2 + R3  |
| sub R1, R2, R3    | R1 = R2 - R3  |
| neg R1, R2        | R1 = -R2      |

---

## Logical commands

| syntax            | meaning       |
| ------            | -------       |
| and R1, R2, R3    | R1 = R2 & R3  |
| or R1, R2, R3     | R1 = R2 | R3  |
| xor R1, R2, R3    | R1 = R2 ^ R3  |
| not R1, R2        | R1 = ~R2      |

---

## Memory commands:

| syntax            | meaning                           |
| ------            | -------                           |
| lw R1, imm        | R1 = memory\[imm+3 : imm\]        |
| lh R1, imm        | R1 = memory\[imm+1 : imm\]        |
| lb R1, imm        | R1 = memory\[imm : imm\]          |
| lw R1, imm(R2)    | R1 = memory\[imm+R2+3 : imm+R2\]  |
| lh R1, imm(R2)    | R1 = memory\[imm+R2+1 : imm+R2\]  |
| lb R1, imm(R2)    | R1 = memory\[imm+R2 : imm+R2\]    |

---

## Unconditional jumps:

| syntax            | meaning       |
| ------            | -------       |

---

## conditional jumps:

| syntax            | meaning       |
| ------            | -------       |

---

## miscellaneous:

| syntax            | meaning       |
| ------            | -------       |
| nop               | no operation  |
| clr R1            | R1 = 0        |

---

rd,rs1,rs2: (sll sla srl sra) (slt sltu sgt sgtu)
rd,rs: (inc dec) (mv swp) (seqz snez sltz sgtz)
r1,i(r2): (lb lh lw lbu lhu) (sb sh sw) (jalr - true)
r1,r2,i: (addi subi xori ori andi) (slli slri srai slai) (slti,sltiu) (beq bne blt bge bltu bgeu ble bgt bleu bgtu)
r,i: (inci deci) li (beqz bnez bltz bgez blez bgtz) (jal - true)
i: j (jal - pseudo)
r: noti jr (jalr - pseudo)
ret