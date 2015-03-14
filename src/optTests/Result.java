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
	
	public String[] getParams() {
		return params;
	}
	
	public double[][] getResults() {
		return results;
	}
	
	@Override
	public String toString() {
		String result = "+++++++++++++++++++++++++++++++++++++++++++++++\n";
		result += "For " + name + " with the following parameters:\n";
		for (String param : params) {
			String[] parts = param.split(",");
			result += parts[0] + "," + parts[1] + "\n";
		}
		result += "+++++++++++++++++++++++++++++++++++++++++++++++\n";
		for (int i = 0; i < results.length; i++) {
			result += "\n RUN NUMBER " + i + " FITNESS FUNCTION VALUES:\n\n";
			for (int j = 0; j < results[i].length - 1; j++) {
				result += j + "," + results[i][j] + "\n";
			}
			result += "\nFINAL OPTIMAL VALUE: " + results[i][results[i].length-1] + "\n";
			result += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
		}
		
		return result;
	}
	
}
