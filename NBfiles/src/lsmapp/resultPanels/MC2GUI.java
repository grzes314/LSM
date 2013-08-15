
package lsmapp.resultPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.montecarlo.Convergence;
import finance.methods.montecarlo.MonteCarlo;
import finance.parameters.ModelParams;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import plot.PlotObject;
import plot.PlotPanel;
import plot.PlotPoint;

/**
 *
 * @author Grzegorz Los
 */
public class MC2GUI implements ResultHandler
{
    public MC2GUI(ResultDisplay displayer)
    {
        this.displayer = displayer;
    }

    public Instr getInstr()
    {
        return instr;
    }

    @Override
    public void setInstr(Instr instr)
    {
        this.instr = instr;
    }

    public MonteCarlo getMethod()
    {
        return method;
    }

    @Override
    public void setMethod(Method method)
    {
        this.method = (MonteCarlo) method;
    }

    public ModelParams getModelParams()
    {
        return modelParams;
    }

    @Override
    public void setModelParams(ModelParams modelParams)
    {
        this.modelParams = modelParams;
    }

    @Override
    public void setAll(Method method, ModelParams mp, Instr instr)
    {
        this.instr = instr;
        this.modelParams = mp;
        this.method = (MonteCarlo) method;
    }
    
    @Override
    public void result(double price)
    {
        showResults(price);
        reset();
    }
    
    public void reset()
    {
        method = null;
        instr = null;
    }
    
    private void showResults(double price)
    {
        JTabbedPane results = new JTabbedPane();
        
        results.addTab("Description", Auxiliary.basicInfo(method, instr, price));
        results.addTab("Convergence plot", plotConvergence(method));
        
        displayer.addResults(instr.toString(), results);
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
    
    private final ResultDisplay displayer;
    private MonteCarlo method;
    private Instr instr;
    private ModelParams modelParams;

}
