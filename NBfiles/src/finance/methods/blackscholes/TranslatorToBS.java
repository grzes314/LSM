
package finance.methods.blackscholes;

import finance.instruments.*;
import finance.parameters.BarrierParams;
import finance.parameters.VanillaOptionParams;
import java.util.ArrayList;

/**
 * Allows the user to price {@code Instr} using Black-Scholes method if it is priceable.
 * @author Grzegorz Los
 */
public class TranslatorToBS
{
    public boolean mayBePriced(Instr instr)
    {
        Object[] wrapped = getAllWrapped(instr);
        return mayBePriced(wrapped);
    }
    
    private Object[] getAllWrapped(Instr instr)
    {
        ArrayList<Instr> l = new ArrayList<>();
        while (instr != null) {
            l.add(instr);
            instr = getWrapped(instr);
        }
        return l.toArray();
    }
    
    private Instr getWrapped(Instr instr)
    {
        Modificator mod;
        try {
            mod = (Modificator) instr;
        } catch (ClassCastException ex) {
            return null;
        }
        return mod.getWrapped();
    }
    
    private boolean mayBePriced(Object[] wrapped)
    {
        Object core = getCore(wrapped);
        if (isOption(core))
        {
            if (wrapped.length == 3)
            return hasEuExercise(wrapped) && hasBarrier(wrapped);
            if (wrapped.length == 2)
                return hasEuExercise(wrapped);
        }
        if (isBond(core))
            return wrapped.length == 1;
        return false;   
    }

    private Object getCore(Object[] wrapped)
    {
        return wrapped[wrapped.length-1];
    }    
    
    private boolean isOption(Object instr)
    {
        return instr instanceof Option;
    }
    
    private boolean isBond(Object instr)
    {
        return instr instanceof Bond;
    }
    
    private boolean hasBarrier(Object[] wrapped)
    {
        for (Object ob: wrapped) {
            try {
                Barrier b = (Barrier) ob;
                return true;
            } catch (ClassCastException ex) {}
        }
        return false;
    }
    
    private boolean hasEuExercise(Object[] wrapped)
    {
        for (Object ob: wrapped) {
            try {
                EuExercise b = (EuExercise) ob;
                return true;
            } catch (ClassCastException ex) {}
        }
        return false;
    }

    public double price(BlackScholes bs, Instr instr)
    {
        Object[] wrapped = getAllWrapped(instr);
        if (!mayBePriced(wrapped))
            throw new IllegalArgumentException();
        return price(bs, wrapped);
    }

    private double price(BlackScholes bs, Object[] wrapped)
    {
        BarrierParams bp = getBarrierParams(wrapped);
        return price(bs, bp, getCore(wrapped));
    }
    
    private double price(BlackScholes bs, BarrierParams bp, Object instr)
    {
        if (instr instanceof Option)
        {
            Option op = (Option) instr;
            return price(bs, bp, op);
        }
        else if (instr instanceof Bond)
        {
            assert bp == null;
            Bond bond = (Bond) instr;
            return bs.priceBond(bond.getNominal(), bond.getT());
        }
        else throw new RuntimeException("Flow should not reach that statement");
    }

    private double price(BlackScholes bs, BarrierParams bp, Option op)
    {
        VanillaOptionParams vop = op.vop;
        if (bp == null)
            return bs.price(vop);
        else
            return bs.price(vop, bp);
    }
    
    private BarrierParams getBarrierParams(Object[] wrapped)
    {
        for (Object ob: wrapped) {
            try {
                Barrier b = (Barrier) ob;
                return b.bp;
            } catch (ClassCastException ex) {}
        }
        return null;
    }    
}
