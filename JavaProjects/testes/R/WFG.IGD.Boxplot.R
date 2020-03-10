postscript("WFG.IGD.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
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
qIndicator(indicator, "WFG1")
qIndicator(indicator, "WFG2")
qIndicator(indicator, "WFG3")
qIndicator(indicator, "WFG4")
qIndicator(indicator, "WFG5")
qIndicator(indicator, "WFG6")
qIndicator(indicator, "WFG7")
qIndicator(indicator, "WFG8")
qIndicator(indicator, "WFG9")
