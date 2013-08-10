/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.awt.Dimension;
import model.RotationModel;
import model.TransformData;
import model.TransformDataChangeObserver;

/**
 *
 * @author nikolamarkovic
 */
public class FirstPersonCameraScene extends Scene{
    
    private SimpleApplication app;
    
    private final JmeCanvasContext context;
    
    public FirstPersonCameraScene(SimpleApplication app, JmeCanvasContext pContext){
        this.context= pContext;
        this.app= app;
        Dimension dim = new Dimension(1024, 768);
        context.getCanvas().setPreferredSize(dim);
    }
    
    public Canvas getCanvas(){
        return context.getCanvas();
    }

    public void onTransformDataChanged(Object source, TransformData data) {
        if(active)
            camera.setRotation(data.getQuaternion());
    }

    @Override
    protected void initCamera() {
        camera= app.getCamera();
        camera.setLocation(new Vector3f(xGeom, yGeom, zGeom));
        camera.lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
        camera.setRotation(RotationModel.getInstance().getTransformData().getQuaternion());
        app.getRenderManager().setCamera(camera, true);
        app.getFlyByCamera().setEnabled(false);
        app.getInputManager().setCursorVisible(true);
        
        
    }

    @Override
    protected void initGeometry() {
        
    }

    @Override
    protected void initNodes() {
        
    }

    @Override
    protected void initInput() {
        
    }

    @Override
    protected void initGUI() {
        
    }

    public void willDisappear(){
        active= false;
    }
    
    @Override
    public void show() {
        
        active= true;
        initCamera();
    }
}
