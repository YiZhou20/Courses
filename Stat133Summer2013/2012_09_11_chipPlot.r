# This plotting exercise introduces many of the features
# in the R graphics functions. 
# There are many more features to investigate.
# Documentation can be found by calling plot.default()
# and also by asking for help with the par function, i.e. ?par
# In addition, you might want to visit some of the R visualization
# web sites to learn more about making beautiful plots.
# These include:
# http://rdataviz.wordpress.com/
# http://gallery.r-enthusiasts.com/
# http://www.stat.auckland.ac.nz/~paul/RGraphics/rgraphics.html
# http://blog.revolutionanalytics.com/2009/01/10-tips-for-making-your-r-graphics-look-their-best.html

# We begin by loading in our data
chips = read.table("http://www.stat.berkeley.edu/users/nolan/data/chip04.txt", header = TRUE)

# Determine the names of the variables in the data frame
names(chips)

dim(chips)

head(chips)

# Make an initial plot of Transistors against Date
plot(chips$Date, chips$Transistors)

# Rather than plot points, make a line plot
# Also note that we use the parameter names in this call
# That way, we don't have to worry about which order they are in
plot(x = chips$Date, y = chips$Transistors, type = "l")

# The line is too thin so we specify the line width parameter, lwd
plot(chips$Date, chips$Transistors, type = "l", lwd = 3)

# We plot the data on a log scale to bank at 45 degrees and
# to fill the data region
plot(chips$Date, chips$Transistors, type = "l", 
     lwd = 3, col = "green", log = "y")


# An alternative to the log parameter is to transform the data,
# i.e. plot log(chips$Transistors) 
# The advantage to log = "y" is that the tick mark labels
# stay in the original units

# We next add axes labels to the plot
# We first store the labels in variables to make it 
# easier if we need to make the plot again.
ylabel = "Growth in comparison to 1975 - log scale"
xlabel = "Year"

# We also transform the data so that we examine relative growth
# This way, we can add multiple line graphs to the same canvas
# because they will be unitless.
trans = chips$Transistors/chips$Transistors[1]
plot(chips$Date, trans, type ="l", lwd = 3, 
     col = "green", log = "y", ylab = ylabel, 
     xlab = xlabel)

# We transform the rest of the variables into relative growth
names(chips)
chipsN = chips[ , c(3,4,5,7)]

chipsC = lapply(chipsN, function(x) x/x[1])
chipsC$Microns = 1/chipsC$Microns
chipsC = as.data.frame(chipsC)

# The matplot function allows us to plot all the variables
# in a data frame on the same canvas, i.e. superposed.
matplot(chips$Date, chipsC)
matplot(chips$Date, chipsC, type = "l")
matplot(chips$Date, chipsC, type = "l", log = "y")
matplot(chips$Date, chipsC, type = "l", log = "y", 
        lwd = 2, lty = 1)

cols = c("black", "red", "green", "blue")
titleP = "Intel Microprocessor"

matplot(chips$Date, chipsC, type = "l", log = "y", 
        lwd = 2, lty = 1, 
        ylab = ylabel, xlab = xlabel, main = titleP)

# Note that we could have made a plot of the first variable
# and then added additional line graphs with the points()
# function with type = "l" (or used the lines() function)

# We next add a legend. 
legend(1976, 1000, legend = names(chipsC), fill = cols)

# We play around a bit with the legend location
# We also remove the box from around the legend so it 
# doesn't get in the way (obscure the data)
matplot(chips$Date, chipsC, type = "l", log ="y", 
        lwd = 2, lty =1, 
        ylab = ylabel, xlab = xlabel, main = titleP)
legend(1976, 10000, 
       legend = names(chipsC), fill = cols, bty = "n")

# Lastly, we add refernce lines and text to the plot
# Specifically, we use abline to add a vertical line at 1993
# and at 1985. We add text next to the lines to indicate
# the processor that was released at that date.
matplot(chips$Date, chipsC, type = "l", log ="y", 
        lwd = 2, lty = 1, 
        ylab = ylabel, xlab = xlabel, main = titleP)
legend(1976, 10000, 
       legend = names(chipsC), fill = cols, bty = "n")

# We can either specify a vertical line with v,  
# a horizontal line with h, or a line with any slope 
# and intercept with the a and b paramters
abline(v = 1993, col = "grey")
abline(v = 1985, col = "grey")

# The mtext function places text in the margin of the plot.
# The side parameter determines which margin, 1 is bottom,
# 2 is left, 3 is top, and 4 is the right side.
# The text() function places text in the plotting region
# With text, you specify the x,y location of the text
mtext(text = "Pentium", side = 3, line = -1.2, 
      at = 1993 + 0.2, adj = 0)
mtext(text = "32 bit proc", side = 3, line = -1.2, 
      at = 1985 + 0.2, adj = 0)


