package main;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

import opt.ContinuousAddOneNeighbor;
import opt.OptimizationAlgorithm;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.NeuralNetworkOptimizationProblem;
import opt.ga.StandardGeneticAlgorithm;

import org.junit.Before;
import org.junit.Test;

import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;
import shared.reader.DataSetLabelBinarySeperator;
import func.nn.backprop.BackPropagationNetwork;
import func.nn.backprop.BackPropagationNetworkFactory;


public class EegEyeStateTest {
    
    private static final int NUM_INSTANCES = 14980;
    private static final String DATA_FILE_NAME = "EEGEyeStateTesting.txt";
	private static final int NUM_ATTRIBUTES = 14;
	
    private static Instance[] instances = initializeInstances();

    // Taken from 
    private static int inputLayer = NUM_ATTRIBUTES, hiddenLayer = 15, outputLayer = 1, trainingIterations = 1000;
    private static int[] networkLayers = {inputLayer, hiddenLayer, outputLayer};
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(instances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
    	if (outputLayer == 2) {
    		DataSetLabelBinarySeperator.seperateLabels(set);
    	}
    	Scanner keyboard = new Scanner(System.in);
//    	System.out.println("(1): RHC (2): SA (3): GA");
//    	String oaProblem = keyboard.nextLine();

    	OptimizationAlgorithm oa = null;
    	String oaName = "";
    	BackPropagationNetwork network = factory.createClassificationNetwork(networkLayers);
    	NeuralNetworkOptimizationProblem currNnop = null;
//    	System.out.println("Num training iterations? ");
//    	int trainingIterations = Integer.parseInt(keyboard.nextLine());
//    	System.out.println("How many times would you like to run this problem?");
//    	int numProbIterations = Integer.parseInt(keyboard.nextLine());
//    	System.out.println("File name to save?");
//    	String name = keyboard.nextLine();
//    	System.out.println("Start temp: ");
//		double startTemp = Double.parseDouble(keyboard.nextLine());
//		System.out.println("Cooling exp:");
//		double coolExp = Double.parseDouble(keyboard.nextLine());
//
//    	System.out.println("Population size: ");
//		int popSize = Integer.parseInt(keyboard.nextLine());
//		System.out.println("Num to mate each iteration: ");
//		int numMate = Integer.parseInt(keyboard.nextLine());
//		System.out.println("Num to mutate each iteration: ");
//		int numMutate = Integer.parseInt(keyboard.nextLine());
    	int popSize = 200;
    	int numMate = 150;
    	int numMutate = 25;
    	String name = "200IterNnetOpt";
    	int numProbIterations = 10;
    	int trainingIterations = 200;
//    	double neighborAdd = 0;
//    	switch (oaProblem) {
//    		case "1":
 //   			System.out.println("Amount to change attribute by for neighbor func: ");
 //   			neighborAdd = Double.parseDouble(keyboard.nextLine());
//    			currNnop = new NeuralNetworkOptimizationProblem(set, network, measure
//    					);
//    			oa = new RandomizedHillClimbing(currNnop);
//    			oaName = "RHC";
//    			for (int i = 0; i < numProbIterations; i++) {
//        			String results = runRHC(trainingIterations, -1);
//    				writeResultsToFile(name+i+"RHC.txt", results);
//
//    			}
//    			break;
//    		case "2":
    			
//    			for (int i = 0; i < numProbIterations; i++) {
//    				String results = runSA(trainingIterations, -1, startTemp, coolExp);
//    				writeResultsToFile(name+i+"SA.txt", results);
//    			}
//    			break;
//    		case "3":
    			
    			for (int i = 3; i < numProbIterations; i++) {
    				String results = runGA(trainingIterations, -1, popSize, numMate, numMutate);
    				writeResultsToFile(name+i+"GA.txt", results);
    			}
//    			break;
    			
    	}
    	
//    	
//    	for (int i = 0; i < numProbIterations; i++) {
//	    	NeuralNetworkOptimizationProblemRunner runner =
//	    			new NeuralNetworkOptimizationProblemRunner(
//	    					oa,
//	    					factory.createClassificationNetwork(networkLayers),
//	    					oaName,
//	    					set,
//	    					measure,
//	    					currNnop);
//	    	runner.run(trainingIterations, name+i+".txt");
//	    	System.out.println("Run " + i + " complete.");
//    	}
//    	keyboard.close();
    	
