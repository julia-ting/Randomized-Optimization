package optTests;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.OptimizationProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.prob.ProbabilisticOptimizationProblem;

public interface TestableOptimizationProblem {

	
    public HillClimbingProblem getHillClimbingProblem();
    
    public GeneticAlgorithmProblem getGeneticAlgorithmProblem();
    
    public ProbabilisticOptimizationProblem getProbOptProblem();
    
    public EvaluationFunction getEvalFunc();
    
    public EvaluationFunction[] getEvalFuncs();
    
}
