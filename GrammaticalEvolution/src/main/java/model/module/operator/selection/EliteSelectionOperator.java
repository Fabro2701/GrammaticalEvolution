package model.module.operator.selection;

import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;

public class EliteSelectionOperator extends SelectionOperator{
	public EliteSelectionOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);

	}

	@Override
	public void selectPopulation(Population population) {
		population.sort(Comparator.comparing(Individual::getFitness));
		for(int i=0;i<selectionSize;i++) {
			this.selectedPopulation.add(new Individual(population.get(population.size()-1-i)));
		}
		DoubleSummaryStatistics stats = this.selectedPopulation.stream().mapToDouble(Individual::getFitness).summaryStatistics();
		System.out.println("avg selection "+stats.getAverage());
	}

}
