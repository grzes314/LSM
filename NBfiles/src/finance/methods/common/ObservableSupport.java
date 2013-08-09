
package finance.methods.common;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Grzegorz Los
 */
public class ObservableSupport implements ProgressObservable
{    
    @Override
    public void addObserver(ProgressObserver ob) {
        observers.add(ob);
    }
    @Override
    public void removeObserver(ProgressObserver ob) {
        observers.remove(ob);
    }
    @Override
    public void notifyObservers(Progress pr) {
        for (ProgressObserver ob: observers)
            ob.update(pr);
    }
    private List<ProgressObserver> observers = new LinkedList<>();
}
