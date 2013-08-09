
package lsmapp.resultPanels;

import math.approx.Polynomial;
import finance.instruments.Instr;
import finance.instruments.Option;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import lsmapp.MainFrame;
import lsmapp.controlPanels.LSMPanel;
import lsmapp.controlPanels.ResultHandler;
import finance.methods.lsm.LSM;
import finance.parameters.VanillaOptionParams;
import plot.PlotObject;
import plot.PlotPanel;
import plot.PlotPoint;

/**
 *
 * @author Grzegorz Los
 */
public class LSM2GUI implements ResultHandler
{
    public LSM2GUI(MainFrame frame, LSMPanel lsmPanel)
    {
        this.frame = frame;
        this.lsmPanel = lsmPanel;
    }
    
    @Override
    public void result(double price)
    {
        showResults(lsmPanel.getModel(), lsmPanel.getInstr(), price);
    }
    
    private void showResults(LSM model, Instr instr, double price)
    {
        JTabbedPane results = new JTabbedPane();
        
        results.addTab("Description", Auxiliary.basicInfo(model, instr, price));
        
        if (instr instanceof Option) {
            results.addTab("Stopping", stoppingPlot(model, (Option) instr));
        }
        results.addTab("Regression", regressionView(model, instr));
        
        frame.addResults(instr.toString(), results);
    }
    
    private Component stoppingPlot(LSM model, Option opt)
    {
        if (opt.getType() == VanillaOptionParams.CallOrPut.CALL)
            return new JLabel("It is always worth to not"
                    + " exercise american call option.");
        
        JPanel panel = new JPanel(new BorderLayout());
        PlotPanel plot = new PlotPanel();
        panel.add(plot, BorderLayout.CENTER);
        panel.add(plot.getLegend(), BorderLayout.EAST);
        panel.add(plot.getControls(), BorderLayout.SOUTH);
        
        PlotObject po = stoppingPlotPut(model, opt);
        plot.addPlotObject(po);
        
        plot.resetLimits();
        plot.resetSpinners();
        
        return panel;
    }
    
    private PlotObject stoppingPlotPut(LSM model, Option opt)
    {
        PlotObject po = new PlotObject("Stopping price", Color.RED,
                PlotObject.Type.Lines);
        
        Polynomial[] P = model.getEst();
        for (int i = 1; i < P.length; ++i)
        {
            double x0 = 0d;
            for (double x = opt.getStrike(); x > 0; x -= 0.005d)
            {
                double val = P[i].value(x);
                if (val < opt.intrisnicValue(x))
                {
                    x0 = x;
                    break;
                }
            }
            po.addPoint(new PlotPoint(i,x0));
        }
        return po;
    }

    private Component regressionView(final LSM model, final Instr instr)
    {
        final Polynomial[] P = model.getEst();
        
        final JPanel panel = new JPanel(new BorderLayout());
        final PlotPanel plot = new PlotPanel();        
        panel.add(plot, BorderLayout.CENTER);
        panel.add(plot.getLegend(), BorderLayout.EAST);
        panel.add(plot.getControls(), BorderLayout.SOUTH);
        //plot.setSize(400, 300);

        final PlotObject poEF = new PlotObject("Future estimated flow", Color.RED,
            PlotObject.Type.Lines);
        final PlotObject poIV = new PlotObject("Intrisnic value", Color.BLUE,
            PlotObject.Type.Lines);
        plotPolinomial(poEF, 0, 2*model.getS(), P[1]);
        plotIV(poIV, 0, 2*model.getS(), instr);
        plot.addPlotObject(poEF);
        plot.addPlotObject(poIV);
        plot.resetLimits();
        plot.resetSpinners();
        
        JPanel north = new JPanel();
        north.add(new JLabel("Select day"));
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new javax.swing.SpinnerNumberModel(
                Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(P.length-1), Integer.valueOf(1)));
        spinner.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int v = (Integer) spinner.getValue();
                plotPolinomial(poEF, 0, 2*model.getS(), P[v]);
                plotIV(poIV, 0, 2*model.getS(), instr);
                plot.repaint();
            }
        } );
        north.add(spinner);
        panel.add(north, BorderLayout.NORTH);

        return panel;
    }
    
    private void plotPolinomial(PlotObject po, double beg, double end, Polynomial P)
    {
        po.clear();
        int steps = 1000;
        double step = (end - beg)/steps;
        for (int i = 0; i < steps; ++i)
        {
            double x = beg + i*step;
            po.addPoint( new PlotPoint(x, P.value(x)));
        }
    }
    
    private void plotIV(PlotObject po, double beg, double end, Instr instr)
    {
        po.clear();
        int steps = 1000;
        double step = (end - beg)/steps;
        for (int i = 0; i < steps; ++i)
        {
            double x = beg + i*step;
            po.addPoint( new PlotPoint(x, instr.intrisnicValue(x)) );
        }
    }
    
    private final MainFrame frame;
    private final LSMPanel lsmPanel;
}
