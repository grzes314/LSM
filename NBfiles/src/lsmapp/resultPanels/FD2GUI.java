
package lsmapp.resultPanels;

import instruments.Instr;
import lsmapp.MainFrame;
import lsmapp.controlPanels.FDPanel;
import lsmapp.controlPanels.ResultHandler;
import models.FDModel;

/**
 *
 * @author Grzegorz Los
 */
public class FD2GUI implements ResultHandler
{

    public FD2GUI(MainFrame frame, FDPanel fdPanel)
    {
        this.frame = frame;
        this.fdPanel = fdPanel;
    }
    
    @Override
    public void result(double price)
    {
        FDModel model = fdPanel.getModel();
        Instr instr = fdPanel.getInstr();
        frame.addResults(instr.toString(), Auxiliary.basicInfo(model, instr, price));
    }

    private final MainFrame frame;
    private final FDPanel fdPanel;
}
