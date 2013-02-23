
package lsmprototype;

import approx.NoSolutionException;
import approx.Polinomial;
import instruments.AmOption;
import instruments.Instr;
import instruments.JustPrice;
import instruments.PriceInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import models.LSModel;
import models.Model;
import models.WrongInstrException;
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
    
    private JPanel createControls()
    {
        cp = new ControlPanel();
        PropertyChangeListener pcl = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (evt.getPropertyName().equalsIgnoreCase("PriceClicked"))
                {
                    priceClicked();
                }
            }
        };
        cp.addPropertyChangeListener(pcl);
        return cp;
    }
    
    private void priceClicked()
    {
        cp.setPriceBttnEnabled(false);
        new SwingWorker<PriceInfo, Void>() {

            @Override
            protected PriceInfo doInBackground() throws Exception
            {
                m = createModel();
                instr = createInstr();
                try {
                    return m.price(instr);
                } catch (WrongInstrException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return new JustPrice(0.0);
                }
            }
            
            @Override
            protected void done()
            {
                try {
                    PriceInfo pi = get();
                    showResults(pi, m, instr);
                } catch (Exception ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                cp.setPriceBttnEnabled(true);
            }
            
            Model m;
            Instr instr;
        }.execute();
    }
    
    private Model createModel()
    {
        return cp.createModel();
    }
    
    private Instr createInstr()
    {
        return cp.createInstr();
    }
    
    private void showResults(PriceInfo pi, Model model, Instr instr)
    {
        if (model instanceof LSModel)
        {
            showResultsLSM(pi, (LSModel) model, instr);
            return ;
        }
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setText(
                model.desc() + "\n" + instr.desc() +
                "\n\nPrice: " + pi.getPrice() );
        tabs.addTab("" + instr, pane);
        tabs.setTabComponentAt(tabs.indexOfComponent(pane), new TabLabel(tabs));
    }
    
    private void showResultsLSM(PriceInfo pi, LSModel model, Instr instr)
    {
        JTabbedPane results = new JTabbedPane();
        
        JTextPane desc = new JTextPane();
        desc.setEditable(false);
        desc.setText(
                model.desc() + "\n" + instr.desc() +
                "\n\nPrice: " + pi.getPrice() );
        results.addTab("Description", desc);
        
        if (instr instanceof AmOption) {
            results.addTab("Stopping", stoppingPlot(model, (AmOption) instr));
        }
        results.addTab("Regression", regressionView(model, instr));
        
        
        tabs.addTab("" + instr, results);
        tabs.setTabComponentAt(tabs.indexOfComponent(results),
                new TabLabel(tabs));
    }
       
    private Component stoppingPlot(LSModel model, AmOption opt)
    {
        if (opt.getType() == AmOption.CALL)
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
    
    private PlotObject stoppingPlotPut(LSModel model, AmOption opt)
    {
        PlotObject po = new PlotObject("Stopping price", Color.RED,
                PlotObject.Type.Lines);
        
        Polinomial[] P = model.getEst();
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

    private Component regressionView(final LSModel model, final Instr instr)
    {
        final Polinomial[] P = model.getEst();
        
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
    
    private void plotPolinomial(PlotObject po, double beg, double end, Polinomial P)
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
    ControlPanel cp;
}
