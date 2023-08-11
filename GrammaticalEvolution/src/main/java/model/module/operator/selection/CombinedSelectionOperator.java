package model.module.operator.selection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import model.individual.Chromosome;
import model.individual.Individual;
import model.individual.Population;


public class CombinedSelectionOperator extends SelectionOperator{
	private List<SelectionOperator>ops;
	double t;//generation
	public CombinedSelectionOperator(Properties properties, Random rnd) {
		super(properties, rnd);
		this.ops = new ArrayList<>();
		t=0;
	}
	public CombinedSelectionOperator addOperator(SelectionOperator op) {
		ops.add(op);
		return this;
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);

	}

	@Override
	public void selectPopulation(Population population) {
		t++;
		int k;
		double max = 0d;
		Population finalPop = null;
		SelectionOperator opSelected = null;
		for(SelectionOperator op:ops) {
			Population tmp = new Population();
			op.setSelectedPopulation(tmp);
			k=1;
			if(op instanceof Stochastic) {
				k=10;
			}
			double maxS = 0d;
			double s = 0d;
			Population finalPopS = null;
			for(int i=0;i<k;i++) {
				op.selectPopulation(population);
				double c = calculateCriterion(tmp);
				if(c>maxS) {
					maxS=c;
					finalPopS = tmp;
				}
				s += c;
				//System.out.printf("%f %s\n", c, op.getClass().getSimpleName());
			}
			s /= k;
			if(s>max) {
				max = s;
				finalPop = finalPopS;
				opSelected = op;
			}
			System.out.printf("%f %s\n", s, op.getClass().getSimpleName());
		}
		System.out.println(opSelected.getClass().getSimpleName()+" selected");
		this.selectedPopulation.addAll(finalPop);
	}
	private double calculateCriterion(Population p) {
		return (this.calculateDiversity(p)/t)+((t-1)*this.calculateQuality(p)/t);
	}
	private double calculateQuality(Population p) {
		double min = p.stream().min(Comparator.comparing(Individual::getFitness)).get().getFitness();
		double max = p.stream().max(Comparator.comparing(Individual::getFitness)).get().getFitness();
		return max/(Math.sqrt(max*max+min*min));
	}
	private double calculateDiversity(Population p) {
		double max=0d;
		double d=0d;
		double sum=0d;
		for(Individual ind:p) {
			for(Individual ind2:p) {
				if(ind!=ind2) {
					d = euclideanDistance(ind.getGenotype().getChromosome(0), 
										  ind2.getGenotype().getChromosome(0));
					if(d>max)max=d;
					sum +=d;
				}
			}
		}
		return sum/(max*Math.log(p.size()-1));
	}
	private double euclideanDistance(Chromosome c1, Chromosome c2) {
		double s=0d;
		for(int i=0;i<Math.min(c1.getLength(), c2.getLength());i++) {
			s += Math.pow(c1.getCodon(i)-c2.getCodon(i), 2);
		}
		return Math.sqrt(s/Math.min(c1.getLength(), c2.getLength()));
	}
}
