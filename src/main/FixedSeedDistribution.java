package main;

import shared.DataSet;
import shared.Instance;
import dist.DiscreteDistribution;
import dist.Distribution;

public class FixedSeedDistribution extends DiscreteDistribution implements Distribution {

	
	public FixedSeedDistribution(double[] arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void estimate(DataSet arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double logp(Instance arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Instance mode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance mode(Instance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double p(Instance arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Instance sample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance sample(Instance arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
