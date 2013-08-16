
package lsmapp.instrPanels;

import finance.instruments.Instr;
import java.util.Set;

/**
 *
 * @author Grzegorz Los
 */
public class InstrPanel extends javax.swing.JPanel
{
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane = new javax.swing.JSplitPane();
        instrNameLabel = new javax.swing.JLabel();

        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);

        instrNameLabel.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        instrNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instrNameLabel.setText("InstrName");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(instrNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(instrNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel instrNameLabel;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables


    /** Creates new form InstrPanel */
    public InstrPanel(SpecificInstrPanel specificPanel, NewInstrInfo info)
            throws NoAssetsException
    {
        initComponents();
        this.instrName = info.instrName;
        this.specificPanel = specificPanel;
        makeBarrierPanel(info);
        praparePanel(instrName, specificPanel);
    }

    private void makeBarrierPanel(NewInstrInfo info) throws NoAssetsException
    {
        try
        {
            this.barriersPanel = new BarriersPanel(this);
        }
        catch (NoAssetsException ex)
        {
            if (info.type != NewInstrInfo.InstrType.Bond)
                throw ex;
        }
    }

    private void praparePanel(String instrName, SpecificInstrPanel specificPanel)
    {
        instrNameLabel.setText(instrName);
        splitPane.setLeftComponent(specificPanel);
        splitPane.setRightComponent(barriersPanel);
    }

    public String getInstrName()
    {
        return instrName;
    }

    public BarriersPanel getBarriersPanel()
    {
        return barriersPanel;
    }

    public SpecificInstrPanel getSpecificPanel()
    {
        return specificPanel;
    }

    public Instr makeInstr()
    {
        Instr instr = specificPanel.makeInstr();
        instr = barriersPanel.wrapInBarriers(instr);
        instr.setName(instrName);
        return instr;
    }
    
    public boolean isUsing(String assetName)
    {
        return specificPanel.isUsing(assetName) || 
                (barriersPanel == null ? false : barriersPanel.isUsing(assetName));
    }
    
    public Set<String> getUnderlyings()
    {
        Set<String> res = specificPanel.getUnderlyings();
        res.addAll(barriersPanel.getUnderlyings());
        return res;
    }

    void assetDeleted(String assetName)
    {
        specificPanel.assetDeleted(assetName);
    }

    void assetAdded(String assetName)
    {
        specificPanel.assetAdded(assetName);
    }

    private String instrName;
    private SpecificInstrPanel specificPanel;
    private BarriersPanel barriersPanel;
}
