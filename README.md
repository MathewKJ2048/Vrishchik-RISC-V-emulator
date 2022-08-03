# Vrishchik RISC-V emulator
An open-source RISC-V emulator written in java, using the swing framework.


## Features:

### General
- independent compiler and processor with generation of standard binary files
- all 32 basic commands of RISC
- additional pseudo commands

### Compiler
- transcript of step-by-step compilation
- binary file preview
- decompiled code preview
- points out line number and nature of error

Processer:
- step execution
- breakpoints
- execution in independent threads to prevent crashes

GUI:
11) inbuilt console
12) support for 2,8,10,16 bases, signed and unsigned, ASCII
13) customizeable look and feel
14) customizeable font size

## Installation:

Java 17 is required to run Vrishchik. A headless JRE is unsuitable since swing is used in the GUI.
Note that config.json will initially be flagged as corrupted. Vrishchik automatically corrects this issue after being opened the first time.

### For Linux:

1. Download [linux.zip](https://github.com/MathewKJ2048/Vrishchik-RISC-V-emulator/blob/main/downloads/linux.zip?raw=true)
2. Extract the contents.
3. Give __run.sh__ permission to execute.
4. Execute __run.sh__.


### For Windows:

1. Download [windows_executable.zip](https://github.com/MathewKJ2048/Vrishchik-RISC-V-emulator/blob/main/downloads/windows_executable.zip?raw=true)
2. Extract the contents.
3. Double-click __Vrishchik.exe__ to run the program.

Alternatively,

1. Download [windows_jar.zip](https://github.com/MathewKJ2048/Vrishchik-RISC-V-emulator/blob/main/downloads/windows_jar.zip?raw=true)
2. Extract the contents.
3. Double-click __run.bat__.

### For OSX:

~~Go pound sand~~
download [Vrishchik.jar](https://rebrand.ly/r1ckr0l13r) and execute it.

---

Screenshots:

---

## Credits:

The following people were involved in this project:

- Supervision - [Dr Raghavendra Kanakagiri](https://scholar.google.com/citations?user=7udEeZcAAAAJ&hl=en)
- Compiler - [Mathew K J](https://github.com/MathewKJ2048)
- Virtual Processor - [V Bharadwaj](https://github.com/Bharadwaj1720)
- Pipelining - [V Bharadwaj](https://github.com/Bharadwaj1720)
- Data forwarding - [V Bharadwaj](https://github.com/Bharadwaj1720)
- Decompiler - [Mathew K J](https://github.com/MathewKJ2048)
- GUI - [Mathew K J](https://github.com/MathewKJ2048)
- Example programs - [Mathew K J](https://github.com/MathewKJ2048)
- Documentation - [Mathew K J](https://github.com/MathewKJ2048)
- Testing - [V Bharadwaj](https://github.com/Bharadwaj1720)
- Art - [Mathew K J](https://github.com/MathewKJ2048)

The following software was used in this project:

- [BlueJ](https://www.bluej.org/)
- [IntelliJ IDEA Community edition](https://www.jetbrains.com/idea/)
- [Inkscape](https://inkscape.org/)
- [launch4j](http://launch4j.sourceforge.net/)
- [freeconvert](https://www.freeconvert.com)
