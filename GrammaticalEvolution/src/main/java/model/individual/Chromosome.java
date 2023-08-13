package model.individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import model.grammar.AbstractGrammar;
import model.grammar.AbstractGrammar.Symbol;



public class Chromosome {
	int codons[];
	int modcodons[];
	AbstractGrammar.Symbol[] symcodons;
	int length;
	int usedCodons;

	
	public Chromosome(int l) {
		length = l;
		codons = new int[length];
		modcodons = new int[length];
		symcodons = new AbstractGrammar.Symbol[length];
		usedCodons = 0;
		
	}
	public Chromosome(Chromosome copy) {
		length = copy.length;
		codons = new int[length];
		modcodons = new int[length];
		symcodons = new AbstractGrammar.Symbol[length];
		for(int i=0; i<length; i++) {
			codons[i] = copy.codons[i];
		}
		usedCodons = copy.usedCodons;
		for(int i=0; i<usedCodons; i++) {
			modcodons[i] = copy.modcodons[i];
		}
		for(int i=0; i<usedCodons; i++) {
			symcodons[i] = copy.symcodons[i];
		}
	}
	public void init(Random rnd) {
		for(int i=0; i<length; i++) {
			codons[i] = rnd.nextInt(256);
		}
	}
	public void clear() {
		this.codons = null;
		this.modcodons = null;
		this.symcodons = null;
	}
	public void shrink(int l) {
		codons = Arrays.copyOf(codons, l);
		this.length=l;
	}
	public void setIntToCodon(int i, int v) {
		codons[i] = v;
	}
	public void setIntToModCodon(int i, int v) {
		modcodons[i] = v;
	}
	public void setSymToCodon(int i, AbstractGrammar.Symbol v) {
		symcodons[i] = v;
	}
	public void setArrayIntToCodon(int ...v) {
		for(int i=0;i<v.length;i++) {
			codons[i] = v[i];
		}
	}
	public int[] getRawCodons() {
		return this.codons;
	}
	public int getUsedCodons() {
		return usedCodons;
	}
	public void setUsedCodons(int usedCodons) {
		this.usedCodons = usedCodons;
	}
	public int getLength() {return this.length;}
	public int getCodon(int i) {return this.codons[i];}
	public int getModCodon(int i) {return this.modcodons[i];}
	public Symbol getSymCodon(int i) {return this.symcodons[i];}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chromosome other = (Chromosome) obj;
		return Arrays.equals(codons, other.codons);
	}
	@Override
	public String toString() {
		return "Chromosome [codons=" + Arrays.toString(codons) + "]";
	}

}
