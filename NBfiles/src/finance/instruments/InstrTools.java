
package finance.instruments;

import finance.parameters.VanillaOptionParams;
import java.util.ArrayList;

/**
 * Class used for extracting typical instruments from abstract {@code Instr} hierarchy.
 * @author Grzegorz Los
 */
public class InstrTools
{
    /**
     * Returns a list of all modifications applied to given instrument and core of that instrument.
     * @param instr instrument for analysis.
     * @return a list of all modifications applied to given instrument and core of that instrument.
     */
    public static ArrayList<Instr> getAllWrapped(Instr instr)
    {
        ArrayList<Instr> l = new ArrayList<>();
        while (instr != null) {
            l.add(instr);
            instr = getWrapped(instr);
        }
        return l;
    }
    
    /**
     * Returns interior or modification if (@code instr} is a {@code Modification} or null
     * if (@code instr} is a core.
     * @param instr an instrument to decompose.
     * @return interior of instrument.
     */
    public static Instr getWrapped(Instr instr)
    {
        Modificator mod;
        try {
            mod = (Modificator) instr;
        } catch (ClassCastException ex) {
            return null;
        }
        return mod.getWrapped();
    }
    
    /**
     * Returns core of the instrument whose modifications are written in wrapped array. 
     * @param wrapped array of modification obtained by {@code getAllWrapped}.
     * @return core of the instrument.
     */
    public static Instr getCore(ArrayList<Instr> wrapped)
    {
        return wrapped.get(wrapped.size()-1);
    }    
    
    /**
     * Returns core of given instrument.
     * @param instr instrument for decomposition.
     * @return  core of given instrument.
     */
    public static Instr getCore(Instr instr)
    {
        return getCore(getAllWrapped(instr));
    }
    
    /**
     * Checks if the most outer layer of the {@code instr} is an option.
     * @param instr instrument for analysis.
     * @return true if it is an option.
     */
    public static boolean isOption(Instr instr)
    {
        return instr instanceof Option;
    }
    
    /**
     * Checks if the most outer layer of the {@code instr} is a bond.
     * @param instr instrument for analysis.
     * @return true if it is a bond.
     */
    public static boolean isBond(Instr instr)
    {
        return instr instanceof Bond;
    }
    
    /**
     * Return VanillaOptionParams of given instrument if it is a vanilla option (European or
     * American). 
     * @param instr instrument to analyze.
     * @return options parameters.
     */
    public static VanillaOptionParams extractOptionParams(Instr instr)
    {
        try {
            return tryExtractAmOptionParams(instr);
        } catch (ClassCastException ex) {
            try {
                return tryExtractEuOptionParams(instr);
            } catch (ClassCastException ex2) {
                return null;
            }
        }
    }
    
    private static VanillaOptionParams tryExtractAmOptionParams(Instr instr)
    {
        Option o = (Option) instr;
        return extractOptionParams(o, VanillaOptionParams.AmOrEu.AM);
    }
    
    private static VanillaOptionParams tryExtractEuOptionParams(Instr instr)
    {
        EuExercise e = (EuExercise) instr;
        Option o = (Option) e.getWrapped();
        return extractOptionParams(o, VanillaOptionParams.AmOrEu.EU);
    }

    private static VanillaOptionParams extractOptionParams(Option option, VanillaOptionParams.AmOrEu amOrEu)
    {
        if (amOrEu == VanillaOptionParams.AmOrEu.AM)
            return option.vop.asAmerican();
        else
            return option.vop.asEuropean();
    }
    
}
