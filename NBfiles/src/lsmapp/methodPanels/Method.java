
package lsmapp.methodPanels;

import finance.instruments.Instr;
import finance.parameters.ModelParams;

/**
 *
 * @author Grzegorz Los
 */
public interface Method
{
    abstract double price(ModelParams mp, Instr instr);
    abstract boolean priceable(Instr instr);
}
