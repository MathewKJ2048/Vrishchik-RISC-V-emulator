.code
nop
nop
jalr $t0, start($zero)
nop
nop
start:
nop
nop
addi $t2, $t2, 4
jalr $t1, start($t2)