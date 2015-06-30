### YOUR NAME HERE

###### 
# 5 GETTING THE DATA frame AllData
# 1 pt for extracting infant mortality
# 1 pt for extracting population
# 2 points for merging these
# 1 pt for merging with lat lon

# 5 Making the pushpin plot
# 2 pt for creating makeBaseDocument
# 2 pt for adding correct code to addPlacemark() 
# 1pt for creating the document 


# 5 Making the fancy plot
# 1 pt for cutting the variables
# 2 pts for adding styles to document
# 2 pts for creating the document correctly
#### 

### Load HW6.rda and attach the XML library


### Part 1.  Create the data frame
### Look at the instructions in HW6.pdf.
### Functions you'll want to use: xmlParse(), xmlRoot(), xpathSApply(), xmlGetAttr().
### It also might make it easier to use: xmlToList(), merge().

### Load the data frame called LatLon from HW6.rda.  
load("http://www.stanford.edu/~vcs/stat133/HW6.rda")

### Parse the XML document at:
### http://www.stat.berkeley.edu/users/nolan/stat133/data/factbook.xml
### and create an XML "tree" in R 
library(XML)
xd = xmlParse("http://www.stanford.edu/~vcs/stat133/factbook.xml")
### Use XPath to extract the infant mortality and the CIA country codes from the XML tree
### 
xr = xmlRoot(xd)
ranks = getNodeSet(xr, "//field[@id='f2091']/rank")
im = sapply(ranks, xmlGetAttr, "number")
imC = sapply(ranks, xmlGetAttr, "country")
### Create a data frame called IM using this XML file.
### The data frame should have 2 columns: for Infant Mortality and CIA.Codes.

IM = data.frame(im = as.numeric(im), ctry = imC, 
                stringsAsFactors = FALSE)

### Extract the country populations from the same XML document
### Create a data frame called Pop using these data.
### This data frame should also have 2 columns, for Population and CIA.Codes.

ranks = getNodeSet(xr, "//field[@id='f2119']/rank")
pop = sapply(ranks, xmlGetAttr, "number")
popC = sapply(ranks, xmlGetAttr, "country")

Pop = data.frame(pop = as.numeric(pop), ctry = popC, 
                stringsAsFactors = FALSE)
### Merge the two data frames to create a data frame called IMPop with 3 columns:
### IM, Pop, and CIA.Codes

IMPop = merge(IM, Pop, by.x = "ctry", by.y = "ctry")

### Now merge IMPop with LatLon (from HW6.rda) to create a data frame called AllData that has 6 columns
### for Latitude, Longitude, CIA.Codes, Country Name, Population, and Infant Mortality

Locs = LatLon
Locs$Latitude = LatLon$Longitude
Locs$Longitude = LatLon$Latitude
Locs$CIA.Codes = as.character(tolower(LatLon$CIA.Codes))
AllData = merge(IMPop, Locs, by.x = "ctry", by.y = "CIA.Codes")


### Part 2.  Create a KML document
### Make the KML document described in HW6.pdf.  It should have the basic
### structure shown in that document.  You can use the addPlacemark function below to make
### the Placemark nodes, you just need to complete the line for the Point node and
### figure out how to use the function.

makeBaseDocument = function(){
### This code creates the template KML document 
  xd = newXMLDoc()
  xr = newXMLNode("kml", doc = xd)
  doc = newXMLNode("Document", parent = xr)
  newXMLNode("name", "Country Facts", parent = doc)
  newXMLNode("description", "Infant mortality", parent = doc)
  LA = newXMLNode("LookAt", parent = doc)
  newXMLNode("longitude", "-121", parent = LA)
  newXMLNode("latitude", "43", parent = LA)
  newXMLNode("altitude", "4100000", parent = LA)
  newXMLNode("tilt", "0", parent = LA)
  newXMLNode("heading", "0", parent = LA)
  newXMLNode("altitudeMode", "absolute", parent = LA)
  newXMLNode("Folder", parent = doc)
  return(xd)
}

