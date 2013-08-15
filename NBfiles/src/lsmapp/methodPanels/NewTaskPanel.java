
package lsmapp.methodPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.parameters.ModelParams;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lsmapp.Pricer;
import lsmapp.instrPanels.InstrManager;
import lsmapp.instrPanels.InstrNumberChangeObserver;
import lsmapp.resultPanels.ResultHandler;
import math.matrices.NotPositiveDefiniteMatrixException;

/**
 *
 * @author Grzegorz Los
 */
public class NewTaskPanel extends javax.swing.JPanel
{
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jLabel1 = new javax.swing.JLabel();
        methodCombo = new javax.swing.JComboBox();
        methodContainer = new javax.swing.JPanel();
        price = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        instrCombo = new javax.swing.JComboBox();

        setMinimumSize(new java.awt.Dimension(400, 250));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Select method:");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, methodCombo, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        methodCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                methodComboItemStateChanged(evt);
            }
        });

        methodContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder(0));
        methodContainer.setLayout(new java.awt.GridLayout(1, 1));

        price.setText("Price");
        price.setEnabled(false);
        price.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Select instrument: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(methodCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(instrCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(methodContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(methodCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(instrCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(methodContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void methodComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_methodComboItemStateChanged
        methodSelected();
    }//GEN-LAST:event_methodComboItemStateChanged

    private void priceActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_priceActionPerformed
    {//GEN-HEADEREND:event_priceActionPerformed
        priceClicked();
    }//GEN-LAST:event_priceActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox instrCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox methodCombo;
    private javax.swing.JPanel methodContainer;
    private javax.swing.JButton price;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    public NewTaskPanel(InstrManager instrManager, ProgressesContainer progressesContainer)
    {
        this.progressesContainer = progressesContainer;
        initComponents();
        prepareMethodPanels();
        prepareMethodCombo();
        prepareInstrCombo(instrManager);
    }

    private void prepareMethodPanels()
    {
        methodPanels = new HashMap<>();
        methodPanels.put("Analitical formulas", new BSPanel());
        methodPanels.put("Monte Carlo", new MCPanel());
    }

    private void prepareMethodCombo()
    {
        for (String name: methodPanels.keySet())
            methodCombo.addItem(name);
    }

    private void methodSelected()
    {
        String name = (String) methodCombo.getSelectedItem();
        methodContainer.removeAll();
        methodContainer.add(methodPanels.get(name));
        methodContainer.revalidate();
        methodContainer.repaint();
    }

    private void prepareInstrCombo(InstrManager instrManager)
    {
        instrManager.addInstrNumberChangeObserver(
            new InstrNumberChangeObserver() {
                @Override public void addInstr(String name) {
                    NewTaskPanel.this.addInstr(name);
                }
                @Override public void delInstr(String name) {
                    NewTaskPanel.this.delInstr(name);
                }
            });
    }
    
    private void addInstr(String name)
    {
        instrCombo.addItem(name);
        price.setEnabled(true);
    }
    
    private void delInstr(String name)
    {
        instrCombo.removeItem(name);
        if (instrCombo.getItemCount() == 0)
            price.setEnabled(false);
    }

    private void priceClicked()
    {
        try {
            preparePricingTask();
            task.execute();
        } catch (NotPositiveDefiniteMatrixException ex) {
            Logger.getLogger(NewTaskPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(NewTaskPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void preparePricingTask() throws NotPositiveDefiniteMatrixException
    {
        makeMethod();
        makeModelParams();
        makeInstr();
        makeResultHandler();
        task = new PricingTask(method, modelParams, instr, resultHandler);
        makeProgressPanel();
    }
    
    private void makeMethod()
    {
        String methodName = (String) methodCombo.getSelectedItem();
        method = methodPanels.get(methodName).makeMethod();
    }
    
    private void makeModelParams() throws NotPositiveDefiniteMatrixException
    {
        modelParams = Pricer.getApp().getModelManager().toParams();
    }

    private void makeInstr()
    {
        String instrName = (String) instrCombo.getSelectedItem();
        instr = Pricer.getApp().getInstrManager().makeInstr(instrName);
    }
    
    private void makeResultHandler()
    {
        String methodName = (String) methodCombo.getSelectedItem();
        resultHandler =  methodPanels.get(methodName)
            .makeResultHandler(method, modelParams, instr);
    }

    private void makeProgressPanel()
    {
        panel = new ProgressPanel(progressesContainer, task);
        task.setProgressPanel(panel);
        method.addObserver(panel);
        progressesContainer.add(panel);
        progressesContainer.revalidate();
        progressesContainer.repaint();
    }
    
    private Method method;
    private ModelParams modelParams;
    private Instr instr;
    private ResultHandler resultHandler;
    private PricingTask task;
    private ProgressPanel panel;
    private ProgressesContainer progressesContainer;
    private Map<String, MethodPanel> methodPanels;
}
