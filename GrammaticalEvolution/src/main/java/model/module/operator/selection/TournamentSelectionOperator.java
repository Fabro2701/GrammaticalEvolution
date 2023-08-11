package model.module.operator.selection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;

public class TournamentSelectionOperator extends SelectionOperator implements Stochastic{
	int k;
	public TournamentSelectionOperator(Properties properties, Random rnd) {
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
		for(int i=0;i<selectionSize;i++) {
			this.selectedPopulation.add(new Individual(getWinner(population)));
		}
	}
	private Individual getWinner(Population population) {
		List<Individual>l = new ArrayList<Individual>();
		for(int i=0;i<k;i++) {
			l.add(population.get(rnd.nextInt(population.size())));
		}
		l.sort(Comparator.comparing(Individual::getFitness));
//		for(Individual ind:l) {
//			System.out.print(ind.getFitness()+" , ");
//		}System.out.println();
//		if(l.get(l.size()-1).isValid())System.out.println("winner: "+l.get(l.size()-1).getPhenotype().getVisualCode());
//		else System.out.println("unvalid winner");
		return l.get(l.size()-1);
	}

	 

}
