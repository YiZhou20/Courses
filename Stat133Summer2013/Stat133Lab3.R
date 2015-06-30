#First we'll produce a very simple graph using the values in the cars vector:

# Define the cars vector with 5 values
cars <- c(1, 3, 6, 4, 9)

# Graph the cars vector with all defaults
plot(cars)

#add a title, a line to connect the points, and some color
plot(cars,type = "l", col = "blue", main = "Cars")

# Graph cars using blue points overlayed by a line 
plot(cars, type = "b",pch = 16, col = "blue")

# Create a title with a red, bold/italic font
title(main="Autos", col.main="red", font.main=4) #hint

#Now let's add a red line for trucks and specify the y-axis range directly so 
#it will be large enough to fit the truck data:

# Define 2 vectors
cars <- c(1, 3, 6, 4, 9)
trucks <- c(2, 5, 4, 5, 12)

# Graph cars using a y axis that ranges from 0 to 12
plot(cars, ylim = c(0,12), type = "b",pch = 16, col = "blue")

# Graph trucks with red dashed line and square points
lines(trucks, type = "b", lty = 2, pch = 15, col = "red")

# Create a title with a red, bold/italic font
title(main="Autos", col.main="red", font.main=4)

#Next let's change the axes labels to match our data and add a legend. 
#We'll also compute the y-axis values using the max function so any 
#changes to our data will be automatically reflected in our graph.
# Define 2 vectors
cars <- c(1, 3, 6, 4, 9)
trucks <- c(2, 5, 4, 5, 12)

# Calculate range from 0 to max value of cars and trucks
yMax = max(c(cars,trucks),na.rm = TRUE)

# Graph autos using y axis that ranges from 0 to max 
# value in cars or trucks vector.  Turn off axes and 
# annotations (axis labels) so we can specify them ourself
plot(cars,ylim = c(0,yMax),type = "b",pch = 16,col = "blue",
     xaxt="n",yaxt="n",ann = FALSE)
lines(trucks, type = "b", lty = 2, pch = 15, col = "red")
title(main="Autos", col.main="red", font.main=4)
# Make x axis using Mon-Fri labels
axis(side = 1, at = 1:5, labels = c("Mon","Tue","Wed","Thu","Fri"))

# Make y axis with horizontal labels that display ticks at 
# every 4 marks. 4*0:g_range[2] is equivalent to c(0,4,8,12).
axis(side = 2, at = seq(0,yMax,by=4),las = 1)

# Create box around plot
# Graph trucks with red dashed line and square points
# Create a title with a red, bold/italic font
# Label the x and y axes with dark green text
title(xlab = "Weekdays", ylab = "Number of Vehicles", col.lab = "darkgreen")

# Create a legend at (1, g_range[2]) that is slightly smaller 
# (cex) and uses the same line colors and points used by 
# the actual plots 
legend(1, 12, legend = c("Cars","Trucks"),
       col = c("blue","red"), pch = c(16,15))