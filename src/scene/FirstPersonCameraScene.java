/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.system.JmeCanvasContext;
import com.jme3.ui.Picture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Canvas;
import java.awt.Dimension;
import model.RotationModel;
import model.TransformData;
import model.TransformDataChangeObserver;
import mygame.DataManager;

/**
 *
 * @author nikolamarkovic
 */
public class FirstPersonCameraScene extends Scene implements ScreenController{
    
    private SimpleApplication app;
    
    private final JmeCanvasContext context;
    private Nifty nifty;
    private Screen screen;
    
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

    private NiftyJmeDisplay niftyDisplay;
           
    private Picture pictureLeft;
    private Picture pictureLeftPressed;
    private Picture pictureRight;
    private Picture pictureRightPressed;
    
    @Override
    protected void initGUI() {
//        niftyDisplay = new NiftyJmeDisplay(
//            app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
//        /** Create a new NiftyGUI object */
//        
//        Nifty nifty = niftyDisplay.getNifty();
//        /** Read your XML and initialize your custom ScreenController */
//        nifty.fromXml("Interface/gui.xml", "start");
//        
//        // attach the Nifty display to the gui view port as a processor
//        app.getGuiViewPort().addProcessor(niftyDisplay);
        
        float picWidth= 100;
        float picHeight= 100;
        
        
        pictureLeft= new Picture("left");
        pictureLeft.setImage(app.getAssetManager(), "Textures/dm_left_arrow.png", true);
        pictureLeft.setWidth(picWidth);
        pictureLeft.setHeight(picHeight);
        pictureLeft.setPosition(50, 200);
        app.getGuiNode().attachChild(pictureLeft);
        
        pictureLeftPressed= new Picture("leftPressed");
        pictureLeftPressed.setImage(app.getAssetManager(), "Textures/dm_left_arrow_on.png", true);
        pictureLeftPressed.setWidth(picWidth);
        pictureLeftPressed.setHeight(picHeight);
        pictureLeftPressed.setPosition(50, 200);
//        app.getGuiNode().attachChild(pictureLeftPressed);
        
        pictureRight= new Picture("right");
        pictureRight.setImage(app.getAssetManager(), "Textures/dm_right_arrow.png", true);
        pictureRight.setWidth(picWidth);
        pictureRight.setHeight(picHeight);
        pictureRight.setPosition(400, 200);
        app.getGuiNode().attachChild(pictureRight);
        
        pictureRightPressed= new Picture("rightPressed");
        pictureRightPressed.setImage(app.getAssetManager(), "Textures/dm_right_arrow_on.png", true);
        pictureRightPressed.setWidth(picWidth);
        pictureRightPressed.setHeight(picHeight);
        pictureRightPressed.setPosition(400, 200);
//        app.getGuiNode().attachChild(pictureRightPressed);
    }

    public void willDisappear(){
        active= false;
        hideGUI();
    }
    
    @Override
    public void show() {
        
        active= true;
        initCamera();
        initGUI();
    }
    
    private void hideGUI(){
       app.getGuiViewPort().removeProcessor(niftyDisplay);
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty= nifty;
        this.screen= screen;
    }

    public void onStartScreen() {
        
    }

    public void onEndScreen() {
        
    }
    
