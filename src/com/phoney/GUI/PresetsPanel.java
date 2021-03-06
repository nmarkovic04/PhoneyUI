/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.phoney.model.preset.ModelChangeObserver;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.phoney.animation.AnimationManager;

import com.phoney.model.preset.PresetModel;
import com.phoney.model.RotationModel;
import com.phoney.model.TransformData;
import com.phoney.model.preset.Preset;


import com.phoney.model.preset.PresetManager;
import com.phoney.util.Settings;

/**
 *
 * @author nikolamarkovic
 */
public class PresetsPanel extends javax.swing.JPanel implements ModelChangeObserver{

    /**
     * Creates new form PresetPanel
     */
    
    public PresetsPanel() {
        
        initComponents();
        jTable1.getColumnModel().getColumn(0).setMaxWidth(20);
        jTable1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_DELETE && jTable1.getSelectedRowCount()>0){
                    PresetModel.getInstance().removePresetAtIndex(jTable1.getSelectedRow());
                }
            }
        });
        
        PresetModel.getInstance().addObserver(this);
        TableColumn durationColumn = jTable1.getColumnModel().getColumn(5);

        JComboBox comboBox = new JComboBox();
        for(String name : AnimationManager.getInstance().getFunctionNames()){
            comboBox.addItem(name);
        }
        
        durationColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        setBorder(BorderFactory.createTitledBorder("Presets"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        loopCheckBox = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(350, 55));
        jPanel2.setMinimumSize(new java.awt.Dimension(450, 29));
        jPanel2.setPreferredSize(new java.awt.Dimension(450, 45));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        playButton.setText("PLAY");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        jPanel2.add(playButton);

        stopButton.setText("STOP");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        jPanel2.add(stopButton);

        loopCheckBox.setText("Loop");
        loopCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loopCheckBoxActionPerformed(evt);
            }
        });
        jPanel2.add(loopCheckBox);

        add(jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(650, 32767));

        jTable1.setModel(new PresetsTableModel());
        jScrollPane1.setViewportView(jTable1);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 247, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        add(jPanel3);
    }// </editor-fold>//GEN-END:initComponents

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        PresetManager.getInstance().play();
    }//GEN-LAST:event_playButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        PresetManager.getInstance().stop();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void loopCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loopCheckBoxActionPerformed
        Settings.loop= loopCheckBox.isSelected();
    }//GEN-LAST:event_loopCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JCheckBox loopCheckBox;
    private javax.swing.JButton playButton;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables

    public void onModelChange(PresetModel model) {
        ((DefaultTableModel)jTable1.getModel()).fireTableDataChanged();
    }
}
