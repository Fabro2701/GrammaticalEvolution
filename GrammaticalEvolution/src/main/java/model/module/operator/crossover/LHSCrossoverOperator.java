package model.module.operator.crossover;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

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
		

		int crossPoint1 = rnd.nextInt(c1.getUsedCodons());
		Symbol crossSymbol1 = c1.getSymCodon(crossPoint1);
		
		int crossPoint2 = searchSymbol(c2, crossSymbol1, rnd.nextInt(c2.getUsedCodons()));
		
		if(crossPoint2==-1) {//one-point
			for(int i=crossPoint1;i<child1.getLength();i++) {
				child1.setIntToCodon(i, c2.getCodon(i));
				child2.setIntToCodon(i, c1.getCodon(i));
			}
		}
		else {
			int nExp1 = expansionsNeeded(c1, crossPoint1);
			int nExp2 = expansionsNeeded(c2, crossPoint2);
			
			replaceAndPush(child1.getRawCodons(),crossPoint1,nExp1,c2.getRawCodons(),crossPoint2,nExp2);
			replaceAndPush(child2.getRawCodons(),crossPoint2,nExp2,c1.getRawCodons(),crossPoint1,nExp1);
		}
		

		return new Pair<Chromosome, Chromosome>(child1,child2);
	}

	/**
	 * 
	 * @param arr1 destination
	 * @param ini1
	 * @param n1
	 * @param arr2 source
	 * @param ini2
	 * @param n2
	 */
	private static void replaceAndPush(int[] arr1, int ini1, int n1, int[] arr2, int ini2, int n2) {
		if(n1==n2) {
			for(int i=0;i<n2&&i+ini1<arr1.length&&i+ini2<arr2.length;i++)arr1[i+ini1]=arr2[i+ini2];
		}
		else if(n1>n2) {
			for(int i=0;i<n2&&i+ini1<arr1.length&&i+ini2<arr2.length;i++)arr1[i+ini1]=arr2[i+ini2];
			for(int i=ini1+n1;i<arr1.length;i++)arr1[i-(n1-n2)]=arr1[i];
		}
		else if(n1<n2) {
			for(int i=arr1.length-1-(n2-n1);i>=ini1+n1;i--)arr1[i+(n2-n1)]=arr1[i];
			for(int i=0;i<n2&&i+ini1<arr1.length&&i+ini2<arr2.length;i++)arr1[i+ini1]=arr2[i+ini2];
		}
	}

	private int searchSymbol(Chromosome c, Symbol symbol, int mid) {
		int i=0;
		
		while(mid+i<c.getLength()||mid-i>=0) {
			if(mid+i<c.getLength()&&symbol.equals(c.getSymCodon(mid+i)))return mid+i;
			if(mid-i>=0&&symbol.equals(c.getSymCodon(mid-i)))return mid-i;
			i++;
		}
		return -1;
	}

	private int expansionsNeeded(Chromosome c, int crossPoint) {
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
				if(crossPoint+i>=100) {
					int a=0;
				}
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
	}
}
