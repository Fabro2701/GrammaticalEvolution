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

public class InterleavedFitnessModule extends FitnessModule{
	List<FitnessEvaluationOperator> ops;	
	List<FitnessEvaluationOperator> selectedOperators;	
	float interleavedProb;
	public InterleavedFitnessModule(Population population, Properties properties, Random rnd) {
		super(population, properties, rnd);
		ops = new ArrayList<>();
		selectedOperators = new ArrayList<>();
	}
	
	@Override
	public void execute() {
		selectedOperators.clear();
		for(FitnessEvaluationOperator op:ops) {
			if(interleavedProb<rnd.nextFloat()) {
				selectedOperators.add(op);
			}
		}
		if(selectedOperators.isEmpty())selectedOperators.add(ops.get(rnd.nextInt(ops.size())));
		
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
				ind = InterleavedFitnessModule.this.population.get(idx);
				//if(ind.isEvaluated())continue;
				if(ind.isValid()) {
					float s = 0f;
					for(FitnessEvaluationOperator op:InterleavedFitnessModule.this.selectedOperators) {
						s += op.evaluate(ind);
					}
					ind.setFitness(s);
				}
				else {
					ind.setFitness(InterleavedFitnessModule.this.defaultFitness);
				}
				ind.setEvaluated(true);
			}
		}
		
		
	}
	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		interleavedProb = Float.parseFloat(properties.getProperty("interleaved", "0.1"));
	}
	public InterleavedFitnessModule addOperator(FitnessEvaluationOperator op) {
		this.ops.add(op);
		return this;
		
	}
	@Override
	public void addOperator(Operator op) {
		System.err.println("inappropriate method for InterleavedFitnessModule");
	}
}
