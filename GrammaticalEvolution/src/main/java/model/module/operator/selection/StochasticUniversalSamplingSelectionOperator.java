package model.module.operator.selection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import model.Constants;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;

public class StochasticUniversalSamplingSelectionOperator extends SelectionOperator{
	public StochasticUniversalSamplingSelectionOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
	}


	@Override
	public void selectPopulation(Population population) {
		List<Individual>inds = population.stream().filter(Individual::isValid).collect(Collectors.toList());
		double sum = inds.stream().mapToDouble(Individual::getFitness).sum();
		
		double delta = sum/selectionSize;

		double s = 0d;
		double tmp = rnd.nextDouble()*delta;
		
		for(Individual ind:inds) {
			s += ind.getFitness();
			if(tmp<s) {
				this.selectedPopulation.add(new Individual(ind));
				tmp += delta;
			}
		}
	}
}
