package model.algorithm;

import model.individual.Population;

public abstract class AbstractSearchAlgorithm {
	protected AbstractPipeline initPipeline,loopPipeline;
	
	public abstract void init();
	public abstract void step();
	public abstract void run(int its);
	
	public AbstractPipeline getInitPipeline() {
		return initPipeline;
	}

	public void setInitPipeline(AbstractPipeline initPipeline) {
		this.initPipeline = initPipeline;
	}

	public AbstractPipeline getLoopPipeline() {
		return loopPipeline;
	}

	public void setLoopPipeline(AbstractPipeline loopPipeline) {
		this.loopPipeline = loopPipeline;
	}
}
