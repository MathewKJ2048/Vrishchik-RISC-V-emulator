.data
prompt_base: .asciiz "\nEnter the base:\n"
prompt_size: .asciiz "\nEnter the size:\n"
prompt_sign: .asciiz "\nEnter 0 if signed, 1 else:\n";
error_message: .asciiz "I"
.align 2
n: .word FFFFFFFF_x
.code
print_string:
li $a1, -1
li $a2, 9
ecall
ret

main:
li $s0, 1
li $s1, 2
li $s2, 4

start:
li $a0, prompt_base
jalr $ra, print_string

li $a2, 1
ecall
mov $t0, $a0    # reads base

li $a0, prompt_size
jalr $ra, print_string

li $a2, 1
ecall
mov $t1, $a0    # reads size

li $a0, prompt_sign
jalr $ra, print_string

li $a2, 1
ecall
mov $t2, $a0    # reads sign



beq $t1, $s0, byte
beq $t1, $s1, half
beq $t1, $s2, word

li $a0, error_message # base case
jalr $ra, print_string
j start

word:
li $a2, 8
j call
half:
li $a2, 7
j call
byte:
li $a2, 6
j call
call:
li $a0, n
mov $a1, $t0
mov $a3, $t2
ecall

j start