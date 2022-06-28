.data
n:
.word 4660
p:
.word 4660
q:
.word 4660 # 0x1234
.code
addi $t0, $zero, 5
sw $t0, n
sh $t0, p
sb $t0, q
