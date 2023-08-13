package model.module;

import java.util.Properties;
import java.util.Random;

import model.grammar.AbstractGrammar;
import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.grammar.GrammarOperator;

public class GrammarModule extends Module{
	GrammarOperator operator;
	AbstractGrammar grammar;
	public GrammarModule(Population population, Properties properties, Random rnd, AbstractGrammar grammar) {
		super(population, properties, rnd);
		this.grammar = grammar;
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		operator.modify(population, grammar, null);
	}

	@Override
	public void addOperator(Operator op) {
		operator = (GrammarOperator)op;
	}

}
