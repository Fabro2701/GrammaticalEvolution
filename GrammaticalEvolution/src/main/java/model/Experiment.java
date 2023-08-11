package model;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.Random;

import model.algorithm.AbstractSearchAlgorithm;
import model.algorithm.BasicSearchAlgorithm;
import model.grammar.AbstractGrammar;
import model.individual.Population;
import model.module.operator.collector.CollectorOperator;
import model.module.operator.crossover.CrossoverOperator;
import model.module.operator.fitness.FitnessEvaluationOperator;
import model.module.operator.initialization.InitializationOperator;
import model.module.operator.join.JoinOperator;
import model.module.operator.mutation.MutationOperator;
import model.module.operator.selection.SelectionOperator;

public abstract class Experiment {
	protected AbstractSearchAlgorithm algorithm;
	volatile protected Population generalPopulation;
	volatile protected Population selectedPopulation;
	protected Random rnd;

	public Experiment(AbstractSearchAlgorithm algorithm) {
		this.algorithm  = algorithm;
		generalPopulation = new Population();
		selectedPopulation = new Population();
		rnd = new Random();
	}

	public Experiment() {
		this(new BasicSearchAlgorithm());
	}
	public abstract void setup(Properties properties);
	public void run(Properties properties) {
		this.algorithm.run(Integer.parseInt(properties.getProperty(Constants.ITERATIONS, Constants.DEFAULT_ITERATIONS)));
	}
	protected AbstractGrammar loadGrammar(Properties properties) {
		AbstractGrammar grammar = null;
		try {
			grammar = (AbstractGrammar)Class.forName(properties.getProperty(Constants.GRAMMAR, Constants.DEFAULT_GRAMMAR)).getConstructors()[0].newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		grammar.parseBNF(properties.getProperty(Constants.GRAMMAR_FILE,Constants.DEFAULT_GRAMMAR_FILE));
		return grammar;
	}
	protected InitializationOperator loadInitializer(Properties properties, AbstractGrammar grammar) {
		InitializationOperator op = null;
		try {
			op = (InitializationOperator)Class.forName(properties.getProperty(Constants.INITIALIZER_CLASS, Constants.DEFAULT_INITIALIZER_CLASS)).getConstructor(Properties.class,Random.class,AbstractGrammar.class).newInstance(properties,rnd,grammar);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return op;
	}
	protected CrossoverOperator loadCrossover(Properties properties) {
		CrossoverOperator op = null;
		try {
			op = (CrossoverOperator)Class.forName(properties.getProperty(Constants.CROSSOVER_CLASS, Constants.DEFAULT_CROSSOVER_CLASS)).getConstructor(Properties.class,Random.class).newInstance(properties,rnd);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return op;
	}
	protected MutationOperator loadMutation(Properties properties) {
		MutationOperator op = null;
		try {
			op = (MutationOperator)Class.forName(properties.getProperty(Constants.MUTATION_CLASS, Constants.DEFAULT_MUTATION_CLASS)).getConstructor(Properties.class,Random.class).newInstance(properties,rnd);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return op;
	}
	protected FitnessEvaluationOperator loadFitness(Properties properties) {
		FitnessEvaluationOperator op = null;
		try {
			op = (FitnessEvaluationOperator)Class.forName(properties.getProperty(Constants.FITNESS_CLASS, Constants.DEFAULT_FITNESS_CLASS)).getConstructor(Properties.class,Random.class).newInstance(properties,rnd);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return op;
	}
	protected JoinOperator loadJoin(Properties properties) {
		JoinOperator op = null;
		try {
			op = (JoinOperator)Class.forName(properties.getProperty(Constants.JOIN_CLASS, Constants.DEFAULT_JOIN_CLASS)).getConstructor(Properties.class,Random.class).newInstance(properties,rnd);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return op;
	}
	protected SelectionOperator loadSelection(Properties properties) {
		SelectionOperator op = null;
		try {
			op = (SelectionOperator)Class.forName(properties.getProperty(Constants.SELECTION_CLASS, Constants.DEFAULT_SELECTION_CLASS)).getConstructor(Properties.class,Random.class).newInstance(properties,rnd);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return op;
	}
	protected CollectorOperator loadCollector(Properties properties) {
		CollectorOperator op = null;
		try {
			op = (CollectorOperator)Class.forName(properties.getProperty(Constants.COLLECTOR_CLASS, Constants.DEFAULT_COLLECTOR_CLASS)).getConstructor(Properties.class,Random.class).newInstance(properties,rnd);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| SecurityException | ClassNotFoundException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return op;
	}
}
