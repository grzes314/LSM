
package lsmapp.instrPanels;

import javax.swing.DefaultListModel;
import lsmapp.Pricer;

/**
 *
 * @author Grzegorz Los
 */
public class BarriersPanel extends javax.swing.JPanel
{

    /** Creates new form BarriersPanel */
    public BarriersPanel()
    {
        initialName = Pricer.getApp().getModelManager().getAssets().iterator().next();
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        barriersList = new javax.swing.JList();
        addBarrier = new javax.swing.JButton();
        removeBarrier = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        barrierDesc = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Barriers");

        barriersList.setModel(new DefaultListModel<BarrierWrapper>());
        barriersList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        barriersList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                barriersListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(barriersList);

        addBarrier.setText("Add barrier");
        addBarrier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBarrierActionPerformed(evt);
            }
        });

        removeBarrier.setText("Remove barrier");
        removeBarrier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBarrierActionPerformed(evt);
            }
        });

        barrierDesc.setColumns(20);
        barrierDesc.setRows(5);
        jScrollPane2.setViewportView(barrierDesc);

        jLabel2.setText("Barrier description:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addBarrier, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(removeBarrier, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addBarrier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeBarrier)
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void barriersListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_barriersListValueChanged
        listItemSelected();
}//GEN-LAST:event_barriersListValueChanged


private void addBarrierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBarrierActionPerformed
    addBarrierClicked();
}//GEN-LAST:event_addBarrierActionPerformed

private void removeBarrierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBarrierActionPerformed
    removeBarrierClicked();
}//GEN-LAST:event_removeBarrierActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBarrier;
    private javax.swing.JTextArea barrierDesc;
    private javax.swing.JList barriersList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton removeBarrier;
    // End of variables declaration//GEN-END:variables

    private void listItemSelected()
    {
        BarrierWrapper bw = (BarrierWrapper) barriersList.getSelectedValue();
        barrierDesc.setText(bw.getDescription());
    }

    private void addBarrierClicked()
    {
        BarrierWrapper bw = NewBarrierDialog.showNewBarrierDialog(
                Pricer.getApp().getModelManager().getAssets(), initialName);
        if (bw != null)
            ((DefaultListModel<BarrierWrapper>) barriersList.getModel()).addElement(bw);
    }

    private void removeBarrierClicked()
    {
        Object selected = barriersList.getSelectedValue();
        if (barriersList.getSelectedValue() != null)
            ((DefaultListModel<BarrierWrapper>) barriersList.getModel()).removeElement(selected);
    }

    public String getInitialName()
    {
        return initialName;
    }

    public void setInitialName(String initialName)
    {
        this.initialName = initialName;
    }
    
    private String initialName;
}
