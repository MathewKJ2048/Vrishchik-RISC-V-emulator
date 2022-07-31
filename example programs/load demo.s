.data
n:
.word xDEADBEED

.code

lw $t0, n	# load word
lh $t1, n	# load half with sign extension
lb $t2, n	# load byte with sign extension
lhu $t3, n  # load half with zero extension
lbu $t4, n  # load byte with zero extension