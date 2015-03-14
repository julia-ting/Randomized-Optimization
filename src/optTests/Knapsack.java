package optTests;

import java.util.Arrays;
import java.util.Random;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.example.KnapsackEvaluationFunction;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.ProbabilisticOptimizationProblem;

public class Knapsack implements TestableOptimizationProblem {
	
	/** Random number generator */
    private static final Random random = new Random();
    /** The number of items */
    private static final int NUM_ITEMS = 40;
    /** The number of copies each */
    private static final int COPIES_EACH = 4;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum volume for a single element */
    private static final double MAX_VOLUME = 50;
    /** The volume of the knapsack */
    private static final double KNAPSACK_VOLUME = 
         MAX_VOLUME * NUM_ITEMS * COPIES_EACH * .4;
    
    private EvaluationFunction ef;
    private Distribution odd;
    private NeighborFunction nf;
    private MutationFunction mf;
    private CrossoverFunction cf;
    private Distribution df;
    private HillClimbingProblem hcp;
    private GeneticAlgorithmProblem gap;
    private ProbabilisticOptimizationProblem pop;
    
    public Knapsack() {
    	int[] copies = new int[NUM_ITEMS];
        Arrays.fill(copies, COPIES_EACH);
        double[] weights = new double[NUM_ITEMS];
        double[] volumes = new double[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++) {
            weights[i] = random.nextDouble() * MAX_WEIGHT;
            volumes[i] = random.nextDouble() * MAX_VOLUME;
        }
         int[] ranges = new int[NUM_ITEMS];
        Arrays.fill(ranges, COPIES_EACH + 1);
        ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
        odd = new DiscreteUniformDistribution(ranges);
        nf = new DiscreteChangeOneNeighbor(ranges);
        mf = new DiscreteChangeOneMutation(ranges);
        cf = new UniformCrossOver();
        df = new DiscreteDependencyTree(.1, ranges); 
        hcp = new GenericHillClimbingProblem(ef, odd, nf);
        gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
    }
    
	@Override
	public HillClimbingProblem getHillClimbingProblem() {
		return hcp;
	}

	@Override
	public GeneticAlgorithmProblem getGeneticAlgorithmProblem() {
		return gap;
	}

	@Override
	public ProbabilisticOptimizationProblem getProbOptProblem() {
		return pop;
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
