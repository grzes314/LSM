
package lsmapp;

import approx.Polynomial;
import instruments.Instr;
import instruments.Option;
import instruments.PriceInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import models.LSModel;
import plot.PlotObject;
import plot.PlotPanel;
import plot.PlotPoint;

/**
 *
 * @author grzes
 */
public class MainFrame extends JFrame
{
    public MainFrame()
    {
        setSize(800,600);
        tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.add(createControls(), "Controls");
        setContentPane(tabs);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
    private Component createControls()
    {
        JTabbedPane pane = new JTabbedPane();
        PropertyChangeListener pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equalsIgnoreCase("new_results"))
                {
                    PriceInfo pi;
                    try {
                        pi = (PriceInfo) evt.getNewValue();
                        showResults(pi);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Result of pricing is not PriceInfo object.",
                                "Internal error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        ModelPanel mp = new LSMPanel();
        mp.addPropertyChangeListener(pcl);
        pane.add(mp, "Longstaff-Schwartz");
        
        mp = new BSPanel();
        mp.addPropertyChangeListener(pcl);        
        pane.add(mp, "Black-Scholes");
        
        mp = new FDPanel();
        mp.addPropertyChangeListener(pcl);        
        pane.add(mp, "Finite Difference");
        
        return pane;
    }
    
    private void showResults(PriceInfo pi)
    {
        Object model = pi.getModel();
        Instr instr = pi.getInstr();
        if (model instanceof LSModel)
        {
            showResultsLSM(pi);
            return ;
        }
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setText(
                model + "\n" + instr.desc() +
                "\n\nPrice: " + pi.getPrice() );
        tabs.addTab("" + instr, pane);
        tabs.setTabComponentAt(tabs.indexOfComponent(pane), new TabLabel(tabs));
    }
    
    private void showResultsLSM(PriceInfo pi)
    {
        LSModel model = (LSModel) pi.getModel();
        Instr instr = pi.getInstr();
        JTabbedPane results = new JTabbedPane();
        
        JTextPane desc = new JTextPane();
        desc.setEditable(false);
        desc.setText(
                model + "\n" + instr.desc() +
                "\n\nPrice: " + pi.getPrice() );
        results.addTab("Description", desc);
        
        if (instr instanceof Option) {
            results.addTab("Stopping", stoppingPlot(model, (Option) instr));
        }
        results.addTab("Regression", regressionViewLSM(model, instr));
        
        
        tabs.addTab("" + instr, results);
        tabs.setTabComponentAt(tabs.indexOfComponent(results),
                new TabLabel(tabs));
    }
    
    private Component stoppingPlot(LSModel model, Option opt)
    {
        if (opt.getType() == Option.CALL)
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
    
    private PlotObject stoppingPlotPut(LSModel model, Option opt)
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

    private Component regressionViewLSM(final LSModel model, final Instr instr)
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
    
    JTabbedPane tabs;
}
