package main;

import opt.ContinuousAddOneNeighbor;
import opt.NeighborFunction;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.ContinuousAddOneMutation;
import opt.ga.CrossoverFunction;
import opt.ga.MutationFunction;
import opt.ga.UniformCrossOver;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import func.nn.NeuralNetwork;

public class CustomNeuralNetworkOptimizationProblem extends NeuralNetworkOptimizationProblem {

    private CrossoverFunction crossoverF;
    /**
     * The neighbor function
     */
    private NeighborFunction neighborF;
    /**
     * The mutation function
     */
    private MutationFunction mutateF;

	public CustomNeuralNetworkOptimizationProblem(DataSet dataset,
			NeuralNetwork nnet, ErrorMeasure measure, NeighborFunction neighbor,
			MutationFunction mutate, CrossoverFunction crossover) {
		super(dataset, nnet, measure);
		this.neighborF = neighbor;
		this.mutateF = mutate;
		this.crossoverF = crossover;
	}
	
	/**
	 * Used for RHC
	 * 
	 * @param dataset
	 * @param nnet
	 * @param measure
	 * @param neighbor
	 */
	public CustomNeuralNetworkOptimizationProblem(DataSet dataset, NeuralNetwork nnet,
			ErrorMeasure measure, NeighborFunction neighbor) {
		this(dataset, nnet, measure, neighbor, new ContinuousAddOneMutation(),
				new UniformCrossOver());
	}
	
	/**
	 * Used for GA
	 * 
	 * @param dataset
	 * @param nnet
	 * @param measure
	 * @param mutate
	 * @param crossover
	 */
	public CustomNeuralNetworkOptimizationProblem(DataSet dataset, NeuralNetwork nnet,
			ErrorMeasure measure, MutationFunction mutate, CrossoverFunction crossover) {
		this(dataset, nnet, measure, new ContinuousAddOneNeighbor(), mutate, crossover);
	}
	
	public CustomNeuralNetworkOptimizationProblem(DataSet dataset, NeuralNetwork nnet,
			ErrorMeasure measure) {
		this(dataset, nnet, measure, new ContinuousAddOneNeighbor(), new ContinuousAddOneMutation(),
				new UniformCrossOver());
	}
	
	@Override
    public Instance neighbor(Instance d) {
        return neighborF.neighbor(d);
    }
	
	@Override
	public Instance mate(Instance da, Instance db) {
		return crossoverF.mate(da, db);
	}
	
	@Override
	public void mutate(Instance d) {
	    mutateF.mutate(d);
	}
	
	public void setMutationFunction(MutationFunction func) {
		this.mutateF = func;
	}
	
	public void setCrossoverFunction(CrossoverFunction func) {
		this.crossoverF = func;
	}
	
	public void setNeighborFunction(NeighborFunction func) {
		this.neighborF = func;
	}

	

}
