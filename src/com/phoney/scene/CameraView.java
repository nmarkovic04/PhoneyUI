/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.scene;

import com.jme3.renderer.Camera;
import com.phoney.model.RotationModel;
import com.phoney.model.TransformDataChangeObserver;

/**
 *
 * @author nikolamarkovic
 */
public abstract class CameraView implements TransformDataChangeObserver{
    public static final String IM_MOUSE_LEFT_CLICK = "IM_MOUSE_LEFT_CLICK";
    public static final String IM_MOUSE_MOVE = "IM_MOUSE_MOVE";
    
    protected Camera camera;
    protected boolean dragging = false;
    protected boolean active= false;
    
    // position of the phone spatial 
    
    public static float xGeom= 100.45131f;
    public static float yGeom= 4.499937f;
    public static float zGeom= 47.94615f;
 
    public void init(){
        initCamera();
        initNodes();
        initGeometry();
        initInput();
        RotationModel.getInstance().addObserver(this);
    }
    
    public final void show(){
        showCameraView();
    }
    
    protected abstract void showCameraView();
    
    public final void willAppear(){
        cameraViewWillAppear();
    }
    public final void didAppear(){
        active = true;
        cameraViewDidAppear();
    }
    public final void willDisappear(){
        cameraViewWillDisappear();
    }
    public final void didDisappear(){
        active= false;
        cameraViewDidDisappear();
    }
    
    protected void cameraViewWillAppear(){};
    protected void cameraViewDidAppear(){};
    protected void cameraViewWillDisappear(){};
    protected void cameraViewDidDisappear(){};
    
    public Camera getCamera(){
        return camera;
    }
   
    protected void initCamera(){};
    protected void initGeometry(){};
    protected void initNodes(){};
    protected void initInput(){};
    protected void initGUI(){};
    
    public void setDragging(boolean dragging){
        this.dragging= dragging;
    }
    
    public boolean isDragging(){
        return this.dragging;
    }
    
    public void onMouseDrag() { }
    public void onMouseMove() { }
    public void onMouseClick(String name, boolean isPressed, float tpf) {}
    
    public void update() { }
}
