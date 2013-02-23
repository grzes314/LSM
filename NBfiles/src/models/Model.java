
package models;

import instruments.Instr;
import instruments.PriceInfo;

/**
 *
 * @author grzes
 */
public interface Model
{
    public PriceInfo price(Instr instr) throws WrongInstrException;
    abstract public String desc();
}
