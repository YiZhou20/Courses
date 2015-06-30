bagOfWords = c("storm","push","president","race", 
               "spotlight", "romney", "lead", "like",
               "vote", "run", "mislead","ad","obama","close")

A = c(1,1,1,1,1,0,0,0,0,0,0,0,0,0)
B = c(0,0,0,1,0,1,1,1,1,0,0,0,0,0)
C = c(0,0,0,0,0,0,0,0,1,1,1,1,0,0)
D = c(0,0,1,2,0,1,0,0,0,1,0,0,1,1)

termFreq = matrix(c(A,B,C,D), ncol=4, byrow = FALSE)

wordsInDoc = apply(termFreq, 2, sum)

normTermFreq = matrix(0, nrow = nrow(termFreq), 
                      ncol = ncol(termFreq))

for (i in 1:ncol(termFreq)) {
  normTermFreq[, i] = termFreq[, i]/wordsInDoc[i]
}

idf = apply(termFreq, 1, function(x) sum(x > 0))


simMatrix = computeSJDistance(tf = termFreq, 
                              df = idf, terms = bagOfWords, 
                              logdf = FALSE)

rownames(simMatrix) = c("A","B","C","D")
colnames(simMatrix) = c("A","B","C","D")

documents = as.dist(simMatrix)
hc = hclust(documents)
plot(hc)

hc2 = hclust(documents,"single" )
plot(hc2)

mds =cmdscale(simMatrix)
plot(mds, type = "n", xlab = "", ylab = "", main="Documents")
text(mds, rownames(simMatrix))
