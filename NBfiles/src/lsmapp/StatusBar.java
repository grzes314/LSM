
package lsmapp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Grzegorz Los
 */
public class StatusBar extends JLabel
{
    public StatusBar()
    {
        super();
        timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clear();
            }
        });
    }
    
    @Override
    public void setText(String lbl)
    {
        if (timer == null) // this is necessary because JLabel calls setText in constructor
            return;        // before timer is initialized
        if (timer.isRunning())
            timer.stop();
        super.setText(lbl);
        timer.start();
    }
    
    private void clear()
    {
        super.setText("");
    }
    
    Timer timer;
}
