package optTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.Instance;

public class SimulatedAnnealingRunner implements OptimizationAlgorithmRunnable, Runnable {
	private HillClimbingProblem hcp;
	private int iterations;
	private int numRuns;
	private EvaluationFunction ef;
	private String fileName;
	private double temp;
	private double coolingExp;
	
	
	public SimulatedAnnealingRunner(HillClimbingProblem hcp, int numIterations,
			int numRuns, EvaluationFunction evals, double temp, double coolingExp,
			String fileName) {
		this.hcp = hcp;
		this.iterations = numIterations;
		this.numRuns = numRuns;
		this.ef = evals;
		this.fileName = fileName;
		this.temp = temp;
		this.coolingExp = coolingExp;
		
	}
	@Override
	public void run() {
		double[][] resultsSa = new double[numRuns][iterations];
		Instance[] optimalVals = new Instance[numRuns];

		for (int j = 0; j < numRuns; j++) {
			SimulatedAnnealing sa = new SimulatedAnnealing(temp, coolingExp, hcp); 
			resultsSa[j] = train(sa, ef, iterations);
			optimalVals[j] = sa.getOptimal();
    	}
		Result saR = null;
		if (hcp instanceof NeuralNetworkOptimizationProblem) {
			saR = new NnetResult(hcp, optimalVals, resultsSa, "SA",
					"numIterations," + iterations,
					" temp,"+ temp, " coolingExp," + coolingExp);
		} else {
			saR = new Result(resultsSa, "SA",
					"numIterations," + iterations,
					" temp,"+ temp, " coolingExp," + coolingExp);
		}
		try {
			PrintWriter write = new PrintWriter(new File(fileName+"T"+temp+"E"+coolingExp+"SA.txt"));
			write.println(saR.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}
