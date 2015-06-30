################################################################################

library(XML)

### Simple example from slide 36 in lecture note #13
doc = newXMLDoc()
root = newXMLNode("toplevel", doc = doc)
child1 = newXMLNode("level1", parent = root)
newXMLNode("level2", "This is the content", parent = child1)
doc
saveXML(doc, file = "simple.xml")

### Use system() or shell.exec()
system("open C:/Users/Henry/Documents/simple.xml")
shell.exec("file:///C:/Users/Henry/Documents/simple.xml")

################################################################################

con <- xmlOutputBuffer(nsURI="http://www.opengis.net/kml/2.2",
      nameSpace="")
?xmlOutputBuffer

con$addTag("kml",close=FALSE)
  con$addTag("Placemark",close=FALSE)
    con$addTag("name",  "SISG")
    con$addTag("description", "Registration, Coffee, Snacks")
    con$addTag("Point", close=FALSE)
      con$addTag("coordinates", "-122.3093834881461,47.6496137385853,0")
    con$closeTag() # Point
  con$closeTag() # Placemark
con$closeTag() #kml

cat(con$value(), file="tempcoffee.kml")

shell.exec("file:///C:/Users/Henry/Documents/tempcoffee.kml")

################################################################################

# library("XML")
# first, reprise the CRANberries and biomaRt examples

cr     <- readLines("http://dirk.eddelbuettel.com/cranberries/index.rss",
encoding="ISO-8859-1")
head(cr,20) # nasty

cr     <- iconv(cr, to="UTF-8") # convert to another encoding
head(cr) # fairly nasty

crt    <- xmlTreeParse(cr, useInternal=TRUE)
str(crt) # less nasty
### str() compactly displays the internal structure of an R object, alternative to summary

titles <- xpathApply(crt, "/rss/channel/item/title")
str(titles)

titlelist <- sapply(titles, xmlToList)
titlelist

length(grep("New package", titlelist))
### count the number of strings that contain the key words

################################################################################

source("http://bioconductor.org/biocLite.R")
biocLite("biomaRt")
library("biomaRt")
ens <- useMart("ensembl")

listDatasets(ens) # 61 not 50!

human <- useDataset("hsapiens_gene_ensembl",mart=ens)
mouse <- useDataset("mmusculus_gene_ensembl",mart=ens)

getLDS(attributes = c("hgnc_symbol","chromosome_name",
"start_position"),
filters = "hgnc_symbol", values = "NOX1",
mart = human,
attributesL =c("chromosome_name","start_position"),
martL = mouse)

################################################################################

## Q1 -- writing KML files
## this version uses the XML package, not just cat(), paste() etc

kml<-function(lat, long, name){
  con <- xmlOutputBuffer(nsURI="http://www.opengis.net/kml/2.2", 
                         nameSpace="")
  con$addTag("kml",close=FALSE)
  con$addTag("Document",close=FALSE)
  for(i in 1:length(lat)){
    con$addTag("Placemark",close=FALSE) 
    con$addTag("name", name[i]) 
    con$addTag("Point", close=FALSE) 
    con$addTag("coordinates", paste(long[i],lat[i],2000,sep=","))
    con$closeTag() # point 
    con$closeTag() # placemark
  }
  con$closeTag() # Document
  con$closeTag() # kml
  con$value()
}

# get airport data - each in http://www.stanford.edu/~vcs/stat133
f <- read.csv("http://www.stanford.edu/~vcs/stat133/SEAflights.csv")
head(f)

g <- read.csv("http://www.stanford.edu/~vcs/stat133/airportlocations.csv")
head(g)

g2 <- g
names(g2) <- c("Origin", "o.lat", "o.long")

g3 <- g
names(g3) <- c("Dest", "d.lat", "d.long")

h <-  merge(f, g2, by="Origin")
h2 <- merge(h, g3, by="Dest")

### Get the mean by origin
delays<-aggregate(
	h2[,c("ArrDelay","DepDelay","AirTime")], 
	list(h2$Origin), mean, na.rm=TRUE) 

plot(ArrDelay~DepDelay, data=delays)

airports<-read.csv("http://www.stanford.edu/~vcs/stat133/airportlocations.csv")

# function to write KML file given airport code
airportKML<-function(abbrev){
   wanted<-subset(airports, locationID %in% abbrev)
   if (!all(abbrev %in% airports$locationID ))
     warning("some airports not found")
   kml(wanted$Latitude,-wanted$Longitude, wanted$locationID)
}
### We subset the airports data with location ID == abbrev
### Create an warning message if we find the subset does not contain all the abbrev ID

# function to do the identify() parts - system() starts Google Earth
# - the default application for KML, on this system
# - you could also use shell.exec() for the same job

#install.packages("XML") # if you need to
library("XML")
repeat({
  selected<-with(delays, identify(DepDelay, ArrDelay, labels=as.character(Group.1),n=1) )
  cat(airportKML(delays$Group.1[selected]),file="temp.kml")
  system("open C:/Users/Henry/Documents/temp.kml")
})

################################################################################

## Q2 -- extracting some parts of XML structures
#2. Read in the XML file phiSITE767857.xml from http://www.stanford.edu/~vcs/stat133, 
#which describes promoter sites for a set of bacteriophage viruses (from phisite.org). 
#(a) Use xpathApply() to extract the organism name for each site 
#(/phisite/site/organism/name) and the sequence (/phisite/site/sequence)

phage<-xmlInternalTreeParse(readLines("http://www.stanford.edu/~vcs/stat133/phiSITE767857.xml"))

xpathApply(phage, "/phisite/site/organism/name", xmlValue)

table(unlist(xpathApply(phage, "/phisite/site/organism/name", xmlValue)))

#which one is longest?

xpathApply(phage, "/phisite/site/sequence", xmlValue)

ord <- order(
	-sapply(
		xpathApply(phage, "/phisite/site/sequence", xmlValue)
	, nchar)
) ### the negative sign makes it in descending order of nchar

xpathApply(phage, "/phisite/site/sequence", xmlValue)[[ord[1]]] ### the first one is the longest one

max(sapply(xpathApply(phage, "/phisite/site/sequence", xmlValue), nchar)) ### the length of the longest one

################################################################################

## Q3  -- which promoter sites have experimental evidence?
#3. Using the phiSITE767857.xml file, extract the sequence for promoter sites that 
#have experimental evidence using xpathApply().
#Notes:
#- xpathApply() takes a function as its third argument, which is passed each of the 
#XML elements returned by the xpath. 
#- If site is an element of type ‘site’ then the element 
#site[["evidence"]][["type"]] is the type of evidence (experimental or predicted) 
#for the site
#- xmlValue() extracts the actual content from an XML element (e.g. a string)
#- The sequence element for a site is site[["sequence"]]

have.exper.edi <- xpathApply(phage, "/phisite/site", 
   function(e) {
     if(xmlValue(e[["evidence"]][["type"]])=="experimental") 
          xmlValue(e[["sequence"]])
}
)

have.exper.edi # nasty format
# nicer format - and only the first 20 lines

head(
unlist( 
   lapply(
     have.exper.edi, 
     function(x){ ifelse(is.null(x), NA, x) }
     )
   ), 20)

################################################################################
