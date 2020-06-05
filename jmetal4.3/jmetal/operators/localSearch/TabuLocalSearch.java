//  MutationLocalSearch.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
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

package jmetal.operators.localSearch;

import jmetal.core.*;
import jmetal.encodings.variable.Permutation;
import jmetal.operators.mutation.Mutation;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;
import jmetal.util.comparators.DominanceComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Implementation of Tabu Local Search
 */
public class TabuLocalSearch extends LocalSearch {

    /**
     * Stores the problem to solve
     */
    private Problem problem_;

    /**
     * Stores a reference to the archive in which the non-dominated solutions are
     * inserted
     */
    private SolutionSet archive_;

    private int improvementRounds_;

    /**
     * Stores comparators for dealing with constraints and dominance checking,
     * respectively.
     */
    private Comparator constraintComparator_;
    private Comparator dominanceComparator_;


    /**
     * Stores the number of evaluations_ carried out
     */
    private int evaluations_;

    private int[][] tabuList_;
    private int numberOfNeighbors;
    private int tabuLenghtTime;
    private int prohibitionRule;

    public TabuLocalSearch(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("problem") != null)
            problem_ = (Problem) parameters.get("problem");
        if (parameters.get("improvementRounds") != null)
            improvementRounds_ = (Integer) parameters.get("improvementRounds");
        if (parameters.get("numberOfNeighbors") != null)
            numberOfNeighbors = (Integer) parameters.get("numberOfNeighbors");
        if (parameters.get("tabuLenghtTime") != null)
            tabuLenghtTime = (Integer) parameters.get("tabuLenghtTime");
        if (parameters.get("prohibitionRule") != null)
            prohibitionRule = (Integer) parameters.get("prohibitionRule");

        evaluations_ = 0;
        archive_ = null;
        dominanceComparator_ = new DominanceComparator();
        constraintComparator_ = new OverallConstraintViolationComparator();
    } //Mutation improvement


    //Generate the moves to find neighbors in permutation problems
    private int[][] generateNeighborsSwaps(int permutationLength/*Solution solution*/) {
        //int permutationLength;
        // permutationLength = ((Permutation)solution.getDecisionVariables()[0]).getLength() ;

        int[][] moves = new int[numberOfNeighbors][2];

        //Matrix that controls the neighbors already generated
        int[][] alreadyGenerated = new int[permutationLength][permutationLength];

        int i = 0;
        int pos1;
        int pos2;
        int aux;

        while (true) {
            pos1 = PseudoRandom.randInt(0, permutationLength - 1);
            pos2 = PseudoRandom.randInt(0, permutationLength - 1);

            if (pos1 > pos2) {
                aux = pos1;
                pos1 = pos2;
                pos2 = aux;
            }

            if (pos1 == pos2 || alreadyGenerated[pos1][pos2] != 0) {
                continue;
            } else {
                alreadyGenerated[pos1][pos2] = 1;
                moves[i][0] = pos1;
                moves[i][1] = pos2;
                i++;
            }
            if (i == numberOfNeighbors)
                break;
        }
        return moves;
    }


