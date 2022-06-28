.code
addi $t0, $zero, 0011_b #3
addi $t1, $zero, 0101_b #5
addi $t2, $zero, -4
addi $t3, $zero, 2
add $s0, $t0, $t2
add $s0, $t1, $t2
and $s0, $t0, $t1
or $s0, $t0, $t1
or $s0, $t2, $t1
xor $s0, $t0, $t1
slt $s1, $t0, $t1
clr $s1
slt $s1, $t1, $t0
clr $s1
slt $s1, $t2, $t1
clr $s1
slt $s1, $t1, $t2
clr $s1
sltu $s1, $t0, $t1
clr $s1
sltu $s1, $t1, $t0
clr $s1
sltu $s1, $t2, $t1
clr $s1
sltu $s1, $t1, $t2
clr $s1

sll $s0, $t0, $t3
srl $s0, $t0, $t3
sra $s0, $t0, $t3

sll $s0, $t2, $t3
srl $s0, $t2, $t3
sra $s0, $t2, $t3