/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lsmapp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;

/**
 *
 * @author grzes
 */
public class ListChoicePanel extends javax.swing.JPanel
{
    /**
     * Creates new form ListChoicePanel
     */
    public ListChoicePanel()
    {
        initComponents();
        mainPanel.setLayout( new BorderLayout() );
    }

    public void close(int id)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void addChoice(Component comp, String lbl)
    {
        int id = nextId++;
        ((DefaultListModel<ListLabel>) jList.getModel()).addElement(
                new ListLabel(id, lbl));
        comps.put(id, comp);
    }
    
    private void listClicked()
    {
        mainPanel.removeAll();
        ListLabel ll = (ListLabel) jList.getSelectedValue();
        mainPanel.add(comps.get(ll.getId()));
        mainPanel.revalidate();
        repaint();
    }
    
    private Map<Integer, Component> comps = new HashMap<>();
    private int nextId = 0;
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();
        mainPanel = new javax.swing.JPanel();

        jList.setModel(new DefaultListModel<ListLabel>());
        jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList);

        jSplitPane1.setLeftComponent(jScrollPane1);

        mainPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 364, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 213, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_jListValueChanged
    {//GEN-HEADEREND:event_jListValueChanged
        listClicked();
    }//GEN-LAST:event_jListValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables

}
