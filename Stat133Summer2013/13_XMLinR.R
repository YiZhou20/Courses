
library(XML)
docP = xmlParse("http://www.stat.berkeley.edu/~nolan/stat133/data/plant_catalog.xml")
root = xmlRoot(docP)
xmlSize(root)
#[1] 36
root[[1]]
#<PLANT>
#  <COMMON>Bloodroot</COMMON>
#  <BOTANICAL>Sanguinaria canadensis</BOTANICAL>
#  <ZONE>4</ZONE>
#  <LIGHT>Mostly Shady</LIGHT>
#  <PRICE>$2.44</PRICE>
#  <AVAILABILITY>031599</AVAILABILITY>
#</PLANT>


root[[3]]
#<PLANT>
#  <COMMON>Marsh Marigold</COMMON>
#  <BOTANICAL>Caltha palustris</BOTANICAL>
#  <ZONE>4</ZONE>
#  <LIGHT>Mostly Sunny</LIGHT>
#  <PRICE>$6.81</PRICE>
#  <AVAILABILITY>051799</AVAILABILITY>
#</PLANT> 

root[[3]][['COMMON']]
#<COMMON>Marsh Marigold</COMMON>
  
xmlValue(root[[3]][['COMMON']])
#[1] "Marsh Marigold"


varNames = names(root[[1]])
varNames
#        COMMON      BOTANICAL           ZONE          LIGHT 
#      "COMMON"    "BOTANICAL"         "ZONE"        "LIGHT" 
#         PRICE   AVAILABILITY 
#       "PRICE" "AVAILABILITY" 

getVar = function(x, var) xmlValue(x[[var]])
res = lapply(varNames, function(var) {
   xmlSApply(root, getVar, var) })

plants = data.frame(res)
head(plants)
#               COMMON              BOTANICAL ZONE        LIGHT PRICE
#1           Bloodroot Sanguinaria canadensis    4 Mostly Shady $2.44
#2           Columbine   Aquilegia canadensis    3 Mostly Shady $9.37
#3      Marsh Marigold       Caltha palustris    4 Mostly Sunny $6.81
#4             Cowslip       Caltha palustris    4 Mostly Shady $9.90
#5 Dutchman's-Breeches    Dicentra cucullaria    3 Mostly Shady $6.44
#6        Ginger, Wild       Asarum canadense    3 Mostly Shady $9.03


common = xpathSApply(root, "/CATALOG/PLANT/COMMON", xmlValue)
head(common)
#[1] "Bloodroot"           "Columbine"           "Marsh Marigold"     
#[4] "Cowslip"             "Dutchman's-Breeches" "Ginger, Wild"  

xpx = paste("/CATALOG/PLANT/", varNames, sep="")
res = sapply(xpx, function(var)xpathSApply(root, var, xmlValue))

res[1:2, 1:3]
#     /CATALOG/PLANT/COMMON /CATALOG/PLANT/BOTANICAL /CATALOG/PLANT/ZONE
#[1,] "Bloodroot"           "Sanguinaria canadensis" "4"                
#[2,] "Columbine"           "Aquilegia canadensis"   "3"  


docEx = newXMLDoc()
root = newXMLNode("toplevel", doc = docEx)
child1 = newXMLNode("level1", parent = root)
newXMLNode("level2", "This is the content", parent = child1)
saveXML(docEx, file = "simple.xml")
doc
#<?xml version="1.0"?>
#<toplevel>
#  <level1>
#    <level2>This is the content</level2>
#  </level1>
#</toplevel>


docKML = newXMLDoc()
docKML
#<?xml version="1.0"?>
root = newXMLNode("kml", doc = docKML)
#<kml/>
doct = newXMLNode("Document", parent = root)
root
#<kml>
#  <Document/>
#</kml> 

newXMLNode("name", "Earthquakes", parent = doct)
root
#<kml>
#  <Document>
#    <name>Earthquakes</name>
#  </Document>
#</kml> 

newXMLNode("description", "6+ Earthquakes, 1968-2008", parent = doct)
folder = newXMLNode("Folder", parent = doct)
folder
#<Folder>
#  <name>Quakes</name>
#</Folder> 

root
#<kml>
#  <Document>
#    <name>Earthquakes</name>
#    <description>6+ Earthquakes, 1968-2008</description>
#    <Folder>
#      <name>Quakes</name>
#    </Folder>
#  </Document>
#</kml> 

newXMLNode("name", "Quakes", parent = folder)

las = c(-124.95, -118.83)
los = c(41.04, 37.59)
for (i in 1:length(las)){
newXMLNode("Placemark", newXMLNode("Point", 
                        newXMLNode("coordinates", 
                                   paste(las[i], los[i], 0, sep=","))), 
            parent = folder)
}
docKML
#<?xml version="1.0"?>
#<kml>
#  <Document>
#    <name>Earthquakes</name>
#    <description>6+ Earthquakes, 1968-2008</description>
#    <Folder>
#      <name>Quakes</name>
#      <Placemark>
#        <Point>
#          <coordinates>-124.95,41.04,0</coordinates>
#        </Point>
#      </Placemark>
#      <Placemark>
#        <Point>
#          <coordinates>-118.83,37.59,0</coordinates>
#        </Point>
#      </Placemark>
#    </Folder>
#  </Document>
#</kml>

saveXML(docKML, "smallQuake.kml")
# [1] "smallQuake.kml"

