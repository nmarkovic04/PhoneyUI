/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import com.jme3.renderer.Camera;
import model.RotationModel;
import model.TransformDataChangeObserver;

/**
 *
 * @author nikolamarkovic
 */
public abstract class Scene implements TransformDataChangeObserver{
    
    protected Camera camera;
    
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
    

    
}
