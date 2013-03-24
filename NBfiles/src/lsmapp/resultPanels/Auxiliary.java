
package lsmapp.resultPanels;

import instruments.Instr;

/**
 *
 * @author Grzegorz Los
 */
public class Auxiliary
{
    public static InfoPanel basicInfo(Object model, Instr instr, double price)
    {
        return new InfoPanel("" + model, instr.desc(), price);
    }
}