addPlacemark = function(lat, lon, ctryCode, ctryName, pop, infM, 
                        parent, 
                        inf1 = NULL, pop1 = NULL, style = FALSE)
{
  pm = newXMLNode("Placemark", 
                  newXMLNode("name", ctryName), attrs = c(id = ctryCode), 
                  parent = parent)
  if (style) newXMLNode("styleUrl", 
                       paste("#YOR", inf1, "-", pop1, sep = ''), 
                       parent = pm)
  newXMLNode("description", paste(ctryName, "\n Population: ", pop, 
                                  "\n Infant Mortality: ", infM, sep =""),
             parent = pm)

  newXMLNode("Point", newXMLNode("coordinates", 
                                 paste(lon, lat, 0, sep=",")),
             parent = pm)
             
### You need to fill in the code for making the Point node above, including coordinates.
### The line below won't work until you've run the code for the next section to set up
### the styles.

  
}

### Save your KML document here, call it Part2.kml, and open it in Google Earth.
### (You will need to install Google Earth.)  
### It should have pushpins for all the countries.

ver1 = makeBaseDocument()
ver1R = xmlRoot(ver1)
for (i in 1:nrow(AllData)){
  addPlacemark(lat = AllData$Lat[i],
               lon = AllData$Lon[i],
               ctryCode = AllData$ctryCode[i],
               ctryName = AllData$Country.Name[i],
               pop = AllData$pop[i], infM = AllData$im[i],
               parent = ver1R[["Document"]][["Folder"]])
}

saveXML(ver1, file = "Part2.kml")

### Part 3.  Add Style to your KML
### Now you are going to make the visualizatiion a bit fancier.  Pretty much all the code is given to you
### below to create style elements that are to be placed near the top of the document.
### These , you just need to figure out what it all does.

### Start fresh with a new KML document, by calling makeBaseDocument()

doc2 = makeBaseDocument()
doc2R = xmlRoot(doc2)

### The following code is an example of how to create cut points for 
### different categories of infant mortality and population size.
### Figure out what cut points you want to use and modify the code to create these 
### categories.
infCut = cut(AllData$im, breaks=c(0, 37, 50, 65, 80, 200))
infCut = as.numeric(infCut)
breaks = c(0, 20000000, 50000000, 100000000, 200000000, 2000000000)
popCut = cut((AllData$pop), breaks = breaks)
popCut = as.numeric(popCut)

### Now figure out how to add styles and placemarks to doc2
### You'll want to use the addPlacemark function with style = TRUE

### Below is code to make style nodes. 
### You should not need to do much to it.

### You do want to figure out what scales to you for the sizes of your circles
scales = c(0.5, 1, 3, 5, 8)

addStyle = function(col1, pop1, parent, urlBase, scales = scales)
{
  st = newXMLNode("Style", 
                  attrs = c("id" = paste("YOR", col1, "-", pop1, sep="")), 
                  parent = parent)
  newXMLNode("IconStyle", 
             newXMLNode("scale", scales[pop1]), 
             newXMLNode("Icon", paste(urlBase, "yor", col1, "ball.png", sep ="")), 
             parent = st)
}


DocNode = doc2R[["Document"]]
for (k in 1:5)
{
  for (j in 1:5)
  {
    addStyle(j, k, parent = DocNode, scales = scales,
             'http://www.stanford.edu/~vcs/stat133/circles/')
  }
}

Folder = DocNode[["Folder"]]
for (i in 1:nrow(AllData)){
  addPlacemark(lat = AllData$Lat[i],
               lon = AllData$Lon[i],
               ctryCode = AllData$ctryCode[i],
               ctryName = AllData$Country.Name[i],
               pop = AllData$pop[i], infM = AllData$im[i],
               inf1 = infCut[i], pop1 = popCut[i],
               parent = Folder, style = TRUE)
}
### You will need to figure out what order to call addStyle() and addPlacemark()
### so that the tree is built properly 

### Finally, save your KML document, call it Part3.kml and open it in Google Earth to 
### verify that it works.  For this assignment, you only need to submit your code, 
### nothing else.  You can assume that the grader has already loaded HW6.rda.
saveXML(doc2, file = "Part3.kml")
