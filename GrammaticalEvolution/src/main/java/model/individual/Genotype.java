package model.individual;

import java.util.ArrayList;
import java.util.Random;

import model.Constants;


public class Genotype extends ArrayList<Chromosome>{
	public Genotype(Chromosome c) {
		super();
		this.add(c);
	}
	public Genotype() {
		super();
		Chromosome c = new Chromosome(Constants.CROMOSOME_LENGTH);
		this.add(c);
	}
	public Genotype(Genotype copy) {
		super();
		for(Chromosome c:copy) {
			this.add(new Chromosome(c));
		}
	}
	public void init(Random rnd) {
		this.get(0).init(rnd);
	}
	public Chromosome getChromosome() {
		return this.get(0);
	}
	
}
