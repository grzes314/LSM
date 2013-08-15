
package lsmapp.instrPanels;

import finance.instruments.Instr;
import java.util.*;

/**
 *
 * @author Grzegorz Los
 */
public class InstrManager implements InstrNumberChangeInfo
{
    public Collection<String> getInstrsWhicheUseAsset(String name)
    {
        ArrayList<String> res = new ArrayList<>();
        for (InstrPanel panel: instrPanels.values())
            if (panel.isUsing(name))
                res.add(panel.getInstrName());
        return res;
    }

    public void setInstrTab(InstrTab instrTab)
    {
        this.instrTab = instrTab;
    }
    
    public InstrPanel getInstrPanel(String instrName)
    {
        return instrPanels.get(instrName);
    }

    public void addInstr(NewInstrInfo info)
            throws IllegalArgumentException, NoAssetsException, UnsupportedOperationException
    {
        ensureNewInstrNameOK(info.instrName);
        InstrPanel instrPanel = createNewPanel(info);
        instrTab.addNewInstr(info.instrName);
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
        instrTab.removeInstr(instr);
        updateAssetLists();
        informAboutAddition(instr);
    }

    public int getNumberOfInstrs()
    {
        return instrPanels.size();
    }
    
    public Collection<String> getInstrNames()
    {
        return instrPanels.keySet();
    }
    
    public void updateAssetLists()
    {
        for (InstrPanel ip: instrPanels.values())
            ip.updateAssetLists();
    }
    
    public void clear()
    {
        instrPanels.clear();
        instrTab.clear();
    }
    
    @Override
    public void addInstrNumberChangeObserver(InstrNumberChangeObserver observer)
    {
        observers.add(observer);
    }

    @Override
    public void removeInstrNumberChangeObserver(InstrNumberChangeObserver observer)
    {
        observers.remove(observer);
    }

    @Override
    public void informAboutDeletion(String instrName)
    {
        for (InstrNumberChangeObserver ob: observers)
            ob.delInstr(instrName);
    }

    @Override
    public void informAboutAddition(String instrName)
    {
        for (InstrNumberChangeObserver ob: observers)
            ob.addInstr(instrName);
    }

    public Instr makeInstr(String instrName)
    {
        InstrPanel panel = instrPanels.get(instrName);
        if (panel == null)
            throw new RuntimeException("There is no such instrument: \"" +
                instrName + "\".");
        return panel.makeInstr();
    }
    
    private InstrTab instrTab;
    private Map<String, InstrPanel> instrPanels = new HashMap<>();
    private List<InstrNumberChangeObserver> observers = new ArrayList<>();
}
