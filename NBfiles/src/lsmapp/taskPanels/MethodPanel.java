
package lsmapp.taskPanels;

import finance.methods.common.Method;
import javax.swing.JPanel;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author Grzegorz Los
 */
public abstract class MethodPanel extends JPanel
{
    abstract Method makeMethod();
    
    abstract ResultHandler makeResultHandler();

    abstract String getPriceableDesc();
}
