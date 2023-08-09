package model.module.operator.initialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.grammar.AbstractGrammar;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;

public class RandomValidNoDupInitializerOperator extends InitializationOperator implements MultipleInitializer{
	public RandomValidNoDupInitializerOperator(Properties properties, Random rnd, AbstractGrammar grammar) {
		super(properties, rnd, grammar);
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}
	@Override
	public List<Individual> createIndividuals(int n) {
		List<Individual>inds = new ArrayList<>();
		for(int i=0;i<n;i++) {
			Chromosome chr = new Chromosome(Constants.CROMOSOME_LENGTH);
			chr.init(rnd);
			Genotype geno = new Genotype(chr);
			Phenotype pheno = new Phenotype();
			Individual ind = new Individual(geno,pheno,this.grammar);
			while(!ready(inds,ind)) {
				chr.init(rnd);
				ind.revaluate();
			}
			inds.add(ind);
		}
		return inds;
	}
	private boolean ready(List<Individual>inds, Individual ind) {
		if(!ind.isValid())return false;
		for(Individual i2:inds)
			if(i2.getGenotype().getChromosome(0).equals(ind.getGenotype().getChromosome(0)))return false;
		return true;
	}
	@Override
	public Individual createIndividual() {
		
		return null;
	}
	

}
