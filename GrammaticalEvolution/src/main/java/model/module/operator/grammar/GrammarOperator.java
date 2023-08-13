package model.module.operator.grammar;

import java.util.Properties;
import java.util.Random;

import model.grammar.AbstractGrammar;
import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.fitness.FitnessEvaluationOperator;

public abstract class GrammarOperator extends Operator{
	protected int updateRate;
	public GrammarOperator(Properties properties, Random rnd) {
		super(properties, rnd);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}

	public abstract void modify(Population population, AbstractGrammar grammar);

	public int getUpdateRate() {
		return updateRate;
	}
}
