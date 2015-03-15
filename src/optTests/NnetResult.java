package optTests;

import java.text.DecimalFormat;

import opt.OptimizationProblem;
import opt.example.NeuralNetworkOptimizationProblem;
import shared.Instance;
import func.nn.backprop.BackPropagationNetwork;

public class NnetResult extends Result {

    private static DecimalFormat df = new DecimalFormat("0.000");
    private OptimizationProblem op;
    private Instance[] optimalVals;

	public NnetResult(OptimizationProblem op, Instance[] optimalVals, double[][] results, String name, String... params) {
		super(results, name, params);
		this.op = op;
		this.optimalVals = optimalVals;
	}
	
	@Override
	public String toString() {
		String result = "+++++++++++++++++++++++++++++++++++++++++++++++\n";
		result += "For " + getName() + " with the following parameters:\n";
		for (String param : getParams()) {
			String[] parts = param.split(",");
			result += parts[0] + "," + parts[1] + "\n";
		}
		result += "+++++++++++++++++++++++++++++++++++++++++++++++\n";
		double[][] results = getResults();
		for (int run = 0; run < results.length; run++) {
			result += "RunNumber," + run + ",";
		}
		result = result.substring(0, result.length());
		result += "\n";
		for (int iter = 0; iter < results[0].length - 2; iter++) {
			result += iter + ",";
			for (int run = 0; run < results.length; run++) {
				result += results[run][iter] + ",";
			}
			result = result.substring(0, result.length()) + "\n";
		}
		result += "TrainingTime,";
		for (int run = 0; run < results.length; run++) {
			result+= results[run][results[0].length-1] + ",";
		}
		result = result.substring(0, result.length()) + "\n";
		result += getCorrectlyClassifiedString(results.length);
//		for (int i = 0; i < results.length; i++) {
//			result += "\n RUN NUMBER " + i + " FITNESS FUNCTION VALUES:\n\n";
//			for (int j = 0; j < results[i].length - 2; j++) {
//				result += j + "," + results[i][j] + "\n";
//			}
//			result += "\nFINAL OPTIMAL VALUE," + results[i][results[i].length-2] + "\n";
//			result += getCorrectlyClassifiedString(i);
//			result += "Training time," + results[i][results[i].length-1] + "\n";
//			result += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
//		}
		
		return result;
	}
	
	public String getCorrectlyClassifiedString(int totalRuns) {
		String result = "";
        double[][] accuracy = calculateCorrectlyClassified(totalRuns);
        result += "TestingTime,";
        for (int run = 0; run < totalRuns; run++) {
        	result += accuracy[run][2] + ",";
        }
		result = result.substring(0, result.length()) + "\n";
		result += "CorrectClassified,";
        for (int run = 0; run < totalRuns; run++) {
        	result += accuracy[run][0] + ",";
        }
		result = result.substring(0, result.length()) + "\n";
		result += "IncorrectClassified,";
        for (int run = 0; run < totalRuns; run++) {
        	result += accuracy[run][1] + ",";
        }
		result = result.substring(0, result.length()) + "\n";

        return result;
	}
	
	private double[][] calculateCorrectlyClassified(int totalRuns) {
		double predicted, actual = 0;
		int correct = 0, incorrect = 0;
		Instance[] instances = ((NeuralNetworkOptimizationProblem)op).getDataSet().getInstances();
		BackPropagationNetwork network = (BackPropagationNetwork)((NeuralNetworkOptimizationProblem)op).getNetwork();
		double[][] accuracy = new double[totalRuns][3];
		for (int run = 0; run < totalRuns; run++) {
			double start = System.nanoTime();
			network.setWeights(optimalVals[run].getData());
			for(int j = 0; j < instances.length; j++) {
	             network.setInputValues(instances[j].getData());
	             network.run();
	
	             predicted = Double.parseDouble(instances[j].getLabel().toString());
	             actual = (network.getBinaryOutputValue()) ? 1 : 0;
	
	             double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
			}
	        double end = System.nanoTime();
	        double testingTime = (end - start) / Math.pow(10,9);
			accuracy[run] = new double[] {correct, incorrect, testingTime};
		}
        return accuracy;

	}

}
