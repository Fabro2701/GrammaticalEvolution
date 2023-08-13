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
	int count;
	public GrammarModule(Population population, Properties properties, Random rnd, AbstractGrammar grammar) {
		super(population, properties, rnd);
		this.grammar = grammar;
		this.count = 0;
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute() {
		count++;
		if(count%operator.getUpdateRate()==0)operator.modify(population, grammar);
	}

	@Override
	public void addOperator(Operator op) {
		operator = (GrammarOperator)op;
	}

}
