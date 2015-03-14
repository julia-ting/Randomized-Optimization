package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;

import opt.NeighborFunction;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.CrossoverFunction;
import opt.ga.MutationFunction;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import func.nn.backprop.BackPropagationNetwork;

/**
 * Performs training and testing of given network with
 * given optimization algorithm.
 * @author Julia Ting (julia.ting@gatech.edu)
 *
 */
public class NeuralNetworkOptimizationProblemRunner {

	private OptimizationAlgorithm oa;
	private BackPropagationNetwork network;
	private String oaName;
	private DataSet set;
	private Instance[] instances;
	//private int percentSplitIndex;
    private static DecimalFormat df = new DecimalFormat("0.000");
    private ErrorMeasure measure;
    private NeuralNetworkOptimizationProblem nnop;

	
	public NeuralNetworkOptimizationProblemRunner(OptimizationAlgorithm oa, BackPropagationNetwork network,
			String oaName, DataSet set, ErrorMeasure measure,
			NeuralNetworkOptimizationProblem nnop) {
		this.oa = oa;
		this.network = network;
		this.oaName = oaName;
		this.set = set;
		this.instances = set.getInstances();
		//this.percentSplitIndex = calculateSplitIndex(percentSplit);
		this.measure = measure;
		this.nnop = nnop;
	}
	
	public void run(int trainingIterations, String fileName) {

    	double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
    	
        String results = train(trainingIterations); //trainer.train();
        
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);
        
        Instance optimalInstance = oa.getOptimal();
        network.setWeights(optimalInstance.getData());

        start = System.nanoTime();
        int[] accuracy = calculateCorrectlyClassified();
        correct = accuracy[0];
        incorrect = accuracy[1];
        end = System.nanoTime();
        testingTime = end - start;
        testingTime /= Math.pow(10,9);

        results +=  "\nResults for " + "RHC" + ": \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        writeResultsToFile(fileName, results);
	}

//	private int calculateSplitIndex(int percentSplit) {
//    	return (percentSplit == -1 || percentSplit == 0) ? -1 : (int)(instances.length * (percentSplit / 100.0));
//    }
//    
//    private DataSet trainingSplit(Instance[] instances, int index) {
//    	if (index == -1) {
//    		return set;
//    	}
//    	Instance[] splitInstances = Arrays.copyOf(instances, index);
//    	return new DataSet(splitInstances);
//    }
	
	private void writeResultsToFile(String fileName, String results) {
    	PrintWriter f = null;
    	try {
    		f = new PrintWriter(fileName, "UTF-8");
    		f.print(results);
    		f.close();
    	} catch (UnsupportedEncodingException | FileNotFoundException e) {
    		e.printStackTrace();
    	} finally {
    		f.close();
    	}
		
	}

	public int[] calculateCorrectlyClassified() {
        double predicted, actual;
        int correct = 0, incorrect = 0;
        for(int j = 0; j < instances.length; j++) {
            network.setInputValues(instances[j].getData());
            network.run();

            predicted = Double.parseDouble(instances[j].getLabel().toString());
            actual = Double.parseDouble(network.getOutputValues().toString());

            double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
        }
        return new int[] {correct, incorrect};
    }
	
	private String train(int trainingIterations) {
        String results = "\nError results for " + oaName + "\n---------------------------\n";
      
        for(int i = 0; i < trainingIterations; i++) {
            oa.train();

            double error = calculateError();
            String currError = i + ": " + df.format(error) + "\n";
            //System.out.print(currError);
            results += currError;
            
        }
        return results;
    }
	
	/**
     * This is our evaluation function metric. The nnet uses 1 / error as its fitness function.
     * 
     * @param network
     * @param percentSplitIndex
     * @return
     */
    public double calculateError() {
    	double error = 0;
    	//int index = (percentSplitIndex == -1) ? instances.length : percentSplitIndex;
        for(int j = 0; j < instances.length; j++) {
            network.setInputValues(instances[j].getData());
            network.run();

            Instance expected = instances[j].getLabel(), networkResult = new Instance(network.getOutputValues());
            networkResult.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
            error += measure.value(expected, networkResult);
        }
        return error; //nnet evaluation function is 1/error. That is our fitness function we are MAXIMIZING.
    }
	
}


