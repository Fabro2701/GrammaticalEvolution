package model.module.operator.initialization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import model.Constants;
import model.grammar.AbstractGrammar;
import model.grammar.StandardGrammar;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Rule;
import model.grammar.AbstractGrammar.Symbol;
import model.grammar.AbstractGrammar.SymbolType;
import model.grammar.derivations.DerivationTree;
import model.grammar.derivations.TreeNode;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;

public class SensibleInitializerOperator extends InitializationOperator implements MultipleInitializer{
	int maxDepth;
	public SensibleInitializerOperator(Properties properties, Random rnd, AbstractGrammar grammar) {
		super(properties, rnd, grammar);
		grammar.calculateAttributes();
	}
	@Override
	public void setProperties(Properties properties) {
		//super.setProperties(properties);
		this.maxDepth = Integer.parseInt(properties.getProperty("maxDepth", "10"));
	}
	@Override
	public List<Individual> createIndividuals(int n) {
		List<Individual>inds = new ArrayList<>();
		for(int i=0;i<n;i++) {
			Chromosome chr = i%2==0?grow():full();
			Genotype geno = new Genotype(chr);
			Phenotype pheno = new Phenotype();
			Individual ind = new Individual(geno,pheno,this.grammar);
			
			inds.add(ind);
		}
		return inds;
	}
	private Chromosome grow() {
		Chromosome c = new Chromosome(Constants.CROMOSOME_LENGTH);
		List<Integer>choices = new ArrayList<Integer>();
		Production p=null;
		Rule rule = null;
		TreeNode current = null;
		Symbol currentS = null;
		int choice;
		
		int i=0;
		DerivationTree tree = new DerivationTree(grammar);
		tree.addNode(new TreeNode(grammar.getInitial()));
		LinkedList<TreeNode>pending = new LinkedList<TreeNode>();
		pending.add(tree.get_current());
		

		while(!pending.isEmpty()&&i<c.getLength()) {
			current = pending.pollFirst();
			tree.setCurrent(current);
			currentS = current.getData();
			choices.clear();
			if(currentS.getType()==AbstractGrammar.SymbolType.Terminal)continue;
			rule = grammar.getRule(currentS);
			
			if(rule.size()==1) {
				tree.expandAndPushNode(0,pending);
				continue;
			}
			
			for(int pi = 0; pi<rule.size(); pi++) {
				p = rule.get(pi);
				if(current.get_depth()+p.get_minimumDepth()<=maxDepth) {
					choices.add(pi);
				}
			}
			if(choices.size()==0) {
				System.err.println("errr no choice possible RHH");
				//impossible?
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					choices.add(pi);
				}
			}
			choice = this._randomChoice(choices);
			c.setIntToCodon(i, choice);
			tree.expandAndPushNode(choice,pending);
			i++;
		}
		//c.shrink(i);
		//System.out.println("created depth: "+tree.getDeepest().get_depth());
		//System.out.println(tree);
		return c;
	}
	private Chromosome full() {
		Chromosome c = new Chromosome(Constants.CROMOSOME_LENGTH);
		List<Integer>choices = new ArrayList<Integer>();
		Production p=null;
		Rule rule = null;
		TreeNode current = null;
		Symbol currentS = null;
		int choice;
		
		int i=0;
		DerivationTree tree = new DerivationTree(grammar);
		tree.addNode(new TreeNode(grammar.getInitial()));
		LinkedList<TreeNode>pending = new LinkedList<TreeNode>();
		pending.add(tree.get_current());
		
		while(!pending.isEmpty()&&i<c.getLength()) {
			current = pending.pollFirst();
			tree.setCurrent(current);
			currentS = current.getData();
			choices.clear();
			if(currentS.getType()==AbstractGrammar.SymbolType.Terminal)continue;
			rule = grammar.getRule(currentS);
			
			if(rule.size()==1) {
				tree.expandAndPushNode(0,pending);
				continue;
			}
			
			if(rule.is_recursive()) {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(p.is_recursive() && current.get_depth()+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
				}
			}
			else {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(current.get_depth()+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
				}
			}
			if(choices.size()==0) {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(!p.is_recursive() && current.get_depth()+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
				}
				if(choices.size()==0) {
					for(int pi = 0; pi<rule.size(); pi++) {
						p = rule.get(pi);
						choices.add(pi);
					}
				}
			}
			
			choice = this._randomChoice(choices);
			c.setIntToCodon(i, choice);
			tree.expandAndPushNode(choice,pending);
			i++;
		}
		//c.shrink(i);
		//System.out.println("created depth: "+tree.getDeepest().get_depth());
		return c;
	}
	private int _randomChoice(List<Integer> choices) {
		return choices.get(rnd.nextInt(choices.size()));
	}
	@Override
	public Individual createIndividual() {
		
		return null;
	}
	
	public static void main(String args[]) {
		StandardGrammar g = new StandardGrammar();
		g.parseBNF("resources/grammar/default.bnf");
		
		Properties p = new Properties();
		p.put("maxDepth", "7");
		Random rnd = new Random(100);
		SensibleInitializerOperator op = new SensibleInitializerOperator(p,rnd,g);
		
		List<Individual> is = op.createIndividuals(100);
		for(Individual ind:is) {
			System.out.println(ind);
			//DerivationTree t = new DerivationTree(g);
			//t.buildFromChromosome(ind.)
		}
		
	}
}
