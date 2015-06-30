wl = '169.237.46.168 - - [26/Jan/2004:10:47:58 -0800] "GET /stat141/Winter04 HTTP/1.1" 301 328 "http://anson.ucdavis.edu/courses/" "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.1.4322)'

wl
#[1] "169.237.46.168 - - [26/Jan/2004:10:47:58 -0800] 
# \"GET /stat141/Winter04 HTTP/1.1\" 301 328 
# \"http://anson.ucdavis.edu/courses/\" 
# \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; 
# .NET CLR 1.1.4322)"

# split the string. 
# Create a vector with one character per element 
wpieces = strsplit(wl, "")

# Search for the [ and the ]
beg = which(wpieces =="[")
end = which(wpieces == "]")

# Check for the results
beg
# integer(0)

# Not what we expected
# Let's examin further
wpieces

#[[1]]
#  [1] "1"  "6"  "9"  "."  "2"  "3"  "7"  "."  "4"  "6"  "."  "1"  "6" 
# [14] "8"  " "  "-"  " "  "-"  " "  "["

class(wpieces)
# [1] "list"

# Need to pull the vector out of the list
wpieces = strsplit(wl, "")[[1]]
beg = which(wpieces =="[")
end = which(wpieces == "]")

beg
# [1] 20

# If we add/subtract 1, we can get exactly the date
beg = which(wpieces =="[") + 1
end = which(wpieces == "]") - 1

# Extract the date piece of the string
wholeDate = substr(wl, beg, end)
wholeDate
# [1] "26/Jan/2004:10:47:58 -0800"

# Now that we have the wholeDate, let us split it
notime = strsplit(wholeDate, ":")
notime
# [[1]]
# [1] "26/Jan/2004" "10"          "47"          "58 -0800"   
notime[[1]][1]
# [1] "26/Jan/2004"

# So we want the first element 
# and to split it up
# This time the / is what we want to split on
datePieces = strsplit(notime[[1]][1], "/")
datePieces
# [[1]]
# [1] "26"   "Jan"  "2004"
