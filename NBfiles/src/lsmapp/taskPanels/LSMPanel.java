
package lsmapp.taskPanels;

import finance.instruments.Instr;
import finance.methods.common.Method;
import finance.methods.lsm.LSMProxy;
import finance.methods.lsm.LSMRoot;
import lsmapp.frame.Pricer;
import lsmapp.resultPanels.LSM2GUI;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author glos
 */
public class LSMPanel extends MethodPanel
{
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        varianceGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        simulations = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        timeSteps = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        degree = new javax.swing.JSpinner();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16));
        jLabel1.setText("Longstaff-Schwartz");

        simulations.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100000), Integer.valueOf(100), null, Integer.valueOf(10000)));

        jLabel3.setText("Number of simulations: ");

        jLabel4.setText("Number of time steps: ");

        timeSteps.setModel(new javax.swing.SpinnerNumberModel(100, 1, 10000, 10));

        jLabel5.setText("<html>Degree of approximating polynomial:");

        degree.setModel(new javax.swing.SpinnerNumberModel(3, 1, 20, 1));

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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(degree, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
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
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(degree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner degree;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSpinner simulations;
    private javax.swing.JSpinner timeSteps;
    private javax.swing.ButtonGroup varianceGroup;
    // End of variables declaration//GEN-END:variables


    /** Creates new form MCPanel */
    public LSMPanel()
    {
        initComponents();
    }

    @Override
    Method makeMethod(Instr instr)
    {
        LSMRoot lsm = lsm_proxy.chooseMethod(instr);
        lsm.setN( (Integer) simulations.getValue() );
        lsm.setK( (Integer) timeSteps.getValue() );
        lsm.setM( (Integer) degree.getValue() );
        return lsm;
    }

    @Override
    ResultHandler makeResultHandler()
    {
        return new LSM2GUI(Pricer.getApp().getResultDisplay());
    }

    @Override
    String getPriceableDesc()
    {
        return "LSM can price only instruments depending on at most two assets.";
    }
    
    LSMProxy lsm_proxy = new LSMProxy();
}
