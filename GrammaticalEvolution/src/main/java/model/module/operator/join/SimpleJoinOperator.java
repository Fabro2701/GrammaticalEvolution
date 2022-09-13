package model.module.operator.join;

import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Random;

import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;

public class SimpleJoinOperator extends JoinOperator{

	public SimpleJoinOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
	}


	@Override
	public void joinOutsiders(Population outsiders) {
		if(outsiders.size()>num) {
			trimpopulation(outsiders,outsiders.size()-num);
			generalPopulation.clear();
		}
		else if(generalPopulation.size()+outsiders.size()>num) {
			trimpopulation(generalPopulation,generalPopulation.size()+outsiders.size()-num);
		}
		//System.out.println("ideal num "+num);
		System.out.println("joining "+outsiders.size()+" to "+generalPopulation.size());
//		System.out.println("___outsiders: ");
//		for(Individual ind : outsiders) {
//			System.out.println(ind.isValid()?ind:"nonvalid");
//		}
		this.generalPopulation.addAll(outsiders);
		
	}

	private void trimpopulation(Population population, int size) {
		if(size<1)return;
		//System.out.println("removing: "+size);
		Collections.sort(population, Collections.reverseOrder());
		for(int i=0;i<size;i++) {
			population.remove(population.size()-1);
		}
		
	}

}
