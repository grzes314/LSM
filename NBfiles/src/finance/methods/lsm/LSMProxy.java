
package finance.methods.lsm;

import finance.instruments.Instr;

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
            return new LSM();
        if (n == 1)
        {
            if (instr.areYou("european"))
                return new LSM();
            if (instr.areYou("asian"))
                return new LSM_Asian();
            if (instr.areYou("lookback"))
                return new LSM_Lookback();
            return new LSM();
        }
        if (n == 2)
            return new LSM2();
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
}
