/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fabrizio Ortega
 */
public class IndividualTableModel extends DefaultTableModel {

    public IndividualTableModel() {
        super(new Vector(),new Vector<String>(List.of("Age","Fitness","Code")));
     
    }
    
}
