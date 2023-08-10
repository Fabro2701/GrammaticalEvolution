package model.module.operator.initialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.grammar.AbstractGrammar;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Rule;
import model.grammar.AbstractGrammar.Symbol;
import model.grammar.StandardGrammar;
import model.grammar.derivations.DerivationTree;
import model.grammar.derivations.TreeNode;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;

/**
 * Variation from the Luke's PTC2
 * @author Fabrizio Ortega
 *
 */
public class PTC2InitializerOperator extends InitializationOperator implements MultipleInitializer{
	int botSize,upSize;
	public PTC2InitializerOperator(Properties properties, Random rnd, AbstractGrammar grammar) {
		super(properties, rnd, grammar);
		grammar.calculateAttributes();
	}
	@Override
	public void setProperties(Properties properties) {
		//super.setProperties(properties);
		this.botSize = Integer.parseInt(properties.getProperty("botSize", "10"));
		this.upSize = Integer.parseInt(properties.getProperty("upSize", "50"));
	}
	@Override
	public List<Individual> createIndividuals(int n) {
		int amount = n/(upSize-botSize+1);
		if(amount<=0)System.err.println("Invalid range PTC2InitializerOperator");
		List<Individual>inds = new ArrayList<>();
		for(int i=botSize;i<=upSize;i++) {
			//System.out.println("-----"+i+"------");
			for(int j=0;j<amount;j++) {
				Chromosome chr = grow(i);
				Genotype geno = new Genotype(chr);
				Phenotype pheno = new Phenotype();
				Individual ind = new Individual(geno,pheno,this.grammar);
				
				inds.add(ind);
			}
		}
		return inds;
	}
	private Chromosome grow(int maxExp) {
		Chromosome c = new Chromosome(Constants.CROMOSOME_LENGTH);
		List<Integer>choices = new ArrayList<Integer>();
		List<Double>ws = new ArrayList<>();
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
		int pos=0;
		int expCount=0;
		
		while(!pending.isEmpty()&&i<c.getLength()) {
			choices.clear();

			for(int ri = 0; ri<pending.size(); ri++) choices.add(ri);
			choice = this._randomChoice(choices);
			pos = choice;
			current = pending.remove(choice);
			
			tree.setCurrent(current);
			currentS = current.getData();
			choices.clear();
			ws.clear();
			rule = grammar.getRule(currentS);

			
			if(rule.size()==1) {
				tree.expandAndPushNode(0,pending,pos);
				continue;
			}
			else {
				expCount++;
			}
			
			

			int pexp = getPendingExpantions(pending);
			for(int pi = 0; pi<rule.size(); pi++) {
				p = rule.get(pi);
				if(expCount+pexp+p.get_minimumExp()<=maxExp) {
					choices.add(pi);
					ws.add(p.get_minimumExp()-(1d-1.5*(double)expCount/maxExp));
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
			choice = this._randomWeightedChoice(choices,ws);
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
		//System.out.println(c.getUsedCodons());
		return c;
	}
	private int _randomChoice(List<Integer> choices) {
		return choices.get(rnd.nextInt(choices.size()));
	}
	private int _randomWeightedChoice(List<Integer> choices, List<Double> ws) {
		double totalWeight = 0;
        for (Double weight : ws) {
            totalWeight += weight;
        }

        // Generate a random value between 0 and totalWeight
        double randomValue = rnd.nextDouble() * totalWeight;

        // Find the index of the chosen element based on weighted probability
        double cumulativeWeight = 0;
        for (int i = 0; i < ws.size(); i++) {
            cumulativeWeight += ws.get(i);
            if (randomValue <= cumulativeWeight) {
                return choices.get(i);
            }
        }

        // Fallback to returning the last choice (shouldn't happen)
        return choices.get(choices.size() - 1);
	}
	private int getPendingExpantions(LinkedList<TreeNode>pending) {
		return pending.stream().mapToInt(p->grammar.getRule(p.getData()).get_minimumExp()+(grammar.getRule(p.getData()).size()==1?0:1)).sum();
		//return (int) pending.stream().filter(p->grammar.getRule(p.getData()).size()!=1).count();
	}
	@Override
	public Individual createIndividual() {
		
		return null;
	}
	
	public static void main(String args[]) {
		StandardGrammar g = new StandardGrammar();
		g.parseBNF("resources/grammar/default.bnf");
		
		Properties p = new Properties();
		p.put("botSize", "10");
		p.put("upSize", "50");
		Random rnd = new Random(10);
		PTC2InitializerOperator op = new PTC2InitializerOperator(p,rnd,g);
		
		int arr[] = new int[100];
		Arrays.fill(arr, 0);
		List<Individual> is = op.createIndividuals(500);
		for(Individual ind:is) {
			arr[ind.getGenotype().getChromosome(0).getUsedCodons()]++;
			//System.out.println(ind);
			//DerivationTree t = new DerivationTree(g);
			//t.buildFromChromosome(ind.)
		}
		for(int i=0;i<arr.length;i++) {
			System.out.printf("%d: %d\n",i,arr[i]);
		}
	}
}
