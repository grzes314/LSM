
package lsmapp.instrPanels;

import finance.instruments.Instr;
import finance.instruments.InvalidInstrParametersException;
import java.util.*;
import lsmapp.modelTab.AssetCountObserver;

/**
 *
 * @author Grzegorz Los
 */
public class InstrManager implements InstrCountInfo, AssetCountObserver
{    
    public InstrPanel getInstrPanel(String instrName)
    {
        return instrPanels.get(instrName);
    }

    public void addInstr(NewInstrInfo info)
            throws IllegalArgumentException, NoAssetsException, UnsupportedOperationException
    {
        ensureNewInstrNameOK(info.instrName);
        InstrPanel instrPanel = createNewPanel(info);
        instrPanels.put(instrPanel.getInstrName(), instrPanel);
        informAboutAddition(info.instrName);
    }

    private InstrPanel createNewPanel(NewInstrInfo info)
            throws NoAssetsException, UnsupportedOperationException
    {
        SpecificInstrPanel panel = createSpecificPanel(info);
        InstrPanel instrPanel = new InstrPanel(panel, info);
        instrPanels.put(info.instrName, instrPanel);
        return instrPanel;
    }
    
    private SpecificInstrPanel createSpecificPanel(NewInstrInfo info)
    {
        switch (info.type)
        {
            case Bond:
                return new BondPanel();
            case Vanilla:
                return new OptionPanel();
            case Asian:
                return new AsianOptionPanel();
            case Lookback:
                return new LookbackOptionPanel();
            case Basket:
                return new BasketOptionPanel();
            default:
                throw new UnsupportedOperationException();
        }
    }
    
    private void ensureNewInstrNameOK(String instrName)
    {
        if (instrName == null || "".equals(instrName.trim()))
            throw new IllegalArgumentException("Instrument name invalid.");
        if (instrPanels.containsKey(instrName))
            throw new IllegalArgumentException("There is already an instrument with that name.");
    }
    
    public void removeInstr(String instr)
    {
        instrPanels.remove(instr);
        informAboutDeletion(instr);
    }

    public int getNumberOfInstrs()
    {
        return instrPanels.size();
    }
    
    public Collection<String> getInstrNames()
    {
        return instrPanels.keySet();
    }
    
    public void clear()
    {
        Object[] names = instrPanels.keySet().toArray();
        for (Object name: names)
            removeInstr((String) name);
    }
    
    @Override
    public void addInstrCountObserver(InstrCountObserver observer)
    {
        observers.add(observer);
    }

    @Override
    public void removeInstrCountObserver(InstrCountObserver observer)
    {
        observers.remove(observer);
    }

    @Override
    public void informAboutDeletion(String instrName)
    {
        for (InstrCountObserver ob: observers)
            ob.instrDeleted(instrName);
    }

    @Override
    public void informAboutAddition(String instrName)
    {
        for (InstrCountObserver ob: observers)
            ob.instrAdded(instrName);
    }

    public Instr makeInstr(String instrName) throws InvalidInstrParametersException
    {
        InstrPanel panel = instrPanels.get(instrName);
        if (panel == null)
            throw new RuntimeException("There is no such instrument: \"" +
                instrName + "\".");
        return panel.makeInstr();
    }

    @Override
    public void assetAdded(String name)
    {
        for (InstrPanel ip: instrPanels.values())
            ip.assetAdded(name);
    }

    @Override
    public void assetDeleted(String name)
    {
        deleteInstrs( getInstrsWhicheUseAsset(name) );
        for (InstrPanel ip: instrPanels.values())
            ip.assetDeleted(name);
    }    
    
    public Collection<String> getInstrsWhicheUseAsset(String name)
    {
        ArrayList<String> res = new ArrayList<>();
        for (InstrPanel panel: instrPanels.values())
            if (panel.isUsing(name))
                res.add(panel.getInstrName());
        return res;
    }
    
    private void deleteInstrs(Collection<String> instrs)
    {
        for (String instr: instrs)
            removeInstr(instr);
    }
    
    private Map<String, InstrPanel> instrPanels = new HashMap<>();
    private List<InstrCountObserver> observers = new ArrayList<>();
}
