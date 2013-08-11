/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scene;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Torus;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import model.Axis;
import model.RotationModel;
import model.TransformData;
import mygame.DataManager;
import mygame.Main;

/**
 *
 * @author nikolamarkovic
 */
public class FixedCameraScene extends Scene{
    
    private final String NODE_AXES = "NODE_AXES";
    private Node axesNode;
   
    private Geometry phoneGeometry;
    // outter ring geom
    private Geometry xAxisGeom;
    private Geometry yAxisGeom;
    private Geometry zAxisGeom;
    // inner geom
    private Geometry ixAxisGeom;
    private Geometry iyAxisGeom;
    private Geometry izAxisGeom;
    
    private List<Axis> axes;
    
    private JmeCanvasContext context;
    private SimpleApplication app;
    
    
    
    public FixedCameraScene(SimpleApplication app, JmeCanvasContext pContext){
        this.context= pContext;
        this.app= app;
        Dimension dim = new Dimension(1024, 768);
        
        rotateAxis = RotateAxis.UNDEFINED;
        active= true;
        context.getCanvas().setPreferredSize(dim);
    }
    
    public Canvas getCanvas(){
        return context.getCanvas();
    }
    
    private final int AXIS_INNER_X = 1;
    private final int AXIS_INNER_Y = 2;
    private final int AXIS_INNER_Z = 3;
    private final int AXIS_OUTTER_X = -1;
    private final int AXIS_OUTTER_Y = -2;
    private final int AXIS_OUTTER_Z = -3;

    private void createAxes() {
        int circleSamples = 300;
        int radialSamples = 20;
        float innerRadius = 0.01f;
        float outterRadius = 1.5f;

        float innerRadiusTr = 0.08f;
        float outterRadiusTr = 1.54f;

        // inner X
        Torus t1 = new Torus(circleSamples, radialSamples, innerRadius, outterRadius);
        ixAxisGeom = new Geometry("xAxis", t1);
        Material xAxisMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        xAxisMat.setColor("Color", ColorRGBA.Red);
        ixAxisGeom.setMaterial(xAxisMat);
        ixAxisGeom.rotate(0f, (float) Math.toRadians(90.0), 0f);
        addAxis(ixAxisGeom, xAxisMat, AXIS_INNER_X);


        // outer X
        Torus t1TR = new Torus(circleSamples, radialSamples, innerRadiusTr, outterRadiusTr);
        xAxisGeom = new Geometry("xAxisTr", t1TR);
        Material xAxisMatTr = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        xAxisMatTr.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        xAxisMatTr.setColor("Color", ColorRGBA.BlackNoAlpha);
        xAxisMatTr.setTransparent(true);

        xAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        xAxisGeom.setMaterial(xAxisMatTr);
        xAxisGeom.rotate(0f, (float) Math.toRadians(90.0), 0f);
        addAxis(xAxisGeom, xAxisMatTr, AXIS_OUTTER_X);


        // inner Y
        Torus t2 = new Torus(circleSamples, radialSamples, innerRadius, outterRadius);
        iyAxisGeom = new Geometry("yAxis", t2);
        Material yAxisMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        yAxisMat.setColor("Color", ColorRGBA.Blue);
        iyAxisGeom.setMaterial(yAxisMat);
        ixAxisGeom.rotate(0f, 0f, (float) Math.toRadians(90.0));
        addAxis(iyAxisGeom, yAxisMat, AXIS_INNER_Y);

        //outer Y
        Torus t2TR = new Torus(circleSamples, radialSamples, innerRadiusTr, outterRadiusTr);
        yAxisGeom = new Geometry("xAxisTr", t2TR);
        Material yAxisMatTr = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        yAxisMatTr.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        yAxisMatTr.setColor("Color", ColorRGBA.BlackNoAlpha);
        yAxisMatTr.setTransparent(true);

        yAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        yAxisGeom.setMaterial(yAxisMatTr);
        yAxisGeom.rotate(0f, 0f, (float) Math.toRadians(90.0));
        addAxis(yAxisGeom, yAxisMatTr, AXIS_OUTTER_Y);

        // inner Z
        Torus t3 = new Torus(circleSamples, radialSamples, innerRadius, outterRadius);
        izAxisGeom = new Geometry("zAxis", t1);
        Material zAxisMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        zAxisMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        zAxisMat.setColor("Color", ColorRGBA.Green);
        zAxisMat.setTransparent(true);

        izAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        izAxisGeom.setMaterial(zAxisMat);
        izAxisGeom.rotate((float) Math.toRadians(90.0), 0f, 0f);
        addAxis(izAxisGeom, zAxisMat, AXIS_INNER_Z);

        //outer Z
        Torus t3TR = new Torus(circleSamples, radialSamples, innerRadiusTr, outterRadiusTr);
        zAxisGeom = new Geometry("xAxisTr", t3TR);
        Material zAxisMatTr = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        zAxisMatTr.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        zAxisMatTr.setColor("Color", ColorRGBA.BlackNoAlpha);
        zAxisMatTr.setTransparent(true);

        zAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        zAxisGeom.setMaterial(zAxisMatTr);
        zAxisGeom.rotate((float) Math.toRadians(90.0), 0f, 0f);
        addAxis(zAxisGeom, zAxisMatTr, AXIS_OUTTER_Z);


    }

