
package lsmapp.instrPanels;

/**
 *
 * @author glos
 */
public class NewInstrInfo
{
    public enum InstrType {
        Bond, Vanilla, Basket, Asian, Lookback
    }

    public NewInstrInfo(InstrType type, String instrName)
    {
        this.type = type;
        this.instrName = instrName;
    }
    final InstrType type;
    final String instrName;
}
