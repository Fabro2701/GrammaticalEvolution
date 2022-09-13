package model.module;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.Util.Pair;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.crossover.CrossoverOperator;

public class CrossoverModule extends Module{
	int num;
	CrossoverOperator operator;
	
	public CrossoverModule(Population population, Properties properties, Random rnd) {
		super(population, properties, rnd);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void execute() {
		Pair<Individual,Individual>parents = new Pair<Individual,Individual>(null,null);
		for(int i=0;i<population.size();i++) {
			parents.first = this.population.get(rnd.nextInt(this.population.size()));
			parents.second = this.population.get(rnd.nextInt(this.population.size()));
			this.operator.cross(parents);
		}
	}
	@Override
	public void setProperties(Properties properties) {
		num = Integer.parseInt(properties.getProperty(Constants.POPULATION_SIZE, Constants.DEFAULT_NUM_POPULATION_SIZE));
		
	}
	@Override
	public void addOperator(Operator op) {
		this.operator = (CrossoverOperator)op;
		
	}
}
