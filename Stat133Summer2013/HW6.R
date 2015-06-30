### YOUR NAME HERE Yi Zhou

### Load HW6.rda and attach the XML library
load("C:/Users/Yi/Downloads/HW6.rda")
library(XML)
### Part 1.  Create the data frame
### Look at the instructions in HW6.pdf.
### Functions you'll want to use: xmlParse(), xmlRoot(), xpathSApply(), xmlGetAttr().
### It also might make it easier to use: xmlToList(), merge().

### Parse the XML document at:
### http://www.stanford.edu/~vcs/stat133/factbook.xml
### and create an XML "tree" in R 
raw = xmlParse("http://www.stanford.edu/~vcs/stat133/factbook.xml")
catalog = xmlRoot(raw)
### Use XPath to extract the infant mortality and the CIA country codes from the XML tree
mortality = xpathSApply(catalog, "//field[@name='Infant mortality rate']/rank/@number")
Mcodes = xpathSApply(catalog, "//field[@name='Infant mortality rate']/rank/@country")
### Create a data frame called IM using this XML file.
### The data frame should have 2 columns: for Infant Mortality and CIA.Codes.
CIA.Codes = toupper(unname(Mcodes))
Infant.Mortality = as.numeric(unname(mortality))
IM = data.frame(Infant.Mortality,CIA.Codes)
### Extract the country populations from the same XML document
### Create a data frame called Pop using these data.
### This data frame should also have 2 columns, for Population and CIA.Codes.
Pop = xpathSApply(catalog, "//field[@name='Population']/rank/@number")
Pcodes = xpathSApply(catalog, "//field[@name='Population']/rank/@country")
CIA.PCodes = toupper(unname(Pcodes))
Population = as.numeric(unname(Pop))
Pop = data.frame(Population,CIA.PCodes)
### Merge the two data frames to create a data frame called IMPop with 3 columns:
### IM, Pop, and CIA.Codes
IMPop = merge(IM,Pop,by.x="CIA.Codes",by.y="CIA.PCodes")
IMPop = IMPop[,c(2,3,1)]
### Now merge IMPop with LatLon (from HW6.rda) to create a data frame called AllData that has 6 columns
### for Latitude, Longitude, CIA.Codes, Country Name, Population, and Infant Mortality
AllData = merge(IMPop,LatLon,by.x="CIA.Codes",by.y="CIA.Codes")
AllData = AllData[,c(6,5,1,4,3,2)]
### Part 2.  Create a KML document
### Make the KML document described in HW6.pdf.  It should have the basic
### structure shown in that document.  You can use the addPlacemark function below to make
### the Placemark nodes, you just need to complete the line for the Point node and
### figure out how to use the function.

makeBaseDocument = function(){
### This code creates the template KML document 

  doc<-newXMLDoc()
  root<-newXMLNode("Document", doc = doc)
  child1<-newXMLNode("name", "Country Facts",parent = root)
  child2<-newXMLNode("description","Infant Mortality",parent = root)
  child3<-newXMLNode("LookAt",parent = root)
  LA1<-newXMLNode("longitude","-121",parent = child3)
  LA2<-newXMLNode("latitude","43",parent = child3)
  LA3<-newXMLNode("altitude","4100000",parent = child3)
  LA4<-newXMLNode("tilt","0",parent = child3)
  LA5<-newXMLNode("heading","0",parent = child3)
  LA6<-newXMLNode("altitudeMode","absolute",parent = child3)
  child4<-newXMLNode("Style",attrs = c(id = "YOR5"),parent = root)
  St<-newXMLNode("IconStyle",parent = child4)
  IS1<-newXMLNode("scale","0.525",parent = St)
  IS2<-newXMLNode("Icon","http://www.stat.berkeley.edu/users/nolan/data/KML/circles/yor5ball.png",parent = St)
  child5<-newXMLNode("Folder",parent = root)
  Fo1<-newXMLNode("name","CIA Fact Book",parent = child5)
  
  return(doc)

}

