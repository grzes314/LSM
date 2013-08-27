
package lsmapp.taskPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import javax.swing.JPanel;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author Grzegorz Los
 */
public abstract class MethodPanel extends JPanel
{
    abstract ResultHandler makeResultHandler();

    abstract String getPriceableDesc();

    abstract Method makeMethod(Instr instr) throws MethodInstantiationException;
}
