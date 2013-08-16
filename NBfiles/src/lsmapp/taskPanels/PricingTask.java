
package lsmapp.taskPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.common.WrongInstrException;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import lsmapp.Pricer;
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
    
    public String getDesc()
    {
        return "Pricing " + instr.getName() + " using " + method.toString() +
            "in a " + mp.toString();
    }

    @Override
    protected Double doInBackground() throws InterruptedException
    {
        try {
            return doPricing();
        } catch (WrongInstrException | WrongModelException ex) {
            Logger.getLogger(PricingTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Nie wiem co zrobic z tym wyjatkiem");
        //TODO zastanowic sie jak handlowac te bledy
    }

    private Double doPricing() throws WrongInstrException, WrongModelException, InterruptedException
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
            showStatusMessage("Task finished: " + getDesc());
        } catch (ExecutionException ex) {
            if (progressPanel != null)
                progressPanel.showError(ex.getMessage());
            showStatusMessage("Task failed: " + getDesc());
        } catch (InterruptedException | CancellationException ex) {
            if (progressPanel != null)
                progressPanel.die();
            showStatusMessage("Task cancelled: " + getDesc());
        }
    }
        
    private void showStatusMessage(String mssg)
    {
        Pricer.getApp().setStatus(mssg);
    }
    
    Method method;
    ResultHandler resultHandler;
    ModelParams mp;
    Instr instr;
    ProgressPanel progressPanel;
}
