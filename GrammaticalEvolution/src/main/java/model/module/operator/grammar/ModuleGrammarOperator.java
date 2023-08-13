package model.module.operator.grammar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;


import model.grammar.AbstractGrammar;
import model.grammar.AbstractGrammar.Module;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Symbol;
import model.grammar.StandardGrammar;
import model.grammar.derivations.DerivationTree;
import model.grammar.derivations.TreeNode;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;
import model.individual.Population;
import model.module.operator.fitness.FitnessEvaluationOperator;



public class ModuleGrammarOperator extends GrammarOperator{
	ID_TYPE identificationMethod;
	int maxModules;
	int n;
	double p;
	FitnessEvaluationOperator fitnessOp;
	public ModuleGrammarOperator(Properties properties, Random rnd, FitnessEvaluationOperator fitnessOp) {
		super(properties, rnd);
		this.fitnessOp = fitnessOp;
	}
	public enum ID_TYPE{MUTATION,INSERTION,FREQUENCY,RANDOM}
	
	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		identificationMethod = ID_TYPE.valueOf(properties.getProperty("module_id_method", "RANDOM"));
		maxModules = Integer.valueOf(properties.getProperty("maxModules", "20"));
		n = Integer.valueOf(properties.getProperty("module_n", "10"));
		p = Double.valueOf(properties.getProperty("module_p", "0.5"));
		updateRate = Integer.valueOf(properties.getProperty("module_update_rate", "20"));
	}
	
	@Override
	public void modify(Population population, AbstractGrammar grammar) {

		System.out.println("Updating Grammar with new modules");
		
		List<Module> modules = null;
		
		List<Individual>validIndividuals = population.stream().filter(Individual::isValid).collect(Collectors.toList());
		
		switch(identificationMethod) {
		case FREQUENCY:
			break;
		case INSERTION:
			break;
		case MUTATION:
			modules = this.mutationModules(validIndividuals, grammar, fitnessOp);
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
			System.out.println("Introducing Module: "+modules.get(i).toString());
			grammar.addModule(modules.get(i));
		}

		System.out.println("Update finished ---------------");
		for(Individual ind:population)ind.revaluate();
		
		modules.clear();
		modules = null;
		validIndividuals = null;
	}

	private List<Module> randomModules(List<Individual> inds, AbstractGrammar grammar) {
		List<Module> modules = new ArrayList<>();
		for(int i=0;i<inds.size()/10;i++) {
			Individual ind = randomChoice(inds);
			//Individual ind = inds.get(i);
			DerivationTree tree = ind.getTree();
			TreeNode node = randomChoice(tree.flat());
			modules.add(grammar.new Module(node, ind.getFitness(), node.getFlatString()));
		}
		return modules;
	}
	private List<Module> mutationModules(List<Individual> inds, AbstractGrammar grammar, FitnessEvaluationOperator fitnessOp) {
		List<Module> modules = new ArrayList<>();

		Individual ind = randomChoice(inds);//or the best
		//System.out.println(ind);
		//System.out.println("-------");
		Chromosome originalCh = ind.getGenotype().getChromosome(0);
		double f0 = fitnessOp.evaluate(ind);
		DerivationTree tree = ind.getTree();
		int idx = rnd.nextInt(tree.getNTNodeCount());
		TreeNode node = tree.flat().get(idx);
		int originalExp = expansionsNeeded(originalCh, idx, grammar);

		double f[] = new double[n];
		for(int i=0;i<n;i++) {
			Individual indR = new Individual(ind);
			Chromosome c = indR.getGenotype().getChromosome(0);
			for(int j=idx; j<c.getLength();j++) {
				c.setIntToCodon(j, rnd.nextInt(256));
			}
			indR.revaluate();
			if(indR.isValid()) {
				int exp = expansionsNeeded(c, idx, grammar);
				f[i] = fitnessOp.evaluate(indR);
				int[]codons = c.getRawCodons();
				for(int j=0;j+idx+exp<codons.length&&j+idx+originalExp<originalCh.getLength();j++) {
					codons[j+idx+exp] = originalCh.getCodon(j+idx+originalExp);
				}
				indR.revaluate();
				if(indR.isValid()) {
					//System.out.println(indR);
				}
				else {
					i--;
				}
			}
			else {
				i--;
			}
		}
		
		//if its better than n*p passes
		int count = 0;
		double fm = 0d;
		for(int i=0;i<n;i++) {
			fm += f[i];
			if(f0>f[i])count++;
		}
		fm /= n;
		
		if(count >= n*p)modules.add(grammar.new Module(node, fm, node.getFlatString()));
		
		return modules;
	}

	private <T> T randomChoice(List<T>l) {
	    return l.get(rnd.nextInt(l.size()));
	}
	private int expansionsNeeded(Chromosome c, int crossPoint, AbstractGrammar grammar) {
		Symbol t = c.getSymCodon(crossPoint);
		List<Production> ps;
		LinkedList<Symbol> q = new LinkedList<Symbol>();
		
		int i=0;
		while(true) {
			ps = grammar.getRule(t);
			int m = ps.size();
			if(m==1) {
				q.addAll(0, ps.get(0).stream().filter(s->s.getType()==AbstractGrammar.SymbolType.NTerminal).collect(Collectors.toList()));
				i--;
			}
			else {
				int r = c.getModCodon(crossPoint+i);
				q.addAll(0, ps.get(r).stream().filter(s->s.getType()==AbstractGrammar.SymbolType.NTerminal).collect(Collectors.toList()));
			}

			i++;
			if(q.isEmpty())break;
			t = q.pop();
			
		}
		return i;
	}
	public static void main(String args[]) {

		Properties p = new Properties();
		p.put("module_id_method", "MUTATION");
		Random rnd = new Random(34);
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
		
		FitnessEvaluationOperator fitnessOp = new FitnessEvaluationOperator(p,rnd) {
			@Override
			public float evaluate(Individual ind) {
				return rnd.nextFloat();
			}
			
		};
		
		GrammarOperator moduleGrammarOp = new ModuleGrammarOperator(p, rnd, fitnessOp);
		moduleGrammarOp.modify(pop, g);
		
		
		
	}
}
