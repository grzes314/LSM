
package lsmapp.methodPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.common.WrongInstrException;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author Grzegorz Los
 */
public class PricingTask extends SwingWorker<Double, Void>
{
    public PricingTask(Method method, ModelParams mp,
                       Instr instr, ResultHandler resultHandler)
    {
        this.method = method;
        this.resultHandler = resultHandler;
        this.mp = mp;
        this.instr = instr;
    }

    public void setProgressPanel(ProgressPanel progressPanel)
    {
        this.progressPanel = progressPanel;
    }

    @Override
    protected Double doInBackground()
    {
        try {
            return doPricing();
        } catch (WrongInstrException ex) {
            Logger.getLogger(PricingTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongModelException ex) {
            Logger.getLogger(PricingTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Nie wiem co zrobic z tym wyjatkiem");
        //TODO zastanowic sie jak handlowac te bledy
    }

    private Double doPricing() throws WrongInstrException, WrongModelException
    {
        method.setModelParams(mp);
        return method.price(instr);
    }
    
    @Override
    public void done()
    {
        try {
            resultHandler.result(get());
            if (progressPanel != null)
                progressPanel.die();
        } catch (InterruptedException | ExecutionException ex) {
            if (progressPanel != null)
                progressPanel.showError();
        }
    }
    
    Method method;
    ResultHandler resultHandler;
    ModelParams mp;
    Instr instr;
    ProgressPanel progressPanel;
}
