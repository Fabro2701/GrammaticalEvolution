package model.module.operator.collector;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;

import model.individual.Individual;
import model.module.operator.fitness.FitnessEvaluationOperator;
import view.GrammaticalEvolutionMainFrame;

public class FitnessCollectorOperator extends CollectorOperator{
	GrammaticalEvolutionMainFrame frame;
	Individual lastBest;
	Map<String,FitnessEvaluationOperator>ops;
	Map<String,Double>validations;
	public FitnessCollectorOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		ops = new HashMap<>();
		validations = new HashMap<>();
	}
	public void setFrame(GrammaticalEvolutionMainFrame frame) {
		this.frame = frame;
	}
	public void addValidationOps(Map<String,FitnessEvaluationOperator>ops) {
		this.ops.putAll(ops);
	}
	
	@Override
	public void collect() {
		DoubleSummaryStatistics stats = this.objetivePopulation.stream().mapToDouble(Individual::getFitness).summaryStatistics();
		Individual best = this.objetivePopulation.stream().max((e1,e2)->Float.compare(e1.getFitness(), e2.getFitness())).get();
		//if(best.isValid())System.out.println(best.getPhenotype().getVisualCode());
		if(best.isValid())System.out.println(best.getPhenotype().getPlainSymbols());
		System.out.println("Best Individual: "+stats.getMax());
		System.out.println("Avg Individual: "+stats.getAverage());		
		
		if(best!=lastBest) {
			for(Entry<String,FitnessEvaluationOperator>op:ops.entrySet()) {
				this.validations.put(op.getKey(), (double) op.getValue().evaluate(best));
			}
			lastBest = best;
		}
		
		
		if(frame!=null)frame.updateStats(stats.getMax(),stats.getAverage(),this.validations);
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		
	}



}
