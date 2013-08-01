
package finance.methods.montecarlo;

import java.util.ArrayList;

/**
 * Class storing data presenting how the result converged as number of replications
 * increases.
 * @author Grzegorz Los
 */
public class Convergence
{
    public void add(int rep, double val)
    {
        replications.add(rep);
        value.add(val);
    }
    
    public int getLength()
    {
        return value.size();
    }
    
    public int getRep(int i)
    {
        return replications.get(i);
    }
    
    public double getVal(int i)
    {
        return value.get(i);
    }
    
    ArrayList<Integer> replications = new ArrayList<>();
    ArrayList<Double> value = new ArrayList<>();
}
