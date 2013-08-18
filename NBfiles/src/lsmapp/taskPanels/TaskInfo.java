
package lsmapp.taskPanels;

/**
 *
 * @author Grzegorz Los
 */
public interface TaskInfo
{
    public void addTaskObserver(TaskObserver observer);
    public void removeTaskObserver(TaskObserver observer);
    public void fireNewTask(String methodName, String instrName, PricingTask task);
}
