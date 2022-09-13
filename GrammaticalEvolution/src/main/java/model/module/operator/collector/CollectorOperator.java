package model.module.operator.collector;

import java.util.Properties;
import java.util.Random;

import model.individual.Population;
import model.module.operator.Operator;

public abstract class CollectorOperator extends Operator{
	Population objetivePopulation;
	public CollectorOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}
	public abstract void collect();

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}
	
	public void setObjetivePopulation(Population objetivePopulation) {
		this.objetivePopulation = objetivePopulation;
	}


}
