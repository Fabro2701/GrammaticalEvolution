package model.module.operator.collector;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Properties;
import java.util.Random;

import model.Util;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;

public class SimilarityCollectorOperator extends CollectorOperator{

	public SimilarityCollectorOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void collect() {
		ArrayList<ArrayList<Float>> matrix = Util.genotypeSimilarityMatrix(objetivePopulation);
		float avg = matrix.stream().map(l->l.stream().reduce(0.f,Float::sum)).reduce(0.f,Float::sum) / (objetivePopulation.size()*(objetivePopulation.size()-1));
		System.out.println("Average genotype similarity: "+avg);	
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}
}
