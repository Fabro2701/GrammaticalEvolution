package model.module.operator.collector;

import java.util.DoubleSummaryStatistics;
import java.util.Properties;
import java.util.Random;

import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;
import view.GrammaticalEvolutionMainFrame;

public class FitnessCollectorOperator extends CollectorOperator{
	GrammaticalEvolutionMainFrame frame;
	public FitnessCollectorOperator(Properties properties, Random rnd) {
		super(properties, rnd);
	}
	public void setFrame(GrammaticalEvolutionMainFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void collect() {
		DoubleSummaryStatistics stats = this.objetivePopulation.stream().mapToDouble(Individual::getFitness).summaryStatistics();
		Individual best = this.objetivePopulation.stream().max((e1,e2)->Float.compare(e1.getFitness(), e2.getFitness())).get();
		//if(best.isValid())System.out.println(best.getPhenotype().getVisualCode());
		if(best.isValid())System.out.println(best.getPhenotype().getPlainSymbols());
		System.out.println("Best Individual: "+stats.getMax());
		System.out.println("Avg Individual: "+stats.getAverage());		
		
		if(frame!=null)frame.updateStats(stats.getMax(),stats.getAverage());
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}



}
