package model.algorithm;

import javax.swing.SwingUtilities;

import model.individual.Population;

public class BasicSearchAlgorithm extends AbstractSearchAlgorithm{

	@Override
	public void init() {
		this.initPipeline.step();
	} 

	@Override
	public void step() {
		this.loopPipeline.step();
	}

	@Override
	public void run(int its) {
		long start = System.currentTimeMillis();
		init();
		System.out.println("-----------------------------------------Genration done in: "+(System.currentTimeMillis()-start)+"-----------------------------------------");

		for(int i=0;i<its;i++) {
			System.out.println("Starting Generation "+(i+1));
			long start2 = System.currentTimeMillis();
			step();
			System.out.println("-----------------------------------------Genration done in: "+(System.currentTimeMillis()-start2)+"-----------------------------------------");
		}
		System.out.println("-----------------------------------------Simulation done in: "+(System.currentTimeMillis()-start)+"-----------------------------------------");
	}

	
}
