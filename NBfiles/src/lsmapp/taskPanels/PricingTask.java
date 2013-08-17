
package lsmapp.taskPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.common.WrongInstrException;
import finance.methods.common.WrongModelException;
import finance.parameters.ModelParams;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
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
            " in a " + mp.toString();
    }

    @Override
    protected Double doInBackground() throws InterruptedException, WrongInstrException,
                                             WrongModelException
    {
            return doPricing();
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
            handleResult();
        } catch (ExecutionException ex) {
            handleOtherException(ex);
        } catch (InterruptedException | CancellationException ex) {
            handleTaskCancellation();
        } catch (Throwable ex) {
             handleOtherException(ex);
        }
    }

    private void handleOtherException(Throwable ex)
    {
        try {
            throw ex.getCause();
        } catch (WrongInstrException | WrongModelException ex2) {
            if (progressPanel != null)
            {
                progressPanel.showErrorDialog(ex2.getMessage());
                progressPanel.die();
            }
        } catch (Throwable ex2)
        {
            if (progressPanel != null)
                progressPanel.showError(ex.toString());
            showStatusMessage("Task failed: " + getDesc());
        }
    }

    private void handleTaskCancellation()
    {
        if (progressPanel != null)
            progressPanel.die();
        showStatusMessage("Task cancelled: " + getDesc());
    }

    private void handleResult() throws InterruptedException, ExecutionException
    {
        resultHandler.result(get());
        if (progressPanel != null)
            progressPanel.die();
        showStatusMessage("Task finished: " + getDesc());
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
