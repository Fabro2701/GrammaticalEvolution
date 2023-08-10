package model.module.operator.initialization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.grammar.AbstractGrammar;
import model.grammar.StandardGrammar;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Rule;
import model.grammar.AbstractGrammar.Symbol;
import model.grammar.derivations.DerivationTree;
import model.grammar.derivations.TreeNode;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;
import model.individual.Population;
import model.module.operator.Operator;

public class PositionIndependentInitializerOperator extends InitializationOperator{
	int maxDepth;
	public PositionIndependentInitializerOperator(Properties properties, Random rnd, AbstractGrammar grammar) {
		super(properties, rnd, grammar);
		grammar.calculateAttributes();
	}
	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
		this.maxDepth = Integer.parseInt(properties.getProperty("maxDepth", "10"));
	}
	@Override
	public Individual createIndividual() {
		Chromosome chr = grow();
		Genotype geno = new Genotype(chr);
		Phenotype pheno = new Phenotype();
		
		return new Individual(geno,pheno,this.grammar);
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
		int nRecursive = 1;
		boolean expand=false;
		int pos=0;
		
		while(!pending.isEmpty()&&i<c.getLength()) {
			choices.clear();

			for(int ri = 0; ri<pending.size(); ri++) choices.add(ri);
			choice = this._randomChoice(choices);
			pos = choice;
			current = pending.remove(choice);
			
			tree.setCurrent(current);
			currentS = current.getData();
			choices.clear();
			rule = grammar.getRule(currentS);
			expand=false;
			if(rule.is_recursive()) {
				nRecursive = this._updateRecursiveCount(pending,grammar);
				if(nRecursive==0)expand=true;
			}
			
			if(rule.size()==1) {
				tree.expandAndPushNode(0,pending,pos);
				continue;
			}
			
			if(expand) {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(p.is_recursive() && current.get_depth()+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
				}
			}
			if(choices.size()==0) {
				for(int pi = 0; pi<rule.size(); pi++) {
					p = rule.get(pi);
					if(current.get_depth()+p.get_minimumDepth()<=maxDepth) {
						choices.add(pi);
					}
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
			//c.setIntToCodon(i, choice);later
			current.setExpansion(choice);
			tree.expandAndPushNode(choice,pending,pos);
			i++;
		}
		//c.shrink(i);
		//System.out.println("created depth: "+tree.getDeepest().get_depth());
		if(i>=c.getLength())return c;
		
		tree.buildChromosome(c);
		//System.out.println(tree);
		return c;
	}
	private int _updateRecursiveCount(LinkedList<TreeNode> pending, AbstractGrammar grammar) {
		return (int) pending.stream().filter(t->grammar.getRule(t.getData()).is_recursive()).count();
	}
	@Override
	public List<Individual> createIndividuals(int n) {
		// TODO Auto-generated method stub
		return null;
	}
	private int _randomChoice(List<Integer> choices) {
		return choices.get(rnd.nextInt(choices.size()));
	}
	
	
	public static void main(String args[]) {
		StandardGrammar g = new StandardGrammar();
		g.parseBNF("resources/grammar/default.bnf");
		
		Properties p = new Properties();
		p.put("maxDepth", "10");
		Random rnd = new Random(2);
		PositionIndependentInitializerOperator op = new PositionIndependentInitializerOperator(p,rnd,g);
		
		for(int i=0;i<100;i++) {
			Individual is = op.createIndividual();
			System.out.println(is);
		}
		
	}
	

}
