fileLoc = "C:/Users/Henry/Documents/Berkeley/STAT 133/Lecture Code/weblog.txt"

wl = readLines(fileLoc)
length(wl)
# [1] 2

class(wl)
# [1] "character"

wl
# [1] "169.237.46.168 -- [26/Jan/2004:10:47:58 -0800] \"GET /stat141/Winter04 HTTP/1.1\" 301 328 \"http://anson.ucdavis.edu/courses/\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.1.4322)\""  
# [2] "169.237.46.168 -- [26/Jan/2004:10:47:58 -0800] \"GET /stat141/Winter04/ HTTP/1.1\" 200 2585 \"http://anson.ucdavis.edu/courses/\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.1.4322)\""

### Here are what you find in the txt file
### 169.237.46.168 -- [26/Jan/2004:10:47:58 -0800] "GET /stat141/Winter04 HTTP/1.1" 301 328 "http://anson.ucdavis.edu/courses/" "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.1.4322)"
### 169.237.46.168 -- [26/Jan/2004:10:47:58 -0800] "GET /stat141/Winter04/ HTTP/1.1" 200 2585 "http://anson.ucdavis.edu/courses/" "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.1.4322)"

# Why are they different?
# R uses \ to escape "

# Suppose we want to make a data frame with the variables:
# ip address
# day of month
# month
# year
# operation (GET, PUt, etc)
# URL

# Notice that the strucutre is such that we can pull out
# pieces of information using regular expressions.
# To begin we split on quotes and brackets

wlist = strsplit(wl, " \"| -- \\[|\" ")
# Here, we split with ( "), ( -- [), or (" )
# Important! We use \ to escape " and \\ to escape Meta character [

wlist[[1]]
#[1] "169.237.46.168"                                                           
#[2] "26/Jan/2004:10:47:58 -0800]"                                              
#[3] "GET /stat141/Winter04 HTTP/1.1"                                           
#[4] "301 328"                                                                  
#[5] "http://anson.ucdavis.edu/courses/"                                        
#[6] "\"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; .NET CLR 1.1.4322)\""

# We still see " in the 6th element because we did not split with ("), 
# Again, we use \ to escape "

# We have done a good job of splitting up the pieces of the data
# Now let's use these pieces to create our variables
# Some are easy, we just need to pick off the particular 
# elements in the list.
ipAddr = sapply(wlist, function(x) x[1])
ipAddr

url = sapply(wlist, function(x) x[5])
url

# To get the operation, we can use gsub to eliminate
# the unwanted stuff from the third element
op = sapply(wlist, function(x) 
  gsub(" .*", "", x[3]))
op

### Greedy matching
op.match = sapply(wlist, function(x) 
  gregexpr(" .*", x[3]))
op.match

# To get the pieces of the date, we can use strsplit
# on the second element
dates = sapply(wlist, function(x) 
  strsplit(x[2], "/|:"))
dates

# Let's take a look at what we get
dates[[1]]
#[1] "26"        "Jan"       "2004"      "10"        "47"       
#[6] "58 -0800]"
# perfect! we only need the first three elements

# Here's another way to pick them out
day = sapply(dates, "[", 1) # We use index function
month = sapply(dates, "[", 2)
year = sapply(dates, "[", 3)
day; month; year

# Then we can put them all in a data frame
wlDF = data.frame(ipAddr = ipAddr, day =day, month = month,
                  year = year, op = op, url = url)
wlDF

###########
### Fixed Width Data
## An alternative to regular expressions
## Note that the information appears in the same position 
## in the line for each log entry
## We can specify where to pull the variables from

wl2 = read.fwf(fileLoc, widths = c(14,5,2,1,3,1,4,18,3))
wl2
#              V1    V2 V3 V4  V5 V6   V7                 V8  V9
#1 169.237.46.168  -- [ 26  / Jan  / 2004 :10:47:58 -0800] " GET
#2 169.237.46.168  -- [ 26  / Jan  / 2004 :10:47:58 -0800] " GET
View(wl2)

wlDF2 = wl2[ , c(1, 3, 5, 7, 9)]
names(wlDF2) = c("ipAddr", "day", "month", "year", "op")
wlDF2
#          ipAddr day month year  op
#1 169.237.46.168  26   Jan 2004 GET
#2 169.237.46.168  26   Jan 2004 GET

# Notice that this isn't going to work for the URL 
# because they could be of different lengths
# There are other examples where this would work better.

########
### Delimited Data
fileLoc = "http://www-958.ibm.com/software/data/cognos/manyeyes/datasets/olympic2012withgdp/versions/1.txt"
## If we visit this site, we can see the data
#ISO  Gold/medals	Silver/medals	Bronze/medals	total/medals	total weight/points	GDP.2011	pop.2010	GDP/per
#ABW	0	0	0	0	0	2,456,000,000.00	108,000	22740.74074
#AFG	0	0	1	1	1	20,343,461,030.00	34,385,000	591.6376626
#AGO	0	0	0	0	0	100,990,000,000.00	19,082,000	5292.422178
#ALB	0	0	0	0	0	12,959,563,902.00	3,205,000	4043.545679
#...

# These data are tab delimited, meaning that a tab character
# is used to separate out the pieces of information

# We can use read.csv or read.table to read these data into R

data = read.csv(fileLoc, skip = 1, sep = "\t", header = FALSE,
                colClasses = c("character", rep("numeric", 5), 
                               rep("character", 3)))



head(data)

#   V1 V2 V3 V4 V5 V6                 V7         V8          V9
#1 ABW  0  0  0  0  0   2,456,000,000.00    108,000 22740.74074
#2 AFG  0  0  1  1  1  20,343,461,030.00 34,385,000 591.6376626
#3 AGO  0  0  0  0  0 100,990,000,000.00 19,082,000 5292.422178
#4 ALB  0  0  0  0  0  12,959,563,902.00  3,205,000 4043.545679
#5 AND  0  0  0  0  0   3,491,000,000.00     84,864 41136.40649
#6 ARE  0  0  0  0  0 360,245,000,000.00  7,512,000 47955.93717

# We can use regular expressions to clean up the data, e.g.,
# eliminate the commas in the numbers and convert them to numeric.
