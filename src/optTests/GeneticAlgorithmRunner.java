package optTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import opt.EvaluationFunction;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.StandardGeneticAlgorithm;
import shared.Instance;

public class GeneticAlgorithmRunner implements OptimizationAlgorithmRunnable, Runnable {
	private GeneticAlgorithmProblem gap;
	private int iterations;
	private int numRuns;
	private EvaluationFunction ef;
	private String fileName;
	private int popSize, toMate, toMutate;
	
	public GeneticAlgorithmRunner(GeneticAlgorithmProblem gap, int numIterations,
			int numRuns, EvaluationFunction evals, int popSize, int toMate, int toMutate,
			String fileName) {
		this.gap = gap;
		this.iterations = numIterations;
		this.numRuns = numRuns;
		this.ef = evals;
		this.fileName = fileName;
		this.popSize = popSize;
		this.toMate = toMate;
		this.toMutate = toMutate;
	}
	
	@Override
	public void run() {

		double[][] resultsGa = new double[numRuns][iterations];
		Instance[] optimalVals = new Instance[numRuns];
		for (int j = 0; j < numRuns; j++) {
			StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(popSize, toMate, toMutate,
					gap);
			resultsGa[j] = train(ga, ef, iterations);
			optimalVals[j] = ga.getOptimal();
    	}
		Result gaR = null;
		if (gap instanceof NeuralNetworkOptimizationProblem) {
			gaR = new NnetResult(gap, optimalVals, resultsGa, "GA",
				"numIterations," + iterations, " popSize,"+popSize,
				" toMate," + toMate, " toMutate," + toMutate );
		} else {
			gaR = new Result(resultsGa, "GA",
					"numIterations," + iterations, " popSize,"+popSize,
					" toMate," + toMate, " toMutate," + toMutate);
		}

		try {
			PrintWriter write = new PrintWriter(new File(fileName+"M"+toMate+"N"+toMutate+"GA.csv"));
			write.println(gaR.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
