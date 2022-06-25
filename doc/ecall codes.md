# Ecall codes
The environment may be invoked by the use of the __ecall__ command. This is useful for tasks like writing output to and reading input from the console.
The ecall codes used are unique to the Vrishchik environment.

The ecall code is stored in __$a2__

| code  | purpose                   | arguments             | return    |   
| ----  | -------                   | --------------------  | ------    |
| 0     | terminate                 | none                  | none      |
| 1     | read word                 | none                  | $a0       |
| 2     | read byte (character)     | none                  | $a0 (low) |
| 3     | read string (ASCII)       | $a0 stores start memory address, <br> $a1 stores buffer size (in bytes) | memory[$a0]
| 4     | print register            | $a0 stores value to print,<br>$a1 stores base to print the value in,<br>$a3 is 0 if the value is to be treated as signed and not 0 otherwise| none |
| 5     | print character           | $a0 (low) stores the character to be printed in ASCII | none |
| 6     | print byte                | $a0 stores start memory address,<br>$a1 atores the base to print the value in,<br>$a3 is 0 if the value is to be treated as signed and not 0 otherwise | none |
| 7     | print half                | $a0 stores start memory address,<br>$a1 atores the base to print the value in,<br>$a3 is 0 if the value is to be treated as signed and not 0 otherwise | none |
| 8     | print word                | $a0 stores start memory address,<br>$a1 atores the base to print the value in,<br>$a3 is 0 if the value is to be treated as signed and not 0 otherwise | none |



If the argument for base is 0, base ten is assumed by default.
