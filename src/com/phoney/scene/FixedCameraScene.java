package com.phoney.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Torus;
import com.jme3.system.JmeCanvasContext;
import com.phoney.model.Axis;
import com.phoney.model.RotationModel;
import com.phoney.model.TransformData;
import com.phoney.util.Settings;
import java.awt.Canvas;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author nikolamarkovic
 */
public class FixedCameraScene extends CameraView {

    /* *** CONSTANTS *** */
    private final String NODE_AXES = "NODE_AXES";
    
    private final int AXIS_INNER_X = 1;
    private final int AXIS_INNER_Y = 2;
    private final int AXIS_INNER_Z = 3;
    private final int AXIS_OUTTER_X = -1;
    private final int AXIS_OUTTER_Y = -2;
    private final int AXIS_OUTTER_Z = -3;
    
    private final double cRotateSpeed = 1.0;
    private boolean filtersInitialized = false;
    /* *** GEOMETRY *** */

    private Node axesNode;
    private Spatial phoneSpatial;
    
    // Outter ring geometry is a transparent wrapper around the visible geometry
    // making the hitbox bigger and easier to click on
    
    // outter ring geometry
    private Geometry xAxisGeom;
    private Geometry yAxisGeom;
    private Geometry zAxisGeom;
    
    // inner ring geometry
    private Geometry ixAxisGeom;
    private Geometry iyAxisGeom;
    private Geometry izAxisGeom;
    private List<Axis> axes;
    
    /* *** UI/JME *** */
    private final ColorRGBA cGlowColor= ColorRGBA.Green;
    
    private JmeCanvasContext context;
    private SimpleApplication app;

    /* *** SCENE *** */
    private Vector2f lastMouseDragPosition = null;
    private Material glowing;
    
    public FixedCameraScene(SimpleApplication app, JmeCanvasContext pContext) {
        this.context = pContext;
        this.app = app;
        Dimension dim = new Dimension(Settings.WIDTH, Settings.HEIGHT);

        // no rotate axis at the beginning
        rotateAxis = RotateAxis.UNDEFINED;
        active = true;
        context.getCanvas().setPreferredSize(dim);
    }

    public Canvas getCanvas() {
        return context.getCanvas();
    }
    
