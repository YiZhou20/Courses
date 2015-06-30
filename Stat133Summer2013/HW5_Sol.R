# The data are available on the Web at 
# http://www.stanford.edu/~vcs/stat133/stateoftheunion1790-2012.txt.zip
	
#### Person A: Preparation 9 POINTS FOR THE PREPARATION
### Use readLines() to read this plain text file into R and assign it to speeches

speeches = readLines("C:/Users/Henry/Documents/Berkeley/STAT 133/HW/stateoftheunion1790-2012.txt")
	
### Use grep and regular expressions to determine which 
### lines of speeches have the three asterisks in them. 

# [1 PT]
breaks = grep("\\*\\*\\*", speeches)

### Use breaks to identify the elements in speeches (which correspond to lines in the file
###	that have the date of the speech.  
### Place these dates in a character vector called tempDates.

# [1 PT]
tempDates = speeches[breaks[-length(breaks)]+4]
	
### Use gregexpr to identify the location of the year in 
### each tempDates. Then use substr() to extract the year 
### Convert the year to numeric. Call it speechYr

# [1 PT]
yrLocs = as.numeric(gregexpr("[0-9]{4}$", tempDates))
speechYr = as.numeric(substr(tempDates, yrLocs, yrLocs+3))

### Use gsub to extract the month of the speech from tempDates. 
### Convert the month to numeric. 
### Assign it to a vector called speechMo.

# [2 PTS]
speechMo = gsub("[[:blank:]].*", "", tempDates)
speechMo = sapply(speechMo, switch,  
                  January = 1, February = 2, March = 3,
                  April = 4, May = 5, June = 6, July = 7,
                  August = 8, September = 9, October = 10,
                  November = 11, December = 12)

###  Use breaks to extract the name of the president from speeches
###  Assign the names to the character vector presidents. 

# [2 PTS]
presidents = speeches[breaks[-length(breaks)] + 3]

### Cut up the speeches vector according to speech, 
### and place each speech into an element of the list.
### Call the list speechesL.
### Each element of the list should be a character vector
### Each element in the character vector should correspond
### to a sentence in the speech (not a line in the file).
### To create this character vector, you probably want to 
### first collapse all of the lines of text for one speech 
### into one long line and then split up this line according 
### to the appropriate literals that mark the end of a sentence. 
### Don't worry about the first sentence if it gets lumped in
### with the greeting.

# [2 PTS]
speechesL = list()
for (i in 1:(length(breaks)-1)) {
	str = paste(speeches[(breaks[i]+6):(breaks[i+1]-1)],
              sep=" ", collapse=" ")
  speechesL[[i]] = unlist(strsplit(str, "[.?!]"))
}


### For each speech, create a word vector that counts
### the number of occurrences of each word used in the
### speech.  

	
### The following steps will help you create a word vector for 
### each speech:
	
###  Complete the function speechToWords() shown below
library(Rstem)

speechToWords = function(sentences) {
# sentences is character vector of sentences for each speech
# Eliminate apostrophes and numbers, 
# and turn characters to lower case.

# [1 PT]
  sen = gsub("'", "", sentences)
  sen = tolower(gsub("[0-9]+", "", sen))

# Drop the phrase [Applause]
  sen = gsub("\\[applause\\]", "", sen)
  
# Split the text up by blanks and punctuation 
# [1 PT]
  words = unlist(strsplit(sen, "[[:punct:][:blank:]]+"))
# Unlist the return value and assign it to tempW

# Drop any empty words 
# [1 PT]
  words = words[words != ""]

# Use wordStem() to stem the words
# [1 PT] NOTE IT IS OK TO REMOVE STOP WORDS INSTEAD OF STEMMING
  words = wordStem(words)
  return(words)
# return a character vector of all words in the speech
}

# Apply the speechToWords() to each speech in speechesL
# Assign it to the list speechWords
# [1 PT]
speechWords = lapply(speechesL, speechToWords)
	
### Unlist the return value and use unique() 
### to get the bag of words.  
### Alphabetize the bag of words, and call it uniqueWords
# [1 PT]
uniqueWords = sort(unique(unlist(speechWords)))
	
### For each speech create a word vector 
### Think about vector operations to do this. 
### Consider the table function, and 
### assignment/indexing vectors using names.  
### Ultimately, create a matrix with columns corresponding to
### speeches and rows to words 
# [2 PTS]
emptyVec = rep(0, length(uniqueWords))
names(emptyVec) = uniqueWords

wordVecs = lapply(speechWords, function(x){
  counts = table(x)
  temp = emptyVec
  temp[names(counts)] = counts
  return(temp)
})

wordMat = matrix(unlist(wordVecs), 
                 ncol  = length(wordVecs), 
                 byrow = FALSE)

### Use hierarchical clustering to produce a visualization 
### of  the results.
# [1 PT]
hc = hclust(as.dist(presDist))
plot(hc)
	