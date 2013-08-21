/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Nikola
 */
public class TransformData {
    private double rotationX;
    private double rotationY;
    private double rotationZ;

    @JsonIgnore private Quaternion quaternion;
    @JsonIgnore private Quaternion lastRotation;
    public TransformData(Quaternion q){
        this();
        setQuaternion(q);
    }
    
    public TransformData(TransformData other){
        this.rotationX= other.getRotationX();
        this.rotationY= other.getRotationY();
        this.rotationZ= other.getRotationZ();
        this.quaternion= other.getQuaternion();
    }
    
    public TransformData(){
       Quaternion q= new Quaternion().fromAngleAxis(0, Vector3f.UNIT_X);
       
       setQuaternion(q);
    }
    
    /**
     * @return the rotationX
     */
    public double getRotationX() {
        return rotationX;
    }

    /**
     * @param rotationX the rotationX to set
     */
    public void setRotationX(double rotationX) {
        this.rotationX = rotationX;
        
    }

    /**
     * @return the rotationY
     */
    public double getRotationY() {
        return rotationY;
    }

    /**
     * @param rotationY the rotationY to set
     */
    public void setRotationY(double rotationY) {
        this.rotationY = rotationY;
    }

    /**
     * @return the rotationZ
     */
    public double getRotationZ() {
        return rotationZ;
    }

    /**
     * @param rotationZ the rotationZ to set
     */
    public void setRotationZ(double rotationZ) {
        this.rotationZ = rotationZ;
    }

    /**
     * @return the quaterion
     */
    public Quaternion getQuaternion() {
        return quaternion;
    }

    
    
    /**
     * @param quaterion the quaterion to set
     */
    public void setQuaternion(Quaternion quaternion) {
        this.quaternion = new Quaternion(quaternion);
        float angles[]= new float[3];
        quaternion.toAngles(angles);
        
        setRotationX(angles[0]);
        setRotationY(angles[1]);
        setRotationZ(angles[2]);
    }
    
    public void calculateQuaternion(){
        quaternion= new Quaternion().fromAngles(new float[]{ (float)rotationX, 
                                                             (float)rotationY, 
                                                             (float)rotationZ});
    }

    /**
     * @return the lastRotation
     */
    public Quaternion getLastRotation() {
        return lastRotation;
    }

    /**
     * @param lastRotation the lastRotation to set
     */
    public void setLastRotation(Quaternion lastRotation) {
        this.lastRotation = lastRotation;
    }
    
    
}
