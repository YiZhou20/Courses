# Let's read data into R that is JSON formatted
# We will use the RJSONIO library
# It has a function fromJSON that converts JSON formatted data into an R list 

# Use install.packages("RJSONIO")
library(RJSONIO)
fileLoc = "C:/Users/Henry/Documents/Berkeley/STAT 133/Lecture Code/1.json"
kiva = fromJSON(fileLoc)

# Let's examine the R object returned from the call
names(kiva)
#[1] "header"  "lenders"

length(kiva$lenders)
#[1] 1000

kiva$lenders[[1]]
#$lender_id
#[1] "matt"
#$name
#[1] "Matt"
#$image
#$image$id
#[1] 12829
#$image$template_id
#[1] 1
#$whereabouts
#[1] "San Francisco CA
#$country_code
#[1] "US"
#$uid
#[1] "matt"
#$member_since
#[1] "2006-01-01T09:01:01Z"
#$personal_url
#[1] "www.socialedge.org/blogs/kiva-chronicles"
#$occupation
#[1] "Entrepreneur"
#$loan_because
#[1] "I love the stories. "
#$occupational_info
#[1] "I co-founded a startup nonprofit (this one!) and I work with an amazing group of people dreaming up ways to alleviate poverty through personal lending. "
#$loan_count
#[1] 89
#$invitee_count
#[1] 23

# Check that it indeed a list
class(kiva$lenders)
# [1] "list"
class(kiva$lenders[[1]])
# [1] "list"

# Look at the names of the elements of the first element in the list, the first lender
names(kiva$lenders[[1]])
# [1] "lender_id"         "name"              "image"            
# [4] "whereabouts"       "country_code"      "uid"              
# [7] "member_since"      "personal_url"      "occupation"       
# [10] "loan_because"      "occupational_info" "loan_count"       
# [13] "invitee_count"  

# Let's try extracting the loan count from all 1000 lenders into a vector  
loanCt = sapply(kiva$lenders, function(x) x[["loan_count"]])
# Error in x[["loan_count"]] : subscript out of bounds

# Some lists do not have "loan_count"
summary(kiva$lenders)
kiva$lenders[[941]]

# Subset lists contain "loan_count"
index.sub = rep(FALSE, length(kiva$lenders))
for(i in 1:length(index.sub)){
  index.sub[i] = any(names(kiva$lenders[[i]]) %in% "loan_count")
}
index.sub
sum(index.sub)
# [1] 985
index = which(index.sub ==TRUE)
index

# Summary for loan_count
loanCt = rep(0, length(index))
kiva.lenders = kiva$lenders[[index]]
loanCt = sapply(kiva$lenders, function(x) x[["loan_count"]])
for(i in 1:length(index)){
  loanCt[i] = kiva$lenders[[index[i]]][["loan_count"]]
}

class(loanCt)
# [1] "numeric"

head(loanCt)
# [1]  89  54 169  12   7  10

length(loanCt)
# [1] 985

max(sapply(loanCt, length))
# [1] 1

summary(loanCt)
# Min. 1st Qu.  Median    Mean 3rd Qu.    Max. 
# 1.0     7.0    15.0    45.8    34.0  3202.0 

