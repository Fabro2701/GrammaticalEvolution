package model.algorithm;

import java.util.ArrayList;

import model.individual.Population;
import model.module.Module;

public abstract class AbstractPipeline extends ArrayList<Module>{
	
	
	public AbstractPipeline() {
		super();
	}
	public abstract void step();
	public void addModule(Module m) {
		this.add(m);
	}
}
