
package lsmapp.instrPanels;

import finance.parameters.BarrierParams;
import finance.parameters.PartialBarrierParams;
import java.util.Collection;
import lsmapp.frame.Pricer;
import lsmapp.modelTab.OneAssetPanel;

/**
 *
 * @author Grzegorz Los
 */
public class NewBarrierDialog extends javax.swing.JDialog
{
    /** Creates new form NewBarrierDialog */
    public NewBarrierDialog(Collection<String> assetNames, String initialName)
    {
        super(Pricer.getApp(), true);
        initComponents();
        putAssetNamesToCombo(assetNames, initialName);
        partialPanel.setVisible(false);
        this.setLocationRelativeTo(Pricer.getApp());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        typeGroup = new javax.swing.ButtonGroup();
        offOrOnGroup = new javax.swing.ButtonGroup();
        OK = new javax.swing.JButton();
        cance = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        uai = new javax.swing.JRadioButton();
        uao = new javax.swing.JRadioButton();
        dai = new javax.swing.JRadioButton();
        dao = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        level = new javax.swing.JSpinner();
        partial = new javax.swing.JCheckBox();
        partialPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        borderTime = new javax.swing.JSpinner();
        early = new javax.swing.JRadioButton();
        late = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        assetCombo = new javax.swing.JComboBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        OK.setText("OK");
        OK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKActionPerformed(evt);
            }
        });

        cance.setText("Cancel");
        cance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canceActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setText("Adding Barrier");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Barrier type:");

        typeGroup.add(uai);
        uai.setSelected(true);
        uai.setText("Up-and-in");

        typeGroup.add(uao);
        uao.setText("Up-and-out");

        typeGroup.add(dai);
        dai.setText("Down-and-in");

        typeGroup.add(dao);
        dao.setText("Down-and-out");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Level:");

        level.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(1.0d)));

        partial.setText("partial");
        partial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partialActionPerformed(evt);
            }
        });

        partialPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, java.awt.Color.darkGray));
        partialPanel.setEnabled(false);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Border time:");

        borderTime.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(0.1d)));

        offOrOnGroup.add(early);
        early.setSelected(true);
        early.setText("Early");

        offOrOnGroup.add(late);
        late.setText("Late");

        jLabel5.setText("Barrier is:");

        javax.swing.GroupLayout partialPanelLayout = new javax.swing.GroupLayout(partialPanel);
        partialPanel.setLayout(partialPanelLayout);
        partialPanelLayout.setHorizontalGroup(
            partialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partialPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(partialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(partialPanelLayout.createSequentialGroup()
                        .addComponent(early)
                        .addGap(18, 18, 18)
                        .addComponent(late))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, partialPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)))
                .addGap(45, 45, 45)
                .addGroup(partialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(borderTime, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        partialPanelLayout.setVerticalGroup(
            partialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(partialPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(partialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(partialPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(borderTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(partialPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(partialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(late)
                            .addComponent(early))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setText("On asset:");

        assetCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                assetComboItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(221, Short.MAX_VALUE))
            .addComponent(filler1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(partialPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 242, Short.MAX_VALUE)
                        .addComponent(OK, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cance, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(partial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(345, 345, 345))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(assetCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(level, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(uai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dai, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(uao)
                                    .addComponent(dao))))
                        .addGap(0, 38, Short.MAX_VALUE)))
                .addGap(4, 4, 4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(assetCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uai)
                    .addComponent(uao)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dai)
                    .addComponent(dao))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(partial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(partialPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cance, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(OK, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void canceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_canceActionPerformed
    setVisible(false);
}//GEN-LAST:event_canceActionPerformed

private void partialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partialActionPerformed
    partialEnabled = !partialEnabled;
    partialPanel.setVisible(partialEnabled);
}//GEN-LAST:event_partialActionPerformed

private void OKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKActionPerformed
    OK_hit = true;
    setVisible(false);
}//GEN-LAST:event_OKActionPerformed

    private void assetComboItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_assetComboItemStateChanged
    {//GEN-HEADEREND:event_assetComboItemStateChanged
        assetSelected();
    }//GEN-LAST:event_assetComboItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OK;
    private javax.swing.JComboBox assetCombo;
    private javax.swing.JSpinner borderTime;
    private javax.swing.JButton cance;
    private javax.swing.JRadioButton dai;
    private javax.swing.JRadioButton dao;
    private javax.swing.JRadioButton early;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JRadioButton late;
    private javax.swing.JSpinner level;
    private javax.swing.ButtonGroup offOrOnGroup;
    private javax.swing.JCheckBox partial;
    private javax.swing.JPanel partialPanel;
    private javax.swing.ButtonGroup typeGroup;
    private javax.swing.JRadioButton uai;
    private javax.swing.JRadioButton uao;
    // End of variables declaration//GEN-END:variables

    static BarrierWrapper showNewBarrierDialog(Collection<String> assetNames, String initialAsset)
    {
        NewBarrierDialog dialog = new NewBarrierDialog(assetNames, initialAsset);
        dialog.setVisible(true);
        BarrierWrapper bw;
        if (dialog.OK_hit)
            bw = dialog.makeBarrierWrapper();
        else
            bw = null;
        dialog.dispose();
        return bw;
    }

    private BarrierWrapper makeBarrierWrapper()
    {
        BarrierParams bp = makeBarrierParams();
        String asset = "" + assetCombo.getSelectedItem();
        if (partial.isSelected())
            return new BarrierWrapper( makePartialBarrierParams(bp), asset );
        else return new BarrierWrapper(bp, asset);        
    }

    private BarrierParams makeBarrierParams()
    {
        BarrierParams.Type type;
        if (uai.isSelected()) type = BarrierParams.Type.UAI;
        else if (uao.isSelected()) type = BarrierParams.Type.UAO;
        else if (dai.isSelected()) type = BarrierParams.Type.DAI;
        else type = BarrierParams.Type.DAO;
        double v = (Double) level.getValue();
        return new BarrierParams(type, v);
    }
    
    private PartialBarrierParams makePartialBarrierParams(BarrierParams bp)
    {
        double t = (Double) borderTime.getValue();
        PartialBarrierParams.PartType type = early.isSelected() ?
                PartialBarrierParams.PartType.EARLY : PartialBarrierParams.PartType.LATE;
        return new PartialBarrierParams(bp, type, t);
    }

    private void putAssetNamesToCombo(Collection<String> assetNames, String initialName)
    {
        for(String name: assetNames)
            assetCombo.addItem(name);
        assetCombo.setSelectedItem(initialName);
    }
    
    private void assetSelected()
    {
        String assetName = (String) assetCombo.getSelectedItem();
        OneAssetPanel oap = Pricer.getApp().getModelManager().getAsset(assetName);
        fillDefaultLevel( oap.makeOneAssetParams().S );
    }

    private void fillDefaultLevel(double spot)
    {
        level.setValue(spot);
    }
    
    private boolean OK_hit;
    private boolean partialEnabled = false;
}
