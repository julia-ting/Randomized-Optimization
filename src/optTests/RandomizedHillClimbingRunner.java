package optTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.RandomizedHillClimbing;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.Instance;

public class RandomizedHillClimbingRunner implements OptimizationAlgorithmRunnable, Runnable {

	private HillClimbingProblem hcp;
	private int iterations;
	private int numRuns;
	private EvaluationFunction ef;
	private String fileName;
	
	public RandomizedHillClimbingRunner(HillClimbingProblem hcp, int numIterations,
			int numRuns, EvaluationFunction evals, String fileName) {
		this.hcp = hcp;
		this.iterations = numIterations;
		this.numRuns = numRuns;
		this.ef = evals;
		this.fileName = fileName;
		
	}
	@Override
	public void run() {
		double[][] resultsRhc = new double[numRuns][iterations];
		Instance[] optimalVals = new Instance[numRuns];
		for (int j = 0; j < numRuns; j++) {
			RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp); 
			resultsRhc[j] = train(rhc, ef, iterations);
			optimalVals[j] = rhc.getOptimal();
    	}
		Result rhcR = null;
		if (hcp instanceof NeuralNetworkOptimizationProblem) {
			rhcR = new NnetResult(hcp, optimalVals, resultsRhc, "RHC", "numIterations," + iterations);
		} else {
			rhcR = new Result(resultsRhc, 
				"RHC", "numIterations," + iterations);
		}
		try {
			PrintWriter write = new PrintWriter(new File(fileName+"RHC.csv"));
			write.println(rhcR.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
