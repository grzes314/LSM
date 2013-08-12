
package lsmapp.resultPanels;

import finance.instruments.Instr;
import finance.methods.montecarlo.Convergence;
import finance.methods.montecarlo.MonteCarlo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import lsmapp.oldFrame.MainFrame;
import lsmapp.controlPanels.MCPanel;
import lsmapp.controlPanels.ResultHandler;
import plot.PlotObject;
import plot.PlotPanel;
import plot.PlotPoint;

/**
 *
 * @author Grzegorz Los
 */
public class MC2GUI implements ResultHandler
{

    public MC2GUI(MainFrame frame, MCPanel mcPanel)
    {
        this.frame = frame;
        this.mcPanel = mcPanel;
    }
    
    /*@Override
    public void result(double price)
    {
        FDModel model = fdPanel.getModel();
        Instr instr = fdPanel.getInstr();
        frame.addResults(instr.toString(), Auxiliary.basicInfo(model, instr, price));
    }*/
    
    @Override
    public void result(double price)
    {
        showResults(mcPanel.getModel(), mcPanel.getInstr(), price);
    }
    
    private void showResults(MonteCarlo model, Instr instr, double price)
    {
        JTabbedPane results = new JTabbedPane();
        
        results.addTab("Description", Auxiliary.basicInfo(model, instr, price));
        results.addTab("Convergence plot", plotConvergence(model));
        
        frame.addResults(instr.toString(), results);
    }
    
    private Component plotConvergence(MonteCarlo model)
    {
        JPanel panel = new JPanel(new BorderLayout());
        PlotPanel plot = new PlotPanel();
        panel.add(plot, BorderLayout.CENTER);
        panel.add(plot.getLegend(), BorderLayout.EAST);
        panel.add(plot.getControls(), BorderLayout.SOUTH);
        
        PlotObject po = plotConvergence_(model);
        plot.addPlotObject(po);
        
        plot.resetLimits();
        plot.resetSpinners();
        
        return panel;
    }
    
    private PlotObject plotConvergence_(MonteCarlo model)
    {
        PlotObject po = new PlotObject("Pricie of the option", Color.RED,
                PlotObject.Type.Lines);
        Convergence con = model.getLastConvergence();
        for (int k = 0; k < con.getLength(); ++k)
        {
            po.addPoint(new PlotPoint(con.getRep(k), con.getVal(k)));
        }
        return po;
    }
    
    private final MainFrame frame;
    private final MCPanel mcPanel;
}
