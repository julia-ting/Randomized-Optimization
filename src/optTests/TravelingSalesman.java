package optTests;

import java.util.Arrays;
import java.util.Random;

import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.SwapNeighbor;
import opt.example.TravelingSalesmanCrossOver;
import opt.example.TravelingSalesmanEvaluationFunction;
import opt.example.TravelingSalesmanRouteEvaluationFunction;
import opt.example.TravelingSalesmanSortEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.SwapMutation;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.ProbabilisticOptimizationProblem;
import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

public class TravelingSalesman implements TestableOptimizationProblem {
	
    private static final int N = 50;

	Random random = new Random();
    // create the random points
    double[][] points = new double[N][2];
    
    // for rhc, sa, and ga we use a permutation based encoding
    private EvaluationFunction ef;
    private Distribution odd;
    private NeighborFunction nf;
    private MutationFunction mf;
    private CrossoverFunction cf;
    private Distribution df;
    private HillClimbingProblem hcp;
    private GeneticAlgorithmProblem gap;
    private ProbabilisticOptimizationProblem pop;
    private TravelingSalesmanEvaluationFunction sortEval;
    private int[] ranges;

	
	public TravelingSalesman() {
		for (int i = 0; i < points.length; i++) {
	        points[i][0] = random.nextDouble();
	        points[i][1] = random.nextDouble();   
	    }
	
	    // for rhc, sa, and ga we use a permutation based encoding
	    this.ef = new TravelingSalesmanRouteEvaluationFunction(points);
	    this.odd = new DiscretePermutationDistribution(N);
	    this.nf = new SwapNeighbor();
	    this.mf = new SwapMutation();
	    this.cf = new TravelingSalesmanCrossOver((TravelingSalesmanEvaluationFunction)ef);
	    
	    sortEval = new TravelingSalesmanSortEvaluationFunction(points);
	    ranges = new int[N];
        Arrays.fill(ranges, N);
        Distribution oddUniform = new  DiscreteUniformDistribution(ranges);
        df = new DiscreteDependencyTree(.1, ranges); 
        
//	    hcp = new GenericHillClimbingProblem(ef, odd, nf);
//	    gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
//	    pop = new GenericProbabilisticOptimizationProblem(sortEval, oddUniform, df);
	}
	

	@Override
	public HillClimbingProblem getHillClimbingProblem() {
		return new GenericHillClimbingProblem(ef, odd, nf);
	}

	@Override
	public GeneticAlgorithmProblem getGeneticAlgorithmProblem() {
		return new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
	}

	@Override
	public ProbabilisticOptimizationProblem getProbOptProblem() {
        Distribution oddUniform = new  DiscreteUniformDistribution(ranges);
		return new GenericProbabilisticOptimizationProblem(sortEval, oddUniform, df);
	}

	@Override
	public EvaluationFunction getEvalFunc() {
		return ef;
	}


	@Override
	public EvaluationFunction[] getEvalFuncs() {
		EvaluationFunction[] evals = new EvaluationFunction[4];
		for (int i = 0; i < evals.length; i++) {
			evals[i] = ef;
		}
		evals[3] = sortEval;
		return evals;
	}
	
	
}
