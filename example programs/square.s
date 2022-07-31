#*
This is a program to print n^2 after reading n from the user.
n^2 is calculated using iteration
Author: Mathew K J
*#

.data
prompt:
.asciiz "Enter the number:"
result:
.asciiz "The square is:"
newline:
.asciiz "\n"

.code

print:		# function which prints a null-terminated string with start address stored in $a0
	li $a2, 9
	li $a1, -1
	ecall
	ret

exit:		# function to exit the program
	li $a2, 0			
	ecall

multiply:	# function to multiply $a0 with $a1, returns product in $a0. $a0 cannot be negative
	li $t1, 0
	start:
		beqz $a0, end
		deci $a0, 1
		inc $t1, $a1
		j start
	end:
		mov $a0, $t1
		ret

main:

li $a0, prompt
jalr $ra, print

li $a2, 1
ecall

# now $a0 has the value of n

bgtz $a0, continue	# takes absolute value of $a0
inva $a0		
continue:
mov $a1, $a0
jalr $ra, multiply

# $a0 now holds n^2
mov $s0, $a0	# $s0 is preserved, $a0 is not. So value is saved in $s0

li $a0, newline
jalr $ra, print

li $a0, result
jalr $ra, print

li $a2, 4
li $a1, 0
mov $a0, $s0
ecall

j exit


