
package finance.methods.blackscholes;

import finance.instruments.*;
import finance.methods.common.*;
import finance.parameters.BarrierParams;
import finance.parameters.ModelParams;
import finance.parameters.SimpleModelParams;
import finance.parameters.VanillaOptionParams;
import java.util.ArrayList;

/**
 * Allows the user to price {@code Instr} using Black-Scholes method if it is priceable.
 * @author Grzegorz Los
 */
public class BSMethod implements Method
{

    public BSMethod(BlackScholes method)
    {
        bs = method;
    }

    public BSMethod()
    {
        bs = new BlackScholes();
    }
    
    @Override
    public void setModelParams(ModelParams mp) throws WrongModelException
    {
        SimpleModelParams smp;
        try {
            smp = (SimpleModelParams) mp;
        } catch (ClassCastException ex) {
            throw new WrongModelException("Black-Scholes method can only be used"
                    + "with model on single asset");
        }
        bs.setParams(smp);
    }
    
    @Override
    public boolean isPriceable(Instr instr)
    {
        Object[] wrapped = getAllWrapped(instr);
        return mayBePriced(wrapped, instr);
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
    
    private boolean mayBePriced(Object[] wrapped, Instr instr)
    {
        Object core = getCore(wrapped);
        if (isOption(core))
        {
            if (wrapped.length == 3)
                return instr.areYou("european") && instr.areYou("barrier");
            if (wrapped.length == 2)
                return instr.areYou("european");
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
    
    @Override
    public double price(Instr instr) throws WrongInstrException
    {
        Object[] wrapped = getAllWrapped(instr);
        if (!mayBePriced(wrapped, instr))
            throw new WrongInstrException("BSMethod can price only european "
                    + "vanilla and simple barrier options and bonds.");
        return price(wrapped);
    }

    private double price(Object[] wrapped)
    {
        BarrierParams bp = getBarrierParams(wrapped);
        return price(bp, getCore(wrapped));
    }
    
    private double price(BarrierParams bp, Object instr)
    {
        if (instr instanceof Option)
        {
            Option op = (Option) instr;
            return price(bp, op);
        }
        else if (instr instanceof Bond)
        {
            assert bp == null;
            Bond bond = (Bond) instr;
            return bs.priceBond(bond.getNominal(), bond.getT());
        }
        else throw new RuntimeException("Flow should not reach that statement");
    }

    private double price(BarrierParams bp, Option op)
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

    @Override
    public String getDesc()
    {
        return "Black-Scholes or other analitical formula";
    }
    
    @Override
    public String toString()
    {
        return "Analitical formula";
    }

    @Override
    public void removeObserver(ProgressObserver ob)
    {
        bs.removeObserver(ob);
    }

    @Override
    public void notifyObservers(Progress pr)
    {
        bs.notifyObservers(pr);
    }

    @Override
    public void addObserver(ProgressObserver ob)
    {
        bs.addObserver(ob);
    }
    
    
    private BlackScholes bs;
}
