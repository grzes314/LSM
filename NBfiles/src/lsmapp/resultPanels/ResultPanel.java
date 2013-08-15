
package lsmapp.resultPanels;

import java.awt.Component;
import lsmapp.oldFrame.ListChoicePanel;

/**
 *
 * @author Grzegorz Los
 */
public class ResultPanel extends ListChoicePanel implements ResultDisplay
{
    @Override
    public void addResults(String lbl, Component comp)
    {
        addChoice(comp, lbl);
    }
}
