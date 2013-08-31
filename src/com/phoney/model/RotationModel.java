/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.model;

import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.phoney.data.DataManager;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author nikolamarkovic
 */
public class RotationModel {
    private static RotationModel instance= new RotationModel();
    private List<TransformDataChangeObserver> observers;
    
    private TransformData transformData;
    
    private RotationModel() { 
        transformData= new TransformData();
        observers= new LinkedList<TransformDataChangeObserver>();
    }
    
    public static RotationModel getInstance(){
        return instance;
    }

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
   
        Quaternion q= new Quaternion(new float[] { (float)x, (float)y, (float)z} );
        this.setQuaternion(source, q);        
    }
   
    public void setTransformData(Object source, TransformData transformData) {
        this.transformData = transformData;
        notifyTransformDataChanged(source);
    }
    
    public void addObserver(TransformDataChangeObserver observer){
        observers.add(observer);
    }
    
    public void notifyTransformDataChanged(Object source){
        Matrix4f m4= new Matrix4f();
        
        // before notifying the data changed, send the data
        DataManager.getInstance().send(transformData.getQuaternion().toRotationMatrix(m4));
        for(TransformDataChangeObserver obs : observers){
            obs.onTransformDataChanged(source, transformData);
        }
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
    
    public void resetToDefaultOrientation(){
        Quaternion rotation= new Quaternion(new float[] { 0, 0, 0 } );
        getTransformData().setQuaternion(rotation);
        getTransformData().setLastRotation(null);
        notifyTransformDataChanged(null);
    }
}
