.data
n: .word xFFFFFFFF
p: .word xFFFFFFFF
q: .word x80000000
r: .word x7FFFFFFF
s: .word xDEADBEAD
t: .half 7
u: .half xFFFF
v: .half x8000
w: .half xDEAD
x: .byte 7
y: .byte xFF
z: .byte x80
.code
lw $t0, n
lw $t0, p
lw $t0, q
lw $t0, r
lw $t0, s
nop
lh $t0, n
lh $t0, p
lh $t0, q
lh $t0, r
lh $t0, s
lh $t0, t
lh $t0, u
lh $t0, v
lh $t0, w
nop
lhu $t0, n
lhu $t0, p
lhu $t0, q
lhu $t0, r
lhu $t0, s
lhu $t0, t
lhu $t0, u
lhu $t0, v
lhu $t0, w
nop
lb $t0, n
lb $t0, p
lb $t0, q
lb $t0, r
lb $t0, s
lb $t0, t
lb $t0, u
lb $t0, v
lb $t0, w
lb $t0, x
lb $t0, y
lb $t0, z
nop
lbu $t0, n
lbu $t0, p
lbu $t0, q
lbu $t0, r
lbu $t0, s
lbu $t0, t
lbu $t0, u
lbu $t0, v
lbu $t0, w
lbu $t0, x
lbu $t0, y
lbu $t0, z