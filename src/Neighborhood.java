import java.util.Set;

import shared.Instance;


public class Neighborhood {

	private double weightUpdate;
	private Instance currInstance;
	private Set<Instance> neighbors;
	private Set<Instance> visited;
	
	public Neighborhood(Instance d, double weightUpdate) {
		this.weightUpdate = weightUpdate;
		this.currInstance = d;
	}
	
	public Set<Instance> getNeighborhood() {
		return neighbors;
	}
	
	public void markVisited(Instance d) {
		if (neighbors.contains(d)) {
			visited.add(d);
		}
	}
}
