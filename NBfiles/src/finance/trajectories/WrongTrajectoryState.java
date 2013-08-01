
package finance.trajectories;

/**
 *
 * @author Grzegorz Los
 */
public class WrongTrajectoryState extends RuntimeException
{
    public WrongTrajectoryState(String message)
    {
        super(message);
    }

    public WrongTrajectoryState()
    {
    }
}
