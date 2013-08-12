/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

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
        getTransformData().setQuaterion(q);
        notifyTransformDataChanged(source);
    }
    public void setTransformData(Object source, float x, float y, float z){
        System.out.println("Rotation changed to "+x+" "+y+" "+z);
        this.transformData.setRotationX(x);
        this.transformData.setRotationY(y);
        this.transformData.setRotationZ(z);
        this.transformData.calculateQuaternion();
        notifyTransformDataChanged(source);
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
            if(obs!=source){
                obs.onTransformDataChanged(source, transformData);
            }
        }
        
    }
    
}
