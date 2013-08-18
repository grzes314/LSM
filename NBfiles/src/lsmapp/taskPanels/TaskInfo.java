
package lsmapp.taskPanels;

/**
 *
 * @author Grzegorz Los
 */
public interface TaskInfo
{
    public void addTaskObserver(TaskObserver observer);
    public void removeTaskObserver(TaskObserver observer);
    public void fireNewTask(PricingTask task);
}
