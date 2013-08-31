/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.system.JmeCanvasContext;
import com.jme3.ui.Picture;
import com.phoney.model.RotationModel;
import com.phoney.model.TransformData;
import com.phoney.util.Settings;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 *
 * @author nikolamarkovic
 */
public class FirstPersonCameraView extends CameraView {
    
    /* *** SCENE *** */
    private float xRotation= 0;
    private float yRotation= 0;
    private float zRotation= 0;
    
    /* *** UI/JME *** */
    private SimpleApplication app;
    private final JmeCanvasContext context;
    
    public FirstPersonCameraView(SimpleApplication app, JmeCanvasContext pContext){
        this.context= pContext;
        this.app= app;
        Dimension dim = new Dimension(Settings.WIDTH, Settings.HEIGHT);
        context.getCanvas().setPreferredSize(dim);
    }
    
    public Canvas getCanvas(){
        return context.getCanvas();
    }

    public void onTransformDataChanged(Object source, final TransformData data) {
        if(!active) {
            return;
        }
        
        app.enqueue(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                camera.setRotation(baseRotateQuaternion(data.getQuaternion()));
                return true;
            }
        });
           
    }

    @Override
    protected void initCamera() {
        camera= app.getCamera();
        camera.setLocation(new Vector3f(xGeom, yGeom, zGeom));
        camera.setRotation(baseRotateQuaternion(RotationModel.getInstance().getTransformData().getQuaternion()));
        app.getRenderManager().setCamera(camera, true);
        app.getFlyByCamera().setEnabled(false);
        app.getInputManager().setCursorVisible(true);
    }
    
    // Couldn't bother with Nifty GUI so I made pictures with click listeners
    // for rotation GUI
   
    @Override
    protected void initGUI() {
        
        float picWidth= 100;
        float picHeight= 100;
        float sidePicWidth= 50;
        float sidePicHeight= 100;
        int padding= 20;
        
        int x, y;
        
        // position the GUI "buttons" 
        x= padding; 
        y= (int)(Settings.HEIGHT/2-picHeight/2);
        addPicture("left", "Textures/left.png", picWidth, picHeight, x, y, true);
        addPicture("leftPressed", "Textures/left_pressed.png", picWidth, picHeight, x, y, false);
        
        x= (int)(Settings.WIDTH-padding-picWidth);
        addPicture("right", "Textures/right.png", picWidth, picHeight, x, y, true);
        addPicture("rightPressed", "Textures/right_pressed.png", picWidth, picHeight, x, y, false);

        x= (int)(Settings.WIDTH/2-picWidth/2);
        y= (int)(Settings.HEIGHT-padding - picHeight);
        addPicture("up", "Textures/up.png", picWidth, picHeight, x, y, true);
        addPicture("upPressed", "Textures/up_pressed.png", picWidth, picHeight, x, y, false);
        
        y= (int)padding;
        addPicture("down", "Textures/down.png", picWidth, picHeight, x, y, true);
        addPicture("downPressed", "Textures/down_pressed.png", picWidth, picHeight, x, y, false);
        
        x= (int)(Settings.WIDTH/2-5*padding);
        y= (int)(Settings.HEIGHT/2-picHeight/2);
        addPicture("sideLeft", "Textures/arc_left.png", sidePicWidth, sidePicHeight, x, y, true);
        addPicture("sideLeftPressed", "Textures/arc_left_pressed.png", sidePicWidth, sidePicHeight, x, y, false);
        
        x= (int)(Settings.WIDTH/2+(Settings.WIDTH/2-(x+sidePicWidth)));
        
        addPicture("sideRight", "Textures/arc_right.png", sidePicWidth, sidePicHeight, x, y, true);
        addPicture("sideRightPressed", "Textures/arc_right_pressed.png", sidePicWidth, sidePicHeight, x, y, false);
        
    }

    private Map<String, Picture> pictures;
    
    private void addPicture(String name, String imageName, float w, float h, int x, int y, boolean addToScene){
        if(pictures==null){
            pictures= new HashMap<String, Picture>();
        }
        Picture pic;
        if((pic=pictures.get(name))==null){
            pic= new Picture(name);
            pic.setImage(app.getAssetManager(), imageName, true);
            pic.setWidth(w);
            pic.setHeight(h);
            pic.setPosition(x,y);
            pictures.put(name, pic);
        }
        if(addToScene){
            app.getGuiNode().attachChild(pic);
        }    
    }
    
    @Override
    protected void cameraViewDidAppear(){
        camera.setRotation(baseRotateQuaternion(RotationModel.getInstance().getTransformData().getQuaternion()));
    }
    
    @Override
    protected void cameraViewWillDisappear(){
        hideGUI();
    }
    
    protected void showCameraView(){
        initCamera();
        initGUI();
    }

    private void hideGUI(){
       app.getGuiNode().detachAllChildren();
    }
    
    private boolean rotating= false;
    
    @Override
     public void onMouseClick(String name, boolean isPressed, float tpf) { 
        if (name.equals(IM_MOUSE_LEFT_CLICK) && isPressed) {
            Vector2f click2d = new Vector2f(app.getInputManager().getCursorPosition());
            // applying rotation 
            if(isClickedOnImage(picture("left"), click2d) || isClickedOnImage(picture("leftPressed"), click2d)){
                xRotation= 0;
                yRotation= rotateFactor;
                zRotation= 0;
            }else if(isClickedOnImage(picture("right"), click2d) || isClickedOnImage(picture("rightPressed"), click2d)){
                xRotation= 0;
                yRotation= -rotateFactor;
                zRotation= 0;
            }else if(isClickedOnImage(picture("down"), click2d) || isClickedOnImage(picture("downPressed"), click2d)){
                xRotation= -rotateFactor;
                yRotation= 0;
                zRotation= 0;
            }else if(isClickedOnImage(picture("up"), click2d) || isClickedOnImage(picture("upPressed"), click2d)){
                xRotation= rotateFactor;
                yRotation= 0;
                zRotation= 0;
            }else if(isClickedOnImage(picture("sideLeft"), click2d) || isClickedOnImage(picture("sideLeftPressed"), click2d)){
                xRotation= 0;
                yRotation= 0;
                zRotation= rotateFactor;
            }else if(isClickedOnImage(picture("sideRight"), click2d) || isClickedOnImage(picture("sideRightPressed"), click2d)){
                xRotation= 0;
                yRotation= 0;
                zRotation= -rotateFactor;
            }else{
                // should never enter here
                xRotation= 0;
                yRotation= 0;
                zRotation= 0;
            }
            rotating= true;
        }else{
            rotating= false;
        }
    }
     
     
    @Override
    public void update(){
        if(rotating){
            rotate(xRotation, yRotation, zRotation);
        }
    }
    private float rotateFactor= 0.55f;
     
    private void rotate(float x, float y, float z){
        x= (float)Math.toRadians(x);
        y= (float)Math.toRadians(y);
        z= (float)Math.toRadians(z);
        
        RotationModel.getInstance().rotateLocal(this, x, y, z);
    }
     
     private boolean isClickedOnImage(Picture pic, Vector2f pos){
         Vector3f picPosition= pic.getLocalTranslation();
         return pos.x > picPosition.x && pos.y > picPosition.y && pos.x < picPosition.x+100 &&
                 pos.y < picPosition.y+ 100;
     }
     
    @Override
     public void onMouseMove(){
         int mouseOverCursor= Cursor.HAND_CURSOR;
         int mouseOutCursor= Cursor.DEFAULT_CURSOR;
         boolean mouseOver= false;
         
         Vector2f click2d = new Vector2f(app.getInputManager().getCursorPosition());
         if(isClickedOnImage(picture("left"), click2d) || isClickedOnImage(picture("leftPressed"), click2d)){
             app.getGuiNode().attachChild(picture("leftPressed"));
             mouseOver= true;
         }else{
             app.getGuiNode().detachChild(picture("leftPressed"));
         }
         
         if(isClickedOnImage(picture("right"), click2d) || isClickedOnImage(picture("rightPressed"), click2d)){
             app.getGuiNode().attachChild(picture("rightPressed"));
             mouseOver= true;
         }else{
             app.getGuiNode().detachChild(picture("rightPressed"));
         }
         
         if(isClickedOnImage(picture("up"), click2d) || isClickedOnImage(picture("upPressed"), click2d)){
             app.getGuiNode().attachChild(picture("upPressed"));
             mouseOver= true;
         }else{
             app.getGuiNode().detachChild(picture("upPressed"));
         }
         
         if(isClickedOnImage(picture("down"), click2d) || isClickedOnImage(picture("downPressed"), click2d)){
             app.getGuiNode().attachChild(picture("downPressed"));
             mouseOver= true;
         }else{
             app.getGuiNode().detachChild(picture("downPressed"));
         }
         
         if(isClickedOnImage(picture("sideLeft"), click2d) || isClickedOnImage(picture("sideLeftPressed"), click2d)){
             app.getGuiNode().attachChild(picture("sideLeftPressed"));
             mouseOver= true;
         }else{
             app.getGuiNode().detachChild(picture("sideLeftPressed"));
         }
         
         if(isClickedOnImage(picture("sideRight"), click2d) || isClickedOnImage(picture("sideRightPressed"), click2d)){
             app.getGuiNode().attachChild(picture("sideRightPressed"));
             mouseOver= true;
         }else{
             app.getGuiNode().detachChild(picture("sideRightPressed"));
         }
         
         int cursor;
         if(mouseOver){
             cursor= mouseOverCursor;
         }else{
             cursor= mouseOutCursor;
         }
         
         getCanvas().setCursor(Cursor.getPredefinedCursor(cursor));
     }
     
     public Picture picture(String name){
         return pictures.get(name);
     }

     // The camera is located on the back side of the phone meaning it has
     // a base rotation if PI compared to the phone spatial
     private Quaternion baseRotateQuaternion(Quaternion q){
         return q.mult(new Quaternion(new float[] { 0f, (float)Math.PI, 0f }));
     }
}
