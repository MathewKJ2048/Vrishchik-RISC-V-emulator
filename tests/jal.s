.code
nop
nop
nop
nop
jal $t0, start
jal start
j start
jalr $t0, 4($t1)
jalr $t1
jr $t1
ret
start:
nop
nop
nop
