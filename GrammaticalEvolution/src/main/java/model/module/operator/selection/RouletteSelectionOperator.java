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

public class RouletteSelectionOperator extends SelectionOperator{
	int k;
	public RouletteSelectionOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		k = Integer.parseInt(properties.getProperty(Constants.TOURNAMENT_SIZE, Constants.DEFAULT_TOURNAMENT_SIZE));
	}


	@Override
	public void selectPopulation(Population population) {
		List<Individual>inds = population.stream().filter(Individual::isValid).collect(Collectors.toList());
		double sum = inds.stream().mapToDouble(Individual::getFitness).sum();
		for(int i=0;i<selectionSize;i++) {
			this.selectedPopulation.add(new Individual(getWinner(inds, rnd.nextDouble()*sum)));  
		}
	}
	private Individual getWinner(List<Individual> population, double v) {
		double s = 0d;

		for(Individual ind:population) {
			s += ind.getFitness();
			if(v<=s)return ind;
		}

		return population.get(population.size()-1);
	}

	 

}
