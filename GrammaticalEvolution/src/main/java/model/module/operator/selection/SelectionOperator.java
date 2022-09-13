package model.module.operator.selection;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.individual.Population;
import model.module.operator.Operator;

public abstract class SelectionOperator extends Operator{
	int selectionSize;
	Population selectedPopulation;
	public SelectionOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
		selectionSize = Integer.parseInt(properties.getProperty(Constants.SELECTION_SIZE, Constants.DEFAULT_SELECTION_SIZE));
		
	}
	
	public abstract void selectPopulation(Population population);
	public void setSelectedPopulation(Population selectedPopulation) {
		this.selectedPopulation = selectedPopulation;
	}
}
