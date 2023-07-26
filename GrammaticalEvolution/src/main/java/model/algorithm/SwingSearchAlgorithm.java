package model.algorithm;

import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.individual.Individual;
import model.individual.Population;
import view.GrammaticalEvolutionMainFrame;

public class SwingSearchAlgorithm extends BasicSearchAlgorithm{
	boolean ini=false;
	GrammaticalEvolutionMainFrame frame;
	public SwingSearchAlgorithm(GrammaticalEvolutionMainFrame frame) {
		this.frame = frame;
	}
	@Override
	public void run(int its) {
		if(its>0 && frame.isRun()) {
			if(!ini) {
    			init();
    			ini = true;
    		}
    		else {
    			System.out.println("Starting Generation "+(its));
    			long start2 = System.currentTimeMillis();
    			step();
    			System.out.println("-----------------------------------------Genration done in: "+(System.currentTimeMillis()-start2)+"-----------------------------------------");
    			    			
    			
    		}	
			//update table
			frame.getTableModel().update(this.initPipeline.get(0).getPopulation());
			frame.repaint();

			SwingUtilities.invokeLater(()->{
				this.run(its-1);
			});
		}
		 	
	}

	
}
