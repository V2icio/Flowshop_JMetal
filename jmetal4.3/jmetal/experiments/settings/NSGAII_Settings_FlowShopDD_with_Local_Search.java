//  NSGAII_Settings.java 
//
//  Authors:
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

package jmetal.experiments.settings;

import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.nsgaII.NSGAIIwLS;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.localSearch.LocalSearch;
import jmetal.operators.localSearch.MutationLocalSearch;
import jmetal.operators.localSearch.TabuLocalSearch;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import jmetal.problems.flowshop.FlowshopDD;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

import java.util.HashMap;

/**
 * Settings class of algorithm NSGA-II (real encoding)
 */
public class NSGAII_Settings_FlowShopDD_with_Local_Search extends Settings {
    public int populationSize_                 ;
    public int maxEvaluations_                 ;
    public double mutationProbability_         ;
    public double crossoverProbability_        ;
    public double mutationDistributionIndex_   ;
    public double crossoverDistributionIndex_  ;
    public int[] localSearchFrequency_ = new int[1];
    public HashMap publicParameters;

    /**
     * Constructor
     * @throws JMException
     */
    public NSGAII_Settings_FlowShopDD_with_Local_Search(String problem) throws JMException {
        super(problem) ;
        //alterado de real para permutation, inclusão do caminho
        //Object [] problemParams = {"Real"};
        Object [] problemParams = {"Permutation","C:\\Users\\Volmir\\Desktop\\Jmetal_4.3\\JavaProjects\\Instancias_Due\\"+ problem};
        //        problem_ = new FlowshopDD("Permutation", "C:\\Users\\carol\\Documents\\Carol\\Submissões_Artigos\\Revistas\\ASOC2019\\MOEAD_LinUCB_FS_Carol_Sandra\\Instancias_Due\\"+problemComponents[0]);

        try {
            problem_ = (new ProblemFactory()).getProblem("FlowshopDD", problemParams);
        } catch (JMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Default settings
        populationSize_              = 100   ;
        maxEvaluations_              = ((FlowshopDD)problem_).getNumberOfJobs_() * populationSize_ * 1000 ;
        mutationProbability_         = 1.0/problem_.getNumberOfVariables() ;
        crossoverProbability_        = 0.9   ;
        mutationDistributionIndex_   = 20.0  ;
        crossoverDistributionIndex_  = 20.0  ;
        localSearchFrequency_[0] = 30;
    } // NSGAII_Settings


    /**
     * Configure NSGAII with user-defined parameter settings
     * @return A NSGAII algorithm object
     * @throws JMException
     */
    public Algorithm configure() throws JMException {
        Algorithm algorithm ;
        Selection  selection ;
        Crossover  crossover ;
        Mutation   mutation  ;
        LocalSearch localSearch;
        LocalSearch localSearch2;

        HashMap  parameters ; // Operator parameters

        QualityIndicator indicators ;

        // Creating the algorithm. There are two choices: NSGAII and its steady-
        // state variant ssNSGAII
        //algorithm = new NSGAII(problem_) ;//Volmir comentou aqui
        algorithm = new NSGAIIwLS(problem_) ;
        //algorithm = new ssNSGAII(problem_) ;



        // Algorithm parameters
        algorithm.setInputParameter("populationSize",populationSize_);
        algorithm.setInputParameter("maxEvaluations",maxEvaluations_);

        // Mutation and Crossover for Real codification
        parameters = new HashMap() ;
        parameters.put("probability", crossoverProbability_) ;
        parameters.put("distributionIndex", crossoverDistributionIndex_) ;
        crossover = CrossoverFactory.getCrossoverOperator("TwoPointsCrossover", parameters);

        parameters = new HashMap() ;
        parameters.put("probability", mutationProbability_) ;
        parameters.put("distributionIndex", mutationDistributionIndex_) ;
        mutation = MutationFactory.getMutationOperator("SwapMutation", parameters);

        int[] localSearchFrequency = (int[]) publicParameters.get("localSearchFrequency");

        if(localSearchFrequency == null){
            algorithm.setInputParameter("localSearchFrequency",localSearchFrequency_);
            System.out.println("localSearchFrequency: " + localSearchFrequency_);
            System.exit(0);
        } else {
            algorithm.setInputParameter("localSearchFrequency",localSearchFrequency);
        }

        int[] proibitionRules = (int[]) publicParameters.get("proibitionRules");

        if(proibitionRules == null || proibitionRules.length == 0 || proibitionRules.length > 2){
            System.out.println("Erro nas regras de proibição. proibitionRules.length = " + proibitionRules.length);
            System.exit(0);
        }

        parameters = new HashMap() ;
        parameters.put("problem",problem_);
        parameters.put("improvementRounds",200);
        parameters.put("numberOfNeighbors",20);
        parameters.put("tabuLenghtTime",10);
        parameters.put("mutation",mutation);
        parameters.put("prohibitionRule",(Integer)proibitionRules[0]);
        //localSearch = new MutationLocalSearch(parameters);
        localSearch = new TabuLocalSearch(parameters);


        if(proibitionRules.length == 2){
            parameters = new HashMap() ;
            parameters.put("problem",problem_);
            parameters.put("improvementRounds",200);
            parameters.put("numberOfNeighbors",20);
            parameters.put("tabuLenghtTime",10);
            parameters.put("mutation",mutation);
            parameters.put("prohibitionRule",(Integer)proibitionRules[1]);
            localSearch2 = new TabuLocalSearch(parameters);
            algorithm.addOperator("localSearch2",localSearch2);
        }
        //localSearch = new MutationLocalSearch(parameters);


        // Selection Operator
        parameters = null ;
        selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters) ;

        // Add the operators to the algorithm
        algorithm.addOperator("crossover",crossover);
        algorithm.addOperator("mutation",mutation);
        algorithm.addOperator("selection",selection);
        algorithm.addOperator("localSearch",localSearch);

    /* Deleted since jMetal 4.2
   // Creating the indicator object
   if ((paretoFrontFile_!=null) && (!paretoFrontFile_.equals(""))) {
      indicators = new QualityIndicator(problem_, paretoFrontFile_);
      algorithm.setInputParameter("indicators", indicators) ;  
   } // if
   */
        return algorithm ;
    } // configure
} // NSGAII_Settings
