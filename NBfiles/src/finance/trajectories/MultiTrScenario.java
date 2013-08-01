
package finance.trajectories;

import finance.parameters.NoSuchAssetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing multi-asset scenrios. 
 * @author Grzegorz Los
 */
public class MultiTrScenario implements Scenario
{

    /**
     * Constructor of scenario without anthitetic paths. Numbering of assets is 1-based,
     * hence for convenience elements in index 0 are ignored.
     * @param names array of assets' names.
     * @param pos array of trajectories.
     */
    public MultiTrScenario(TimeSupport ts, String[] names, Trajectory[] tr)
    {
        this.ts = ts;
        this.names = names;
        this.pos = tr;
        this.neg = null;
        ensureConstructorArgsOK();
        makeNamesMap();
    }

    /**
     * Constructor of scenario with anthitetic paths. Numbering of assets is 1-based,
     * hence for convenience elements in index 0 are ignored.
     * @param names array of assets' names.
     * @param pos array of trajectories.
     * @param neg array of anthitetic trajectories.
     */
    public MultiTrScenario(TimeSupport ts, String[] names,
            Trajectory[] pos, Trajectory[] neg)
    {
        this.ts = ts;
        this.names = names;
        this.pos = pos;
        this.neg = neg;
        ensureConstructorArgsOK();
        makeNamesMap();
    }
    
    private void ensureConstructorArgsOK()
    {
        int l = names.length;
        if (l <= 1)
            throw new IllegalArgumentException("Names array is too short");
        if (pos.length != l)
            throw new IllegalArgumentException("Lengths of array names and pos differ");
        if (neg != null && neg.length != l)
            throw new IllegalArgumentException("Lengths of array names and neg differ");
    }
    
    private void makeNamesMap()
    {
        name2nr = new HashMap<>();
        for (int i = 1; i < names.length; ++i)
        {
            if (name2nr.containsKey(names[i]))
                throw new IllegalArgumentException("Two assets with the same name: \""
                        + names[i] + "\"");
            name2nr.put(names[i], i);
        }
    }

    @Override
    public int getNumberOfAssets()
    {
        return names.length-1;
    }

    @Override
    public Collection<String> getAssetsNames()
    {
        return name2nr.keySet(); // TODO pomyslec czy nei kopiowac
    }

    private void ensureAsserNrOK(int nr)
    {
        if (nr < 1 || nr > getNumberOfAssets())
            throw new NoSuchAssetException("Wrong asset number: " + nr);
    }
    
    private void ensureAsserNameOK(String name)
    {
        if (!name2nr.containsKey(name))
            throw new NoSuchAssetException("Wrong asset name: \"" +
                    name + "\"");            
    }
    
    @Override
    public Trajectory getTr(int nr)
    {
        ensureAsserNrOK(nr);
        return pos[nr];
    }

    @Override
    public Trajectory getTr(String name)
    {
        ensureAsserNameOK(name);
        return pos[ name2nr.get(name) ];
    }

    @Override
    public boolean hasAnthi()
    {
        return neg != null;
    }

    @Override
    public Trajectory getAnthi(int nr)
    {
        ensureAsserNrOK(nr);
        return neg[nr];
    }

    @Override
    public Trajectory getAnthi(String name)
    {
        ensureAsserNameOK(name);
        return neg[ name2nr.get(name) ];
    }

    @Override
    public TimeSupport getTS()
    {
        return ts;
    }

    private TimeSupport ts;
    private String[] names;
    private Trajectory[] pos;
    private Trajectory[] neg;
    private Map<String, Integer> name2nr;
}
