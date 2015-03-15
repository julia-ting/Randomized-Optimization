package optTests;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.example.NeuralNetworkEvaluationFunction;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.example.NeuralNetworkWeightDistribution;
import opt.ga.CrossoverFunction;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.DataSet;
import shared.ErrorMeasure;
import dist.Distribution;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;

public class MyNeuralNet implements TestableOptimizationProblem {

	private NeuralNetworkOptimizationProblem nnop;
	private ErrorMeasure measure;
	private BackPropagationNetwork network;
	private EvaluationFunction ef;
    private Distribution odd;
	private NeighborFunction nf;
	private MutationFunction mf;
    private CrossoverFunction cf;
    private Distribution df;
    private HillClimbingProblem hcp;
    private GeneticAlgorithmProblem gap;
    private BackPropagationNetworkFactory factory;
    private int[] networkLayerCounts;
    private DataSet set;
	
	public MyNeuralNet(DataSet set, int[] networkLayerCounts, ErrorMeasure measure) {
		this.measure = measure;
		this.set = set;
		this.networkLayerCounts = networkLayerCounts;
		this.factory = new BackPropagationNetworkFactory();
		this.network = factory.createClassificationNetwork(networkLayerCounts);
		this.ef = new NeuralNetworkEvaluationFunction(network, set, measure);
	}

	@Override
	public HillClimbingProblem getHillClimbingProblem() {
		return new NeuralNetworkOptimizationProblem(
				set,
				factory.createClassificationNetwork(networkLayerCounts),
				measure);
	}

	@Override
	public GeneticAlgorithmProblem getGeneticAlgorithmProblem() {
		return new NeuralNetworkOptimizationProblem(
				set,
				factory.createClassificationNetwork(networkLayerCounts),
				measure);
	}

	@Override
	public ProbabilisticOptimizationProblem getProbOptProblem() {
		return null;
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
