
package trajectories;

/**
 * Class stores two anthitetic scenarios. 
 * @author Grzegorz Los
 */
public class Anthitetic
{
    public Anthitetic(Scenario pos, Scenario neg)
    {
        this.pos = pos;
        this.neg = neg;
    }
    public final Scenario pos;
    public final Scenario neg;
}
