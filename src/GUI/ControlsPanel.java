package GUI;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import model.RotationModel;
import model.TransformData;
import model.TransformDataChangeObserver;
import model.preset.Preset;
import model.preset.PresetModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Nikola
 */
public class ControlsPanel extends javax.swing.JPanel implements TransformDataChangeObserver{

    /**
     * Creates new form ControlsPanel
     */
    public ControlsPanel() {
        initComponents();
        RotationModel.getInstance().addObserver(this);
        setBorder(BorderFactory.createTitledBorder("Rotation"));
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        xRotationField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        yRotationField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        zRotationField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(206, 100));
        setPreferredSize(new java.awt.Dimension(336, 100));
        setLayout(new java.awt.BorderLayout());

        jLabel1.setText("X:");
        jPanel4.add(jLabel1);

        xRotationField.setText("0");
        xRotationField.setMinimumSize(new java.awt.Dimension(80, 28));
        xRotationField.setName("xRotationField"); // NOI18N
        xRotationField.setPreferredSize(new java.awt.Dimension(80, 28));
        xRotationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xRotationFieldActionPerformed(evt);
            }
        });
        xRotationField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                xRotationFieldKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                xRotationFieldKeyReleased(evt);
            }
        });
        jPanel4.add(xRotationField);
        xRotationField.getAccessibleContext().setAccessibleName("xRotationField");

        jLabel2.setText("Y:");
        jPanel4.add(jLabel2);

        yRotationField.setText("0");
        yRotationField.setMinimumSize(new java.awt.Dimension(80, 28));
        yRotationField.setName("yRotationField"); // NOI18N
        yRotationField.setPreferredSize(new java.awt.Dimension(80, 28));
        yRotationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yRotationFieldActionPerformed(evt);
            }
        });
        yRotationField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                yRotationFieldKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                yRotationFieldKeyReleased(evt);
            }
        });
        jPanel4.add(yRotationField);

        jLabel3.setText("Z:");
        jPanel4.add(jLabel3);

        zRotationField.setText("0");
        zRotationField.setMinimumSize(new java.awt.Dimension(80, 28));
        zRotationField.setName("zRotationField"); // NOI18N
        zRotationField.setPreferredSize(new java.awt.Dimension(80, 28));
        zRotationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zRotationFieldActionPerformed(evt);
            }
        });
        zRotationField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                zRotationFieldKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                zRotationFieldKeyReleased(evt);
            }
        });
        jPanel4.add(zRotationField);

        add(jPanel4, java.awt.BorderLayout.NORTH);

        addButton.setText("ADD");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addButton);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void yRotationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yRotationFieldActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_yRotationFieldActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        PresetModel.getInstance().addPreset(new Preset(RotationModel.getInstance().getTransformData()));
    }//GEN-LAST:event_addButtonActionPerformed

    private void xRotationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xRotationFieldActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_xRotationFieldActionPerformed

    private void zRotationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zRotationFieldActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_zRotationFieldActionPerformed

    private void xRotationFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xRotationFieldKeyTyped
        // TODO add your handling code here:
        
    }//GEN-LAST:event_xRotationFieldKeyTyped

    private void yRotationFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yRotationFieldKeyTyped
        // TODO add your handling code here:
        
    }//GEN-LAST:event_yRotationFieldKeyTyped

    private void zRotationFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zRotationFieldKeyTyped
        // TODO add your handling code here:
        
    }//GEN-LAST:event_zRotationFieldKeyTyped

    private void zRotationFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zRotationFieldKeyReleased
        
        this.updateRotationData();
    }//GEN-LAST:event_zRotationFieldKeyReleased

    private void yRotationFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yRotationFieldKeyReleased
        
        this.updateRotationData();
    }//GEN-LAST:event_yRotationFieldKeyReleased

    private void xRotationFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_xRotationFieldKeyReleased
        
        this.updateRotationData();
    }//GEN-LAST:event_xRotationFieldKeyReleased

    private void validateField(JTextField field){
        if(field.getText().length()==0){
            field.setText("0");
        }
    }
    private void updateRotationData() {

        float x = (float)Math.toRadians(valueFromField(xRotationField));
        float y = (float)Math.toRadians(valueFromField(yRotationField));
        float z = (float)Math.toRadians(valueFromField(zRotationField));
        RotationModel.getInstance().setTransformData(this, x, y, z);
        
    }

    public float valueFromField(JTextField pField) {
        float value = 0f;
        try {
            value = Float.parseFloat(pField.getText());
        } catch (NumberFormatException ex) {
            value = 0f;
        }

        return value;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField xRotationField;
    private javax.swing.JTextField yRotationField;
    private javax.swing.JTextField zRotationField;
    // End of variables declaration//GEN-END:variables

   
    private void setAngleValues(double x, double y, double z) {
        xRotationField.setText(String.valueOf(x));
        yRotationField.setText(String.valueOf(y));
        zRotationField.setText(String.valueOf(z));
    }

    private double angle(double pAngle) {
        if (radians) {
            return pAngle;
        }

        return (float) Math.toDegrees(pAngle);
    }
    private boolean radians;

    public void onTransformDataChanged(Object source, TransformData data) {
        double xAngle = angle(data.getRotationX());
        double yAngle = angle(data.getRotationY());
        double zAngle = angle(data.getRotationZ());

        setAngleValues(xAngle, yAngle, zAngle);
    }
}
