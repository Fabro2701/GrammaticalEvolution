package model.module.operator.join;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Population;
import model.module.operator.Operator;

public abstract class JoinOperator extends Operator{
	Population generalPopulation;
	int num;
	public JoinOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
		num = Integer.parseInt(properties.getProperty(Constants.POPULATION_SIZE, Constants.DEFAULT_NUM_POPULATION_SIZE));
		
	}
	public abstract void joinOutsiders(Population outsiders);
	public void setGeneralPopulation(Population generalPopulation) {
		this.generalPopulation = generalPopulation;
	}
}
