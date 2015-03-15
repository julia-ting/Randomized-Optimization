package optTests;

import opt.EvaluationFunction;
import opt.OptimizationAlgorithm;

public interface OptimizationAlgorithmRunnable {
	
	public default double[] train(OptimizationAlgorithm oa, EvaluationFunction ef, int iterations) {
		double start = System.nanoTime();
        double[] results = new double[iterations+2];
        for (int i = 0; i < iterations; i++) {
        	oa.train();
        	results[i] = ef.value(oa.getOptimal());
        }
        double end = System.nanoTime();
        results[iterations] = ef.value(oa.getOptimal());
        results[iterations+1] = (end-start) / 1000000000;
        return results;
	}

}
