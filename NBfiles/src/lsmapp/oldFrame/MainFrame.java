
package lsmapp.oldFrame;

import lsmapp.resultPanels.ListChoicePanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import lsmapp.StatusBar;
import lsmapp.controlPanels.BSPanel;
import lsmapp.controlPanels.FDPanel;
import lsmapp.controlPanels.LSMPanel;
import lsmapp.controlPanels.MCPanel;
import lsmapp.resultPanels.*;

/**
 *
 * @author grzes
 */
public class MainFrame extends JFrame implements ResultDisplay
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
      
        LSM2GUI lsm2gui = new LSM2GUI(this);
        LSMPanel lsmp = new LSMPanel(lsm2gui);
        panel.addChoice(lsmp, "Longstaff-Schwartz");
        
        BS2GUI bs2gui = new BS2GUI(this);
        BSPanel bsp = new BSPanel(bs2gui);  
        panel.addChoice(bsp, "Black-Scholes");
        
        FD2GUI fd2gui = new FD2GUI(this);
        FDPanel fdp = new FDPanel(fd2gui);    
        panel.addChoice(fdp, "Finite Difference");
        
        MC2GUI mc2gui = new MC2GUI(this);
        MCPanel mcp = new  MCPanel(mc2gui);
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
        Object model = pi.getMethod();
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
