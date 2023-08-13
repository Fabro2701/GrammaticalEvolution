package model.module.operator;

import java.util.Properties;
import java.util.Random;

import model.IProperties;
import model.individual.Population;

public abstract class Operator implements IProperties{
	protected Random rnd;
	protected Properties properties;
	public Operator(Properties properties, Random rnd) {
		this.rnd = rnd;
		this.properties = properties;
		this.setProperties(properties);
	}
	//public abstract Population execute(Population population);

}
