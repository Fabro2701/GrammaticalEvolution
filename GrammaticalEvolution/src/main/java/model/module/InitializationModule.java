package model.module;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.initialization.InitializationOperator;
import model.module.operator.initialization.MultipleInitializer;

public class InitializationModule extends Module{
	InitializationOperator operator;
	int num;
	
	public InitializationModule(Population population, Properties properties, Random rnd) {
		super(population, properties, rnd);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void setProperties(Properties properties) {
		num = Integer.parseInt(properties.getProperty(Constants.POPULATION_SIZE, Constants.DEFAULT_NUM_POPULATION_SIZE));
	}
	@Override
	public void execute() {
		if(operator instanceof MultipleInitializer)population.addAll(operator.createIndividuals(num));
		else {
			for(int i=0;i<num;i++) {
				population.add(operator.createIndividual());
			}
		}
	}
	@Override
	public void addOperator(Operator op) {
		operator = (InitializationOperator)op;
	}
}
