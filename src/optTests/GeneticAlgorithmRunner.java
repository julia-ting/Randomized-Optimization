package optTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;
import opt.SimulatedAnnealing;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.StandardGeneticAlgorithm;

public class GeneticAlgorithmRunner implements Runnable {
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
		for (int j = 0; j < numRuns; j++) {
			resultsGa[j] = train(new StandardGeneticAlgorithm(popSize, toMate, toMutate,
					gap));
    	}
		Result gaR = new Result(resultsGa, "GA",
				"numIterations," + iterations, " popSize,"+popSize,
				" toMate," + toMate, " toMutate," + toMutate);

		try {
			PrintWriter write = new PrintWriter(new File(fileName+"M"+toMate+"N"+toMutate+"GA.txt"));
			write.println(gaR.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public double[] train(StandardGeneticAlgorithm oa) {
        double[] results = new double[iterations+1];
        for (int i = 0; i < iterations; i++) {
        	oa.train();
        	results[i] = ef.value(oa.getOptimal());
        }
        results[iterations] = ef.value(oa.getOptimal());
        return results;
    }
	
}
