
package lsmapp.instrPanels;

/**
 *
 * @author Grzegorz Los
 */
public interface InstrCountInfo
{
    public void addInstrCountObserver(InstrCountObserver observer);
    public void removeInstrCountObserver(InstrCountObserver observer);
    public void informAboutDeletion(String instrNamez);
    public void informAboutAddition(String instrName);
}
