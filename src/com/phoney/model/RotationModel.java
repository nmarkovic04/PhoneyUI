/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.model;

import com.jme3.math.Quaternion;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author nikolamarkovic
 */
public class RotationModel {
    private static RotationModel instance= new RotationModel();
    private List<TransformDataChangeObserver> observers;
    /**
     * @param aInstance the instance to set
     */
    public static void setInstance(RotationModel aInstance) {
        instance = aInstance;
    }
    
    private TransformData transformData;
    
    private RotationModel() { 
        transformData= new TransformData();
        observers= new LinkedList<TransformDataChangeObserver>();
    }
    
    
    public static RotationModel getInstance(){
        return instance;
    }

    /**
     * @return the transformData
     */
    public TransformData getTransformData() {
        return transformData;
    }
   
    public void setQuaternion(Object source, Quaternion q){
        getTransformData().setQuaternion(q);
        notifyTransformDataChanged(source);
    }
    public void setTransformData(float x, float y, float z){
        this.setTransformData(null, x, y, z);
    }
    public void setTransformData(Object source, float x, float y, float z){
        System.out.println("Rotation changed to "+x+" "+y+" "+z);
        Quaternion q= new Quaternion(new float[] { (float)x, (float)y, (float)z} );
        this.setQuaternion(source, q);        
        
//        this.transformData.calculateQuaternion();
//        notifyTransformDataChanged(source);
    }
    /**
     * @param transformData the transformData to set
     */
    
    
    public void setTransformData(Object source, TransformData transformData) {
        this.transformData = transformData;
        notifyTransformDataChanged(source);
    }
    
    public void addObserver(TransformDataChangeObserver observer){
        observers.add(observer);
    }
    
    public void notifyTransformDataChanged(Object source){
        for(TransformDataChangeObserver obs : observers){
            obs.onTransformDataChanged(source, transformData);
        }
        
    }
    
    public void multBy(Object source, float x, float y, float z){
        Quaternion q= new Quaternion(new float[] { x, y, z});
    }

    public void rotateLocal(Object source, float x, float y, float z){
        Quaternion rotation= new Quaternion(new float[] { x, y, z });
        getTransformData().setQuaternion(
                getTransformData().getQuaternion().mult(rotation));
        getTransformData().setLastRotation(rotation);
        notifyTransformDataChanged(source);
    }
    
    public void rotateWorld(Object source, float x, float y, float z){
        Quaternion rotation= new Quaternion(new float[] { x, y, z });
        getTransformData().setQuaternion(rotation.mult(
                getTransformData().getQuaternion()));
        getTransformData().setLastRotation(rotation);
        notifyTransformDataChanged(source);
    }
    
    public void rotateBy(Quaternion q){
        this.rotateBy(null ,q);
    }
    public void rotateBy(Object source, Quaternion q) {
        
        this.transformData.setQuaternion(this.transformData.getQuaternion().mult(
                q));
        this.transformData.setLastRotation(q);
        notifyTransformDataChanged(source);
    }
    
}
