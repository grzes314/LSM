
package lsmapp.methodPanels;

import javax.swing.JPanel;
import lsmapp.controlPanels.ResultHandler;
import lsmapp.modelTab.Pair;

/**
 *
 * @author Grzegorz Los
 */
public abstract class MethodPanel extends JPanel
{
    abstract Pair<Method, ResultHandler> getMethod();
}