// -------------------------------------------//


    // n = number of neighbors, object is the initial solution
    private SolutionSet generateXNeighbors(Object object) throws JMException {
        Solution solution = (Solution) object;

        SolutionSet neighbors = new SolutionSet(numberOfNeighbors); //A set of neighbors

        int permutationLength;
        permutationLength = ((Permutation) solution.getDecisionVariables()[0]).getLength();
        tabuList_ = new int[permutationLength][permutationLength];

        int[][] moves = generateNeighborsSwaps(permutationLength);

        //This will generate the neighbors using the moves and add on the solutionSet neighbors
        for (int i = 0; i < numberOfNeighbors; i++) {
            Solution currentNeighbor;
            currentNeighbor = new Solution(solution);

            int[] swapPermutation = ((Permutation)solution.getDecisionVariables()[0]).vector_;

            int variableAux;

            variableAux = swapPermutation[moves[i][0]];
            swapPermutation[moves[i][0]] = swapPermutation[moves[i][1]];
            swapPermutation[moves[i][1]] = variableAux;


            //currentNeighbor.setDecisionVariables(swapPermutation);
            int[] movement = new int[2];
            movement[0] = moves[i][0];
            movement[1] = moves[i][1];
            currentNeighbor.setSwapMovement(movement);

            problem_.evaluateConstraints(currentNeighbor);
            problem_.evaluate(currentNeighbor);
            evaluations_++;

            neighbors.add(currentNeighbor);
        }

        //Ranking ranking = new Ranking(neighbors);

        //SolutionSet front = ranking.getSubfront(0);

        return neighbors;
    }

    private int validateTabuConstraint(Solution solution, Solution currentSolution, int currentRound){
        int best = dominanceComparator_.compare(currentSolution, solution);

        int[] swaps = currentSolution.getSwapMovement();

        int[] currentSolutionPermutation = ((Permutation)currentSolution.getDecisionVariables()[0]).vector_;

        if(prohibitionRule == 2){
            if (best == -1){
                tabuList_[swaps[0]][currentSolutionPermutation[swaps[1]]] = currentRound + tabuLenghtTime;
                tabuList_[swaps[1]][currentSolutionPermutation[swaps[0]]] = currentRound + tabuLenghtTime;
                return 2;
            }
            if(tabuList_[swaps[0]][currentSolutionPermutation[swaps[1]]] > currentRound ||
                    tabuList_[swaps[1]][currentSolutionPermutation[swaps[0]]] > currentRound)
                return 1;
            else {
                //Não é tabu. Tornar o movimento Tabu
                tabuList_[swaps[0]][currentSolutionPermutation[swaps[1]]] = currentRound + tabuLenghtTime;
                tabuList_[swaps[1]][currentSolutionPermutation[swaps[0]]] = currentRound + tabuLenghtTime;
                return 0;
            }


        } else if(prohibitionRule == 7){
            //Satisfazer um critério de aspiração. Atualizar o Tabu Time dos moves.
            if (best == -1){
                tabuList_[swaps[0]][0] = currentRound + tabuLenghtTime;
                tabuList_[swaps[1]][0] = currentRound + tabuLenghtTime;
                return 2;
            }
            //Se for tabu
            if(tabuList_[swaps[0]][0] > currentRound || tabuList_[swaps[1]][0] > currentRound)
                return 1;
            else {
                //Não é tabu. Tornar o movimento Tabu
                tabuList_[swaps[0]][0] = currentRound + tabuLenghtTime;
                tabuList_[swaps[1]][0] = currentRound + tabuLenghtTime;
                return 0;
            }
        }

        return 1;

    }

    public Object execute(Object object) throws JMException {
        int best = 0;
        evaluations_ = 0;
        Solution solution = (Solution) object;

        int rounds = improvementRounds_;
        archive_ = (SolutionSet) getParameter("archive");

        if (rounds <= 0)
            return new Solution(solution);



        for(int i=0; i < rounds; i++){
            Solution mutatedSolution = new Solution(solution);

            int flagFindNonTabu = 0;

            //SolutionSet neighbors = generateXNeighbors(object);
            SolutionSet neighbors = generateXNeighbors(mutatedSolution);

            Ranking ranking = new Ranking(neighbors);

            //Tá. Aqui eu vou ter que pegar as soluções das subsets conforme for sendo necessário?
            for (int j = 0; j < ranking.getNumberOfSubfronts(); j++) {
                SolutionSet front = ranking.getSubfront(0);

                for (int k = 0; k < front.size(); k++) {
                    Solution currentSolution = front.get(k);
                    int isTabu = validateTabuConstraint(solution, currentSolution, i);

                    if (isTabu == 1) {
                        //é Tabu
                        continue;
                    } else {
                        //Não é Tabu ou Satisfaz algum critério de aspiração.
                        flagFindNonTabu = 1;
                        mutatedSolution = currentSolution;

                        if(isTabu == 2){
                            solution = currentSolution;
                        }
                        break;
                    }
                }
                if (flagFindNonTabu == 1)
                    break;
            }
        }
        return new Solution(solution);
    } // execute

    /**
     * Returns the number of evaluations made
     */
    public int getEvaluations() {
        return evaluations_;
    } // evaluations

} // MutationLocalSearch
