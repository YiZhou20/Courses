# HW 4
# Using the apply function and creating functions
# Due Thursday Jul 11 11:55 p.m. on bspace
# and hard copy Friday Jul 12 in class
# The .R file will contain your code and answers to questions
# The hard copy should also include a printout of your plots (black and white is OK)
# Yi Zhou

# 1. (5) Create a matrix as follows:

m <- matrix(c(1:10, 11:20), nrow = 10, ncol = 2)

# Now find the mean of the rows
apply(m,1,mean)
#[1]  6  7  8  9 10 11 12 13 14 15
# And find the mean of the columns
apply(m,2,mean)
#[1]  5.5 15.5
# Divide all values by 2
m/2

# Examine the following R code. Briefly explain what it is doing.

z.sq <- function(z) return(c(z,z^2)) # define a function outputting both vector and its square
x <- 1:8 # define a new vector of integers from 1 to 8
z.sq(x) # apply the function to vector x
[1] 1 2 3 4 5 6 7 8 1 4 9 16 25 36 49 64
matrix(z.sq(x),ncol=2) # form the results in a matrix of 2 columns, original inputs and their squares
[,1] [,2]
[1,] 1 1
[2,] 2 4
[3,] 3 9
[4,] 4 16
[5,] 5 25
[6,] 6 36
[7,] 7 49
[8,] 8 64

# How could you simplify this? (Hint: Use sapply). Carry out your simplication 
# in R and show the result 
t(sapply(x,z.sq))

#2. (5) Suppose we have a matrix of 1s and 0s, and we want to create a vector that
# has a 1 or a 0 depending on whether the majority of the first c elements in that 
# row are 1 or 0. Here c will be a parameter which we can vary. Write a short 
# function, perhaps called find.majority, that does this. Then apply it to 
# the following matrix X when c=2 and again when c=3:
find.majority = function(v,c=2) {
  if (c > length(v)) {
    warning("c is more than the length of X.")
    c = length(v)
  }
  vSub = v[1:c]
  n1 = sum(vSub == 1)
  # will output 1 if there are equal numbers of 0's and 1's
  if (n1*2 >= c) {
    y = 1
  } else {
    y = 0
  }
  return(y)
}
X <- matrix(c(1,1,1,0, 0,1,0,1, 1,1,0,1, 1,1,1,1, 0,0,1,0),nrow=4)
apply(X,1,find.majority,2) # c = 2
#[1] 1 1 1 1
apply(X,1,find.majority,3) # c = 3
#[1] 1 1 0 1

# 3. (7) There is a famous dataset in R called "iris." It should already be loaded
# in R for you. If you type in ?iris you can see some documentation. Familiarize 
# yourself with this dataset.

# Find the mean petal length by species
tapply(iris$Petal.Length,iris$Species,mean)
#setosa versicolor  virginica 
#1.462      4.260      5.552

# Now obtain the mean of the first 4 variables, by species, but 
# using only one function call.
apply(iris[,-5],2,tapply,iris$Species,mean)
#Sepal.Length Sepal.Width Petal.Length Petal.Width
#setosa            5.006       3.428        1.462       0.246
#versicolor        5.936       2.770        4.260       1.326
#virginica         6.588       2.974        5.552       2.026

# Create a simple scatter plot of Petal Length against Petal
# Width. Title your plot appropriately.
plot(iris$Petal.Width,iris$Petal.Length, main = "Length and Width of Petals",
     xlab = "Petal Width (cm)", ylab = "Petal Length (cm)")
# Now change the plotting symbol to be different for each species.
pch.list = as.numeric(iris$Species)
plot(iris$Petal.Width,iris$Petal.Length, pch = pch.list,
     main = "Length and Width of Petals", xlab = "Petal Width (cm)", ylab = "Petal Length (cm)")
legend(0.1,6.8,levels(iris$Species), pch = c(1,2,3))
# Replot the data using the same symbol for all species, but plot 
# each species in a different color, filling the symbol
cols = c("red","blue","yellow")
col.list = cols[pch.list]
plot(iris$Petal.Width,iris$Petal.Length, pch = 16, col = col.list, cex = 0.8,
     main = "Length and Width of Petals", xlab = "Petal Width (cm)", ylab = "Petal Length (cm)")
legend(0.1,6.8,levels(iris$Species), fill = cols)

# A very useful function in R is "pairs." Use the pairs function to 
# create a plot of the iris data, comparing Petal Length, Petal
# Width, Sepal Length, and Sepal Width. You should have 12 subplots, 
# and 4 labeling plots.
# Use the previous question to code each of the points in a different
# color by species.
pairs(iris[,-5],pch = 16,col = col.list,cex = 0.8)

# What can you conclude about the data, from inspection of the pairs plot?
# There is recognizable difference in the 3 species of flowers in terms of petals and sepals.
# However, the Sepal length and width plots show that group difference here is not as distinguishable.
# Also, Setosa (red) category is always more distinguishable than the other two.
# 4. (5) Create a list with 2 elements as follows:

l <- list(a = 1:10, b = 11:20)

# What is the mean of the values in each element?
sapply(l,mean)
#a    b 
#5.5 15.5 
# What is the sum of the values in each element?
sapply(l,sum)
#a   b 
#55 155

# What type of object is returned if you use lapply? sapply? Show your R code that finds these answers. 
class(sapply(l,sum)) # vector returned
#[1] "integer"
class(lapply(l,sum)) # list returned
#[1] "list"
# Now create the following list:

l.2 <- list(c = c(21:30), d = c(31:40))

# What is the sum of the corresponding elements of l and l.2, using one function call?
mapply(sum,l,l.2)
#a   b 
#310 510
# Take the log of each element in the list l:
lapply(l,log)
# 5. (5) Write a function that finds the sample covariance, following the commenting 
# in this template. Then try your function out on the iris data.

## This is a funtion to find the sample covariance.
## Input: Dataset mat
## Output: Covariance Matrix
samp.cov <- function( mat ) {
# input should be a matrix/data.frame
# find the mean for each column, called sample.mean
  sample.mean = apply(mat,2,mean)
# subtract the sample mean from each observation
  mat.new = apply(mat,2,scale,center = TRUE,scale = FALSE)
# this (scale) is a built-in way, returning a matrix, seems easier, see ?scale
# implement matrix multiplication (hint: just leave the following code as it is)
  y.multiply <- function(y) return(y %*% t(y))

# now use apply() to carry out matrix multiplication over the rows of Mat
# notice the output will have ncol(mat)^2 rows, and nrow(mat) columns
  x.matmult = apply(mat.new,1,y.multiply)

# create the covariance matrix by taking the row means of x.matmult, a vector of length ncol(mat)^2
  cov.matrix = apply(x.matmult,1,mean)
# now use the return function and coerce the output to be matrix valued and 
# have the same number of columns as your input matrix, adding dimnames as column names of mat
  return(matrix(cov.matrix,ncol = ncol(mat),byrow = TRUE,
                dimnames = list(colnames(mat),colnames(mat))))
}

# Load samp.cov into R and give the result when you try your function out on 
# the iris data. Compare to the following output. Do your results differ? If 
# they do, why is that?
samp.cov(iris[,1:4])
cov(iris[,1:4])
# The results' format are identical, individual values are close but not exactly the same.
# Guesses for this subtle inconsistency are method calling hierarchy and rounding procedure during steps.
Bonus: improve on the documentation in samp.cov        