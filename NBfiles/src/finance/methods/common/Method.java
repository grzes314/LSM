
package finance.methods.common;

import finance.instruments.Instr;
import finance.parameters.ModelParams;

/**
 *
 * @author Grzegorz Los
 */
public interface Method extends ProgressObservable
{
    /**
     * Sets model parameters.
     * @param params model parameters.
     * @throws WrongModelException when given parameters are for some reason inappropriate
     * for concrete method.
     */
    abstract void setModelParams(ModelParams mp) throws WrongModelException;
    
    /**
     * Prices given instrument.
     * @param instr Instrument to price.
     * @return value of the instrument.
     * @throws WrongInstrException when implementarion cannot price this instrument.
     */
    abstract double price(Instr instr) throws WrongInstrException;
    
    /**
     * Answers if method can price given instrument.
     * @param instr
     * @return 
     */
    abstract boolean isPriceable(Instr instr);
    
    /**
     * Return description of the method. It may contain several lines and in general
     * be quite long. On the other hand {@code toString} called on method should return a 
     * very brief, one-line description of the method.
     * @return description of the method.
     */
    abstract String getDesc();
}
