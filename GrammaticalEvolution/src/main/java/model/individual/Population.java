package model.individual;

import java.util.ArrayList;

public class Population extends ArrayList<Individual>{
	public Population() {
		super();
	}
	public Population(int l) {
		super(l);
	}
	public void addIndividual(Individual i) {
		this.add(i);
	}
}
