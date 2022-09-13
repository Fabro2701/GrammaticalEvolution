package model.module.operator.crossover;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.Util.Pair;
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
	public abstract void cross(Pair<Individual,Individual>parents);

}
