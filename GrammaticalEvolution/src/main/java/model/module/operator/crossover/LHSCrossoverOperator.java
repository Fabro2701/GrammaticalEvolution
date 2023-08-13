package model.module.operator.crossover;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import model.Util;
import model.Util.Pair;
import model.grammar.AbstractGrammar;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Symbol;
import model.grammar.AbstractGrammar.SymbolType;
import model.grammar.derivations.DerivationTree;
import model.grammar.StandardGrammar;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;

public class LHSCrossoverOperator extends CrossoverOperator{
	AbstractGrammar grammar;
	public LHSCrossoverOperator(Properties properties, Random rnd, AbstractGrammar grammar) {
		super(properties, rnd);
		this.grammar = grammar;
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
	}


	@Override
	public Pair<Chromosome, Chromosome> crossover(Chromosome c1, Chromosome c2) {
		Chromosome child1 = new Chromosome(c1);
		Chromosome child2 = new Chromosome(c2);
		
		if(c1.getUsedCodons()<0) {
			int a=0;
		}
		int crossPoint1 = rnd.nextInt(c1.getUsedCodons());
		Symbol crossSymbol1 = c1.getSymCodon(crossPoint1);
		
		int crossPoint2 = Util.searchSymbol(c2, crossSymbol1, rnd.nextInt(c2.getUsedCodons()));
		
		if(crossPoint2==-1) {//one-point
			for(int i=crossPoint1;i<child1.getLength();i++) {
				child1.setIntToCodon(i, c2.getCodon(i));
				child2.setIntToCodon(i, c1.getCodon(i));
			}
		}
		else {
			int nExp1 = Util.expansionsNeeded(c1, crossPoint1, grammar);
			int nExp2 = Util.expansionsNeeded(c2, crossPoint2, grammar);
			
			Util.replaceAndPush(child1.getRawCodons(),crossPoint1,nExp1,c2.getRawCodons(),crossPoint2,nExp2);
			Util.replaceAndPush(child2.getRawCodons(),crossPoint2,nExp2,c1.getRawCodons(),crossPoint1,nExp1);
		}
		

		return new Pair<Chromosome, Chromosome>(child1,child2);
	}

	

	

	
	public static void main(String args[]) {
		/*int arr1[]= {1,2,3,4,5,6,7,8,9};
		int arr2[]= {10,11,12,13,14,15,16,17,18,19};
		replaceAndPush(arr1,3,3, arr2,4,1);
		System.out.println(Arrays.toString(arr1));
		System.out.println(Arrays.toString(arr2));*/
		Properties p = new Properties();
		p.put("crossover_prob", "1.0");
		Random rnd = new Random(556);
		AbstractGrammar g = new StandardGrammar();
		g.parseBNF("resources/grammar/default2.bnf");
		
		LHSCrossoverOperator op = new LHSCrossoverOperator(p,rnd,g);
		
		Chromosome c1 = new Chromosome(100);
		c1.init(rnd);
		Chromosome c2 = new Chromosome(100);
		c2.init(rnd);
		Individual ind1 = new Individual(new Genotype(c1),new Phenotype(),g);
		Individual ind2 = new Individual(new Genotype(c2),new Phenotype(),g);
		DerivationTree tree1 = new DerivationTree(g);
		System.out.println(tree1.buildFromChromosome(c1));
		DerivationTree tree2 = new DerivationTree(g);
		System.out.println(tree2.buildFromChromosome(c2));
		
		System.out.println(tree1);
		System.out.println("-------------------\n\n");
		System.out.println(tree2);
		op.cross(new Pair<Individual,Individual>(ind1,ind2));
		//[92, 218, 67, 72, 164, 35, 203, 19, 149, 51, 211, 245, 146, 160, 226, 103, 88, 199, 111, 85, 21, 106, 28, 8, 158, 251, 193, 231, 26, 227, 175, 41, 152, 137, 182, 107, 0, 125, 84, 122, 244, 190, 95, 175, 227, 113, 12, 199, 87, 125, 208, 10, 138, 67, 220, 107, 175, 73, 164, 73, 40, 180, 249, 86, 4, 74, 213, 53, 230, 94, 242, 102, 237, 141, 126, 248, 81, 144, 176, 91, 160, 121, 168, 188, 169, 142, 50, 141, 182, 24, 127, 218, 132, 48, 59, 230, 25, 3, 184, 204]
	}
}
