
package lsmapp.instrPanels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Grzegorz Los
 */
public class InstrManager
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
    
    private InstrTab instrTab;
    private Map<String, InstrPanel> instrPanels = new HashMap<>();
}
