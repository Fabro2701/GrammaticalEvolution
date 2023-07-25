package model.module;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.mutation.MutationOperator;

public class MutationModule extends Module{
	MutationOperator operator;
	public MutationModule(Population population, Properties properties, Random rnd) {
		super(population, properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
	}
	@Override
	public void addOperator(Operator op) {
		operator = (MutationOperator)op;
		
	}
	@Override
	public void execute() {
		for(Individual ind:population) {
			operator.mutateIndividual(ind);
		}
	
	}
}
