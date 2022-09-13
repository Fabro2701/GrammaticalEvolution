package model.module.operator.initialization;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.grammar.AbstractGrammar;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;
import model.individual.Population;
import model.module.operator.Operator;

public class RandomInitializerOperator extends InitializationOperator{
	int num;
	public RandomInitializerOperator(Properties properties, Random rnd, AbstractGrammar grammar) {
		super(properties, rnd, grammar);
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}
	@Override
	public Individual createIndividual() {
		Chromosome chr = new Chromosome(Constants.CROMOSOME_LENGTH);
		chr.init(rnd);
		
		Genotype geno = new Genotype(chr);
		Phenotype pheno = new Phenotype();
		
		return new Individual(geno,pheno,this.grammar);
	}
	

}