    private void createAxes() {
        // create axes geometry rings
        
        int circleSamples = 300;
        int radialSamples = 20;
        float innerRadius = 0.02f;
        float outterRadius = 2.8f;

        float innerRadiusTr = 0.1f;
        float outterRadiusTr = 2.8f;

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

 

    public List<Axis> getAxes() {
        if (axes == null) {
            axes = new LinkedList<Axis>();
        }
        return axes;
    }

    private CollisionResult handleCollision() {

        CollisionResults results = new CollisionResults();
        
        // get screen cursor position
        Vector2f click2d = new Vector2f(app.getInputManager().getCursorPosition());
        Vector3f click3d = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        
        // get ray from the cursor position
        Vector3f dir = app.getCamera().getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        // collect intersections between ray and axes(which are attached to the axesNode)
        axesNode.collideWith(ray, results);
        
        if (results.size() > 0) {
            lastMouseDragPosition = new Vector2f(click2d);

            // return the closest collision
            CollisionResult closest = results.getClosestCollision();
            return closest;
        } else {
            // nothing was hit
            lastMouseDragPosition = null;
        }
        return null;
    }

    @Override
    protected void initNodes() {
        axesNode = new Node(NODE_AXES);
        app.getRootNode().attachChild(axesNode);
    }
    
    @Override
    public void onMouseDrag() {
        Vector2f pos = app.getInputManager().getCursorPosition();

        double lastX= (lastMouseDragPosition!=null)?lastMouseDragPosition.x:initialDragPosition.x;
        double lastY= (lastMouseDragPosition!=null)?lastMouseDragPosition.y:initialDragPosition.y;
        
        double dX = lastX - pos.x;
        double dY = lastY - pos.y;

        double dist = dX + dY;
        
        // we calculate rotation by multiplying 2d distance(cursor distance from the original mouse position) 
        // with the rotation speed
        double rotation = dist * cRotateSpeed;

        // apply the world rotation
        RotationModel.getInstance().rotateWorld(this, axis(FixedCameraScene.RotateAxis.X_AXIS) * -(float) Math.toRadians(rotation),
                axis(FixedCameraScene.RotateAxis.Z_AXIS) * (float) Math.toRadians(rotation),
                axis(FixedCameraScene.RotateAxis.Y_AXIS) * -(float) Math.toRadians(rotation));
        
        
        lastMouseDragPosition = new Vector2f(pos);
    }

    @Override
    public void onMouseMove() {
        // remove any previous glowing material from the axes
        if (glowing != null) {
            glowing.setColor("GlowColor", ColorRGBA.BlackNoAlpha);
            glowing = null;
        }
        
        // get the collision result
        CollisionResult result = handleCollision(); 
        if (result == null) {
            return;
        }
        
        // result isn't null, meaning mouse is over an axis
        
        // get geometry
        Geometry geom = result.getGeometry();
        Axis axis = getAxisFromGeometry(geom);

        // if the axis is outter(transparent), get his homie ( yo and stuff, brah )
        if (axis.isOutter()) {
            axis = axis.getHomie();
        }
        
        // highlight the axis
        Material mat = axis.getMaterial();
        mat.setColor("GlowColor", cGlowColor);
        glowing = mat;
    }

    @Override
    public void onMouseClick(String name, boolean isPressed, float tpf) {
        if (name.equals(IM_MOUSE_LEFT_CLICK) && isPressed) {
            CollisionResult result = FixedCameraScene.this.handleCollision();
            if (result == null) {
                dragging = false;
                initialDragPosition = null;
                
                return;
            } else {
                dragging = true;
                initialDragPosition = new Vector2f(app.getInputManager().getCursorPosition());
                

            }

            if (result.getGeometry() == xAxisGeom) {
                rotateAxis = FixedCameraScene.RotateAxis.X_AXIS;
            } else if (result.getGeometry() == yAxisGeom) {
                rotateAxis = FixedCameraScene.RotateAxis.Y_AXIS;
            } else if (result.getGeometry() == zAxisGeom) {
                rotateAxis = FixedCameraScene.RotateAxis.Z_AXIS;
            } else {
                // should never enter here unless someone discovers a fourth axis in 3D space. Now, I hear
                // you saying what about time? CAN YOU CLICK ON TIME DUMMY? DOES IT HAVE A GEOMETRY? GEEZ...
                rotateAxis = FixedCameraScene.RotateAxis.UNDEFINED;
            }
        } else {
            dragging = false;
            rotateAxis = FixedCameraScene.RotateAxis.UNDEFINED;
        }
    }
    private Vector2f initialDragPosition;
  

    public void setRotation(Quaternion q) {
        Quaternion rotation= q.mult(phoneSpatial.getLocalRotation());

        phoneSpatial.setLocalRotation(rotation);
    }
    
    private RotateAxis rotateAxis;

    public void onTransformDataChanged(final Object source, final TransformData data) {
        // enqueue to handle rotation in the right thread
        app.enqueue(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                phoneSpatial.setLocalRotation(data.getQuaternion());
                return true;
            }
        });
    }
    

    @Override
    protected void initCamera() {
        this.camera = app.getCamera();
        app.getFlyByCamera().setEnabled(false);
        app.getInputManager().setCursorVisible(true);

        app.getViewPort().setClearFlags(true, true, true);

        float camDistance = 4.5f;
        app.getCamera().setLocation(new Vector3f(xGeom + camDistance, yGeom + camDistance, zGeom + camDistance));
        app.getCamera().lookAt(new Vector3f(xGeom, yGeom, zGeom), Vector3f.UNIT_Y);

        if (!filtersInitialized) {
            FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
            BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
            fpp.addFilter(bloom);
            app.getViewPort().addProcessor(fpp);
            filtersInitialized = true;
        }
    }

    @Override
    protected void initGeometry() {
        createPhoneGeometry();
        createAxes();
    }

    private void createPhoneGeometry() {
        phoneSpatial = app.getAssetManager().loadModel("Models/iphone_4s_home_screen.j3o");
        phoneSpatial.setLocalTranslation(xGeom, yGeom, zGeom);
        phoneSpatial.setLocalRotation(new Quaternion(new float[]{0, 0, 0}));
        app.getRootNode().attachChild(phoneSpatial);
    }

    @Override
    protected void showCameraView() {
        initCamera();
    }

    private int axis(RotateAxis axis) {
        if (axis == this.rotateAxis) {
            return 1;
        }
        return 0;
    }


    @Override
    protected void cameraViewWillAppear() {
        app.getRootNode().attachChild(phoneSpatial);
    }

    @Override
    protected void cameraViewDidAppear() {
        active = true;
        for (Axis lAxis : getAxes()) {
            axesNode.attachChild(lAxis.getGeometry());
        }
    }

    @Override
    protected void cameraViewWillDisappear() {
        axesNode.detachAllChildren();
        app.getRootNode().detachChild(phoneSpatial);
    }


    enum RotateAxis {
        X_AXIS, Y_AXIS, Z_AXIS, UNDEFINED
    }   
}
