package optTests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import opt.EvaluationFunction;
import shared.DataSet;
import shared.ErrorMeasure;
import shared.Instance;
import shared.SumOfSquaresError;

public class MyOptimizationProblemsDriver {
	
    private static final int NUM_INSTANCES = 14980;
    private static final String DATA_FILE_NAME = "EEGEyeStateTesting.txt";
	private static final int NUM_ATTRIBUTES = 14;
	
    /**
     * The test main
     * @param args ignored
     */
	private static final int NUM_OA_ALGOS = 4;
    public static void main(String[] args) {
//    	double temp = 0;
//    	double coolingExp = 0;
//    	int popSize = 0, toMate = 0, toMutate = 0;
//    	int samples = 0, toKeep = 0;
    	Scanner key = new Scanner(System.in);
//    	System.out.println("Num iterations");
//    	int numIterations = Integer.parseInt(key.nextLine()); // 2000
//    	
//    	System.out.println("Num runs: ");
//    	int numRuns = Integer.parseInt(key.nextLine());
//    	
//    	System.out.println("~~~~~~~~~~~~~SIMULATED ANNEALING PARAMETERS~~~~~~~~~~~~~");
//    	double[] temps = new double[3];
//    	System.out.println("Starting temperature [FourPeaks, TSP, Knapsack]: ");
//    	String[] tempSt = key.nextLine().split(" ");
//    	for (int i = 0; i < tempSt.length; i++) {
//    		temps[i] = Double.parseDouble(tempSt[i]);
//    	}
//    	
//    	System.out.println("Cooling exponent");
//    	coolingExp = Double.parseDouble(key.nextLine());
//    	
//    	System.out.println("~~~~~~~~~~~~~GENETIC ALGORITHMS PARAMETERS~~~~~~~~~~~~~");
//    	
//    	System.out.println("Population size: ");
//    	popSize = Integer.parseInt(key.nextLine());
//    	
//    	System.out.println("Num to mate: ");
//    	toMate = Integer.parseInt(key.nextLine());
//    	
//    	System.out.println("Num to mutate: ");
//    	toMutate = Integer.parseInt(key.nextLine());
//    	
//    	System.out.println("~~~~~~~~~~~~~MIMIC PARAMETERS~~~~~~~~~~~~~");
//    	
//    	System.out.println("Num samples to generate: ");
//    	samples = Integer.parseInt(key.nextLine());
//    	
//    	System.out.println("Num samples to keep: ");
//    	toKeep = Integer.parseInt(key.nextLine());
//    	
//    	System.out.println("Name of file: ");
//    	String fileName = key.nextLine();
    	
//    	System.out.println("Part 1 or Part 2:");
//    	String assignmentPart = key.nextLine();
    	TestableOptimizationProblem[] probs = null;
    	boolean runMimic = false;
    	String assignmentPart = "1";
    	if (assignmentPart.equals("1")) {
//    		System.out.println("Network node counts:");
//    		String[] nn = key.nextLine().split(" ");
    		int[] network = { 14, 7, 1};
//    		for (int i = 0; i < nn.length; i++) {
//    			network[i] = Integer.parseInt(nn[i]);
//    		}
    		
    		ErrorMeasure measure = new SumOfSquaresError();
    		DataSet set = new DataSet(initializeInstances());
    		probs = new TestableOptimizationProblem[] { new MyNeuralNet(set, network, measure) };
    	} else {
    		probs = new TestableOptimizationProblem[3];
	    	probs[0] = new FourPeaks();
	    	probs[1] = new TravelingSalesman();
	    	probs[2] = new Knapsack();
	    	runMimic = true;
    	}
    	ExecutorService exec = Executors.newFixedThreadPool(Integer.parseInt(args[0]));
    	List<Future> resultsF = new ArrayList<Future>();
    	List<Runnable> jobs = new ArrayList<Runnable>();
    	
    	int numIterations = 2000;
    	int numRuns = 10;
    	
    	String fileName = "part1/";
    	double[] startTemps = { 1E9, 1E12, 1E15 };
    	double[] coolingExps = {.90, .95, .99};
 
    	// vary start temp and cooling exp
    	
    	int[] mates = { 125, 150, 175 };
    	int[] mutates = { 10, 15, 20, 25 };
    	// vary mutate and mate for GA

    	int[] samplesSize = { 200, 250, 300 };
    	int[] toKeeps = {50, 75, 100};
    	
    	int popSize = 200;
    	// sample size and to keep size
    	
    	for (int i = 0; i < probs.length; i++) {
    		EvaluationFunction[] efs = probs[i].getEvalFuncs(); // had to do this for traveling salesman
    		
    		String className = probs[i].getClass().getName().replace("optTests.","");
    		
    		RandomizedHillClimbingRunner rhcRun = 
    				new RandomizedHillClimbingRunner(
    						probs[i].getHillClimbingProblem(),
    						numIterations, numRuns, efs[0],
    						fileName+className);
    		jobs.add(rhcRun);
    		
     		for (int k = 0; k < startTemps.length; k++) {
    			for (int l = 0; l < coolingExps.length; l++) {
		    		SimulatedAnnealingRunner saRun =
		    				new SimulatedAnnealingRunner(probs[i].getHillClimbingProblem(), numIterations,
		    						numRuns, efs[1], startTemps[k], coolingExps[l], fileName+className);
		    		jobs.add(saRun);
    			}
    		}
    		if (!runMimic) {
    			numIterations = numIterations / 10;
    		}
    		for (int m = 0; m < mates.length; m++) {
    			for (int n = 0; n < mutates.length; n++) {
		    		GeneticAlgorithmRunner gaRun = new GeneticAlgorithmRunner(probs[i].getGeneticAlgorithmProblem(),
		    				numIterations, numRuns, efs[2], popSize, mates[m], mutates[n], fileName+className);
		    		jobs.add(gaRun);

    			}
    		}
    		if (runMimic) {
	    		for (int s = 0; s < samplesSize.length; s++) {
	    			for (int toK = 0; toK < toKeeps.length; toK++) {
			    		MimicRunner mimicRun = new MimicRunner(probs[i].getProbOptProblem(),
			    				numIterations, numRuns, efs[3], samplesSize[s], toKeeps[toK], fileName+className);
			    		jobs.add(mimicRun);
	    			}
	    		}
    		}
    		//Result[] results = {rhcR, saR, gaR, mimicR};
    		//writeToCsv(fileName + probs[i].getClass().getName().replace("optTests.", "") + ".txt",  results);
    		
    		
    	}
    	for (Runnable job : jobs) {
            resultsF.add(exec.submit(job));
        }
    	try {
	        for (Future<String> result : resultsF) {
	        	result.get();
	    	}
    	} catch (InterruptedException | ExecutionException e) {
    		e.printStackTrace();
    	} finally {
    		exec.shutdown();
    	}
    	
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
