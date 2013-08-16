
package lsmapp.taskPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.parameters.ModelParams;
import javax.swing.JPanel;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author Grzegorz Los
 */
public abstract class MethodPanel extends JPanel
{
    abstract Method makeMethod();
    
    abstract ResultHandler makeResultHandler(Method method, ModelParams mp, Instr instr);
}
