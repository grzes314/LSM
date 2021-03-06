    
package lsmapp.taskPanels;

import finance.instruments.Instr;
import finance.methods.blackscholes.BSMethod;
import finance.methods.common.Method;
import lsmapp.frame.Pricer;
import lsmapp.resultPanels.BS2GUI;
import lsmapp.resultPanels.ResultHandler;

/**
 *
 * @author glos
 */
public class BSPanel extends MethodPanel
{
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        label = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Analitical formulas");

        label.setText("No extra parameters to set.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel label;
    // End of variables declaration//GEN-END:variables

    /** Creates new form BSPanel */
    public BSPanel()
    {
        initComponents();
        label.setText("<html>" + label.getText());
    }
    
    @Override
    Method makeMethod(Instr instr)
    {
        return new BSMethod();
    }

    @Override
    ResultHandler makeResultHandler()
    {
        return new BS2GUI(Pricer.getApp().getResultDisplay());
    }

    @Override
    String getPriceableDesc()
    {
        return "Analitical formulas can price bonds, european vanilla and barrier options.";
    }
}
