
package lsmapp.controlPanels;

import javax.swing.SwingWorker;
import finance.methods.common.Progress;
import finance.methods.common.ProgressObservable;
import finance.methods.common.ProgressObserver;

/**
 *
 * @author Grzegorz Los
 */
abstract public class PricingTask extends SwingWorker<Double, Progress>
  implements ProgressObserver
{
    public PricingTask(ProgressObservable observable)
    {
        this.observable = observable;
        observable.addObserver(this);
    }
    
    abstract protected void done_();

    @Override
    public final void update(Progress pr)
    {
        publish(pr);
    }
    
    @Override
    protected final void done()
    {
        observable.removeObserver(this);
        done_();
    }
    
    
    private ProgressObservable observable;
}
