//  NSGAII.java
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

package jmetal.metaheuristics.nsgaII;

import jmetal.core.*;
import jmetal.operators.localSearch.LocalSearch;
import jmetal.operators.localSearch.MutationLocalSearch;
import jmetal.operators.selection.BestSolutionSelection;
import jmetal.operators.selection.Selection;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.*;
import jmetal.util.comparators.DominanceComparator;

import java.util.Comparator;
import java.util.HashMap;

/**
 *  Implementation of NSGA-II.
 *  This implementation of NSGA-II makes use of a QualityIndicator object
 *  to obtained the convergence speed of the algorithm. This version is used
 *  in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 */

public class NSGAIIwLS extends Algorithm {

	/**
	 * Constructor
	 * @param problem Problem to solve
	 */
	public NSGAIIwLS(Problem problem) {
		super (problem) ;
	} // NSGAII

	/**
	 * Runs the NSGA-II algorithm.
	 * @return a <code>SolutionSet</code> that is a set of non dominated solutions
	 * as a result of the algorithm execution
	 * @throws JMException
	 */
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;

		QualityIndicator indicators; // QualityIndicator object
		int requiredEvaluations; // Use in the example of use of the
		// indicators object (see below)

		SolutionSet population;
		SolutionSet offspringPopulation;
		SolutionSet union;

		Operator mutationOperator;
		Operator crossoverOperator;
		Operator selectionOperator;
		LocalSearch localSearchOperator;
		LocalSearch localSearchOperator2;

		int[] localSearchFrequency; //Frequency of application of local search
		int generations;
		int actualLocalSearchFrequency;

		Distance distance = new Distance();

		//Read the parameters
		populationSize = ((Integer) getInputParameter("populationSize")).intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
		indicators = (QualityIndicator) getInputParameter("indicators");
		localSearchFrequency = ((int[]) getInputParameter("localSearchFrequency"));

		if(localSearchFrequency == null || localSearchFrequency.length == 0)
		{
			System.out.println("Problema com o valor de frequencia de busca local.");
			System.exit(0);
		}

		System.out.print("localSearchFrequency: ");
		for(int i = 0; i<localSearchFrequency.length; i++){
			System.out.print(localSearchFrequency[i] + " ");
		}
		System.out.println();

		actualLocalSearchFrequency = localSearchFrequency[0];


		//Initialize the variables
		population = new SolutionSet(populationSize);
		evaluations = 0;
		generations = 0;

		requiredEvaluations = 0;

		//Read the operators
		mutationOperator = operators_.get("mutation");
		crossoverOperator = operators_.get("crossover");
		selectionOperator = operators_.get("selection");
		localSearchOperator = (LocalSearch) operators_.get("localSearch");
		localSearchOperator2 = (LocalSearch) operators_.get("localSearch2");



