# Quantum Compiler 1

This compiler will compile code that looks like BASIC to a java program.

Tutorial: [Teeny Tiny Compiler](https://austinhenley.com/blog/teenytinycompiler1.html)

```basic
PRINT "How many fibonacci numbers do you want?"
INPUT nums

LET a = 0
LET b = 1
WHILE nums > 0 REPEAT
    PRINT a
    LET c = a + b
    LET a = b
    LET b = c
    LET nums = nums - 1
ENDWHILE
```

The language compiler supports:
* Numerical variables 
* Basic arithmetic 
* If statements 
* While loops
* Print text and numbers
* Input numbers
* Labels and goto
* Comments