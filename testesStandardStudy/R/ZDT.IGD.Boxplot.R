postscript("ZDT.IGD.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
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
par(mfrow=c(3,2))
indicator<-"IGD"
qIndicator(indicator, "ZDT1")
qIndicator(indicator, "ZDT2")
qIndicator(indicator, "ZDT3")
qIndicator(indicator, "ZDT4")
qIndicator(indicator, "ZDT6")
