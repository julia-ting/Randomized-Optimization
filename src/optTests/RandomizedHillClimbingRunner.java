package optTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;

public class RandomizedHillClimbingRunner implements Runnable {

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
		for (int j = 0; j < numRuns; j++) {
			resultsRhc[j] = train(new RandomizedHillClimbing(hcp));
    	}
		Result rhcR = new Result(resultsRhc, 
				"RHC", "numIterations," + iterations);
		try {
			PrintWriter write = new PrintWriter(new File(fileName+"RHC.txt"));
			write.println(rhcR.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public double[] train(RandomizedHillClimbing oa) {
        double[] results = new double[iterations+1];
        for (int i = 0; i < iterations; i++) {
        	oa.train();
        	results[i] = ef.value(oa.getOptimal());
        }
        results[iterations] = ef.value(oa.getOptimal());
        return results;
    }
	
	
}
