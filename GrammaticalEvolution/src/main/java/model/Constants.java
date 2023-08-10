package model;

public class Constants {
	
	public static final int CROMOSOME_LENGTH = 100;
	public static final int PLOIDY = 2;
	
	//default values
	public static final String DEFAULT_NUM_POPULATION_SIZE = "100";
	public static final String DEFAULT_MUTATION_PROBABILITY = "0.01f";
	public static final String DEFAULT_CROSSOVER_PROBABILITY = "0.9f";
	public static final String DEFAULT_SELECTION_SIZE = "30";
	public static final String DEFAULT_FITNESS_VALUE = "-10000f";
	public static final String DEFAULT_TOURNAMENT_SIZE = "3";

	//properties name
	public static final String POPULATION_SIZE = "population_size";
	public static final String MUTATION_PROBABILITY = "mutation_prob";
	public static final String CROSSOVER_PROBABILITY = "crossover_prob";
	public static final String SELECTION_SIZE = "selection_size";
	public static final String DEFAULT_FITNESS = "default_fitness";
	public static final String TOURNAMENT_SIZE = "tournament_size";
	
	//setup experiment
	public static final String GRAMMAR = "grammar";
	public static final String DEFAULT_GRAMMAR = "model.grammar.StandardGrammar";
	public static final String GRAMMAR_FILE = "grammar_file";
	public static final String DEFAULT_GRAMMAR_FILE = "default";
	public static final String ITERATIONS = "iterations";
	public static final String DEFAULT_ITERATIONS = "100";
	public static final String INITIALIZER_CLASS = "initializer";
	public static final String DEFAULT_INITIALIZER_CLASS = "model.module.operator.initialization.RandomInitializerOperator";
	public static final String CROSSOVER_CLASS = "crossover";
	public static final String DEFAULT_CROSSOVER_CLASS = "model.module.operator.crossover.OnePointCrossoverOperator";
	public static final String MUTATION_CLASS = "mutation";
	public static final String DEFAULT_MUTATION_CLASS = "model.module.operator.mutation.BasicCodonFlipMutationOperator";
	public static final String FITNESS_CLASS = "fitness";
	public static final String DEFAULT_FITNESS_CLASS = "model.module.operator.fitness.ProfitFitnessOperator";
	public static final String JOIN_CLASS = "join";
	public static final String DEFAULT_JOIN_CLASS = "model.module.operator.join.SimpleJoinOperator";
	public static final String SELECTION_CLASS = "selection";
	public static final String DEFAULT_SELECTION_CLASS = "model.module.operator.selection.EliteSelectionOperator";
	public static final String COLLECTOR_CLASS = "collector";
	public static final String DEFAULT_COLLECTOR_CLASS = "model.module.operator.collector.FitnessCollectorOperator";

}
