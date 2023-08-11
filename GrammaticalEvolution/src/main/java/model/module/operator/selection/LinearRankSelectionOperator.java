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

public class LinearRankSelectionOperator extends SelectionOperator implements Stochastic{
	public LinearRankSelectionOperator(Properties properties, Random rnd) {
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
		inds.sort(Comparator.comparing(Individual::getFitness));
		int total = (inds.size()*(inds.size()+1))/2;
		for(int i=0;i<selectionSize;i++) {
			this.selectedPopulation.add(new Individual(getWinner(inds, total)));  
		}
	}
	private Individual getWinner(List<Individual> population, int total) {
		double s = 0d;
		double r = rnd.nextDouble();
		for(int i=0;i<population.size();i++) {
			double p = (double)(i+1)/total;
			s += p;
			if(r<=s)return population.get(i);
		}

		return population.get(population.size()-1);
	}

	 

}
