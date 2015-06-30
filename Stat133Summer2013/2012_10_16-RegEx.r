# Source these in to play with some regular expressions
cNames = c("Dewitt County", 
           "Lac qui Parle County", 
           "St John the Baptist Parish", 
           "Stone County")

test = cNames[3]
string = "The Slippery St Frances"

funny = "rep1!c@ted"
subjectLines = c("Re: 90 days",
                 "Fancy rep1!c@ted watches", "It's me")

strings = c("hi mabc", "abc", "abcd", "abccd",
            "abcabcdx", "cab", "abd", "cad")

htmlStr = "<html><head></head><body> <h1>This is a title</h1><para>And this is a short paragraph. It has two sentences.</para></body></html>"

substring(test, 1, 2)
#[1] "St"

substring(test, 1, 2) == "St"
# [1] TRUE

newName = paste("St.", 
                substring(test, 3, nchar(test)), sep = "")
newName
# [1] "St. John the Baptist Parish"


substring(cNames, 1, 2) == "St"
#[1] FALSE FALSE  TRUE  TRUE

substring(cNames, 1, 3)
#[1] "Dew" "Lac" "St " "Sto"

substring(cNames, 1, 3) == "St "
# [1] FALSE FALSE  TRUE FALSE

newNames = cNames
whichRep = substring(cNames, 1, 3) == "St "
whichRep
# [1] FALSE FALSE  TRUE FALSE
newNames[whichRep] = 
     paste("St. ", 
           substring(cNames[whichRep], 4, 
                     nchar(cNames[whichRep])), sep = "")
newNames
# [1] "Dewitt County"               "Lac qui Parle County"       
# [3] "St. John the Baptist Parish" "Stone County"

unlist(strsplit(string, " "))
#[1] "The"      "Slippery" "St"       "Frances"

unlist(strsplit(string, ""))
#[1] "T" "h" "e" " " "S" "l" "i" "p" "p" "e" "r" "y" " " "S" "t" " " "F"
#[18] "r" "a" "n" "c" "e" "s"

chars = unlist(strsplit(string, ""))

possible = which(chars == "S")
possible
#[1] 5 14

substring(string, possible, possible + 2)
#[1] "Sli" "St"

substring(string, possible, possible + 2) == "St "
# [1] FALSE  TRUE

gsub("St ", "St. ", cNames)
# [1] "Dewitt County"               "Lac qui Parle County"       
# [3] "St. John the Baptist Parish" "Stone County"

subjectLines
# [1] "Re: 90 days"              "Fancy rep1!c@ted watches" "It's me" 

grep("[[:alpha:]][[:digit:][:punct:]][[:alpha:]]", 
     subjectLines) 
### look for string with digit or punct (but not both) surrounded by alpha
### c@t in the 2nd element, t's in the 3rd element
# [1] 2 3

grep("[[:alpha:]][[:punct:][:digit:]][[:alpha:]]", 
     subjectLines) ### order does not matter
#[1] 2 3

grep("[[:alpha:]][[:punct:]][[:alpha:]]", 
     subjectLines)
### c@t in the 2nd element, t's in the 3rd element
# [1] 2 3

grep("[[:alpha:]][[:punct:][:digit:]]", 
     subjectLines) ### also include e: in the 1st element 
# [1] 1 2 3

newStrings = gsub("'", "", subjectLines)
newStrings
# [1] "Re: 90 days"              "Fancy rep1!c@ted watches"
# [3] "Its me"

grep("[[:alpha:]][[:digit:][:punct:]][[:alpha:]]", 
     newStrings)
#[1] 2

gregexpr("[[:alpha:]][[:digit:][:punct:]][[:alpha:]]", 
     newStrings)
# [[1]]
# [1] -1                 ### No match
# attr(,"match.length")
# [1] -1
# attr(,"useBytes")
# [1] TRUE

# [[2]]                  ### 2nd element
# [1] 12                 ### c@t where c is the 12th character
# attr(,"match.length")  
# [1] 3                  ### c@t has length of 3
# attr(,"useBytes")
# [1] TRUE

# [[3]]
# [1] -1                 ### No match
# attr(,"match.length")
# [1] -1
# attr(,"useBytes")
# [1] TRUE

gregexpr("[[:alpha:]][[:digit:][:punct:]]+[[:alpha:]]",
         newStrings)
### The plus sign indicates that members from the second character class
### (digits and punctuation) may appear one or more times
# [[1]]
# [1] -1
# attr(,"match.length")
# [1] -1
# attr(,"useBytes")
# [1] TRUE

# [[2]]
# [1] 9                 ### p1!c where p is the 9th character
# attr(,"match.length")
# [1] 4                 ### p1!c has length of 3
# attr(,"useBytes")
# [1] TRUE

# [[3]]
# [1] -1
# attr(,"match.length")
# [1] -1
# attr(,"useBytes")
# [1] TRUE
