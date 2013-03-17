
package lsmapp;

import instruments.Instr;
import instruments.PriceInfo;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JPanel;
import models.PricingTask;
import models.Progress;
import models.ProgressObservable;

/**
 *
 * @author Grzegorz Los
 */
abstract public class ModelPanel extends JPanel
{

    public ModelPanel()
    {
        pcs = new PropertyChangeSupport(this);
    }
    
    abstract public ProgressObservable getModel();
    abstract public Instr getInstr();
    abstract protected void prepareForTask();
    abstract protected PriceInfo calculate();
    abstract protected void progressUpdate(Progress pr);
    abstract protected void cleanAfterTask();
    

    public final void execute()
    {
        prepareForTask();
        new PricingTask(getModel())
        {
            @Override
            protected PriceInfo doInBackground() throws Exception
            {
                return calculate();
            }
            
            @Override
            protected void process(List<Progress> chunks)
            {
                progressUpdate(chunks.get(chunks.size()-1));
            }
            
            @Override
            protected void done_()
            {
                try {
                    pcs.firePropertyChange("new_results", null, get());
                    cleanAfterTask();
                } catch (InterruptedException | ExecutionException ex) {}
            }
            
        }.execute();
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        pcs.addPropertyChangeListener(listener);
    }
    
    private PropertyChangeSupport pcs;
}
