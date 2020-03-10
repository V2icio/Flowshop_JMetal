//  Flowshop.java
package jmetal.problems.flowshop;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.PermutationSolutionType;
import jmetal.encodings.variable.Permutation;
import jmetal.util.JMException;

import java.io.IOException;

/**
 * @author Juan J. Durillo
 * @version 1.0 This class implements the Flowshop problem. Please notice that
 * this class is also valid for the case m = 1 (mono-objective version of the
 * problem)
 */
public class Flowshop extends Problem {

    int[][] dados;
    int numberOfJobs_;
    int numberOfMachines_;

    public Flowshop(String solutionType, int num) throws ClassNotFoundException, IOException {
        this(solutionType, "tai20_5.txt", num);
    }

    /**
     * Creates a new instance of Flowshop problem.
     *
     * @param fileName: name of the file containing the instance
     */
    public Flowshop(String solutionType, String fileName, int num) throws ClassNotFoundException, IOException {

        ReadInstanceUnitario ri = new ReadInstanceUnitario(fileName);
        ri.loadInstance();
        numberOfVariables_ = 1; // the permutation
        numberOfObjectives_ = ri.getNumberOfObjectives();
        numberOfJobs_ = ri.getNumberOfJobs();
        numberOfMachines_ = ri.getNumberOfMachines();
        numberOfConstraints_ = 0;
        problemName_ = "Flowshop";
        dados = ri.getMatrix();

        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];

        // Establishes upper and lower limits for the variables
        for (int var = 0; var < numberOfVariables_; var++) {
            lowerLimit_[var] = 0.0;
            upperLimit_[var] = ri.getNumberOfJobs() - 1;
        } // for

        // Establishes the length of every encodings.variable
        length_ = new int[numberOfVariables_];
        for (int var = 0; var < numberOfVariables_; var++) {
            length_[var] = ri.getNumberOfJobs();

        } // for
        if (solutionType.compareTo("Permutation") == 0) {
            solutionType_ = new PermutationSolutionType(this);
        } else {
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
        int makespan, flowtime, job, prevMachine, p;

        for (int i = 0; i < numberOfMachines_; i++) {
            time[i] = 0;                                                           //inicializa tempos 0.
        }

        p = permutation[0];                                                         //permuta um job.
        time[0] = dados[0][p];

        for (int j = 1; j < numberOfMachines_; j++) {
            time[j] = time[j - 1] + dados[j][p];                                     //adc jobs.
        }

        flowtime = time[numberOfMachines_ - 1];                                       // inicializa a cont do tempo de um job.

        for (int k = 1; k < numberOfJobs_; k++) {
            job = permutation[k];                                                   //job recebe uma perm
            time[0] += dados[0][job];                                               //primeira maq. so adc.
            prevMachine = time[0];

            for (int m = 1; m < numberOfMachines_; m++) {
                time[m] = Math.max(prevMachine, time[m]) + dados[m][job];            //Cmax
                prevMachine = time[m];
            }
            flowtime += time[numberOfMachines_ - 1];                                  //flow time total.    
        }

        makespan = time[numberOfMachines_ - 1];

        //System.out.println(makespan);
        //System.out.println(flowtime);
        solution.setObjective(0, makespan);
        solution.setObjective(1, flowtime);
    }
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
