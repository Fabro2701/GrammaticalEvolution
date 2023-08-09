package model.module.operator.initialization;

import java.util.List;
import java.util.Properties;
import java.util.Random;

import model.grammar.AbstractGrammar;
import model.individual.Individual;
import model.module.operator.Operator;

public abstract class InitializationOperator extends Operator{
	AbstractGrammar grammar;
	public InitializationOperator(Properties properties, Random rnd, AbstractGrammar grammar) {
		super(properties, rnd);
		this.grammar = grammar;
	}
	
	public abstract Individual createIndividual();
	public abstract List<Individual> createIndividuals(int n);
}
