/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import com.jme3.collision.CollisionResult;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.renderer.Camera;
import model.RotationModel;
import model.TransformDataChangeObserver;
import mygame.Main;

/**
 *
 * @author nikolamarkovic
 */
public abstract class Scene implements TransformDataChangeObserver{
    public static final String IM_MOUSE_LEFT_CLICK = "IM_MOUSE_LEFT_CLICK";
    public static final String IM_MOUSE_MOVE = "IM_MOUSE_MOVE";
    protected Camera camera;
    protected boolean dragging = false;
    protected boolean active= false;
    
    public static float xGeom= 100.45131f;
    public static float yGeom= 4.499937f;
    public static float zGeom= 47.94615f;
 
    public void init(){
        initCamera();
        initNodes();
        initGeometry();
        initInput();
        initGUI();
        
        RotationModel.getInstance().addObserver(this);
    }
    
    public abstract void show();
    public void willAppear(){}
    public void didAppear(){}
    public void willDisappear(){}
    public void didDisappear(){}
    
    public Camera getCamera(){
        return camera;
    }
   
    protected abstract void initCamera();
    protected abstract void initGeometry();
    protected abstract void initNodes();
    protected abstract void initInput();
    protected abstract void initGUI();
    
    public void setDragging(boolean dragging){
        this.dragging= dragging;
    }
    
    public boolean isDragging(){
        return this.dragging;
    }
    
    public void onMouseDrag() { }
    public void onMouseMove() { }
    public void onMouseClick(String name, boolean isPressed, float tpf) { 
      
    }
    
    public void update() { }
}
