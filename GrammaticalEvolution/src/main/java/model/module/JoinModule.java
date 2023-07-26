package model.module;

import java.util.Properties;
import java.util.Random;

import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.join.JoinOperator;

public class JoinModule extends Module{
	Population outsiders;
	JoinOperator operator;
	public JoinModule(Population population, Properties properties, Random rnd, Population outsiders) {
		super(population, properties, rnd);
		this.outsiders = outsiders;	
	}
	@Override
	public void execute() {
		this.operator.joinOutsiders(outsiders);
		for(Individual ind:population)ind.setAge(ind.getAge()+1);
	}
	@Override
	public void setProperties(Properties properties) {
		
	}
	@Override
	public void addOperator(Operator op) {
		this.operator = (JoinOperator)op;
		this.operator.setGeneralPopulation(population);
	}
}
