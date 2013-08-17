
package lsmapp.taskPanels;

import finance.methods.common.Progress;
import finance.methods.common.ProgressObserver;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Grzegorz Los
 */
public class ProgressPanel extends JPanel implements ProgressObserver
{
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        progressDesc = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taskDesc = new javax.swing.JTextArea();

        setBorder(javax.swing.BorderFactory.createBevelBorder(0));
        setMaximumSize(new java.awt.Dimension(32767, 120));
        setMinimumSize(new java.awt.Dimension(200, 80));
        setPreferredSize(new java.awt.Dimension(300, 100));

        progressBar.setStringPainted(true);

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        progressDesc.setText("task not started");

        taskDesc.setBackground(new java.awt.Color(204, 204, 204));
        taskDesc.setColumns(20);
        taskDesc.setEditable(false);
        taskDesc.setLineWrap(true);
        taskDesc.setRows(2);
        taskDesc.setWrapStyleWord(true);
        jScrollPane1.setViewportView(taskDesc);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(progressDesc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressDesc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    die();
}//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressDesc;
    private javax.swing.JTextArea taskDesc;
    // End of variables declaration//GEN-END:variables


    /** Creates new form ProgressPanel */
    public ProgressPanel(ProgressesContainer container, PricingTask pricingTask)
    {
        this.container = container;
        this.pricingTask = pricingTask;
        initComponents();
        taskDesc.setBackground(this.getBackground());
        taskDesc.setText(pricingTask.getDesc());
    }

    @Override
    public void update(Progress pr)
    {
        progressDesc.setText(pr.desc);
        progressBar.setValue(pr.percent);
    }

    void showError(String errMssg)
    {
        removeAll();
        setLayout(new BorderLayout());
        add(taskDesc, BorderLayout.NORTH);
        add(new JLabel("<html> Error while pricing: " + errMssg), BorderLayout.CENTER);
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent arg0) {
                removeFromContainer();
            }
        });
        add(close, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
    
    public void die()
    {
        pricingTask.cancel(true);
        removeFromContainer();
    }
    
    private void removeFromContainer()
    {
        container.removeProgress(this);
        container.revalidate();
        container.repaint();
    }

    void showErrorDialog(String message)
    {
        JOptionPane.showMessageDialog(container, "Error while pricing: " +
            pricingTask.getDesc() + "\n" + message, "Pricing error", JOptionPane.ERROR_MESSAGE);
    }
    
    private ProgressesContainer container;
    private PricingTask pricingTask;
}
