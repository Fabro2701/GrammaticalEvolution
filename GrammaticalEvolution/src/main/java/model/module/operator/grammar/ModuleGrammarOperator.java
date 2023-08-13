package model.module.operator.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import model.Util.Pair;
import model.grammar.AbstractGrammar;
import model.grammar.StandardGrammar;
import model.grammar.AbstractGrammar.Module;
import model.grammar.derivations.DerivationTree;
import model.grammar.derivations.TreeNode;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;
import model.individual.Population;
import model.module.operator.crossover.LHSCrossoverOperator;
import model.module.operator.fitness.FitnessEvaluationOperator;



public class ModuleGrammarOperator extends GrammarOperator{
	ID_TYPE identificationMethod;
	int maxModules;
	public ModuleGrammarOperator(Properties properties, Random rnd) {
		super(properties, rnd);
	}
	public enum ID_TYPE{MUTATION,INSERTION,FREQUENCY,RANDOM}
	
	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		identificationMethod = ID_TYPE.valueOf(properties.getProperty("moduleIdentification", "RANDOM"));
		maxModules = Integer.valueOf(properties.getProperty("maxModules", "20"));
		
	}
	
	@Override
	public void modify(Population population, AbstractGrammar grammar, FitnessEvaluationOperator fitnessOp) {
		List<Module> modules = null;
		
		List<Individual>validIndividuals = population.stream().filter(Individual::isValid).collect(Collectors.toList());
		
		switch(identificationMethod) {
		case FREQUENCY:
			break;
		case INSERTION:
			break;
		case MUTATION:
			break;
		case RANDOM:
			modules = this.randomModules(validIndividuals, grammar);
			break;
		default:
			System.err.println("Identification method not supported "+identificationMethod );
			break;
		}
		modules.sort((m1, m2)->-Double.compare(m1.getFitness(), m2.getFitness()));
		for(int i=0;i<this.maxModules&&i<modules.size();i++) {
			grammar.addModule(modules.get(i));
		}
		
		modules.clear();
		modules = null;
		validIndividuals = null;
	}

	private List<Module> randomModules(List<Individual> inds, AbstractGrammar grammar) {
		List<Module> modules = new ArrayList<>();
		for(int i=0;i<inds.size()/1;i++) {
			//Individual ind = randomChoice(inds);
			Individual ind = inds.get(i);
			DerivationTree tree = ind.getTree();
			int idx = rnd.nextInt(tree.getNTNodeCount());
			TreeNode node = tree.flat().get(idx);
			modules.add(grammar.new Module(node, ind.getFitness(), node.getFlatString()));
		}
		return modules;
	}

	private <T> T randomChoice(List<T>l) {
	    return l.get(rnd.nextInt(l.size()));
	}
	
	public static void main(String args[]) {

		Properties p = new Properties();
		Random rnd = new Random(556);
		AbstractGrammar g = new StandardGrammar();
		g.parseBNF("resources/grammar/default2.bnf");
		
		Chromosome c1 = new Chromosome(100);
		c1.init(rnd);
		Chromosome c2 = new Chromosome(100);
		c2.init(rnd);
		Individual ind1 = new Individual(new Genotype(c1),new Phenotype(),g);
		ind1.setFitness(1);
		Individual ind2 = new Individual(new Genotype(c2),new Phenotype(),g);
		/*DerivationTree tree1 = new DerivationTree(g);
		System.out.println(tree1.buildFromChromosome(c1));
		DerivationTree tree2 = new DerivationTree(g);
		System.out.println(tree2.buildFromChromosome(c2));*/
		
		Population pop = new Population();
		pop.add(ind1);
		pop.add(ind2);
		
		GrammarOperator moduleGrammarOp = new ModuleGrammarOperator(p, rnd);
		moduleGrammarOp.modify(pop, g, null);
		
		
		
	}
}
