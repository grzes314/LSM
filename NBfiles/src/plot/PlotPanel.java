/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author grzes
 */
public class PlotPanel extends JPanel
{
    public PlotPanel()
    {
        legend = new JPanel();
        legend.setLayout( new BoxLayout(legend, BoxLayout.Y_AXIS) );
        createControls();
        pobjects = new Vector<PlotObject>();
        minX = minY = -1;
        maxX = maxY = 1;
        showGrid = false;
        setBackground(Color.WHITE);
    }

    @Override
    public void paint(Graphics gr)
    {
        super.paint(gr);
        if (!pobjects.isEmpty())
            markAxes(gr);
        for (PlotObject po: pobjects)
        {
            gr.setColor(po.getColor());
            if (po.getSize() == 0)
                continue;
            else if (po.getSize() == 1)
            {
                for (PlotPoint plotPoint: po)
                {
                    PlotPoint pp = translate(plotPoint);
                    gr.fillOval((int)(pp.x-3), (int)(pp.y-3), 6, 6);
                }
            }
            else if ( po.getType() == PlotObject.Type.Lines )
            {
                PlotPoint prev = null;
                for (PlotPoint plotPoint: po)
                {
                    if (prev == null)  prev = translate(plotPoint);
                    else {
                        PlotPoint pp = translate(plotPoint);
                        gr.drawLine((int)(prev.x), (int)(prev.y),
                                (int)(pp.x), (int)(pp.y));
                        prev = pp;
                    }
                }
            }
            else if ( po.getType() == PlotObject.Type.Points )
            {
                for (PlotPoint plotPoint: po)
                {
                    PlotPoint pp = translate(plotPoint);
                    gr.fillOval((int)(pp.x)-3, (int)(pp.y)-3, 6, 6);
                }
            }
        }
    }

    public void addPlotObject(PlotObject po)
    {
        pobjects.add(po);
        addToLegend(po.getLabel(), po.getColor());
        if (pobjects.size() == 1)
        {
            resetLimits();
            resetSpinners();
        }
        repaint();
    }

    public void resetLimits()
    {
        minX = minX();
        minY = minY();
        maxX = maxX();
        maxY = maxY();
        
        if (minX == Double.POSITIVE_INFINITY || minX == Double.NaN) minX = -1;
        if (minX == Double.NEGATIVE_INFINITY) minX = -1000;
        if (minY == Double.POSITIVE_INFINITY || minY == Double.NaN) minY = -1;
        if (minY == Double.NEGATIVE_INFINITY) minY = -1000;
        if (maxX == Double.NEGATIVE_INFINITY || maxX == Double.NaN) maxX = 1;
        if (maxX == Double.POSITIVE_INFINITY) maxX = 1000;
        if (maxY == Double.NEGATIVE_INFINITY || maxY == Double.NaN) maxY = 1;
        if (maxY == Double.POSITIVE_INFINITY) maxY = 1000;
    }

    public JPanel getLegend()
    {
        return legend;
    }

    public void clear()
    {
        pobjects.clear();
        repaint();
    }
    
    public double minX()
    {
        if (pobjects.isEmpty()) return -1;
        double res = pobjects.get(0).minX();
        for(PlotObject po: pobjects)
        {
            double min = po.minX();
            if (res > min) res = min;
        }
        return res;
    }

    public double minY()
    {
        if (pobjects.isEmpty()) return -1;
        double res = pobjects.get(0).minY();
        for(PlotObject po: pobjects)
        {
            double min = po.minY();
            if (res > min) res = min;
        }
        return res;
    }

    public double maxX()
    {
        if (pobjects.isEmpty()) return -1;
        double res = pobjects.get(0).maxX();
        for(PlotObject po: pobjects)
        {
            double max = po.maxX();
            if (res < max) res = max;
        }
        return res;
    }
    
    public double maxY()
    {
        if (pobjects.isEmpty()) return -1;
        double res = pobjects.get(0).maxY();
        for(PlotObject po: pobjects)
        {
            double max = po.maxY();
            if (res < max) res = max;
        }
        return res;
    }

    public boolean setMinX(double d)
    {
        if (d <= maxX-1)
        {
            minX = d;
            return true;
        }
        else return false;
    }
    
    public boolean setMinY(double d)
    {
        if (d <= maxY-1)
        {
            minY = d;
            return true;
        }
        else return false;
    }
    
    public boolean setMaxX(double d)
    {
        if (d >= minX+1)
        {
            maxX = d;
            return true;
        }
        else return false;
    }
    
    public boolean setMaxY(double d)
    {
        if (d >= minY+1)
        {
            maxY = d;
            return true;
        }
        else return false;
    }

    public void showGrid(boolean b)
    {
        showGrid = b;
    }

    public JPanel getControls()
    {
        return controls;
    }
    
    public void resetSpinners()
    {
        jsMinX.setValue( minX );
        jsMaxX.setValue( maxX );
        jsMinY.setValue( minY );
        jsMaxY.setValue( maxY );
    }