    private void addAxis(Geometry geom, Material mat, int ID) {
        Axis axis = new Axis();
        axis.setGeometry(geom);
        axis.setMaterial(mat);
        axis.setID(ID);
        axesNode.setLocalTranslation(xGeom, yGeom, zGeom);
        axesNode.attachChild(geom);
        getAxes().add(axis);

        for (Axis lAxis : getAxes()) {
            if (axis.getID() == -lAxis.getID()) {
                axis.setHomie(lAxis);
                lAxis.setHomie(axis);
                return;
            }
        }
    }

    private Axis getAxisFromGeometry(Geometry geom) {
        if (geom == null) {
            return null;
        }

        for (Axis lAxis : getAxes()) {
            if (lAxis.getGeometry() == geom) {
                return lAxis;
            }
        }
        return null;
    }

    private Axis getAxisByID(int pID) {

        for (Axis lAxis : getAxes()) {
            if (lAxis.getID() == pID) {
                return lAxis;
            }
        }
        return null;
    }

    public List<Axis> getAxes() {
        if (axes == null) {
            axes = new LinkedList<Axis>();
        }
        return axes;
    }
    private Vector2f startMouseDragPosition = null;
    private Vector2f lastMouseDragPosition = null;
    

    private CollisionResult handleCollision() {

        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Vector2f click2d = new Vector2f(app.getInputManager().getCursorPosition());
        Vector3f click3d = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        // 3. Collect intersections between Ray and Shootables in results list.
        axesNode.collideWith(ray, results);
        // 4. Print the results
//        System.out.println("----- Collisions? " + results.size() + "-----");
        for (int i = 0; i < results.size(); i++) {
            // For each hit, we know distance, impact point, name of geometry.
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint();
            String hit = results.getCollision(i).getGeometry().getName();

        }
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
            // The closest collision point is what was truly hit:
            startMouseDragPosition = click2d;
            lastMouseDragPosition = new Vector2f(click2d);

            CollisionResult closest = results.getClosestCollision();
            return closest;
            // Let's interact - we mark the hit with a red dot.
//          closest.getGeometry().getMaterial().setColor("Color", ColorRGBA.randomColor());
        } else {
            // No hits? Then remove the red mark.
            startMouseDragPosition = null;
            lastMouseDragPosition = null;

        }
        return null;
    }

    protected void initNodes() {
        axesNode = new Node(NODE_AXES);
        app.getRootNode().attachChild(axesNode);
    }
    private double rotateFactorAngle = 0.2;

    protected void initGUI() {}
    
    private int distanceFactor = 20;
    private double rotateFactor = 5.0;

    public void onMouseDrag() {
        Vector2f pos = app.getInputManager().getCursorPosition();

        double dX = initialDragPosition.x - pos.x;
        double dY = initialDragPosition.y - pos.y;

        double dist = dX + dY;

        double rotation = ((int) dist) / distanceFactor * rotateFactor;
        System.out.println("Rotation " + rotation);
        FixedCameraScene.this.rotateBy(axis(FixedCameraScene.RotateAxis.X_AXIS) * -(float) Math.toRadians(rotation),
                axis(FixedCameraScene.RotateAxis.Z_AXIS) * (float) Math.toRadians(rotation),
                axis(FixedCameraScene.RotateAxis.Y_AXIS) * -(float) Math.toRadians(rotation),
                false);

        RotationModel.getInstance().setTransformData(this, FixedCameraScene.this.xRotation, 
                FixedCameraScene.this.yRotation, 
                FixedCameraScene.this.zRotation);
        RotationModel.getInstance().getTransformData().setQuaterion(phoneGeometry.getLocalRotation());
        lastMouseDragPosition = new Vector2f(pos);

        DataManager.getInstance().send(phoneGeometry.getWorldMatrix());
    }
    Material glowing;

    public void onMouseMove() {
        if (glowing != null) {
            glowing.setColor("GlowColor", ColorRGBA.BlackNoAlpha);
            glowing = null;
        }
        CollisionResult result = handleCollision();
        if (result == null) {

            return;
        }
        Geometry geom = result.getGeometry();
        Axis axis = getAxisFromGeometry(geom);

        if (axis.isOutter()) {
            axis = axis.getHomie();
        }

        Material mat = axis.getMaterial();

        mat.setColor("GlowColor", ColorRGBA.Green);
        glowing = mat;
    }
    
    public void onMouseClick(String name, boolean isPressed, float tpf) { 
        if (name.equals(IM_MOUSE_LEFT_CLICK) && isPressed) {
               CollisionResult result = FixedCameraScene.this.handleCollision();
                    if (result == null) {
                        dragging = false;
                        initialDragPosition = null;
                        initialDragRotation = null;
                        return;
                    } else {
                        dragging = true;
                        initialDragPosition = new Vector2f(app.getInputManager().getCursorPosition());
                        initialDragRotation = new Quaternion(phoneGeometry.getLocalRotation());
                        
                    }

                    if (result.getGeometry() == xAxisGeom) {
                        rotateAxis = FixedCameraScene.RotateAxis.X_AXIS;
                    } else if (result.getGeometry() == yAxisGeom) {
                        rotateAxis = FixedCameraScene.RotateAxis.Y_AXIS;
                    } else if (result.getGeometry() == zAxisGeom) {
                        rotateAxis = FixedCameraScene.RotateAxis.Z_AXIS;
                    } else {
                        rotateAxis = FixedCameraScene.RotateAxis.UNDEFINED;
                    }
                } else {
                    dragging = false;
                    rotateAxis = FixedCameraScene.RotateAxis.UNDEFINED;
                }
    }
    
    private Vector2f initialDragPosition;
    private Quaternion initialDragRotation;

    private float xRotation = 0;
    private float yRotation = 0;
    private float zRotation = 0;
    
    protected void initInput() {
        
        
    }
    
    
    public void setRotation(double x, double y, double z) {

        double rX = x;
        double rY = y;
        double rZ = z;

        rotateBy(rX, rY, rZ, true);
        
    }

    public void rotateBy(double x, double y, double z, boolean isBaseRotation) {
        System.out.format("Rotating by %f %f %f\n", x, y, z);
        Quaternion q = new Quaternion();
        q.fromAngleAxis(0, Vector3f.UNIT_Y);
        
        Quaternion newQuat = new Quaternion();
        
        newQuat = newQuat.fromAngleAxis((float)x, Vector3f.UNIT_X);

        newQuat.multLocal(new Quaternion().fromAngleAxis((float)y, Vector3f.UNIT_Y));
        newQuat.multLocal(new Quaternion().fromAngleAxis((float)z, Vector3f.UNIT_Z));

        Quaternion baseRotation = (initialDragRotation != null && !isBaseRotation) ? initialDragRotation
                : new Quaternion().fromAngleAxis(0, Vector3f.UNIT_X);

        phoneGeometry.setLocalRotation(newQuat.mult(baseRotation));

        RotationModel.getInstance().getTransformData().setQuaterion(phoneGeometry.getLocalRotation());
        
        float[] angles = phoneGeometry.getLocalRotation().toAngles(null);
        if (angles != null && angles.length == 3) {
            xRotation = angles[0];
            yRotation = angles[1];
            zRotation = angles[2];
        }
    }
    private RotateAxis rotateAxis;

    
    public void onTransformDataChanged(Object source, final TransformData data) {
        if(!active){
            return;
        }
        app.enqueue(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                
                setRotation(data.getRotationX(), data.getRotationY(), data.getRotationZ());
                return true;
            }
        });
    }
   
    private boolean filtersInitialized= false;

    @Override
    protected void initCamera() {
        this.camera= app.getCamera();
        app.getFlyByCamera().setEnabled(false);
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        app.getInputManager().setCursorVisible(true);
      
      
        app.getViewPort().setClearFlags(true, true, true);
        
        float camDistance = 4.0f;     
        app.getCamera().setLocation(new Vector3f(xGeom+camDistance, yGeom+camDistance, zGeom+camDistance));
        app.getCamera().lookAt(new Vector3f(xGeom, yGeom, zGeom), Vector3f.UNIT_Y);
        
        if(!filtersInitialized){
            FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
            BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
            fpp.addFilter(bloom);
            app.getViewPort().addProcessor(fpp);
            filtersInitialized= true;
        }
    }

    @Override
    protected void initGeometry() {
        
        createPhoneGeometry();
        createAxes();
        
   
        
    }

    private void createPhoneGeometry(){
        Box b = new Box(0.2f, 1.0f, 0.5f);
        phoneGeometry = new Geometry("Box", b);
        phoneGeometry.setLocalTranslation(xGeom, yGeom, zGeom);
        Material mat = new Material(app.getAssetManager(), "Materials/GeomMaterial.j3md");
        mat.setColor("Color", ColorRGBA.White);

        phoneGeometry.setMaterial(mat);
        
        app.getRootNode().attachChild(phoneGeometry);
    }

    @Override
    public void show() {
        active= true;
         initCamera();
    }
    
    enum RotateAxis {

        X_AXIS, Y_AXIS, Z_AXIS, UNDEFINED
    }

    private int axis(RotateAxis axis) {
        if (axis == this.rotateAxis) {
            return 1;
        }
        return 0;
    }
    @Override
    public void willDisappear(){
        active= false;
        axesNode.detachAllChildren();
    }
    
    @Override
    public void didAppear(){
        active= true;
        for (Axis lAxis : getAxes()) {
            axesNode.attachChild(lAxis.getGeometry());
        }
    }
    
    public void left(){
        System.out.println("Left");
    }
}
