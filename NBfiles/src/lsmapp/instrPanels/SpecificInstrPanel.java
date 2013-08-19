
package lsmapp.instrPanels;

import finance.instruments.Instr;
import finance.instruments.InvalidInstrParametersException;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author Grzegorz Los
 */
public abstract class SpecificInstrPanel extends JPanel
{
    /**
     * From data gathered in the panel constructs a suitable instrument.
     * @return Instrument described by values set in the panel.
     */
    abstract Instr makeInstr() throws InvalidInstrParametersException;
    
    /**
     * Returns true if and only if instrument described by data in the panel
     * is dependent on the asset specified by given name.
     * @param asset asset's name.
     * @return true if instrument depends on the asset.
     */
    abstract boolean isUsing(String asset);

    /**
     * Returns names of all assets on which the instrument described by data in the panel
     * depend on.
     * @return all underlyings of the instrument.
     */
    abstract Set<String> getUnderlyings();

    /**
     * Calling this method means that new asset has been add
     * and {@code SpecificInstrPanel} should update its list.
     */
    abstract void assetAdded(String assetName);
    
    /**
     * Calling this method means that given asset has been deleted
     * and {@code SpecificInstrPanel} should update its list.
     */
    abstract void assetDeleted(String assetName);
}
