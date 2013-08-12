
package lsmapp.instrPanels;

import lsmapp.instrPanels.InstrTab.InstrType;

/**
 *
 * @author glos
 */
public class NewInstrInfo
{
    public NewInstrInfo(InstrType type, String instrName)
    {
        this.type = type;
        this.instrName = instrName;
    }
    final InstrTab.InstrType type;
    final String instrName;
}
