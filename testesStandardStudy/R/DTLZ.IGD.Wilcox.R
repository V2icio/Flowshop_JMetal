write("", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex",append=FALSE)
resultDirectory<-"testesStandardStudy/data"
latexHeader <- function() {
  write("\\documentclass{article}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\title{StandardStudy}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\usepackage{amssymb}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{document}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\maketitle", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\section{Tables}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\caption{", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write(problem, "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write(".IGD.}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)

  write("\\label{Table:", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write(problem, "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write(".IGD.}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)

  write("\\centering", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{scriptsize}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\begin{tabular}{", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write(tabularString, "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write(latexTableFirstLine, "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\hline ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
}

latexTableTail <- function() { 
  write("\\hline", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\end{tabular}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\end{scriptsize}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  write("\\end{table}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
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
    write("-- ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  }
  else if (i < j) {
    if (wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE) 
      }
    }
    else {
      write("--", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
  }
}

### START OF SCRIPT 
# Constants
problemList <-c("DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5", "DTLZ6", "DTLZ7") 
algorithmList <-c("NSGAII") 
tabularString <-c("l") 
latexTableFirstLine <-c("\\hline \\\\ ") 
indicator<-"IGD"

 # Step 1.  Writes the latex header
latexHeader()
# Step 2. Problem loop 
for (problem in problemList) {
  latexTableHeader(problem,  tabularString, latexTableFirstLine)

  indx = 0
  for (i in algorithmList) {
    if (i != "NSGAII") {
      write(i , "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
      write(" & ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
      jndx = 0 
      for (j in algorithmList) {
        if (jndx != 0) {
          if (indx != jndx) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
          }
          if (j != "NSGAII") {
            write(" & ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
          }
          else {
            write(" \\\\ ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
          }
        }
        jndx = jndx + 1
      }
      indx = indx + 1
    }
  }

  latexTableTail()
} # for problem

tabularString <-c("| l | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} \\\\") 

# Step 3. Problem loop 
latexTableHeader("DTLZ1 DTLZ2 DTLZ3 DTLZ4 DTLZ5 DTLZ6 DTLZ7 ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "NSGAII") {
    write(i , "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
    write(" & ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
          } 
          if (problem == "DTLZ7") {
            if (j == "NSGAII") {
              write(" \\\\ ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
            } 
            else {
              write(" & ", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
            }
          }
     else {
    write("&", "testesStandardStudy/R/DTLZ.IGD.Wilcox.tex", append=TRUE)
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