		// Create the initial solutionSet
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;
			population.add(newSolution);
		} //for       

		int roundsWithoutImprovement = 0;
		// Generations 
		while (evaluations < maxEvaluations) {
			// Create the offSpring solutionSet
			offspringPopulation = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];

			generations++;

			//Atualizar o actualLocalSearchFrequency
			if(localSearchFrequency.length > 1 && actualLocalSearchFrequency != localSearchFrequency[1] && evaluations >= maxEvaluations/2){
				actualLocalSearchFrequency = localSearchFrequency[1];
			}

			//(tamPop/5) - numObjetivos -> Numero de soluções que serão escolhidas para aplicação da TS



			if(localSearchOperator != null && generations % actualLocalSearchFrequency == 0){
                int numberApplicationsTabuSearch = (populationSize/5) - problem_.getNumberOfObjectives();

			    Ranking firstRanking = new Ranking(population);

				SolutionSet aplicationSet = firstRanking.getSubfront(0);

				while(aplicationSet.size() < numberApplicationsTabuSearch){

                }


				SolutionSet firstFront = firstRanking.getSubfront(0);


				for(int i=0; i<numberApplicationsTabuSearch; i++){



					//Chose a random solution from the first front
					int chosenSolutionPosition = PseudoRandom.randInt(0, firstFront.size() - 1);
					Solution bestSolution = population.get(chosenSolutionPosition);

					double obj1[] = new double[2];
					double obj2[] = new double[2];

					obj1[0] = (double) bestSolution.getObjective(0);
					obj2[0] = (double) bestSolution.getObjective(1);

					if(evaluations < maxEvaluations/2 || localSearchOperator2 == null){
						bestSolution = (Solution) localSearchOperator.execute(bestSolution);
						evaluations += localSearchOperator.getEvaluations();
					} else {
						bestSolution = (Solution) localSearchOperator2.execute(bestSolution);
						evaluations += localSearchOperator2.getEvaluations();
					}

					problem_.evaluate(bestSolution);
					problem_.evaluateConstraints(bestSolution);

					obj1[1] = (double) bestSolution.getObjective(0);
					obj2[1] = (double) bestSolution.getObjective(1);

					double improvement1 = obj1[0] - obj1[1];
					double improvement2 = obj2[0] - obj2[1];
					if(improvement1 > 0 || improvement2 > 0){
						if(roundsWithoutImprovement>0){
							System.out.println("roundsWithoutImprovement: " + roundsWithoutImprovement);
						}
						System.out.println("Improvement LS = makespan: " + improvement1 + " TFT: " + improvement2);
						roundsWithoutImprovement = 0;
					} else {
						roundsWithoutImprovement++;
					}
					population.replace(chosenSolutionPosition, bestSolution);
				}
			}


			for (int i = 0; i < (populationSize / 2); i++) {
				if (evaluations < maxEvaluations) {
					//obtain parents
					parents[0] = (Solution) selectionOperator.execute(population);
					parents[1] = (Solution) selectionOperator.execute(population);
					Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
					mutationOperator.execute(offSpring[0]);
					mutationOperator.execute(offSpring[1]);
					problem_.evaluate(offSpring[0]);
					problem_.evaluateConstraints(offSpring[0]);
					problem_.evaluate(offSpring[1]);
					problem_.evaluateConstraints(offSpring[1]);
					offspringPopulation.add(offSpring[0]);
					offspringPopulation.add(offSpring[1]);
					evaluations += 2;
				} // if                            
			} // for

			// Create the solutionSet union of solutionSet and offSpring
			union = ((SolutionSet) population).union(offspringPopulation);

			// Ranking the union
			Ranking ranking = new Ranking(union);

			int remain = populationSize;
			int index = 0;
			SolutionSet front = null;
			population.clear();

			// Obtain the next front
			front = ranking.getSubfront(index);

			while ((remain > 0) && (remain >= front.size())) {
				//Assign crowding distance to individuals
				distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
				//Add the individuals of this front
				for (int k = 0; k < front.size(); k++) {
					population.add(front.get(k));
				} // for

				//Decrement remain
				remain = remain - front.size();

				//Obtain the next front
				index++;
				if (remain > 0) {
					front = ranking.getSubfront(index);
				} // if
			} // while

			// Remain is less than front(index).size, insert only the best one
			if (remain > 0) {  // front contains individuals to insert                        
				distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
				front.sort(new CrowdingComparator());
				for (int k = 0; k < remain; k++) {
					population.add(front.get(k));
				} // for

				remain = 0;
			} // if                               

			// This piece of code shows how to use the indicator object into the code
			// of NSGA-II. In particular, it finds the number of evaluations required
			// by the algorithm to obtain a Pareto front with a hypervolume higher
			// than the hypervolume of the true Pareto front.
			if ((indicators != null) &&
					(requiredEvaluations == 0)) {
				double HV = indicators.getHypervolume(population);
				if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
					requiredEvaluations = evaluations;
				} // if
			} // if
		} // while

		// Return as output parameter the required evaluations
		setOutputParameter("evaluations", requiredEvaluations);

		// Return the first non-dominated front
		Ranking ranking = new Ranking(population);
		return ranking.getSubfront(0);
	} // execute
} // NSGA-II
