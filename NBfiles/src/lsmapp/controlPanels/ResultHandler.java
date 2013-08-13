
package lsmapp.controlPanels;

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
}
