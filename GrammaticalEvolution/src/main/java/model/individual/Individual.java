package model.individual;

import model.grammar.AbstractGrammar;
import model.grammar.derivations.DerivationTree;
import model.module.operator.crossover.CrossoverOperator;

public class Individual implements Comparable<Individual>, Cloneable{
	
	protected Genotype genotype;
	protected Phenotype phenotype;
	protected AbstractGrammar grammar;
	protected int age;
	protected boolean evaluated,valid;
	protected double fitness;
	
	protected CrossoverOperator crossMethod;
	
	protected DerivationTree tree;
	
	public Individual() {
		evaluated = valid = false;
		age = 0;
	}
	public Individual(Genotype geno, Phenotype pheno, AbstractGrammar grammar) {
		this.phenotype = pheno;
		this.genotype = geno;
		this.grammar = grammar;
		this.phenotype.init(grammar.parse(genotype.getChromosome(0)));
		
		
		this.age = 0;
		this.valid = this.phenotype.isValid();
		this.evaluated = false;
	}
	public Individual(Individual copy) {
		this.phenotype = new Phenotype(copy.phenotype);
		this.genotype = new Genotype(copy.genotype);
		this.grammar = copy.grammar;
		this.age = 0;
		this.valid = copy.valid;
		this.fitness = copy.fitness;
		this.evaluated = copy.evaluated;
		this.crossMethod = copy.crossMethod;
		this.tree = copy.tree;
	}
	public void revaluate() {
		this.evaluated = false;
		this.phenotype.init(grammar.parse(genotype.getChromosome(0)));
		this.valid = this.phenotype.isValid();
		this.tree = null;
	}
	public void clear() {
		this.genotype._clear();
		this.genotype = null;
		this.phenotype.clear();
		this.phenotype = null;
		this.tree = null;
	}
	public Genotype getGenotype() {
		return genotype;
	}
	public void setGenotype(Genotype genotype) {
		this.genotype = genotype;
	}
	public Phenotype getPhenotype() {
		return phenotype;
	}
	public void setPhenotype(Phenotype phenotype) {
		this.phenotype = phenotype;
	}
	public AbstractGrammar getGrammar() {
		return grammar;
	}
	public void setGrammar(AbstractGrammar grammar) {
		this.grammar = grammar;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public boolean isEvaluated() {
		return evaluated;
	}
	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	public CrossoverOperator getCrossMethod() {
		return crossMethod;
	}
	public void setCrossMethod(CrossoverOperator crossMethod) {
		this.crossMethod = crossMethod;
	}
	public DerivationTree getTree() {
		if(tree==null) {
			DerivationTree t = new DerivationTree(grammar);
			if(t.buildFromChromosome(this.genotype.getChromosome(0))) tree = t;
		}
		return tree;
	}
	public void setTree(DerivationTree tree) {
		this.tree = tree;
	}
	@Override
	public int compareTo(Individual o) {

        if(this.fitness < o.fitness) {
            return -1;
        }
        if(this.fitness > o.fitness) {
            return 1;
        } else {
            return 0;
        }
	}
	@Override
	public Object clone() {
		return new Individual(this);
	}
	@Override 
	public String toString() {
		if(this.phenotype.isValid())return this.phenotype.getVisualCode();
		else return "no valid individual";
	}
	
}
