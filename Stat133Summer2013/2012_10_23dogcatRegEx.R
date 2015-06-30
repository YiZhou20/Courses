#Here are some examples of regular expressions

# Match the word cat or at or t.
# Don't match cat embedded within another word
# There can be other words present
cats = c("diplocat", "Hi cat", "mat", "at", "t!", "ct")
# The \< stands for beginning of a word and
# \> stands for the end of a word
# In R we have to escape the \ with an extra \
grep("\\<(cat|at|t)\\>", cats) 
### match words: "cat" (2), "at" (4), and "t" (5) 
### where each word can be surrounded by space or punct
# [1] 2 4 5

grep("\\<ca?t\\>", cats) 
### match words: "cat" (preceding item "a" one time) and "ct" (preceding item "a" zero time)
# [1] 2 6

grep("\\<(ca|a)?t\\>", cats) 
### match words: "cat" (2), "at" (4), and "t" (5) 
# [1] 2 4 5

grep("\\<(ca|a)t\\>", cats) 
# [1] 2 4 
### 5 is removed from the output, why?

grep("\\<(ma|c|a)?t\\>", cats) ### match words: "mat"(3), "at" (4), "t" (5), and "ct" (6)
# [1] 3 4 5 6

# The following do not work as expected
# can you figure out why?
grep("\\<c?a?t)\\>", cats) ### the extra ")" should be removed
# integer(0)

grep("^H", cats) ### find any element starts with "H"
# [1] 2

grep("t$", cats) ### find any element ends with "t"
# [1] 1 2 3 4 6

grep("at", cats) ### find any element contains "at", see the difference from \< and \>
# [1] 1 2 3 4

grep("^(cat|at|t)$", cats) ### What is the output?

# Find the word cat or caat or caaat, etc.
caats = c("cat", "caat.", "caats", "caaaat", "my cat")
grep("\\<ca+t\\>", caats)
# the {1,} is equivalent to +
grep("\\<ca{1}t\\>", caats)   ### Preceding item 1 time
grep("\\<ca{1,2}t\\>", caats) ### Preceding item between 1 and 2 times (inclusive)
grep("\\<ca{1,}t\\>", caats)  ### Preceding item 1 or more times

grep("\\<ca*t\\>", caats) 
### match "ct", "cat", "caat", ...
### preceding item "a" zero or more times

grep("\\<c.*t\\>", caats) 
### . match any single character
### What is the output?

# Now we want to find dog anywhere in the string
# We don't care about capitals
dogs = c("dogmatic", "TopDog","Doggone it!", "RUN DOG RUN")
# The tolower function is handy here.
grep("dog", tolower(dogs))
grep("[Dd][Oo][Gg]", dogs) ### match words contain "DOG", "DOg", "DoG", ... How many combinations?

# Finally we are looking at character vectors where
# each entry must be a number.
# The number can have an optional sign in front of it
# The number can have an optional decimal point followed by digits
nums = c("1.2", "-3000", "5lo", "hi2", "12.", "+57")
gregexpr("^[-+]?[[:digit:]]+(\\.[[:digit:]]+)?$", nums)

