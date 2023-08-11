package model.module.operator.crossover;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.Util.Pair;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.module.operator.Operator;

public abstract class CrossoverOperator extends Operator{
	float probability;
	public CrossoverOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
		probability = Float.parseFloat(properties.getProperty(Constants.CROSSOVER_PROBABILITY, Constants.DEFAULT_CROSSOVER_PROBABILITY));
		
	}
	
	public void cross(Pair<Individual, Individual> parents) {
		if(this.probability > this.rnd.nextFloat()) {
			Pair<Chromosome, Chromosome> ncs = crossover(parents.first.getGenotype().getChromosome(0),parents.second.getGenotype().getChromosome(0));
			parents.first.setGenotype(new Genotype(ncs.first));
			parents.first.revaluate();
			parents.second.setGenotype(new Genotype(ncs.second));
			parents.second.revaluate();
		}
		
	}
	public abstract Pair<Chromosome, Chromosome> crossover(Chromosome c1, Chromosome c2);

}
