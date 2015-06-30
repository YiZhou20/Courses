# Let's start with a simple pie chart graphing the cars vector:
# Define cars vector with 5 values
cars <- c(1, 3, 6, 4, 9)

# Create a pie chart for cars
pie(cars)
#Now let's add a heading, change the colors, and define our own labels:

# Create a pie chart with defined heading and
# custom colors and labels
pie(cars, col=rainbow(length(cars)), labels=c("Mon","Tue","Wed","Thu","Fri"))
title(main="Autos", col.main="red", font.main=4)
#Now let's change the colors, label using percentages, and create a legend:

# Define some colors ideal for black & white print
cols = c("white","azure2","darkgrey","dimgrey","black")
# Calculate the percentage for each day, rounded to one 
# decimal place
car_labels <- round(cars/sum(cars)*100,digit = 1)

# Concatenate a '%' char after each value
car_labels <- paste(car_labels, "%", sep="")

# Create a pie chart with defined heading and custom colors
# and labels
pie(cars, labels = car_labels, col = cols)
title(main="Autos", col.main="red", font.main=4)
# Create a legend at the right   
legend(1.5, 0.5, c("Mon","Tue","Wed","Thu","Fri"), cex=0.8, fill=cols)

#Let's start with a simple dotchart graphing the autos data:
# Read values from tab-delimited autos.dat
autos_data <- data.frame(cars = c(1,3,6,4,9),trucks = c(2,5,4,5,12),suvs = c(4,4,6,6,16))

# Create a dotchart for autos
dotchart(t(autos_data))
#Let's make the dotchart a little more colorful

# Create a colored dotchart for autos with smaller labels
dotchart(t(autos_data), color=c("red","blue","darkgreen"), main="Dotchart for Autos", cex=0.8)

#This example shows all 25 symbols that you can use to produce points in your graphs:
# Make an empty chart
plot(0,0,xlim = c(1,6),ylim = c(0,7), ann = FALSE)

# Plot digits 0-4 with increasing size and color
text(1:5, rep(6,5), labels=0:4, cex=1:5, col=1:5)
# Plot symbols 0-4 with increasing size and color
points(1:5, rep(5,5), cex=1:5, col=1:5, pch=0:4)
text((1:5)+0.1, rep(4.9,5), labels=0:4, cex=0.6)

# Plot symbols 5-9 with labels
points(1:5, rep(4,5), cex=2, pch=(5:9))
text((1:5)+0.1, rep(4,5), cex=0.6, (5:9))

# Plot symbols 10-14 with labels
points(1:5, rep(3,5), cex=2, pch=(10:14))
text((1:5)+0.1, rep(3,5), cex=0.6, (10:14))

# Plot symbols 15-19 with labels
points(1:5, rep(2,5), cex=2, pch=(15:19))
text((1:5)+0.1, rep(2,5), cex=0.6, (15:19))

# Plot symbols 20-25 with labels
points((1:6)*0.8+0.2, rep(1,6), cex=2, pch=(20:25))
text((1:6)*0.8+0.3, rep(1,6), cex=0.6, (20:25))