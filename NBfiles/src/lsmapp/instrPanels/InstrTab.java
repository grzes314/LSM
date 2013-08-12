
package lsmapp.instrPanels;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import lsmapp.Pricer;

/**
 *
 * @author Grzegorz Los
 */
public class InstrTab extends javax.swing.JPanel
{
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        generalInfoContainer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        numberOfInstrs = new javax.swing.JTextField();
        addInstr = new javax.swing.JButton();
        deleteInstr = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 32767));
        jLabel3 = new javax.swing.JLabel();
        instrCombo = new javax.swing.JComboBox();
        instrPanel = new javax.swing.JPanel();

        generalInfoContainer.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Number of instruments: ");

        numberOfInstrs.setEditable(false);
        numberOfInstrs.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        numberOfInstrs.setText("0");

        addInstr.setText("Add instrument");
        addInstr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInstrActionPerformed(evt);
            }
        });

        deleteInstr.setText("Delete instrument");
        deleteInstr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteInstrActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout generalInfoContainerLayout = new javax.swing.GroupLayout(generalInfoContainer);
        generalInfoContainer.setLayout(generalInfoContainerLayout);
        generalInfoContainerLayout.setHorizontalGroup(
            generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalInfoContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numberOfInstrs, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addInstr, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteInstr, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                .addContainerGap())
        );
        generalInfoContainerLayout.setVerticalGroup(
            generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalInfoContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(addInstr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfInstrs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteInstr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Select instrument: ");

        instrCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                instrComboItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout instrPanelLayout = new javax.swing.GroupLayout(instrPanel);
        instrPanel.setLayout(instrPanelLayout);
        instrPanelLayout.setHorizontalGroup(
            instrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
        );
        instrPanelLayout.setVerticalGroup(
            instrPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(instrCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 277, Short.MAX_VALUE))
                    .addComponent(generalInfoContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
            .addComponent(instrPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(generalInfoContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(instrCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(instrPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void addInstrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInstrActionPerformed
        addInstrClicked();
}//GEN-LAST:event_addInstrActionPerformed

private void deleteInstrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInstrActionPerformed
       deleteInstrClicked();
}//GEN-LAST:event_deleteInstrActionPerformed

private void instrComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_instrComboItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
            instrSelected(evt.getItem() + "");
}//GEN-LAST:event_instrComboItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addInstr;
    private javax.swing.JButton deleteInstr;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel generalInfoContainer;
    private javax.swing.JComboBox instrCombo;
    private javax.swing.JPanel instrPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField numberOfInstrs;
    // End of variables declaration//GEN-END:variables


    /** Creates new form InstrTab */
    public InstrTab()
    {
        initComponents();
        instrPanel.setLayout(new BorderLayout());
    }

    public enum InstrType {
        Bond, Vanilla, Basket, Asian, Lookback
    }
    
    private void addInstrClicked()
    {
        NewInstrInfo info = NewInstrDialog.showNewInstrDialog();
        if (info != null)
        {
            createNewPanel(info);
            addNewInstr(info.instrName);
        }
    }

    private void addNewInstr(String instrName)
    {
        instrCombo.addItem(instrName);
        instrCombo.setSelectedItem(instrName);
    }

    private void createNewPanel(NewInstrInfo info)
    {
        SpecificInstrPanel panel;
        if (!checkNewInstrNameOK(info.instrName))
            return;
        switch (info.type)
        {
            case Bond:
                panel = new BondPanel();
                break;
            case Vanilla:
                panel = new OptionPanel(Pricer.getApp().getModelManager().getAssets());
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unsupported.");
                return;
        }
        instrPanels.put(info.instrName, new InstrPanel(panel, info.instrName));
    }
    
    private boolean checkNewInstrNameOK(String instrName)
    {
        if (instrName == null || "".equals(instrName.trim()))
        {
            JOptionPane.showMessageDialog(this, "Instrument name invalid.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (instrPanels.containsKey(instrName))
        {
            JOptionPane.showMessageDialog(this, "There is already an instrument with that name",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    

    private void deleteInstrClicked()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void instrSelected(String name)
    {
        instrPanel.removeAll();
        instrPanel.add(instrPanels.get(name));
        updateView();
    }
    
    void updateView()
    {
        numberOfInstrs.setText("" + instrPanels.size());
        revalidate();
        repaint();
    }
    
    private Collection<String> getInstrNames()
    {
        return instrPanels.keySet();
    }

    private Map<String, InstrPanel> instrPanels = new HashMap<>();
}
