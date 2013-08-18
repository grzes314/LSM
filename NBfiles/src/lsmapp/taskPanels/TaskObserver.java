
package lsmapp.taskPanels;

/**
 *
 * @author Grzegorz Los
 */
public interface TaskObserver
{
    public void newTask(String methodName, String instrName, PricingTask task);
}
