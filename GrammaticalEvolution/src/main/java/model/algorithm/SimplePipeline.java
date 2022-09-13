package model.algorithm;

import model.individual.Population;
import model.module.Module;

public class SimplePipeline extends AbstractPipeline{

	@Override
	public void step() {
		for(Module m:this) {
			m.execute();
		}
	}

}
