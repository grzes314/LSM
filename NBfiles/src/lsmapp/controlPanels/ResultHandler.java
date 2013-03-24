
package lsmapp.controlPanels;

/**
 * Interface for classes elaborating pricing results. Note that in the only 
 * method  there is no information about model nor priced
 * instrument. Therefore every class implementing that instrument shall be
 * connected with only one ModelPanel.
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
