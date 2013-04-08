
package lsmapp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import lsmapp.controlPanels.BSPanel;
import lsmapp.controlPanels.FDPanel;
import lsmapp.controlPanels.LSMPanel;
import lsmapp.controlPanels.MCPanel;
import lsmapp.resultPanels.BS2GUI;
import lsmapp.resultPanels.FD2GUI;
import lsmapp.resultPanels.LSM2GUI;
import lsmapp.resultPanels.MC2GUI;

/**
 *
 * @author grzes
 */
public class MainFrame extends JFrame
{
    public MainFrame()
    {
        setSize(800,600);
        setTitle("The Pricer");
        setContentPane( createContent() );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        statusBar.setText("Hello!");
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
    
    public void addResults(String lbl, Component comp)
    {
        results.addChoice(comp, lbl);     
        
    }
    
    private Container createContent()
    {
        JPanel contents = new JPanel( new BorderLayout() );
        
        tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        controls = createControls();
        results = new ListChoicePanel();
        tabs.add(controls, "Controls");
        tabs.add(results, "Results");
        contents.add(tabs, BorderLayout.CENTER);
        
        statusBar = new StatusBar();
        contents.add(statusBar, BorderLayout.SOUTH);
        
        return contents;
    }
    
    private ListChoicePanel createControls()
    {
        ListChoicePanel panel = new ListChoicePanel();
      
        LSMPanel lsmp = new LSMPanel();
        lsmp.setResultHandler(new LSM2GUI(this, lsmp));
        panel.addChoice(lsmp, "Longstaff-Schwartz");
        
        BSPanel bsp = new BSPanel();
        bsp.setResultHandler(new BS2GUI(this, bsp));    
        panel.addChoice(bsp, "Black-Scholes");
        
        FDPanel fdp = new FDPanel();
        fdp.setResultHandler(new FD2GUI(this, fdp));     
        panel.addChoice(fdp, "Finite Difference");
        
        MCPanel mcp = new  MCPanel();
        mcp.setResultHandler(new MC2GUI(this, mcp));     
        panel.addChoice(mcp, "Monte Carlo");
        
        return panel;
    }
    
    /*private Component createControls()
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
    }*/
    
    /*private void showResults(PriceInfo pi)
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
    }*/
    
    
    JTabbedPane tabs;
    ListChoicePanel controls;
    ListChoicePanel results;
    JLabel statusBar;
}
