package model.module;

import java.util.Properties;
import java.util.Random;

import model.IProperties;
import model.individual.Population;
import model.module.operator.Operator;

public abstract class Module implements IProperties{
	Population population;
	Random rnd;
	public Module(Population population, Properties properties, Random rnd) {
		this.population = population;
		this.setProperties(properties);
		this.rnd = rnd;
	}
	public abstract void execute();
	public abstract void addOperator(Operator op);
	public void setPopulation(Population population) {
		this.population = population;
	}
	public Population getPopulation() {
		return this.population;
	}

}
