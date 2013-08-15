
package lsmapp.methodPanels;

import finance.methods.common.Method;
import javax.swing.JPanel;
import lsmapp.modelTab.Pair;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author Grzegorz Los
 */
public abstract class MethodPanel extends JPanel
{
    abstract Pair<Method, ResultHandler> makeMethod();
}
