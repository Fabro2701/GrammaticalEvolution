package model.module.operator.mutation;

import java.util.Properties;
import java.util.Random;

import model.individual.Individual;
import model.module.operator.Operator;

public abstract class MutationOperator extends Operator{

	public MutationOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void setProperties(Properties properties);
	
	public abstract void mutateIndividual(Individual individual);
}