    private CollisionResult handleCollision() {

        CollisionResults results = new CollisionResults();
       
        Vector2f click2d = new Vector2f(app.getInputManager().getCursorPosition());
        Vector3f click3d = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        app.getGuiNode().collideWith(ray, results);
       
        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            return closest;
        } 
        return null;
    }

    private float xRotation= 0;
    private float yRotation= 0;
    private float zRotation= 0;
    
    private boolean rotating= false;
    
     public void onMouseClick(String name, boolean isPressed, float tpf) { 
        if (name.equals(IM_MOUSE_LEFT_CLICK) && isPressed) {
            Vector2f click2d = new Vector2f(app.getInputManager().getCursorPosition());
            if(isClickedOnImage(pictureLeft, click2d) || isClickedOnImage(pictureLeftPressed, click2d)){
//                rotate(0, rotateFactor, 0);
                xRotation= 0;
                yRotation= rotateFactor;
                zRotation= 0;
                
                
            }else if(isClickedOnImage(pictureRight, click2d) || isClickedOnImage(pictureRightPressed, click2d)){
                xRotation= 0;
                yRotation= -rotateFactor;
                zRotation= 0;
            }
//        Vector3f click3d = app.getCamera().getWorldCoordinates(
//                new Vector2f(click2d.x, click2d.y), 0f).clone();
//        Vector3f dir = app.getCamera().getWorldCoordinates(
//                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
//        Ray ray = new Ray(click3d, dir);
//
//        app.getGuiNode().collideWith(ray, results);
//       
//        if (results.size() > 0) {
//            CollisionResult closest = results.getClosestCollision();
//            return closest;
            rotating= true;
        }else{
            rotating= false;
        }
    }
     
     
    public void update(){
        if(rotating){
            rotate(xRotation, yRotation, zRotation);
        }
    }
    private float rotateFactor= 0.3f;
     
    private void rotate(float x, float y, float z){
        x= (float)Math.toRadians(x);
        y= (float)Math.toRadians(y);
        z= (float)Math.toRadians(z);
//        FirstPersonCameraScene.this.rotateBy((float) Math.toRadians(x),
//                                             (float) Math.toRadians(y),
//                                             (float) Math.toRadians(z));
               
        Quaternion q = new Quaternion();
        q.fromAngleAxis(0, Vector3f.UNIT_Y);
        
        Quaternion newQuat = new Quaternion();
        
        newQuat = newQuat.fromAngleAxis((float)x, Vector3f.UNIT_X);

        newQuat.multLocal(new Quaternion().fromAngleAxis((float)y, Vector3f.UNIT_Y));
        newQuat.multLocal(new Quaternion().fromAngleAxis((float)z, Vector3f.UNIT_Z));

        camera.setRotation(camera.getRotation().mult(newQuat));

        

        RotationModel.getInstance().getTransformData().setQuaterion(camera.getRotation());
        
//        float[] angles = phoneGeometry.getLocalRotation().toAngles(null);
//        if (angles != null && angles.length == 3) {
//            xRotation = angles[0];
//            yRotation = angles[1];
//            zRotation = angles[2];
//        }
//        RotationModel.getInstance().setTransformData(this, FixedCameraScene.this.xRotation, 
//                FixedCameraScene.this.yRotation, 
//                FixedCameraScene.this.zRotation);
//        RotationModel.getInstance().getTransformData().setQuaterion(phoneGeometry.getLocalRotation());
//        lastMouseDragPosition = new Vector2f(pos);
//
//        DataManager.getInstance().send(phoneGeometry.getWorldMatrix());
    }
     
     private boolean isClickedOnImage(Picture pic, Vector2f pos){
         Vector3f picPosition= pic.getLocalTranslation();
         System.out.println("Clicked ("+pos.x+", "+pos.y+") : ("+picPosition.x + ", "+picPosition.y+")");
         return pos.x > picPosition.x && pos.y > picPosition.y && pos.x < picPosition.x+100 &&
                 pos.y < picPosition.y+ 100;
     }
     
     public void onMouseMove(){
         Vector2f click2d = new Vector2f(app.getInputManager().getCursorPosition());
         if(isClickedOnImage(pictureLeft, click2d) || isClickedOnImage(pictureLeftPressed, click2d)){
             app.getGuiNode().attachChild(pictureLeftPressed);
         }else{
             app.getGuiNode().detachChild(pictureLeftPressed);
         }
         
         if(isClickedOnImage(pictureRight, click2d) || isClickedOnImage(pictureRightPressed, click2d)){
             app.getGuiNode().attachChild(pictureRightPressed);
         }else{
             app.getGuiNode().detachChild(pictureRightPressed);
         }
     }
}
