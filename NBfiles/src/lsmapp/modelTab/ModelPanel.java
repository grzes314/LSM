
package lsmapp.modelTab;

import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JOptionPane;
import lsmapp.Pricer;
import lsmapp.instrPanels.InstrManager;

/**
 *
 * @author Grzegorz Los
 */
public class ModelPanel extends javax.swing.JPanel
{
    /**
     * Creates new form ModelPanel
     */
    public ModelPanel(ModelManager modelManager)
    {
        initComponents();
        oneAssetContainer.setLayout( new BorderLayout() );
        this.modelManager = modelManager;
    }

    private void assetSelected(String asset)
    {
        oneAssetContainer.removeAll();
        oneAssetContainer.add(modelManager.getAsset(asset), BorderLayout.CENTER);
        updateView();
    }
    
    private void addAssetClicked()
    {
        String name = JOptionPane.showInputDialog(this, "Name of the asset");
        try {
            if (name != null)
                addAsset(name);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addAsset(String name) throws IllegalArgumentException
    {
        modelManager.addAsset(name);
        assetsList.addItem(name);
        showStatusMessage("New asset added: " + name);
        assetsList.setSelectedItem(name);
    }
    
    private void deleteAssetClicked()
    {
        if (modelManager.getNumberOfAssets() == 0)
        {
            JOptionPane.showMessageDialog(this, "There is nothing to delete.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return ;
        }
        if (modelManager.getNumberOfAssets() == 1)
            maybeDeleteTheOnlyAsset();
        else
            maybeDeleteAsset();
    }

    private void maybeDeleteAsset()
    {
        String choice = chooseAssetToDelete();
        if (choice != null)
        {
            InstrManager im = Pricer.getApp().getInstrManager();
            Collection<String> instrs = im.getInstrsWhicheUseAsset(choice);
            if (confirmInstrsDeletion(instrs))
            {
                deleteInstrs(instrs);
                deleteAsset(choice);
            }
        }
    }
    
    private String chooseAssetToDelete()
    {
        String choice = (String) JOptionPane.showInputDialog(this,
                "Which asset shall be deleted?", "Deleting",
                JOptionPane.QUESTION_MESSAGE, null, modelManager.getAssets().toArray(),
                assetsList.getSelectedItem());
        return choice;
    }
    
    private boolean confirmInstrsDeletion(Collection<String> instrs)
    {
        if (instrs.isEmpty())
            return true;
        String str = "";
        for (String instr: instrs)
            str += instr + "\n";
        int res = JOptionPane.showConfirmDialog(
            this, "Following instruments depend on chosen asset and also must be removed:\n" + str,
            "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION)
            return true;
        else return false;
    }
    
    private void deleteInstrs(Collection<String> instrs)
    {
        InstrManager im = Pricer.getApp().getInstrManager();
        for (String instr: instrs)
            im.removeInstr(instr);
    }

    private void deleteAsset(String name)
    {
        modelManager.deleteAsset(name);
        assetsList.removeItem(name);
        showStatusMessage("Asset \"" + name +"\" deleted.");
        updateView();
    }

    private void maybeDeleteTheOnlyAsset()
    {
        int res = JOptionPane.showConfirmDialog(
            this, "Are you sure you want to delete the only asset?",
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION)
        {
            deleteTheOnlyAsset();
        }
    }

    private void deleteTheOnlyAsset()
    {
        InstrManager im = Pricer.getApp().getInstrManager();
        String asset = modelManager.getAssets().iterator().next();
        Collection<String> instrs = im.getInstrsWhicheUseAsset(asset);
        if (confirmInstrsDeletion(instrs))
        {
            deleteInstrs(instrs);
            modelManager.clear();
            showStatusMessage("Model is now empty");
        }
    }
    
    private void showStatusMessage(String mssg)
    {
        Pricer.getApp().setStatus(mssg);
    }
    
    public void updateView()
    {
        numberOfAssets.setText("" + modelManager.getNumberOfAssets());
        revalidate();
        repaint();
    }
    
    public void setR(double r)
    {
        rate.setValue(r);
    }
    
    public double getR()
    {
        return (Double) rate.getValue();
    }
    
    public void reset()
    {
        oneAssetContainer.removeAll();
        assetsList.removeAllItems();
        Collection<String> assetNames = modelManager.getAssets();
        for (String asset: assetNames)
            assetsList.addItem(asset);
        numberOfAssets.setText("" + modelManager.getNumberOfAssets());
        revalidate();
        repaint();
    }
    
    private ModelManager modelManager;
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        oneAssetContainer = new javax.swing.JPanel();
        generalInfoContainer = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        numberOfAssets = new javax.swing.JTextField();
        addAsset = new javax.swing.JButton();
        deleteAsset = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        rate = new javax.swing.JSpinner();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 0), new java.awt.Dimension(7, 32767));
        jLabel3 = new javax.swing.JLabel();
        assetsList = new javax.swing.JComboBox();

        oneAssetContainer.setBorder(new javax.swing.border.SoftBevelBorder(0));

        javax.swing.GroupLayout oneAssetContainerLayout = new javax.swing.GroupLayout(oneAssetContainer);
        oneAssetContainer.setLayout(oneAssetContainerLayout);
        oneAssetContainerLayout.setHorizontalGroup(
            oneAssetContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        oneAssetContainerLayout.setVerticalGroup(
            oneAssetContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 187, Short.MAX_VALUE)
        );

        generalInfoContainer.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Number of assets: ");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, numberOfAssets, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        numberOfAssets.setEditable(false);
        numberOfAssets.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        numberOfAssets.setText("0");

        addAsset.setText("Add asset");
        addAsset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAssetActionPerformed(evt);
            }
        });

        deleteAsset.setText("Delete asset");
        deleteAsset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAssetActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Riskless interest rate: ");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rate, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel2, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        rate.setModel(new javax.swing.SpinnerNumberModel(0.05d, -1.0d, 5.0d, 0.001d));

        javax.swing.GroupLayout generalInfoContainerLayout = new javax.swing.GroupLayout(generalInfoContainer);
        generalInfoContainer.setLayout(generalInfoContainerLayout);
        generalInfoContainerLayout.setHorizontalGroup(
            generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalInfoContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(generalInfoContainerLayout.createSequentialGroup()
                        .addComponent(numberOfAssets, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addAsset, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteAsset, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                        .addGap(40, 40, 40)
                        .addComponent(filler1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                    .addGroup(generalInfoContainerLayout.createSequentialGroup()
                        .addComponent(rate, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                        .addComponent(addAsset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfAssets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteAsset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(generalInfoContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(rate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jLabel3.setText("Select asset: ");

        assetsList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                assetsListItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(oneAssetContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(generalInfoContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(assetsList, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(generalInfoContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(assetsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(oneAssetContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void assetsListItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_assetsListItemStateChanged
    {//GEN-HEADEREND:event_assetsListItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED)
            assetSelected(evt.getItem() + "");
    }//GEN-LAST:event_assetsListItemStateChanged

    private void addAssetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addAssetActionPerformed
    {//GEN-HEADEREND:event_addAssetActionPerformed
        addAssetClicked();
    }//GEN-LAST:event_addAssetActionPerformed

    private void deleteAssetActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteAssetActionPerformed
    {//GEN-HEADEREND:event_deleteAssetActionPerformed
       deleteAssetClicked();
    }//GEN-LAST:event_deleteAssetActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAsset;
    private javax.swing.JComboBox assetsList;
    private javax.swing.JButton deleteAsset;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel generalInfoContainer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField numberOfAssets;
    private javax.swing.JPanel oneAssetContainer;
    private javax.swing.JSpinner rate;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}