
package lsmapp.taskPanels;

import finance.methods.common.Method;
import finance.methods.montecarlo.AV;
import finance.methods.montecarlo.CMC;
import finance.methods.montecarlo.MonteCarlo;
import lsmapp.Pricer;
import lsmapp.resultPanels.MC2GUI;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author glos
 */
public class MCPanel extends MethodPanel
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

        varianceGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        crudeMonteCarlo = new javax.swing.JRadioButton();
        controlVariates = new javax.swing.JRadioButton();
        anthiteticVariates = new javax.swing.JRadioButton();
        simulations = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        timeSteps = new javax.swing.JSpinner();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Monte Carlo");

        jLabel2.setText("Variance reduction:");

        varianceGroup.add(crudeMonteCarlo);
        crudeMonteCarlo.setSelected(true);
        crudeMonteCarlo.setText("None");

        varianceGroup.add(controlVariates);
        controlVariates.setText("Control variates");

        varianceGroup.add(anthiteticVariates);
        anthiteticVariates.setText("Anthitetic variates");

        simulations.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1000000), Integer.valueOf(100), null, Integer.valueOf(10000)));

        jLabel3.setText("Number of simulations: ");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, simulations, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel3, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        jLabel4.setText("Number of time steps: ");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, timeSteps, org.jdesktop.beansbinding.ObjectProperty.create(), jLabel4, org.jdesktop.beansbinding.BeanProperty.create("labelFor"));
        bindingGroup.addBinding(binding);

        timeSteps.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10000, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(timeSteps)
                            .addComponent(simulations, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(anthiteticVariates)
                            .addComponent(controlVariates)
                            .addComponent(crudeMonteCarlo))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(simulations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(timeSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(crudeMonteCarlo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(controlVariates)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(anthiteticVariates)
                .addContainerGap(125, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton anthiteticVariates;
    private javax.swing.JRadioButton controlVariates;
    private javax.swing.JRadioButton crudeMonteCarlo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSpinner simulations;
    private javax.swing.JSpinner timeSteps;
    private javax.swing.ButtonGroup varianceGroup;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables


    /** Creates new form MCPanel */
    public MCPanel()
    {
        initComponents();
    }    
    
    @Override
    Method makeMethod()
    {
        MonteCarlo mc;
        if (crudeMonteCarlo.isSelected())
            mc = new CMC();
        else if (anthiteticVariates.isSelected())
            mc = new AV();
        else throw new UnsupportedOperationException(); // TODO support
        mc.setN((Integer) simulations.getValue());
        mc.setK((Integer) timeSteps.getValue());
        return mc;
    }

    @Override
    ResultHandler makeResultHandler()
    {
        return new MC2GUI(Pricer.getApp().getResultDisplay());
    }

    @Override
    String getPriceableDesc()
    {
        return "Monte Carlo can price only instruments with european exercise.";
    }
}
