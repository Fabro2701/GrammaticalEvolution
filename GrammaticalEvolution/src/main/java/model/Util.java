package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import model.grammar.AbstractGrammar;
import model.grammar.AbstractGrammar.Production;
import model.grammar.AbstractGrammar.Symbol;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Population;


public class Util {
	public static int searchSymbol(Chromosome c, Symbol symbol, int mid) {
		int i=0;
		
		while(mid+i<c.getLength()||mid-i>=0) {
			if(mid+i<c.getLength()&&symbol.equals(c.getSymCodon(mid+i)))return mid+i;
			if(mid-i>=0&&symbol.equals(c.getSymCodon(mid-i)))return mid-i;
			i++;
		}
		return -1;
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
	public static void replaceAndPush(int[] arr1, int ini1, int n1, int[] arr2, int ini2, int n2) {
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
	public static int expansionsNeeded(Chromosome c, int crossPoint, AbstractGrammar grammar) {
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
	public static ArrayList<ArrayList<Float>> genotypeSimilarityMatrix(Population individuals) {
		ArrayList<ArrayList<Float>> matrix = new ArrayList<ArrayList<Float>>(individuals.size());
		for(int i=0;i<individuals.size();i++) {
			ArrayList<Float>aux = new ArrayList<Float>(individuals.size());
			for(int j=0;j<individuals.size();j++) {
				if(i==j)aux.add(j, 0.f);
				else aux.add(j, genotypeSimilarity(individuals.get(i).getGenotype(), individuals.get(j).getGenotype()));
			}
			matrix.add(i, aux);
		}
		return matrix;
	}
	public static float genotypeSimilarity(Genotype g1, Genotype g2) {
		float similarity=0.0f;
		Chromosome c1 = g1.getChromosome(0);
		Chromosome c2 = g2.getChromosome(0);
		
		for(int i=0;i<c1.getLength() && i<c1.getUsedCodons() && i<c2.getUsedCodons();i++) {
			similarity+=c1.getCodon(i)==c2.getCodon(i)?1.f:0.f;
		}
		similarity /= (float)Math.min(Math.min(c1.getUsedCodons(),c2.getUsedCodons()),c1.getLength());
		return similarity;
	}
	public static class Pair<K, V> {
		public K first;
		public V second;
		public Pair(K f, V s) {
			this.first=f;
			this.second=s;
		}
	}
}
