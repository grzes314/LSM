
package lsmapp.instrPanels;

/**
 *
 * @author Grzegorz Los
 */
public interface InstrNumberChangeInfo
{
    public void addInstrNumberChangeObserver(InstrNumberChangeObserver observer);
    public void removeInstrNumberChangeObserver(InstrNumberChangeObserver observer);
    public void informAboutDeletion(String instrName);
    public void informAboutAddition(String instrName);
}
