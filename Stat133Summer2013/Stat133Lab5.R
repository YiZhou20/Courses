# Start with a simple bar chart graphing the cars vector:
# Define the cars vector with 5 values
cars <- c(1, 3, 6, 4, 9)

# Graph cars
barplot(cars)
# Read the auto data from the autos.dat data file, add labels, blue borders around the bars, and density lines:
# Read values from tab-delimited autos.dat 
autos_data <- data.frame(cars = c(1,3,6,4,9),trucks = c(2,5,4,5,12),suvs = c(4,4,6,6,16))

# Graph cars with specified labels for axes.  Use blue 
# borders and diagnal lines in bars.
barplot(autos_data$cars, main="Cars", xlab="Days",  
        ylab="Total", names.arg=c("Mon","Tue","Wed","Thu","Fri"), 
        border="blue", density=c(10,20,30,40,50))

# Graph the total number of autos per day using some color and show a legend:
# Graph autos with adjacent bars using rainbow colors
barplot(as.matrix(autos_data), main="Autos", ylab= "Total", beside=TRUE, col=rainbow(5))
# Place the legend at the top-left corner with no frame  
# using rainbow colors
legend("topleft", c("Mon","Tue","Wed","Thu","Fri"), cex=0.6, bty="n", fill=rainbow(5))
# Graph the total number of autos per day using a stacked bar chart and place the legend outside of the plot area:
# Expand right side of clipping rect to make room for the legend
par(xpd=TRUE, mar=par()$mar+c(0,0,0,4))

# Graph autos (transposing the matrix) using heat colors,  
# put 10% of the space between each bar, and make labels  
# smaller with horizontal y-axis labels
barplot(t(autos_data), main="Autos", ylab="Total", col=heat.colors(3), space=0.1, cex.axis=0.8, las=1,
        names.arg=c("Mon","Tue","Wed","Thu","Fri"), cex=0.8)
   
# Place the legend at (6,30) using heat colors
legend(6, 30, names(autos_data), cex=0.8, fill=heat.colors(3)) 
# Restore default clipping rect
par(mar=c(5, 4, 4, 2) + 0.1)

# Start with a simple histogram graphing the distribution of the suvs vector:
# Define the suvs vector with 5 values
suvs <- c(4,4,6,6,16)

# Create a histogram for suvs
hist(suvs)

# Plot a histogram of the combined car, truck, and suv data in color.
# Concatenate the three vectors
autos <- c(autos_data$cars, autos_data$trucks, autos_data$suvs)

# Create a histogram for autos in light blue with the y axis
# ranging from 0-10
hist(autos, col="lightblue", ylim=c(0,10))

# Now change the breaks so none of the values are grouped together and flip the y-axis labels horizontally.
# Compute the largest y value used in the autos
max_num <- max(autos)

# Create a histogram for autos with fire colors, set breaks
# so each number is in its own group, make x axis range from
# 0-max_num, disable right-closing of cell intervals, set
# heading, and make y-axis labels horizontal
hist(autos, col=heat.colors(max_num), breaks=max_num, 
     xlim=c(0,max_num), right=FALSE, main="Autos Histogram", las=1)

#Now let's create uneven breaks and graph the probability density.
# Create uneven breaks
brk <- c(0,3,4,5,6,10,16)

# Create a histogram for autos with fire colors, set uneven
# breaks, make x axis range from 0-max_num, disable right-
# closing of cell intervals, set heading, make y-axis labels 
# horizontal, make axis labels smaller, make areas of each
# column proportional to the count
hist(autos, col=heat.colors(length(brk)), breaks=brk, xlim=c(0,max_num),
     right=FALSE, main="Probability Density", las=1, cex.axis=0.8, freq = FALSE)

# Now plot the distribution of 1000 random values that have the log normal distribution
# Get a random log-normal distribution, plot in a histogram
rv <- rlnorm(1000)
hist(rv)

# Get the distribution without plotting it using tighter breaks
dist <- hist(rv, plot=FALSE, breaks=c(seq(0,max(rv)+1, .1)))

# Plot the distribution using log scale on both axes, and use blue points
plot(dist$counts, log="xy", pch=20, col="blue",
     main="Log-normal distribution", xlab="Value", ylab="Frequency")