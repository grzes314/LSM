
package lsmapp.resultPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.parameters.ModelParams;

/**
 * Interface for classes elaborating pricing results. Note that in the only 
 * method  there is no information about method nor priced
 * instrument. Therefore every class implementing that interface shall have
 * another way to obtain that data.
 * @author Grzegorz Los
 */
public interface ResultHandler
{
    /**
     * Invoked when pricing was finished.
     * @param price price of the instrument.
     */
    void result(double price);
    
    /**
     * Informs result handler which model parameters were used for pricing.
     * @param mp model parameters used for pricing.
     */
    void setModelParams(ModelParams mp);
    
    /**
     * Informs result handler which method was used for pricing.
     * @param method method used for pricing.
     */
    void setMethod(Method method);
    
    /**
     * Informs result handler which instrument was used for pricing.
     * @param instr instrument used for pricing.
     */
    void setInstr(Instr instr);
    
    /**
     * Shortcut for setting all components of pricing task with one method.
     * @param method method used for pricing.
     * @param mp model parameters used for pricing.
     * @param instr instrument used for pricing.
     */
    void setAll(Method method, ModelParams mp, Instr instr);
}
