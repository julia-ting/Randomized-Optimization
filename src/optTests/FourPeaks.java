package optTests;

import java.util.Arrays;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.FourPeaksEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.SingleCrossOver;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;
import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

public class FourPeaks implements TestableOptimizationProblem {
	
    /** The n value */
    private static final int N = 200;
    /** The t value */
    private static final int T = N / 5;
    
    int[] ranges = new int[N];
    private EvaluationFunction ef;
    private Distribution odd;
    private NeighborFunction nf;
    private MutationFunction mf;
    private CrossoverFunction cf;
    private Distribution df;
    private HillClimbingProblem hcp;
    private GeneticAlgorithmProblem gap;
    private ProbabilisticOptimizationProblem pop;
    
    public FourPeaks() {
        Arrays.fill(ranges, 2);
    	ef = new FourPeaksEvaluationFunction(T);
    	odd = new DiscreteUniformDistribution(ranges);
    	nf = new DiscreteChangeOneNeighbor(ranges);
    	mf = new DiscreteChangeOneMutation(ranges);
    	cf = new SingleCrossOver();
    	df = new DiscreteDependencyTree(.1, ranges); 
//    	hcp = new GenericHillClimbingProblem(ef, odd, nf);
//    	gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
//    	pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
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
    	return new GenericProbabilisticOptimizationProblem(ef, odd, df);
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
		return evals;
	}
}
