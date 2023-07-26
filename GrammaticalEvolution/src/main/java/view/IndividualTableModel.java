/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import model.individual.Individual;
import model.individual.Population;

/**
 *
 * @author Fabrizio Ortega
 */
public class IndividualTableModel extends DefaultTableModel {

    public IndividualTableModel() {
        super(new Vector(),new Vector<String>(List.of("Age","Fitness","Code")));
     
    }

	public void update(Population population) {
		this.dataVector.clear();
		for(Individual ind:population) {
			if(!ind.isValid())continue;
			this.dataVector.add(new Vector(List.of(ind.getAge(),ind.getFitness(),ind.getPhenotype().getPlainSymbols())));
		}
		this.fireTableDataChanged();
	}
    
}
