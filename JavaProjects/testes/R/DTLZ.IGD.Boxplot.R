postscript("DTLZ.IGD.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data/"
qIndicator <- function(indicator, problem)
{
fileNSGAII<-paste(resultDirectory, "NSGAII", sep="/")
fileNSGAII<-paste(fileNSGAII, problem, sep="/")
fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
NSGAII<-scan(fileNSGAII)

algs<-c("NSGAII")
boxplot(NSGAII,names=algs, notch = FALSE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(3,3))
indicator<-"IGD"
qIndicator(indicator, "DTLZ1")
qIndicator(indicator, "DTLZ2")
qIndicator(indicator, "DTLZ3")
qIndicator(indicator, "DTLZ4")
qIndicator(indicator, "DTLZ5")
qIndicator(indicator, "DTLZ6")
qIndicator(indicator, "DTLZ7")
