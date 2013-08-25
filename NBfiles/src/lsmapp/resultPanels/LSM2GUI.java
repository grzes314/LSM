
package lsmapp.resultPanels;

import finance.instruments.Instr;
import finance.instruments.InstrTools;
import finance.instruments.Option;
import finance.methods.common.Method;
import finance.methods.lsm.LSM;
import finance.methods.lsm.LSMRoot;
import finance.parameters.ModelParams;
import finance.parameters.VanillaOptionParams;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import math.approx.Polynomial;
import plot.PlotObject;
import plot.PlotPanel;
import plot.PlotPoint;

/**
 *
 * @author Grzegorz Los
 */
public class LSM2GUI implements ResultHandler
{
    public LSM2GUI(ResultDisplay displayer)
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

    public LSMRoot getMethod()
    {
        return method;
    }

    @Override
    public void setMethod(Method method)
    {
        this.method = (LSM) method;
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
        this.method = (LSMRoot) method;
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
        
        InfoPanel ip = new InfoPanel(method.getDesc(), modelParams.getDesc(),
                                     instr.getDesc(), price);
        results.addTab("Description", ip);
        
        if (instr instanceof Option) {
            results.addTab("Stopping", stoppingPlot((LSM)method, (Option) instr));
            VanillaOptionParams vop = InstrTools.extractOptionParams(instr);
            if (vop != null)
                results.addTab("Regression", regressionView((LSM)method, vop));
        }
        
        displayer.addResults(instr.getName() + ": " + method.toString(), results);
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

    private Component regressionView(final LSM model, final VanillaOptionParams vop)
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
        plotIV(poIV, 0, 2*model.getS(), vop);
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
                plotIV(poIV, 0, 2*model.getS(), vop);
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
    
    private void plotIV(PlotObject po, double beg, double end, VanillaOptionParams vop)
    {
        po.clear();
        int steps = 1000;
        double step = (end - beg)/steps;
        for (int i = 0; i < steps; ++i)
        {
            double x = beg + i*step;
            po.addPoint( new PlotPoint(x, vop.intrisnicValue(x)) );
        }
    }
    
    private final ResultDisplay displayer;
    private LSMRoot method;
    private Instr instr;
    private ModelParams modelParams;
}
