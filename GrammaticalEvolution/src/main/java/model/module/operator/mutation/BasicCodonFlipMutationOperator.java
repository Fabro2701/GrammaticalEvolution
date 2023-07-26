package model.module.operator.mutation;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Chromosome;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;

public class BasicCodonFlipMutationOperator extends MutationOperator{
	float probability;
	public BasicCodonFlipMutationOperator(Properties properties, Random rnd) {
		super(properties, rnd);
	}

	@Override
	public void setProperties(Properties properties) {
		probability = Float.parseFloat(properties.getProperty(Constants.MUTATION_PROBABILITY, Constants.DEFAULT_MUTATION_PROBABILITY));
	}

	@Override
	public void mutateIndividual(Individual individual) {
		boolean b = false;
		Chromosome c = individual.getGenotype().getChromosome(0);
		for(int i=0;i<c.getLength();i++) {
			if(this.probability > this.rnd.nextFloat()) {
				c.setIntToCodon(i, rnd.nextInt(256));
				b = true;
			}
		}
		if(b)individual.revaluate();
	}
}
