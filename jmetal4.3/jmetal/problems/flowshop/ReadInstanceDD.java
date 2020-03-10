//  ReadInstance.java
//
//  Author:
//       Juan J. Durillo <juan@dps.uibk.ac.at>
//
//  Copyright (c) 2011 Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package jmetal.problems.flowshop;

import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Juan J. Durillo
 * @version 1.0 This class provides some functionalities for reading mQAP from
 * file
 */
public class ReadInstanceDD {

    private String fileName_;         // name of the file containing the instance
    private int[][] matrix;   // a matrix describing the problem
    private int[][] transposta;   // transposta da matrix para trabalhar com as máquinas nas linhas
    private int jobs_ = -1;          // number of facilities
    private int machines_ = -1;
    private int objectives_ = 3;   // number of objectives 
    private int[] dueDates;
    private String singleObjectiveFirstLine_ = "";
    private int num = -1;
    private int cont = 0;

    /**
     * @author Juan J. Durillo Creates a new ReadInstance for the mQAP problem
     * @param name: the name of the file
     */
    public ReadInstanceDD(String name) {
        fileName_ = name;
    } // ReadInstance

    /**
     * Reads the instance from file This method should be called for reading all
     * the data from file.
     */
    public void loadInstance() throws IOException {

        try {
            File archivo = new File(fileName_);
            FileReader fr = null;
            BufferedReader br = null;
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // File reading
            String line;

            line = br.readLine(); // reading the first line (special case)
            StringTokenizer st = new StringTokenizer(line);
            String aux = "";
            try {
                aux = st.nextToken();
                jobs_ = new Integer(aux);
                aux = st.nextToken();
                machines_ = new Integer(aux);
            } catch (NumberFormatException ne) {
            }

            String newLine = "";
            singleObjectiveFirstLine_ = newLine;

            // reading A matrix (discarding empty lines on the way)
            matrix = new int[jobs_][machines_];
            line = br.readLine();

            while (line.isEmpty()) {
                line = br.readLine();
            }
            //line = br.readLine();
            for (int i = 0; i < jobs_; i++) {
                st = new StringTokenizer(line);
                for (int j = 0; j < machines_; j++) {
                    aux = st.nextToken();
                    matrix[i][j] = new Integer(st.nextToken());
                    //System.out.println(matrix[i][j]);
                }
                line = br.readLine();

            }
            dueDates = new int[jobs_];
            for (int i = 0; i < jobs_; i++) {
                line = br.readLine();
                st = new StringTokenizer(line);
                aux = st.nextToken();
                //System.out.println("" + aux);                               
                dueDates[i] = new Integer(st.nextToken());
                // System.out.print(" - " + dueDates[i]);
                aux = st.nextToken();
                aux = st.nextToken();
                //}

            }

            transposta = new int[machines_][jobs_];

            for (int i = 0; i < jobs_; i++) {
                for (int j = 0; j < machines_; j++) {
                    transposta[j][i] = matrix[i][j];
                }
            }
          /*  System.out.println("Aux transposta");
            for (int i = 0; i < machines_; i++) {
                for (int j = 0; j < jobs_; j++) {
                    System.out.print(" " + transposta[i][j]);
                }
                System.out.println();
            }*/

            // System.out.println(" ");
            // System.out.println("" + machines_);
            //  System.out.println("" + jobs_);
            // comprobation
            /*for (int i = 0; i < jobs_; i++) {
                for (int j = 0; j < machines_; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }*/
            // at this point the instances has been read
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadInstance.class.getName()).log(Level.SEVERE, "The file doesn't exist", ex);
        } catch (IOException ex2) {
            Logger.getLogger(ReadInstance.class.getName()).log(Level.SEVERE, "Error reading from file", ex2);
        }
    } // loadInstance

    /**
     * @author Juan J. Durillo This methods creates a single-objective instance
     * using a weighted aggregative approach and write all the information to
     * file
     * @param vector: a vector containing the weights for the aggregative
     * approach
     */
    public void createSingleObjectiveInstance(int[] weights) {
        // safe comprobation: is the number of weights == objectives_?
        if (weights.length != objectives_) {
            Logger.getLogger(ReadInstanceDD.class
                    .getName()).log(Level.SEVERE,
                            "The number of weights and number of objectives don't match");

        } // safe comprobation

        // generating the intance
        try {
            String name = "";
            for (int k = 0; k < objectives_; k++) {
                name += "_" + weights[k];
            }
            System.out.println(name);
            System.out.println(fileName_ + name);
            FileOutputStream fos = new FileOutputStream(fileName_ + name);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(machines_ + "");
            bw.newLine();
            bw.newLine();

            // writting matrix_a
            String line;
            for (int i = 0; i < jobs_; i++) {
                line = "";
                for (int j = 0; j < machines_; j++) {
                    line += matrix[i][j] + " ";
                }
                bw.write(line);
                bw.newLine();
            }
            bw.newLine();
            bw.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadInstanceDD.class
                    .getName()).log(Level.SEVERE, "The file cannot be created", ex);

        } catch (IOException ex1) {
            Logger.getLogger(ReadInstanceDD.class
                    .getName()).log(Level.SEVERE, "Error writting in the file", ex1);
        }
    }

    int[][] getMatrix() {
        return matrix;
    }

    int getNumberOfObjectives() {
        return objectives_;
    }

    int getNumberOfJobs() {
        return jobs_;
    }

    int getNumberOfMachines() {
        return machines_;
    }

    int[] getDueDates() {
        return dueDates;
    }
    
     int[][] getTransposta() {
        return transposta;
    }

    public static void main(String[] args) throws IOException {
        // for (int numInst = 1; numInst <= 10; numInst++) {
        ReadInstanceDD ri = new ReadInstanceDD("C:\\Users\\qwert\\Desktop\\Jmetal\\Instancias_Due\\DD_Ta001.txt");
        ri.loadInstance();
        System.out.println("");
        System.out.println("");
        // }

    }
}
