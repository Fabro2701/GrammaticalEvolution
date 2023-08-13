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
		for(int i=0;i<Constants.PLOIDY;i++) {
			Chromosome c = new Chromosome(Constants.CROMOSOME_LENGTH);
			this.add(c);
		}
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
	public Chromosome getChromosome(int i) {
		return this.get(i);
	}
	public void _clear() {
		for(Chromosome c:this)c.clear();
		this.clear();
	}
	
}
