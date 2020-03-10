//  Flowshop.java
package jmetal.problems.flowshop;

import java.io.IOException;
import java.util.Arrays;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.PermutationSolutionType;
import jmetal.encodings.variable.Permutation;
import jmetal.util.JMException;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;



/**
 * @author Juan J. Durillo
 * @version 1.0 This class implements the Flowshop problem. Please notice that
 * this class is also valid for the case m = 1 (mono-objective version of the
 * problem)
 */
public class FlowshopDD extends Problem {

    int[][] dados;
    int numberOfJobs_;
    int numberOfMachines_;
    int[] dueDates;
    int[][] dadosDue;

    public FlowshopDD(String solutionType) throws ClassNotFoundException, IOException {
        this(solutionType, "DD_Ta001.txt");
    }

    /**
     * Creates a new instance of Flowshop problem.
     *
     * @param fileName: name of the file containing the instance
     */
    public FlowshopDD(String solutionType, String fileName) throws ClassNotFoundException, IOException {
        ReadInstanceDD ri = new ReadInstanceDD(fileName);
        ri.loadInstance();
        numberOfVariables_ = 1; // the permutation
        numberOfObjectives_ = ri.getNumberOfObjectives();
        numberOfJobs_ = ri.getNumberOfJobs();
        numberOfMachines_ = ri.getNumberOfMachines();
        numberOfConstraints_ = 0;
        problemName_ = "FlowshopDD"; //Sandra incluiu o DD
        dados = ri.getTransposta();
        dadosDue = ri.getMatrix();
        dueDates = ri.getDueDates();
        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];

        //System.out.println("Num Objetivos = "+numberOfObjectives_);
      /*  System.out.println("Aux transposta");
        for (int i = 0; i < numberOfMachines_; i++) {
            for (int j = 0; j < numberOfJobs_; j++) {
                System.out.print(" " + dados[i][j]);
            }
            System.out.println();
        }

        System.out.println("Due Dates");
        for (int jj = 0; jj < numberOfJobs_; jj++) {
            System.out.print(" " + dueDates[jj]);
        }
      System.out.println();*/
    
    // Establishes upper and lower limits for the variables
    for (int var = 0;
    var< numberOfVariables_ ;
    var

    
        ++) {
            lowerLimit_[var] = 0.0;
        upperLimit_[var] = ri.getNumberOfJobs() - 1;
    } // for

    // Establishes the length of every encodings.variable
    length_  = new int[numberOfVariables_];
    for (int var = 0;
    var< numberOfVariables_ ;
    var

    
        ++) {
            length_[var] = ri.getNumberOfJobs();

    } // for

    if (solutionType.compareTo ("Permutation") == 0) {
            solutionType_ = new PermutationSolutionType(this);
    }

    
        else {
            try {
            throw new JMException("SolutionType must be Permutation");
        } catch (JMException e) {
            e.printStackTrace();
        }
    }
} // Flowshop

public int getNumberOfJobs_() {
        return numberOfJobs_;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void evaluate(Solution solution) throws JMException {
        int[] permutation = ((Permutation) solution.getDecisionVariables()[0]).vector_;
        int[] time = new int[numberOfMachines_];
        int[] tardiness = new int [numberOfJobs_];
        int[] earliness = new int [numberOfJobs_];
        int makespan, flowtime, job, prevMachine, p, totalTardiness, totalEarliness;
        totalTardiness = 0;
        totalEarliness = 0;
        for (int i = 0; i < numberOfMachines_; i++) {
            time[i] = 0;                                                           //inicializa tempos 0.
        }
        
        for (int ii = 0; ii < numberOfJobs_; ii++) {
            tardiness[ii] = 0;  
            earliness[ii] = 0;  //inicializa tempos 0.
        }

        p = permutation[0];                                                         //permuta um job.
        time[0] = dados[0][p];

        for (int j = 1; j < numberOfMachines_; j++) {
            time[j] = time[j - 1] + dados[j][p];                                     //adc jobs.
        }

        flowtime = time[numberOfMachines_ - 1];                                       // inicializa a cont do tempo de um job.
        int [] completion = new int [numberOfJobs_];
        for (int k = 1; k < numberOfJobs_; k++) {
            job = permutation[k];                                                   //job recebe uma perm
            time[0] += dados[0][job];                                               //primeira maq. so adc.
            prevMachine = time[0];   
            //completion[job]=dados[0][job];
            //prevMachine = tempo do job na máquina anterior
            //time[m] tempo decorrido na máquina atual
            for (int m = 1; m < numberOfMachines_; m++) {
                time[m] = Math.max(prevMachine, time[m]) + dados[m][job];            //Cmax                
                prevMachine = time[m];
            }      
            
            
            completion[job] = prevMachine; 
            flowtime += time[numberOfMachines_ - 1];                                  //flow time total.   
            
            
            tardiness[job] = Math.max(0,(completion[job]- dueDates[job])); 
            //System.out.println("Tardiness = "+tardiness[job]);
            totalTardiness+=tardiness[job];
            
           earliness[job] = Math.max(0,(dueDates[job]) - completion[job]); ///ver como agregar o earliness
           totalEarliness+=earliness[job];
            //System.out.println("Earliness = "+earliness[ti]);
            //if(tardiness[ti] > maxTardiness)
            //    maxTardiness = tardiness[ti];
        }
        makespan = time[numberOfMachines_ - 1];
        
        //System.out.println(makespan);
        //System.out.println(flowtime);
        //System.out.println(totalTardiness);
        //System.out.println(totalEarliness);
        solution.setObjective(0, makespan);
        solution.setObjective(1, flowtime);
        solution.setObjective(2, totalTardiness);
        //System.out.println("Num Objetivos vet= "+solution.getNumberOfObjectives());
        //solution.setObjective(3, totalEarliness);
    }
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