    //	runRHC(2000, "RHC_2000_0_static.txt", -1);
//    }
    
    /**
     * 
     * @param trainingIterations
     * @param fileName
     * @param percentSplit -1 if use all training data as testing data, otherwise the percentage
     * of data you want to use to train. The rest will be used to test.
     */
    public static String runRHC(int trainingIterations, int percentSplit) {
    	int splitIndex = calculateSplitIndex(percentSplit);

    	BackPropagationNetwork network = factory.createClassificationNetwork(networkLayers);
    	NeuralNetworkOptimizationProblem currNnop =
    			new NeuralNetworkOptimizationProblem(trainingSplit(instances, splitIndex), network, measure);
    	OptimizationAlgorithm rhc = new RandomizedHillClimbing(currNnop);
    	
    	double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
    	
        String results = train(rhc, network, "RHC", trainingIterations, splitIndex); //trainer.train();
        
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);
        
        Instance optimalInstance = rhc.getOptimal();
        network.setWeights(optimalInstance.getData());

        start = System.nanoTime();
        int[] accuracy = calculateCorrectlyClassified(network, percentSplit);
        correct = accuracy[0];
        incorrect = accuracy[1];
        end = System.nanoTime();
        testingTime = end - start;
        testingTime /= Math.pow(10,9);

