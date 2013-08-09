
package finance.methods.common;

/**
 *
 * @author Grzegorz Los
 */
public interface ProgressObservable
{
    public void addObserver(ProgressObserver ob);
    public void removeObserver(ProgressObserver ob);
    public void notifyObservers(Progress pr);
}
