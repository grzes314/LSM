
package lsmapp.resultPanels;

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
import lsmapp.controlPanels.FDPanel;
import lsmapp.controlPanels.ResultHandler;
import finance.models.FDModel;
import plot.PlotObject;
import plot.PlotPanel;
import plot.PlotPoint;

/**
 *
 * @author Grzegorz Los
 */
public class FD2GUI implements ResultHandler
{

    public FD2GUI(MainFrame frame, FDPanel fdPanel)
    {
        this.frame = frame;
        this.fdPanel = fdPanel;
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
        showResults(fdPanel.getModel(), fdPanel.getInstr(), price);
    }
    
    private void showResults(FDModel model, Instr instr, double price)
    {
        JTabbedPane results = new JTabbedPane();
        
        results.addTab("Description", Auxiliary.basicInfo(model, instr, price));
        
        if (instr instanceof Option) {
            results.addTab("Stopping", stoppingPlot(model, (Option) instr));
        }
        results.addTab("Price plot", pricePlot(model, instr));
        
        frame.addResults(instr.toString(), results);
    }
    
    private Component stoppingPlot(FDModel model, Option opt)
    {
        if (opt.getType() == Option.CALL)
            return new JLabel("It is always worth to not"
                    + " exercise american call option.");
        
        JPanel panel = new JPanel(new BorderLayout());
        PlotPanel plot = new PlotPanel();
        panel.add(plot, BorderLayout.CENTER);
        panel.add(plot.getLegend(), BorderLayout.EAST);
        panel.add(plot.getControls(), BorderLayout.SOUTH);
        
        PlotObject po = stoppingPlotPut(model);
        plot.addPlotObject(po);
        
        plot.resetLimits();
        plot.resetSpinners();
        
        return panel;
    }
    
    private PlotObject stoppingPlotPut(FDModel model)
    {
        final FDModel.Grid grid = model.getLastGrid();
        PlotObject po = new PlotObject("Stopping price", Color.RED,
                PlotObject.Type.Lines);
        double[] stopPrice = grid.getStopping();
        int K = stopPrice.length-1;
        for (int k = 0; k <= K; ++k)
        {
            double x = grid.timeAt(k);
            po.addPoint(new PlotPoint(x, stopPrice[k]));
        }
        return po;
    }

    private Component pricePlot(final FDModel model, final Instr instr)
    {
        final FDModel.Grid grid = model.getLastGrid();
        double[][] V = grid.getOptionPrices();
                
        final JPanel panel = new JPanel(new BorderLayout());
        final PlotPanel plot = new PlotPanel();        
        panel.add(plot, BorderLayout.CENTER);
        panel.add(plot.getLegend(), BorderLayout.EAST);
        panel.add(plot.getControls(), BorderLayout.SOUTH);
        //plot.setSize(400, 300);

        final PlotObject poPrice = new PlotObject("Option price", Color.RED,
            PlotObject.Type.Lines);
        final PlotObject poIV = new PlotObject("Intrisnic value", Color.BLUE,
            PlotObject.Type.Lines);
        plotGridPrice(poPrice, grid, 0);
        plotIV(poIV, 0, model.getBorder(), instr);
        plot.addPlotObject(poPrice);
        plot.addPlotObject(poIV);
        plot.resetLimits();
        plot.resetSpinners();
        
        JPanel north = new JPanel();
        final JLabel time = new JLabel("t = 0.00");
        north.add(new JLabel("Select day"));
        final JSpinner spinner = new JSpinner();
        spinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, V.length-1, 1));
        spinner.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                int v = (Integer) spinner.getValue();
                int a = (int)(grid.timeAt(v));
                int b = (int)(100 * (grid.timeAt(v)-1));
                time.setText("t = " + String.format("%.2f", grid.timeAt(v)));
                plotGridPrice(poPrice, grid, v);
                plotIV(poIV, 0, model.getBorder(), instr);
                plot.repaint();
            }
        } );
        north.add(spinner);
        north.add(time);
        panel.add(north, BorderLayout.NORTH);

        return panel;
    }
    
    private void plotGridPrice(PlotObject po, FDModel.Grid grid, int k)
    {
        po.clear();
        double V[][] = grid.getOptionPrices();
        int steps = V[k].length;
        for (int i = 0; i < steps; ++i)
        {
            double x = grid.assetPriceAt(i);
            po.addPoint( new PlotPoint(x, V[k][i]) );
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
    private final FDPanel fdPanel;
}
