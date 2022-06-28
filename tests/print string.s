.data
string: .asciiz "Hello there"
.code
li $a0, string
li $a1, -1
li $a2, 9
ecall