    private void createControls()
    {
        JPanel jpMinX = new JPanel( new GridLayout(2,0) ),
               jpMaxX = new JPanel( new GridLayout(2,0) ),
               jpMinY = new JPanel( new GridLayout(2,0) ),
               jpMaxY = new JPanel( new GridLayout(2,0) );
        jsMinX = new JSpinner( new SpinnerNumberModel(-1.0, -1000000.0, 1000000.0, 1.0) );
        jsMaxX = new JSpinner( new SpinnerNumberModel(1.0, -1000000.0, 1000000.0, 1.0) );
        jsMinY = new JSpinner( new SpinnerNumberModel(-1.0, -1000000.0, 1000000.0, 1.0) );
        jsMaxY = new JSpinner( new SpinnerNumberModel(1.0, -1000000.0, 1000000.0, 1.0) );
        
        jpMinX.add( new JLabel("Min X") );
        jpMinX.add(jsMinX);
        jpMaxX.add( new JLabel("Max X") );
        jpMaxX.add(jsMaxX);
        jpMinY.add( new JLabel("Min Y") );
        jpMinY.add(jsMinY);
        jpMaxY.add( new JLabel("Max Y") );
        jpMaxY.add(jsMaxY);

        final JCheckBox check = new JCheckBox("show gird");
        check.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                showGrid( check.isSelected() );
            }
        });

        JPanel buttons = new JPanel();
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setMinX( (Double) jsMinX.getValue() );
                setMaxX( (Double) jsMaxX.getValue() );
                setMinY( (Double) jsMinY.getValue() );
                setMaxY( (Double) jsMaxY.getValue() );
                repaint();
            }
        } );
        buttons.add(refresh);
        
        JButton reset = new JButton("Reset");
        reset.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                resetLimits();
                resetSpinners();
                repaint();
            }
        } );
        buttons.add(reset);


        controls = new JPanel( new GridLayout(0,3,10,10) );
        controls.add(jpMinX);
        controls.add(jpMaxX);
        controls.add(check);
        controls.add(jpMinY);
        controls.add(jpMaxY);
        controls.add(buttons);
    }

    private void addToLegend(String label, Color col)
    {
        JLabel jlabel = new JLabel(label);
        jlabel.setForeground(col);
        legend.add( jlabel );
    }

    /**
     * 
     * @param pp coordinates of original point
     * @return coordinates on this particular panel
     */
    private PlotPoint translate(PlotPoint pp)
    {
        int h = getSize().height;
        int w = getSize().width;
        double W = (maxX - minX)*1.1;
        double H = (maxY - minY)*1.1;
        double minx = minX - (maxX - minX)*0.05;
        double miny = minY - (maxY - minY)*0.05;
        //minX ~ 0, maxX ~ w => pp.x ~ (pp.x-minX) * w / W
        double x = (pp.x - minx)*w/W;
        double y = (pp.y - miny)*h/H;
        //y needs to be reflected
        y = h - y;
        return new PlotPoint(x,y);
    }

    private void markAxes(Graphics gr)
    {
        gr.setColor(Color.LIGHT_GRAY);
        int h = getSize().height;
        int w = getSize().width;
        double W = (maxX - minX)*1.1;
        double H = (maxY - minY)*1.1;
        double  minx = minX - (maxX - minX)*0.05,
                maxx = maxX + (maxX - minX)*0.05,
                miny = minY - (maxY - minY)*0.05,
                maxy = maxY + (maxY - minY)*0.05;
        double interval = getInterval(W);
        Font font = new Font("Monospace", Font.PLAIN, (8 > h/50 ? 8 : h/50));
        gr.setFont(font);
        double x = 0;
        while (x < minx)
            x += interval;
        while (x-interval > minx)
            x -= interval;
        while (x < maxx)
        {
            PlotPoint pp = translate(new PlotPoint(x,miny));
            //System.out.println(pp.x + " " + pp.y);
            if (showGrid) gr.drawLine((int) (pp.x), (int) (pp.y), (int) (pp.x), 0);
            else gr.drawLine((int) (pp.x), (int) (pp.y), (int) (pp.x), (int) (pp.y - 5));
            String str = Double.toString( (double)(Math.round(100*x))/100 );
            gr.drawString( str, (int)(pp.x-5), (int)(pp.y-5) );
            x += interval;
        }
        interval = getInterval(H);
        double y = 0;
        while (y < miny)
            y += interval;
        while (y-interval > miny)
            y -= interval;
        while (y < maxy)
        {
            PlotPoint pp = translate(new PlotPoint(minx,y));
            if (showGrid) gr.drawLine(0, (int) (pp.y), w, (int) (pp.y));
            else gr.drawLine(0, (int) (pp.y), 5, (int) (pp.y));
            String str = Double.toString( (double)( Math.round(100*y))/100 );
            gr.drawString( str, 5, (int)(pp.y) );
            y += interval;
        }
    }

    private double getInterval(double l)
    {
        double interval = 0.1;
        if (l >= 1.0)
        {
            for (int i = 0; l / interval > 10; ++i)
            {
                if (i % 3 == 0) interval *= 2;
                else if (i % 3 == 1) interval *= 2.5;
                else interval *= 2;
            }
        }
        else if (l >= 10e-4)
        {
            interval = 10e-6;
            for (int i = 0; l / interval > 10; ++i)
            {
                if (i % 3 == 0) interval *= 2;
                else if (i % 3 == 1) interval *= 2.5;
                else interval *= 2;
            }
            
        }
        else interval = 10e-6;
        return interval;
    }


    private double minX, minY, maxX, maxY;
    JSpinner jsMinX, jsMinY, jsMaxX, jsMaxY;
    private Vector<PlotObject> pobjects;
    private JPanel legend, controls;
    private boolean showGrid;
}
