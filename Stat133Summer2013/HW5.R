# Yi Zhou
# The data are available on the Web at 
# http://www.stanford.edu/~vcs/stat133/stateoftheunion1790-2012.txt.zip

### Use readLines() to read this plain text file into R and assign it to speeches
speeches = readLines(con = "C:/Users/Yi/Downloads/stateoftheunion1790-2012.txt")
	
### Use grep and regular expressions to determine which 
### lines of speeches have the three asterisks in them. 
threeAster = grep("\\*{3}",speeches)

### Use breaks to identify the elements in speeches (which correspond to lines in the file
###	that have the date of the speech). 
### Place these dates in a character vector called tempDates.
tempDates = unique(grep("^[JFMASOND][a-z]{2,8}[[:blank:]][0-9]{1,2},[[:blank:]][0-9]{4}$",speeches,value = TRUE))

### Use gregexpr to identify the location of the year in 
### each tempDates. Then use substr() to extract the year 
### Convert the year to numeric. Call it speechYr
yearRaw = gregexpr("[0-9]{4}",tempDates)
speechYr = as.numeric(mapply(substr, tempDates,yearRaw,nchar(tempDates)))

### Use gsub to extract the month of the speech from tempDates. 
### Convert the month to numeric. 
### Assign it to a vector called speechMo.
monthRaw = gsub("[[:blank:]][0-9]{1,2},[[:blank:]][0-9]{4}$","",tempDates)
months = c("January","February","March","April","May","June","July","August","September","October","November","December")
speechMo = match(monthRaw,months)

###  Use breaks to extract the name of the president from speeches
###  Assign the names to the character vector presidents. 
presidentsRaw = grep(".+State of the Union Address,.+",speeches,value = TRUE)
presidents = unique(gsub(",[[:blank:]].+","",presidentsRaw))

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
k = 2
speechesL = list(speeches[threeAster[1]:(threeAster[2]-1)])
while (k<length(threeAster)) {
  speechesL[[k]] = speeches[threeAster[k]:(threeAster[k+1]-1)]
  k=k+1
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



# Drop the phrase (Applause.)


  
# Split the text up by blanks and punctuation 



# Unlist the return value and assign it to tempW


# Drop any empty words 



# Use wordStem() to stem the words



# return a character vector of all words in the speech
}

# Apply the speechToWords() to each speech in speechesL
# Assign it to the list speechWords


	
### Unlist the return value and use unique() 
### to get the bag of words.  
### Alphabetize the bag of words, and call it uniqueWords


	
### For each speech create a word vector 
### Think about vector operations to do this. 
### Consider the table function, and 
### assignment/indexing vectors using names.  
### Ultimately, create a matrix with columns corresponding to
### speeches and rows to words 


