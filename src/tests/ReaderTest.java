package tests;

import static org.junit.Assert.assertEquals;
import main.EegEyeStateTest;

import org.junit.Before;
import org.junit.Test;

import shared.DataSet;
import shared.Instance;

public class ReaderTest {
	
	private static final String file = "EEGEyeState.txt";
	private Instance[] instances;
	
	@Before
	public void setUp() throws Exception {
		this.instances = EegEyeStateTest.initializeInstances();
		
	}
	
	@Test
	public void testSize() {
		assertEquals(14980, instances.length);
	}
	
	@Test
	public void testCorrectLabel() {
		assertEquals(instances[0].getLabel().getDiscrete(), 0);
	}
}
