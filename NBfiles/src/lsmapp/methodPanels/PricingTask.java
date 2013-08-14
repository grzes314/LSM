
package lsmapp.methodPanels;

import finance.instruments.Instr;
import finance.parameters.ModelParams;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import lsmapp.controlPanels.ResultHandler;

/**
 *
 * @author Grzegorz Los
 */
public class PricingTask extends SwingWorker<Double, Void>
{

    public PricingTask(Method method, ModelParams mp, Instr instr,
            ProgressPanel progressPanel, ResultHandler resultHandler)
    {
        this.method = method;
        this.resultHandler = resultHandler;
        this.mp = mp;
        this.instr = instr;
        this.progressPanel = progressPanel;
    }

    @Override
    protected Double doInBackground() throws Exception
    {
        return method.price(mp, instr);
    }
    
    public void done()
    {
        try {
            resultHandler.result(get());
            progressPanel.die();
        } catch (InterruptedException | ExecutionException ex) {
            progressPanel.showError();
        }
    }
    
    Method method;
    ResultHandler resultHandler;
    ModelParams mp;
    Instr instr;
    ProgressPanel progressPanel;
}
