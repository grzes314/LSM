
package lsmapp.instrPanels;

import finance.instruments.Instr;
import javax.swing.JPanel;

/**
 *
 * @author Grzegorz Los
 */
public abstract class SpecificInstrPanel extends JPanel
{
    abstract Instr makeInstr();
}
