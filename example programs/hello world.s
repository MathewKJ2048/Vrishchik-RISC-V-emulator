#*
This is a program to print "Hello World" to the console
Author: Mathew K J
*#

.data
message:
.asciiz "H"  # null terminated string, null indicates where to stop printing

.code
main:

li $a2, 9			# ecall code to print string
li $a0, message		# string to be printed
li $a1, -1			# length of string (in this case, it prints everything till the null character)
ecall

exit:
li $a2, 0			# ecall code for exiting
ecall