addPlacemark = function(lat, lon, ctryCode, ctryName, pop, infM, parent, 
                        inf1, pop1, style = FALSE)
{
  pm = newXMLNode("Placemark", 
                  newXMLNode("name", ctryName), attrs = c(id = ctryCode), 
                  parent = parent)
  newXMLNode("description", paste(ctryName, "\n Population: ", pop, 
                                  "\n Infant Mortality: ", infM, sep =""),
             parent = pm)

  newXMLNode("Point",newXMLNode("coordinates", paste(lat, lon, 0, sep=", ")), parent = pm)
             
### You need to fill in the code for making the Point node above, including coordinates.
### The line below won't work until you've run the code for the next section to set up
### the styles.

  if(style) newXMLNode("styleUrl", paste("#YOR", inf1, "-", pop1, sep = ''), parent = pm)
}

PlM = makeBaseDocument()
folder = getNodeSet(PlM, "//Folder")

for(i in 1:nrow(AllData)){
  
  addPlacemark(lat = AllData[,1][i], lon = AllData[,2][i], ctryCode = AllData[,3][i], 
               ctryName = AllData[,4][i], pop = AllData[,5][i], infM = AllData[,6][i], 
               parent = folder[[1]], inf1 = inf1[i], pop1 = pop1[i])
}

### Save your KML document here, call it Part2.kml, and open it in Google Earth.
### (You will need to install Google Earth.)  
### It should have pushpins for all the countries.
saveXML(PlM, file="Part2.kml")
### Part 3.  Add Style to your KML
### Now you are going to make the visualizatiion a bit fancier.  Pretty much all the code is given to you
### below to create style elements that are to be placed near the top of the document.
### These , you just need to figure out what it all does.

### Start fresh with a new KML document, by calling makeBaseDocument()

doc2 = makeBaseDocument()

### The following code is an example of how to create cut points for 
### different categories of infant mortality and population size.
### Figure out what cut points you want to use and modify the code to create these 
### categories.
#infCut = cut(x[,5], breaks = c(0, 10, 25, 50, 75, 200))
#infCut = as.numeric(infCut)
#popCut = cut(log(x[,6]), breaks = 5)
#popCut = as.numeric(popCut)

infCut = cut(AllData[,6], breaks = c(0, 10, 25, 50, 75, 200))
infCut = as.numeric(infCut)
popCut = cut(log(AllData[,5]), breaks = 5)
popCut = as.numeric(popCut)

### Now figure out how to add styles and placemarks to doc2
### You'll want to use the addPlacemark function with style = TRUE
Newfolder = getNodeSet(doc2, "//Folder")

for(i in 1:nrow(AllData)){
  addPlacemark(lat = AllData[,1][i], lon = AllData[,2][i], ctryCode = AllData[,3][i], 
               ctryName = AllData[,4][i], pop = AllData[,5][i], infM = AllData[,6][i], 
               parent = Newfolder[[1]], inf1 = infCut[i], pop1 = popCut[i],style = T)
}
### Below is code to make style nodes. 
### You should not need to do much to it.

### You do want to figure out what scales to you for the sizes of your circles
scales = c(0.5, 1, 3, 5, 10)

addStyle = function(col1, pop1, parent, urlBase, scales = scales)
{
  st = newXMLNode("Style", attrs = c("id" = paste("YOR", col1, "-", pop1, sep="")), parent = parent)
  newXMLNode("IconStyle", 
			 newXMLNode("scale", scales[pop1]), 
		     newXMLNode("Icon", paste(urlBase, "yor", col1, "ball.png", sep ="")), parent = st)
}

for (k in 1:5)
{
  for (j in 1:5)
  {
    addStyle(j, k, xmlRoot(doc2), 'http://www.stanford.edu/~vcs/stat133/circles/', scales)
  }
}

### You will need to figure out what order to call addStyle() and addPlacemark()
### so that the tree is built properly. You may need to adjust the code to call the png files
# addStyle first and then addPlacemark
### Finally, save your KML document, call it Part3.kml and open it in Google Earth to 
### verify that it works.  For this assignment, you only need to submit your code, 
### nothing else.  You can assume that the grader has already loaded HW6.rda.
saveXML(doc2, file="Part3.kml")