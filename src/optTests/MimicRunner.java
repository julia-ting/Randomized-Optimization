package optTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.OptimizationAlgorithm;
import opt.SimulatedAnnealing;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;

public class MimicRunner implements Runnable {
	private ProbabilisticOptimizationProblem pop;
	private int iterations;
	private int numRuns;
	private EvaluationFunction ef;
	private String fileName;
	private int samples;
	private int toKeep;
	
	
	public MimicRunner(ProbabilisticOptimizationProblem pop, int numIterations,
			int numRuns, EvaluationFunction evals, int sample, int toKeep,
			String fileName) {
		this.pop = pop;
		this.iterations = numIterations;
		this.numRuns = numRuns;
		this.ef = evals;
		this.fileName = fileName;
		this.samples = sample;
		this.toKeep = toKeep;
	}
	
	@Override
	public void run() {
		double[][] resultsMimic = new double[numRuns][iterations];
		for (int j = 0; j < numRuns; j++) {
			resultsMimic[j] = train(new MIMIC(samples, toKeep, pop));
    	}
		Result mimicR = new Result(resultsMimic,
				"MIMIC", "numIterations," + iterations,
				" numSamplesGenerated," + samples, " numSamplesToKeep," + toKeep);
		try {
			PrintWriter write = new PrintWriter(new File(fileName+"S"+samples+"K"+toKeep+"MIMIC.txt"));
			write.println(mimicR.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public double[] train(MIMIC oa) {
        double[] results = new double[iterations+1];
        for (int i = 0; i < iterations; i++) {
        	oa.train();
        	results[i] = ef.value(oa.getOptimal());
        }
        results[iterations] = ef.value(oa.getOptimal());
        return results;
    }
	
}
