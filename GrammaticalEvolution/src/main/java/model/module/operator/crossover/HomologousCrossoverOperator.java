package model.module.operator.crossover;

import java.util.Properties;
import java.util.Random;

import model.Constants;
import model.Util.Pair;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;
import model.individual.Phenotype;
import model.individual.Population;
import model.module.operator.Operator;

public class HomologousCrossoverOperator extends CrossoverOperator{
	public HomologousCrossoverOperator(Properties properties, Random rnd) {
		super(properties, rnd);
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
	}

	

	@Override
	public void cross(Pair<Individual, Individual> parents) {
		if(this.probability > this.rnd.nextFloat()) {
			Pair<Chromosome, Chromosome> ncs = crossover(parents.first.getGenotype().getChromosome(0),parents.second.getGenotype().getChromosome(0));
			parents.first.setGenotype(new Genotype(ncs.first));
			parents.first.revaluate();
			parents.second.setGenotype(new Genotype(ncs.second));
			parents.second.revaluate();
		}
		
	}
	public Pair<Chromosome, Chromosome> crossover(Chromosome c1, Chromosome c2) {
		Chromosome child1 = new Chromosome(c1);
		Chromosome child2 = new Chromosome(c2);
		
		int homoPoint=0;
		while(c1.getCodon(homoPoint)==c2.getCodon(homoPoint)) {
			if(homoPoint==c1.getLength()-1)break;
			homoPoint++;
		}
		//System.out.println(homoPoint);
		int crossPoint = rnd.nextInt(c1.getLength()-homoPoint)+homoPoint;
		
		if(homoPoint!=crossPoint) {
			for(int i=homoPoint;i<=crossPoint;i++) {
				child1.setIntToCodon(i, c2.getModCodon(i));
				child2.setIntToCodon(i, c1.getModCodon(i));
			}
		}
		
		return new Pair<Chromosome, Chromosome>(child1,child2);
	}

}
