.code
#*
addi $t1, $t1, 0EF_x
lui $t0, 00ABC_x
addi $t0, $t0, 7EF_x
li $t0, DEADBEEF_x
li $t0, 8000_0000x
nop
nop
addi $t0, $t0, -1
*#
li $t0, xFEDCB898
li $t1, xFEDCB998
li $t2, xFEDCBA98
li $s0, xFEDCBB98
li $s1, xFEDCBC98
li $a0, xFEDCBD98
li $a1, xFEDCBE98
li $a2, xFEDCBF98
nop
li $t0, x7EDCB898
li $t1, x7EDCB998
li $t2, x7EDCBA98
li $s0, x7EDCBB98
li $s1, x7EDCBC98
li $a0, x7EDCBD98
li $a1, x7EDCBE98
li $a2, x7EDCBF98