
package lsmapp.controlPanels;

import finance.instruments.Instr;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JPanel;
import finance.methods.common.Progress;
import finance.methods.common.ProgressObservable;

/**
 * ModelPanel constitutes common base for all panels whose purpose is getting
 * parameters of the model and the instrument.
 * @author Grzegorz Los
 */
abstract public class ModelPanel extends JPanel
{

    public ModelPanel()
    {
    }

    public ModelPanel(ResultHandler handler)
    {
        this.handler = handler;
    }
    
    /**
     * Returns model recently used for pricing. If so far no pricing task was
     * executed returns null. Overriding methods musn't instantiate new 
     * model object in that method!
     * @return model recently used for pricing.
     */
    abstract public ProgressObservable getModel();
    
    /**
     * Returns instrument which was recently priced. If so far no pricing task
     * was executed returns null. Overriding methods musn't instantiate new 
     * instrument object in that method!
     * @return instrument which was recently priced.
     */
    abstract public Instr getInstr();
    
    /**
     * Creates new thread calculating price of the instrument and immediately
     * returns.
     */
    public final void execute()
    {
        prepareForTask();
        createInstr();
        new PricingTask(createModel())
        {
            @Override
            protected Double doInBackground() throws Exception
            {
                if (handler == null) {
                    System.out.println("Handler not set");
                    return -1.;
                } else {
                    try {
                        return calculate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return -1.;
                    }
                }
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
                    handler.result(get());
                    cleanAfterTask();
                } catch (InterruptedException | ExecutionException ex) {}
            }
            
        }.execute();
    }

    public void setResultHandler(ResultHandler handler)
    {
        this.handler = handler;
    }
    
    /**
     * Instantiates new model from the parameters set on the panel.
     * @return new model.
     */
    abstract protected ProgressObservable createModel();
    
    /**
     * Instatiates new instrument from the parameters set on the panel.
     * @return new instrument.
     */
    abstract protected Instr createInstr();
    
    /**
     * Method prepareForTask should do all necessary actions preceding pricing
     * and connected to GUI, for example show labels/bars, etc.
     */
    abstract protected void prepareForTask();
    
    /**
     * Should invoke models pricing method and return its result.
     * @return pricing result.
     */
    abstract protected double calculate();
    
    /**
     * Should update labels/bars with current information on the state of 
     * pricing.
     * @param pr information on progress of pricing.
     */
    abstract protected void progressUpdate(Progress pr);
    
    /**
     * Should reverse effects of prepareForTask, for example hide some
     * labels/bars, etc.
     */
    abstract protected void cleanAfterTask();
    
    private ResultHandler handler;
}
