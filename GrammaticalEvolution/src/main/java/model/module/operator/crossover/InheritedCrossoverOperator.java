package model.module.operator.crossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import model.Util.Pair;
import model.individual.Chromosome;
import model.individual.Genotype;
import model.individual.Individual;

public class InheritedCrossoverOperator extends CrossoverOperator{
	private List<CrossoverOperator>ops;
	public InheritedCrossoverOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		ops = new ArrayList<>();
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
	}
	public InheritedCrossoverOperator addOperator(CrossoverOperator op) {
		ops.add(op);
		return this;
	}

	

	@Override
	public void cross(Pair<Individual, Individual> parents) {
		if(this.probability > this.rnd.nextFloat()) {
			
			CrossoverOperator m1 = parents.first.getCrossMethod();
			CrossoverOperator m2 = parents.second.getCrossMethod();
			
			if(m1==null)m1 = ops.get(rnd.nextInt(ops.size()));
			if(m2==null)m2 = ops.get(rnd.nextInt(ops.size()));
			
			if(m1==m2) {
				Pair<Chromosome, Chromosome> ncs = m1.crossover(parents.first.getGenotype().getChromosome(0),parents.second.getGenotype().getChromosome(0));

				if(0.10f > this.rnd.nextFloat())m1 = ops.get(rnd.nextInt(ops.size()));
				if(0.10f > this.rnd.nextFloat())m2 = ops.get(rnd.nextInt(ops.size()));
				parents.first.setCrossMethod(m2);
				parents.first.setGenotype(new Genotype(ncs.first));
				parents.second.setCrossMethod(m1);
				parents.second.setGenotype(new Genotype(ncs.second));
			}
			else {
				CrossoverOperator inhm = ops.get(rnd.nextInt(ops.size()));
				Pair<Chromosome, Chromosome> ncs = inhm.crossover(parents.first.getGenotype().getChromosome(0),parents.second.getGenotype().getChromosome(0));

				parents.first.setCrossMethod(rnd.nextInt(2)==1?m1:m2);
				parents.first.setGenotype(new Genotype(ncs.first));
				parents.second.setCrossMethod(rnd.nextInt(2)==1?m1:m2);
				parents.second.setGenotype(new Genotype(ncs.second));
			}
			

			parents.first.revaluate();
			parents.second.revaluate();
		}
		
	}

	@Override
	public Pair<Chromosome, Chromosome> crossover(Chromosome c1, Chromosome c2) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
