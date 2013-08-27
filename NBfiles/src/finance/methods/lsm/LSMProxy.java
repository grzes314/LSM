
package finance.methods.lsm;

import finance.instruments.Instr;
import finance.trajectories.Trajectory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Class which decides which LSM implementation should be used to price some option.
 * @author Grzegorz Los
 */
public class LSMProxy
{
    /**
     * Returns LSM implementation which should be used to price given {@code instr}.
     * @param instr {@code Instrument} which will be priced with returned method.
     * @return appropriate LSM implementation or null if this instrument cannot be priced.
     */
    public LSMRoot chooseMethod(Instr instr)
    {
        int n = instr.getUnderlyings().size();
        if (n == 0)
            return new LSM(chooseAuxiliaryStats(instr));
        if (n == 1)
        {
            if (instr.areYou("european"))
                return new LSM(chooseAuxiliaryStats(instr));
            if (instr.areYou("asian"))
                return new LSM_Asian();
            if (instr.areYou("lookback"))
                return new LSM_Lookback();
            return new LSM(chooseAuxiliaryStats(instr));
        }
        if (n == 2)
            return new LSM2(chooseAuxiliaryStats(instr));
        return null;
    }
    
    public boolean isPriceable(Instr instr)
    {
        int n = instr.getUnderlyings().size();
        if (n == 0)
            return false;
        if (n == 1 || n == 2)
            return true;
        return false;
    }
    
    private Collection<Trajectory.Auxiliary> chooseAuxiliaryStats(Instr instr)
    {
        Set<Trajectory.Auxiliary> stats = new HashSet<>();
        if (instr.areYou("asian"))
            stats.add(Trajectory.Auxiliary.AVERAGE);
        if (instr.areYou("lookback") && instr.areYou("call"))
            stats.add(Trajectory.Auxiliary.CUMMIN);
        if (instr.areYou("lookback") && instr.areYou("put"))
            stats.add(Trajectory.Auxiliary.CUMMAX);
        if (instr.areYou("barrier") || instr.areYou("partialbarrier"))
        {
            stats.add(Trajectory.Auxiliary.CUMMIN);
            stats.add(Trajectory.Auxiliary.CUMMAX);            
        }
        return stats;
    }
}
