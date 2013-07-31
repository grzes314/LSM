
package lsmapp.resultPanels;

import finance.instruments.Instr;
import lsmapp.MainFrame;
import lsmapp.controlPanels.BSPanel;
import lsmapp.controlPanels.ResultHandler;
import finance.models.BSModel;

/**
 *
 * @author Grzegorz Los
 */
public class BS2GUI implements ResultHandler
{

    public BS2GUI(MainFrame frame, BSPanel bsPanel)
    {
        this.frame = frame;
        this.bsPanel = bsPanel;
    }

    @Override
    public void result(double price)
    {        
        BSModel model = bsPanel.getModel();
        Instr instr = bsPanel.getInstr();
        frame.addResults(instr.toString(), Auxiliary.basicInfo(model, instr, price));
    }

    private final MainFrame frame;
    private final BSPanel bsPanel;
}
