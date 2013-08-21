/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.GUI;

/**
 *
 * @author nikolamarkovic
 */
public class TopCameraControlsPanel extends javax.swing.JPanel {

    private CameraDelegate cameraDelegate;
    /**
     * Creates new form TopCameraControlsPanel
     */
    public TopCameraControlsPanel() {
        initComponents();
        jToggleButton1.setText("Free Cam");
        jToggleButton1.setSelected(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();

        setLayout(new java.awt.BorderLayout());

        jToggleButton1.setText("jToggleButton1");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        add(jToggleButton1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
       if(cameraDelegate!=null){
            cameraDelegate.didChangeCamera(jToggleButton1.isSelected());
       }
    
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

    public void setDelegate(CameraDelegate delegate){
        this.cameraDelegate= delegate;
    }
}