package model.module;

import java.util.Properties;
import java.util.Random;

import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.selection.SelectionOperator;

public class SelectionModule extends Module{
	Population selectedPopulation;
	SelectionOperator operator;
	public SelectionModule(Population population, Properties properties, Random rnd, Population selectedPopulation) {
		super(population, properties, rnd);
		this.selectedPopulation = selectedPopulation;
	}
	
	@Override
	public void execute() {
		this.selectedPopulation.clear();
		operator.selectPopulation(population);
		//this.population.removeAll(selectedPopulation);
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addOperator(Operator op) {
		this.operator = (SelectionOperator)op;
		this.operator.setSelectedPopulation(selectedPopulation);
		
	}
}
