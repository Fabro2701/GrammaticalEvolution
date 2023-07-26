package model.individual;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;



public class Chromosome {
	int codons[];
	int length;
	int usedCodons;

	
	public Chromosome(int l) {
		length = l;
		codons = new int[length];
		usedCodons = 0;
		
	}
	public Chromosome(Chromosome copy) {
		length = copy.length;
		codons = new int[length];
		for(int i=0; i<length; i++) {
			codons[i] = copy.codons[i];
		}
		usedCodons = copy.usedCodons;
	}
	public void init(Random rnd) {
		for(int i=0; i<length; i++) {
			codons[i] = rnd.nextInt(256);
		}
	}
	public void setIntToCodon(int i, int v) {
		codons[i] = v;
	}
	public void setArrayIntToCodon(int ...v) {
		for(int i=0;i<v.length;i++) {
			codons[i] = v[i];
		}
	}
	
	public int getUsedCodons() {
		return usedCodons;
	}
	public void setUsedCodons(int usedCodons) {
		this.usedCodons = usedCodons;
	}
	public int getLength() {return this.length;}
	public int getCodon(int i) {return this.codons[i];}

}
