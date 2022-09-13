package model.module;

import java.util.Properties;
import java.util.Random;

import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.collector.CollectorOperator;
import model.module.operator.join.JoinOperator;

public class CollectorModule extends Module{
	CollectorOperator operator;
	public CollectorModule(Population population, Properties properties, Random rnd) {
		super(population, properties, rnd);
	}
	@Override
	public void execute() {
		this.operator.collect();
	}
	@Override
	public void setProperties(Properties properties) {
		
	}
	@Override
	public void addOperator(Operator op) {
		this.operator = (CollectorOperator)op;
		this.operator.setObjetivePopulation(population);
	}
}
