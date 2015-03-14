package optTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import opt.EvaluationFunction;
import opt.HillClimbingProblem;
import opt.SimulatedAnnealing;

public class SimulatedAnnealingRunner implements Runnable {
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
		for (int j = 0; j < numRuns; j++) {
			resultsSa[j] = train(new SimulatedAnnealing(temp, coolingExp, hcp));
    	}
		Result saR = new Result(resultsSa, "SA",
				"numIterations," + iterations,
				" temp,"+ temp, " coolingExp," + coolingExp);
		try {
			PrintWriter write = new PrintWriter(new File(fileName+"T"+temp+"E"+coolingExp+"SA.txt"));
			write.println(saR.toString());
			write.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public double[] train(SimulatedAnnealing oa) {
        double[] results = new double[iterations+1];
        for (int i = 0; i < iterations; i++) {
        	oa.train();
        	results[i] = ef.value(oa.getOptimal());
        }
        results[iterations] = ef.value(oa.getOptimal());
        return results;
    }
	
}
