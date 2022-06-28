.code
#*
li $t0, 89ABCDEFx
li $t1, FEDCBA98x
xor $t2, $t1, $t0
xori $t1, $t1, -1
nop
nop
nop
*#
li $t0, FEDCBA98x
li $t0, DEADBEEFx