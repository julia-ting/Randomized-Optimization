package optTests;

public class Result {

	private double[][] results;
	private String[] params;
	private String name;
	
	public Result(double[][] results, String name, String ...params) {
		this.results = results;
		this.params = params;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String[] getParams() {
		return params;
	}
	
	public double[][] getResults() {
		return results;
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
		result += "Run#,";
		for (int run = 0; run < results.length; run++) {
			result += run + ",";
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
//		String result = "+++++++++++++++++++++++++++++++++++++++++++++++\n";
//		result += "For " + name + " with the following parameters:\n";
//		for (String param : params) {
//			String[] parts = param.split(",");
//			result += parts[0] + "," + parts[1] + "\n";
//		}
//		result += "+++++++++++++++++++++++++++++++++++++++++++++++\n";
//		for (int i = 0; i < results.length; i++) {
//			result += "\n RUN NUMBER " + i + " FITNESS FUNCTION VALUES:\n\n";
//			for (int j = 0; j < results[i].length - 2; j++) {
//				result += j + "," + results[i][j] + "\n";
//			}
//			result += "\nFINAL OPTIMAL VALUE: " + results[i][results[i].length-2] + "\n";
//			result += "TRAINING TIME: " + results[i][results[i].length-1] + " seconds\n";
//			result += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
//		}
		
		return result;
	}
	
}
