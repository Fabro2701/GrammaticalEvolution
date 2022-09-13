package model.module.operator.fitness;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;

public abstract class FitnessEvaluationOperator extends Operator{
	protected float defaultFitness;
	public FitnessEvaluationOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
	}

	public abstract float evaluate(Individual ind);

	

}
