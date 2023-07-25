package model.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import model.Constants;
import model.individual.Individual;
import model.individual.Population;
import model.module.operator.Operator;
import model.module.operator.fitness.FitnessEvaluationOperator;

public class FitnessModule extends Module{
	float defaultFitness;
	FitnessEvaluationOperator operator;	
	public FitnessModule(Population population, Properties properties, Random rnd) {
		super(population, properties, rnd);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute() {
		ForkJoinPool pool = ForkJoinPool.commonPool();
		Task task = new Task(0,population.size()-1);
		pool.invoke(task);


//		for(Individual ind:population) {
//			if(ind.isEvaluated())continue;
//			if(ind.isValid()) {
//				ind.setFitness(operator.evaluate(ind));
//			}
//			else {
//				ind.setFitness(this.defaultFitness);
//			}
//			ind.setEvaluated(true);
//		}
		
	}
	private class Task extends RecursiveAction {
		int i,j;
		public Task(int i, int j) {
			this.i = i;
			this.j =j;
		}
		@Override
		protected void compute() {
			if(j-i >= 2) {
				ForkJoinTask.invokeAll(createSubtasks());
			}
			else {
				doCompute();
			}
		}
		private List<Task> createSubtasks() {
			List<Task> subtasks = new ArrayList<Task>();

			int midpoint = i + (j - i) / 2;
	        subtasks.add(new Task(i, midpoint));
	        subtasks.add(new Task(midpoint+1, j));

	        return subtasks;
		}
		private void doCompute() {
			Individual ind = null;
			for(int idx=i; idx<=j; idx++) {
				ind = FitnessModule.this.population.get(idx);
				if(ind.isEvaluated())continue;
				if(ind.isValid()) {
					ind.setFitness(operator.evaluate(ind));
				}
				else {
					ind.setFitness(FitnessModule.this.defaultFitness);
				}
				ind.setEvaluated(true);
			}
		}
		
		
	}
	@Override
	public void setProperties(Properties properties) {
		defaultFitness = Float.parseFloat(properties.getProperty(Constants.DEFAULT_FITNESS, Constants.DEFAULT_FITNESS_VALUE));

	}
	@Override
	public void addOperator(Operator op) {
		operator = (FitnessEvaluationOperator)op;
		
	}
}
