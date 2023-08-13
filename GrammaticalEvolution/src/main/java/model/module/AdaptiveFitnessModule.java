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

public class AdaptiveFitnessModule extends FitnessModule{
	List<FitnessEvaluationOperator> ops;	
	FitnessEvaluationOperator initOp;	
	FitnessEvaluationOperator selectedOp;
	int count;
	int m,g;
	public AdaptiveFitnessModule(Population population, Properties properties, Random rnd) {
		super(population, properties, rnd);
		ops = new ArrayList<>();
		count = 0;
	}
	
	@Override
	public void execute() {
		if(count<g)selectedOp = initOp;
		else {
			int sets = ops.size();
			int idx = (int)((count-g)%(sets*m))/m;
			//System.out.println(idx+"------");
			selectedOp = ops.get(idx);
		}
		count++;
		
		ForkJoinPool pool = ForkJoinPool.commonPool();
		Task task = new Task(0, population.size()-1);
		pool.invoke(task);
		pool.shutdown();
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
				ind = AdaptiveFitnessModule.this.population.get(idx);
				//if(ind.isEvaluated())continue;
				if(ind.isValid()) {
					ind.setFitness(selectedOp.evaluate(ind));
				}
				else {
					ind.setFitness(AdaptiveFitnessModule.this.defaultFitness);
				}
				ind.setEvaluated(true);
			}
		}
		
		
	}
	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		m = Integer.parseInt(properties.getProperty("adaptive_m", "10"));
		g = Integer.parseInt(properties.getProperty("adaptive_g", "10"));
	}
	public AdaptiveFitnessModule addOperator(FitnessEvaluationOperator op) {
		this.ops.add(op);
		return this;
		
	}
	@Override
	public void addOperator(Operator op) {
		System.err.println("inappropriate method for InterleavedFitnessModule");
	}

	public void setInitOp(FitnessEvaluationOperator initOp) {
		this.initOp = initOp;
	}
}
