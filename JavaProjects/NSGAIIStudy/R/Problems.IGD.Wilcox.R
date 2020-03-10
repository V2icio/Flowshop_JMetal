write("", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex",append=FALSE)
resultDirectory<-"/home/usuario/JavaProjects/NSGAIIStudy/data"
latexHeader <- function() {
  write("\\documentclass{article}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\title{StandardStudy}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\usepackage{amssymb}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{document}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\maketitle", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\section{Tables}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\caption{", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write(problem, "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write(".IGD.}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)

  write("\\label{Table:", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write(problem, "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write(".IGD.}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)

  write("\\centering", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{scriptsize}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{tabular}{", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write(tabularString, "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write(latexTableFirstLine, "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\hline ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
}

latexTableTail <- function() { 
  write("\\hline", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\end{tabular}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\end{scriptsize}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  write("\\end{table}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("-- ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  }
  else if (i < j) {
    if (wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE) 
      }
    }
    else {
      write("--", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
  }
}

### START OF SCRIPT 
# Constants
problemList <-c("Flowshop") 
algorithmList <-c("NSGAIIa", "NSGAIIb", "NSGAIIc", "NSGAIId") 
tabularString <-c("lccc") 
latexTableFirstLine <-c("\\hline  & NSGAIIb & NSGAIIc & NSGAIId\\\\ ") 
indicator<-"IGD"

 # Step 1.  Writes the latex header
latexHeader()
# Step 2. Problem loop 
for (problem in problemList) {
  latexTableHeader(problem,  tabularString, latexTableFirstLine)

  indx = 0
  for (i in algorithmList) {
    if (i != "NSGAIId") {
      write(i , "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
      write(" & ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
      jndx = 0 
      for (j in algorithmList) {
        if (jndx != 0) {
          if (indx != jndx) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
          }
          if (j != "NSGAIId") {
            write(" & ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
          }
          else {
            write(" \\\\ ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
          }
        }
        jndx = jndx + 1
      }
      indx = indx + 1
    }
  }

  latexTableTail()
} # for problem

tabularString <-c("| l | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{1}{c|}{NSGAIIb} & \\multicolumn{1}{c|}{NSGAIIc} & \\multicolumn{1}{c|}{NSGAIId} \\\\") 

# Step 3. Problem loop 
latexTableHeader("Flowshop ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "NSGAIId") {
    write(i , "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
    write(" & ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
          } 
          if (problem == "Flowshop") {
            if (j == "NSGAIId") {
              write(" \\\\ ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
            } 
            else {
              write(" & ", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
            }
          }
     else {
    write("&", "/home/usuario/JavaProjects/NSGAIIStudy/R/Problems.IGD.Wilcox.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
    }
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()

