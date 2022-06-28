.code
start:
inci $t0, 1
li $a0, 4   # address
li $a1, 16  # buffer size
li $a2, 3
ecall
nop
nop
j start