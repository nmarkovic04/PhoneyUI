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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import model.RotationModel;
import model.TransformData;
import model.TransformDataChangeObserver;
import mygame.DataManager;
import util.Settings;

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

    public void onTransformDataChanged(Object source, final TransformData data) {
        System.out.println("Angles "+data.getRotationX()+", "+data.getRotationY()+", "+data.getRotationZ());
        System.out.println("Quaternion "+data.getQuaternion().getX()+" "+data.getQuaternion().getY()+" "
                            +data.getQuaternion().getZ()+" "+data.getQuaternion().getW());
        if(!active)
            return;
        app.enqueue(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                System.out.println("RotateEnqueue ");
                camera.setRotation(data.getQuaternion());
                RotationModel.getInstance().getTransformData().setQuaterion(camera.getRotation());
                return true;
            }
        });
           
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
    
    private Picture pictureUp;
    private Picture pictureUpPressed;
    private Picture pictureDown;
    private Picture pictureDownPressed;
    
    private Picture pictureSideLeft;
    private Picture pictureSideLeftPressed;
    private Picture pictureSideRight;
    private Picture pictureSideRightPressed;
    
    
    @Override
    protected void initGUI() {
        
        float picWidth= 100;
        float picHeight= 100;
        float sidePicWidth= 50;
        float sidePicHeight= 100;
        int padding= 20;
        
        int x= 0, y=0;
        
        x= padding; 
        y= (int)(Settings.HEIGHT/2-picHeight/2);
        addPicture("left", "Textures/dm_left_arrow.png", picWidth, picHeight, x, y, true);
        addPicture("leftPressed", "Textures/dm_left_arrow_on.png", picWidth, picHeight, x, y, false);
        
        x= (int)(Settings.WIDTH-padding-picWidth);
        addPicture("right", "Textures/dm_right_arrow.png", picWidth, picHeight, x, y, true);
        addPicture("rightPressed", "Textures/dm_right_arrow_on.png", picWidth, picHeight, x, y, false);

        x= (int)(Settings.WIDTH/2-picWidth/2);
        y= (int)(Settings.HEIGHT-padding - picHeight);
        addPicture("up", "Textures/dm_up_arrow.png", picWidth, picHeight, x, y, true);
        addPicture("upPressed", "Textures/dm_up_arrow_on.png", picWidth, picHeight, x, y, false);
        
        y= (int)padding;
        addPicture("down", "Textures/dm_down_arrow.png", picWidth, picHeight, x, y, true);
        addPicture("downPressed", "Textures/dm_down_arrow_on.png", picWidth, picHeight, x, y, false);
        
        x= (int)(Settings.WIDTH/2-5*padding);
        y= (int)(Settings.HEIGHT/2-picHeight/2);
        addPicture("sideLeft", "Textures/dm_side_left_arrow.png", sidePicWidth, sidePicHeight, x, y, true);
        addPicture("sideLeftPressed", "Textures/dm_side_left_arrow_on.png", sidePicWidth, sidePicHeight, x, y, false);
        
        x= (int)(Settings.WIDTH/2+(Settings.WIDTH/2-(x+sidePicWidth)));
        
        addPicture("sideRight", "Textures/dm_side_right_arrow.png", sidePicWidth, sidePicHeight, x, y, true);
        addPicture("sideRightPressed", "Textures/dm_side_right_arrow_on.png", sidePicWidth, sidePicHeight, x, y, false);
        
        
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
       app.getGuiNode().detachAllChildren();
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
            if(isClickedOnImage(picture("left"), click2d) || isClickedOnImage(picture("leftPressed"), click2d)){
                xRotation= 0;
                yRotation= rotateFactor;
                zRotation= 0;
                
            }else if(isClickedOnImage(picture("right"), click2d) || isClickedOnImage(picture("rightPressed"), click2d)){
                xRotation= 0;
                yRotation= -rotateFactor;
                zRotation= 0;
            }else if(isClickedOnImage(picture("down"), click2d) || isClickedOnImage(picture("downPressed"), click2d)){
                xRotation= rotateFactor;
                yRotation= 0;
                zRotation= 0;
            }else if(isClickedOnImage(picture("up"), click2d) || isClickedOnImage(picture("upPressed"), click2d)){
                xRotation= -rotateFactor;
                yRotation= 0;
                zRotation= 0;
            }else if(isClickedOnImage(picture("sideLeft"), click2d) || isClickedOnImage(picture("sideLeftPressed"), click2d)){
                xRotation= 0;
                yRotation= 0;
                zRotation= -rotateFactor;
            }else if(isClickedOnImage(picture("sideRight"), click2d) || isClickedOnImage(picture("sideRightPressed"), click2d)){
                xRotation= 0;
                yRotation= 0;
                zRotation= rotateFactor;
            }else{
                xRotation= 0;
                yRotation= 0;
                zRotation= 0;
            }
            
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
    private float rotateFactor= 0.55f;
     
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

       
        RotationModel.getInstance().setQuaternion(this, camera.getRotation());
        
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
         if(isClickedOnImage(picture("left"), click2d) || isClickedOnImage(picture("leftPressed"), click2d)){
             app.getGuiNode().attachChild(picture("leftPressed"));
         }else{
             app.getGuiNode().detachChild(picture("leftPressed"));
         }
         
         if(isClickedOnImage(picture("right"), click2d) || isClickedOnImage(picture("rightPressed"), click2d)){
             app.getGuiNode().attachChild(picture("rightPressed"));
         }else{
             app.getGuiNode().detachChild(picture("rightPressed"));
         }
         
         if(isClickedOnImage(picture("up"), click2d) || isClickedOnImage(picture("upPressed"), click2d)){
             app.getGuiNode().attachChild(picture("upPressed"));
         }else{
             app.getGuiNode().detachChild(picture("upPressed"));
         }
         
         if(isClickedOnImage(picture("down"), click2d) || isClickedOnImage(picture("downPressed"), click2d)){
             app.getGuiNode().attachChild(picture("downPressed"));
         }else{
             app.getGuiNode().detachChild(picture("downPressed"));
         }
         
         if(isClickedOnImage(picture("sideLeft"), click2d) || isClickedOnImage(picture("sideLeftPressed"), click2d)){
             app.getGuiNode().attachChild(picture("sideLeftPressed"));
         }else{
             app.getGuiNode().detachChild(picture("sideLeftPressed"));
         }
         
         if(isClickedOnImage(picture("sideRight"), click2d) || isClickedOnImage(picture("sideRightPressed"), click2d)){
             app.getGuiNode().attachChild(picture("sideRightPressed"));
         }else{
             app.getGuiNode().detachChild(picture("sideRightPressed"));
         }
     }
     
     public Picture picture(String name){
         return pictures.get(name);
     }
}
