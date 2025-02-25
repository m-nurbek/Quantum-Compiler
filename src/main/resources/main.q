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

# LET a = 2
# LET c = 2
# LET b = 5
# LET i = 1
# WHILE i < b REPEAT
#   LET a = c * a
#   LET i = i + 1
# ENDWHILE

# PRINT a