        results +=  "\nResults for " + "RHC" + ": \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        return results;
    }
    
    public static String runSA(int trainingIterations, int percentSplit,
    		double startTemp, double coolingExp) {
    	int splitIndex = calculateSplitIndex(percentSplit);

    	BackPropagationNetwork network = factory.createClassificationNetwork(networkLayers);
    	NeuralNetworkOptimizationProblem currNnop =
    			new NeuralNetworkOptimizationProblem(trainingSplit(instances, splitIndex), network, measure);
    	OptimizationAlgorithm sa = new SimulatedAnnealing(startTemp, coolingExp, currNnop);
    	
    	double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
    	
        String results = train(sa, network, "SA", trainingIterations, splitIndex); //trainer.train();
        
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);
        
        Instance optimalInstance = sa.getOptimal();
        network.setWeights(optimalInstance.getData());

        start = System.nanoTime();
        int[] accuracy = calculateCorrectlyClassified(network, percentSplit);
        correct = accuracy[0];
        incorrect = accuracy[1];
        end = System.nanoTime();
        testingTime = end - start;
        testingTime /= Math.pow(10,9);

        results +=  "\nResults for " + "SA" + ": \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        return results;
    }
    
    public static String runGA(int trainingIterations, int percentSplit,
    		int populationSize, int numMate, int numMutate) {
    	int splitIndex = calculateSplitIndex(percentSplit);

    	BackPropagationNetwork network = factory.createClassificationNetwork(networkLayers);
    	NeuralNetworkOptimizationProblem currNnop =
    			new NeuralNetworkOptimizationProblem(trainingSplit(instances, splitIndex), network, measure);
    	OptimizationAlgorithm ga = new StandardGeneticAlgorithm(populationSize, numMate, numMutate, currNnop);
    	
    	double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
    	
        String results = train(ga, network, "GA", trainingIterations, splitIndex); //trainer.train();
        
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10,9);
        
        Instance optimalInstance = ga.getOptimal();
        network.setWeights(optimalInstance.getData());

        start = System.nanoTime();
        int[] accuracy = calculateCorrectlyClassified(network, percentSplit);
        correct = accuracy[0];
        incorrect = accuracy[1];
        end = System.nanoTime();
        testingTime = end - start;
        testingTime /= Math.pow(10,9);

        results +=  "\nResults for " + "SA" + ": \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        return results;
    }
    
    private static void writeResultsToFile(String fileName, String results) {
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

	/**
     * This and the following few methods are created such that if a user chooses
     * to, they are able to split the data into training/testing ratios. The number
     * they enter is the percentage of the data they would like to use to train.
     * The rest will be used to test. The following method calculates the index
     * in their instances array at which the data will be split on.
     * 
     * If the user enters in to the driver the option to have all the training data
     * be used as testing data, then the index is -1 and the full data set is returned
     * as well as the full data set being used to test in train().
     * @param percentSplit
     * @return
     */
    
    private static int calculateSplitIndex(int percentSplit) {
    	return (percentSplit == -1 || percentSplit == 0) ? -1 : (int)(instances.length * (percentSplit / 100.0));
    }
    
    private static DataSet trainingSplit(Instance[] instances, int index) {
    	if (index == -1) {
    		return set;
    	}
    	Instance[] splitInstances = Arrays.copyOf(instances, index);
    	return new DataSet(splitInstances);
    }

    public static void runAllDefault() {
    	String results = "";
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);

        for(int i = 0; i < oa.length; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            System.out.println(train(oa[i], networks[i], oaNames[i], trainingIterations, calculateSplitIndex(-1))); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            start = System.nanoTime();
            int[] accuracy = calculateCorrectlyClassified(networks[i], -1);
            correct = accuracy[0];
            incorrect = accuracy[1];
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);

            results +=  "\nResults for " + oaNames[i] + ": \nCorrectly classified " + correct + " instances." +
                        "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                        + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                        + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        }

        System.out.println(results);
    }

    /**
     * Percentage split is the percent of data you want to use as your training. The rest will be used to test.
     * @param oa
     * @param network
     * @param oaName
     * @param trainingIterations
     * @param percentSplit
     * @return
     */
    private static String train(OptimizationAlgorithm oa, BackPropagationNetwork network,
    		String oaName, int trainingIterations, int percentSplitIndex) {
        String results = "\nError results for " + oaName + "\n---------------------------\n";
      
        for(int i = 0; i < trainingIterations; i++) {
            oa.train();
            
            double error = calculateError(network, percentSplitIndex);
            String currError = i + ": " + df.format(error) + "\n";
            System.out.print(currError);
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
    public static double calculateError(BackPropagationNetwork network, int percentSplitIndex) {
    	double error = 0;
    	//TODO: Might not start at percentsplitIndex. Error is probably calculated on the TRAINING data?
    	int index = (percentSplitIndex == -1) ? instances.length : percentSplitIndex;
        for(int j = 0; j < instances.length; j++) {
            network.setInputValues(instances[j].getData());
            
            network.run();
           // System.out.println(instances[j].getData().toString());
            //System.out.println("OUTPUT: " + network.getOutputValues().toString());
            Instance expected = instances[j].getLabel(), networkResult = new Instance(network.getOutputValues());
            if (outputLayer == 2) {
            	networkResult.setLabel(new Instance(network.getOutputValues()));
            } else {
            	networkResult.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
//            	System.out.println(Double.parseDouble(network.getOutputValues().toString()));
//            	System.out.println("Label: " + Double.parseDouble(instances[j].getLabel().toString()));
            }
            error += measure.value(expected, networkResult);
            
        }
        return error; //nnet evaluation function is 1/error. That is our fitness function we are MAXIMIZING.
    }
    
    public static int[] calculateCorrectlyClassified(BackPropagationNetwork network, int percentSplitIndex) {
        double predicted, actual = 0;
        int correct = 0, incorrect = 0;
        for(int j = percentSplitIndex + 1; j < instances.length; j++) {
            network.setInputValues(instances[j].getData());
            network.run();
            
            if (outputLayer == 2) {
	            Instance predictedLabel = instances[j].getLabel();
	            predicted = predictedLabel.getData().get(0) == 1 ? 0 : 1; // one hot classification
	            double max = network.getOutputValues().get(0);
	            for (int i = 1; i < network.getOutputValues().size(); i++) {
	            	if (network.getOutputValues().get(i) > max) {
	            		max = network.getOutputValues().get(i);
	            		actual = i;
	            	}
	            }
	            double trash = predicted == actual ? correct++ : incorrect++;
            } else {
            	predicted = Double.parseDouble(instances[j].getLabel().toString());
            	actual = Double.parseDouble(network.getOutputValues().toString());
                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
            }
            
        }
        return new int[] {correct, incorrect};
    }

    public static Instance[] initializeInstances() {

        double[][][] attributes = new double[NUM_INSTANCES][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(DATA_FILE_NAME)));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][]; // for attribute array and label array
                attributes[i][0] = new double[NUM_ATTRIBUTES]; // attributes
                attributes[i][1] = new double[1]; // label

                for(int j = 0; j < NUM_ATTRIBUTES; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next()); // scan attribute

                attributes[i][1][0] = Double.parseDouble(scan.next()); // scan label
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            instances[i].setLabel(new Instance(attributes[i][1][0]));
        }
        return instances;
    }
}
