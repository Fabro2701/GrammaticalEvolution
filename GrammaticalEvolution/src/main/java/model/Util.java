package model;

import java.util.ArrayList;

import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Population;


public class Util {
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
			similarity+=c1.getCodon(i).getIntValue()==c2.getCodon(i).getIntValue()?1.f:0.f;
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
