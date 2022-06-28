.data
.word xFFFFFFFF
.word x7FFFFFFF
.word x80000000

.code

auipc $t0, 16		# gives x10
auipc $t0, 48		# gives x30 + 4 = x34
auipc $t0, xDEADF	# gives xDEADF + 8 = xDEAE7
auipc $t0, xFFFFF	# gives xFFFFF + 12 which rolls over to give xB
