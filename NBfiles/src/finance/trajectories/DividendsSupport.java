
package finance.trajectories;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author glos
 */
public class DividendsSupport
{
    public DividendsSupport(TimeSupport ts, Collection<Dividend> dividends)
    {
        this.ts = ts;
        divTable = new ArrayList<>();
        for (int k = 0; k <= ts.getK(); ++k)
            divTable.add(new ArrayList<Dividend>());
        if (dividends != null)
            for (Dividend d: dividends)
                divTable.get(d.getTimeStep(ts)).add(d);
    }
    
    ArrayList<Dividend> getDivindent(int timeStep)
    {
        if (timeStep < 0 || timeStep > ts.getK())
            return new ArrayList<>();
        return divTable.get(timeStep);
    }
    
    private TimeSupport ts;
    private ArrayList<ArrayList<Dividend>> divTable;
}
