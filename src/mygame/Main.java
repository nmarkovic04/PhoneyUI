package mygame;

import GUI.ControlsDelegate;
import GUI.ControlsPanel;
import GUI.SceneDelegate;
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
import com.jme3.math.Matrix3f;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Torus;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Axis;

    
public class Main extends SimpleApplication implements ControlsDelegate{
    private final String IM_MOUSE_LEFT_CLICK= "IM_MOUSE_LEFT_CLICK";
    private final String IM_MOUSE_MOVE= "IM_MOUSE_MOVE";
    private final String NODE_AXES= "NODE_AXES";
    private Node axesNode;
    private SceneDelegate sceneDelegate;
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
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                Main app = new Main();
               
            }
        });
        
    }
    
    public Main(){
        init();
    }
    
    private void init(){
         AppSettings settings= new AppSettings(true);
                settings.setTitle("DummyMotion");
                settings.setResolution(1024, 768);
                
                setSettings(settings);
                createCanvas();
                setDisplayStatView(false);
                setDisplayFps(false);
                setShowSettings(false);
                setPauseOnLostFocus(false);
                
                JmeCanvasContext context= (JmeCanvasContext) getContext();
                Dimension dim= new Dimension(1024, 768);
                context.getCanvas().setPreferredSize(dim);
                
                JFrame frame= new JFrame("DummyMotion");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel panel= new JPanel();
                panel.setLayout(new BorderLayout());
                panel.add(context.getCanvas(), BorderLayout.CENTER);
                panel.add(getControlsPanel(), BorderLayout.SOUTH);
                frame.setContentPane(panel);
                
                frame.pack();
                frame.setVisible(true);
                
                startCanvas();
    }
    
    private ControlsPanel controlsPanel;
  
    private JTextField zAxisField;
    private JPanel getControlsPanel(){
        if(controlsPanel==null){
            controlsPanel= new ControlsPanel();
            controlsPanel.setControlsDelegate(this);
            setSceneDelegate(controlsPanel);
        }
        return controlsPanel;
    }

    @Override
    public void simpleInitApp() {
        rotateAxis= RotateAxis.UNDEFINED;
        
        initNodes();
        initInput();
        initGUI();
        flyCam.setEnabled(false);
       
        inputManager.setCursorVisible(true);
        
        Box b = new Box(0.2f, 1.0f, 0.5f);
        
        createAxes();
        // axes
       
        handleCollision();
        
        phoneGeometry = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Materials/GeomMaterial.j3md");
        mat.setColor("Color", ColorRGBA.White);
        
        phoneGeometry.setMaterial(mat);

        rootNode.attachChild(phoneGeometry);
        float camDistance= 4.0f;
         cam.setLocation(new Vector3f(camDistance, camDistance, camDistance));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        BloomFilter bloom= new BloomFilter(BloomFilter.GlowMode.Objects);        
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private final int AXIS_INNER_X= 1;
    private final int AXIS_INNER_Y= 2;
    private final int AXIS_INNER_Z= 3;
    private final int AXIS_OUTTER_X= -1;
    private final int AXIS_OUTTER_Y= -2;
    private final int AXIS_OUTTER_Z= -3;
    
    
    private void createAxes() {
        int circleSamples= 300;
        int radialSamples= 20;
        float innerRadius= 0.01f;
        float outterRadius= 1.5f;
        
        float innerRadiusTr= 0.08f;
        float outterRadiusTr= 1.54f;
        
        // inner X
        Torus t1= new Torus(circleSamples, radialSamples, innerRadius, outterRadius);
        Geometry ixAxisGeom= new Geometry("xAxis", t1);
        Material xAxisMat= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        xAxisMat.setColor("Color", ColorRGBA.Red);
        ixAxisGeom.setMaterial(xAxisMat);
        ixAxisGeom.rotate(0f, (float)Math.toRadians(90.0), 0f);
        addAxis(ixAxisGeom, xAxisMat, AXIS_INNER_X);
        
        
        // outer X
        Torus t1TR= new Torus(circleSamples, radialSamples, innerRadiusTr, outterRadiusTr);
        xAxisGeom= new Geometry("xAxisTr", t1TR);
        Material xAxisMatTr= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        xAxisMatTr.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        xAxisMatTr.setColor("Color", ColorRGBA.BlackNoAlpha);
        xAxisMatTr.setTransparent(true);
        
        xAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        xAxisGeom.setMaterial(xAxisMatTr);
        xAxisGeom.rotate(0f, (float)Math.toRadians(90.0), 0f);
        addAxis(xAxisGeom, xAxisMatTr, AXIS_OUTTER_X);
        
        
        // inner Y
        Torus t2= new Torus(circleSamples, radialSamples, innerRadius, outterRadius);
        Geometry iyAxisGeom= new Geometry("yAxis", t2);
        Material yAxisMat= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        yAxisMat.setColor("Color", ColorRGBA.Blue);
        iyAxisGeom.setMaterial(yAxisMat);
        ixAxisGeom.rotate(0f, 0f, (float)Math.toRadians(90.0));
        addAxis(iyAxisGeom, yAxisMat, AXIS_INNER_Y);
        
        //outer Y
        Torus t2TR= new Torus(circleSamples, radialSamples, innerRadiusTr, outterRadiusTr);
        yAxisGeom= new Geometry("xAxisTr", t2TR);
        Material yAxisMatTr= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        yAxisMatTr.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        yAxisMatTr.setColor("Color", ColorRGBA.BlackNoAlpha);
        yAxisMatTr.setTransparent(true);
        
        yAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        yAxisGeom.setMaterial(yAxisMatTr);
        yAxisGeom.rotate(0f, 0f, (float)Math.toRadians(90.0));
        addAxis(yAxisGeom, yAxisMatTr, AXIS_OUTTER_Y);
        
        // inner Z
        Torus t3= new Torus(circleSamples, radialSamples, innerRadius, outterRadius);
        Geometry izAxisGeom= new Geometry("zAxis", t1);
        Material zAxisMat= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        zAxisMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        zAxisMat.setColor("Color", ColorRGBA.Green);
        zAxisMat.setTransparent(true);
        
        izAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        izAxisGeom.setMaterial(zAxisMat);
        izAxisGeom.rotate((float)Math.toRadians(90.0), 0f, 0f);
        addAxis(izAxisGeom, zAxisMat, AXIS_INNER_Z);
        
        //outer Z
        Torus t3TR= new Torus(circleSamples, radialSamples, innerRadiusTr, outterRadiusTr);
        zAxisGeom= new Geometry("xAxisTr", t3TR);
        Material zAxisMatTr= new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        zAxisMatTr.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        zAxisMatTr.setColor("Color", ColorRGBA.BlackNoAlpha);
        zAxisMatTr.setTransparent(true);
        
        zAxisGeom.setQueueBucket(RenderQueue.Bucket.Transparent);
        zAxisGeom.setMaterial(zAxisMatTr);
        zAxisGeom.rotate((float)Math.toRadians(90.0), 0f, 0f);
        addAxis(zAxisGeom, zAxisMatTr, AXIS_OUTTER_Z);
        
        
    }
    
    private void addAxis(Geometry geom, Material mat, int ID){
        Axis axis= new Axis();
        axis.setGeometry(geom);
        axis.setMaterial(mat);
        axis.setID(ID);
        axesNode.attachChild(geom);
        getAxes().add(axis);
        
        for(Axis lAxis : getAxes()){
            if(axis.getID()==-lAxis.getID()){
                axis.setHomie(lAxis);
                lAxis.setHomie(axis);
                return;
            }
        }
    }
    
    private Axis getAxisFromGeometry(Geometry geom){
        if(geom==null)
            return null;
        
        for(Axis lAxis : getAxes()){
            if(lAxis.getGeometry()==geom){
                return lAxis;
            }
        }
        return null;
    }

    private Axis getAxisByID(int pID){
       
        for(Axis lAxis : getAxes()){
            if(lAxis.getID()==pID){
                return lAxis;
            }
        }
        return null;
    }
    
    public List<Axis> getAxes(){
        if(axes==null){
            axes= new LinkedList<Axis>();
        }
        return axes;
    }
    
    private Vector2f startMouseDragPosition= null;
    private Vector2f lastMouseDragPosition= null;
    private boolean dragging= false;
    private CollisionResult handleCollision() {
     
        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
        Vector2f click2d = new Vector2f(inputManager.getCursorPosition());
        Vector3f click3d = cam.getWorldCoordinates(
            new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
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
//          System.out.println("* Collision #" + i);
//          System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
        }
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
          // The closest collision point is what was truly hit:
          startMouseDragPosition= click2d;
          lastMouseDragPosition= new Vector2f(click2d);
          
          CollisionResult closest = results.getClosestCollision();
          return closest;
          // Let's interact - we mark the hit with a red dot.
//          closest.getGeometry().getMaterial().setColor("Color", ColorRGBA.randomColor());
        } else {
          // No hits? Then remove the red mark.
          startMouseDragPosition= null;
          lastMouseDragPosition= null;
          
        }
        return null;
      }

    private void initNodes() {
        axesNode= new Node(NODE_AXES);
        rootNode.attachChild(axesNode);
    }

    private double rotateFactorAngle= 0.2;
    
    private void initGUI(){
    }
    
    private void onMouseDrag(){
       Vector2f pos = inputManager.getCursorPosition();
                
                double dX= pos.x-lastMouseDragPosition.x;
                double dY= lastMouseDragPosition.y-pos.y;
                
                double dist;
                if(Math.abs(dX)> Math.abs(dY)){
                    dist= dX;
                }else{
                    
                    dist= dY;
                }
                
                Main.this.rotateBy(axis(RotateAxis.X_AXIS)*-(float)Math.toRadians(dist*rotateFactorAngle), 
                                     axis(RotateAxis.Z_AXIS)*(float)Math.toRadians(dist*rotateFactorAngle),
                                     axis(RotateAxis.Y_AXIS)*-(float)Math.toRadians(dist*rotateFactorAngle));
                
                if(sceneDelegate!=null)
                    sceneDelegate.rotationDidChange(Main.this.xRotation, Main.this.yRotation, Main.this.zRotation);
                
                lastMouseDragPosition= new Vector2f(pos);
                
                DataManager.getInstance().send(phoneGeometry.getWorldMatrix()); 
    }
    
    Material glowing;
    
    private void onMouseMove(){
        if(glowing!=null){    
            glowing.setColor("GlowColor", ColorRGBA.BlackNoAlpha);
            glowing= null;
        }
        CollisionResult result= handleCollision();
        if(result==null){
            
            return;   
        }
        Geometry geom= result.getGeometry();
        Axis axis= getAxisFromGeometry(geom);
        
        if(axis.isOutter()){
            axis= axis.getHomie();
        }
        
        Material mat= axis.getMaterial();
//        mat.setColor("Color", ColorRGBA.Green);
        mat.setColor("GlowColor", ColorRGBA.Green);
        glowing= mat;
    }
    
    private void initInput() {
        inputManager.addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        
        inputManager.addListener(new AnalogListener() {

            public void onAnalog(String name, float value, float tpf) {
                
                if(dragging){  
                   onMouseDrag();
                }else{
                   onMouseMove();
                }
                
                
            }
        }, IM_MOUSE_MOVE);
        
        inputManager.addMapping(IM_MOUSE_LEFT_CLICK, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        inputManager.addListener(new ActionListener() {

            public void onAction(String name, boolean isPressed, float tpf) {
                if(name.equals(IM_MOUSE_LEFT_CLICK) && isPressed){
                    CollisionResult result= Main.this.handleCollision();
                    if(result==null){
                        dragging= false;
                        return;
                    }else{
                        dragging= true;
                    }
                    
                    if(result.getGeometry()==xAxisGeom){
                        rotateAxis= RotateAxis.X_AXIS;
                    }else if(result.getGeometry()==yAxisGeom){
                        rotateAxis= RotateAxis.Y_AXIS;
                    }else if(result.getGeometry()==zAxisGeom){
                        rotateAxis= RotateAxis.Z_AXIS;
                    }else{
                        rotateAxis= RotateAxis.UNDEFINED;
                    }
                }else{
                    dragging= false;
                    rotateAxis= RotateAxis.UNDEFINED;
                }
            }
        }, IM_MOUSE_LEFT_CLICK);
    }
    
    private float xRotation= 0;
    private float yRotation= 0;
    private float zRotation= 0;
    
    public void setRotation(float x, float y, float z){
        
        float rX= (float)Math.toRadians(x)-xRotation;
        float rY= (float)Math.toRadians(y)-yRotation;
        float rZ= (float)Math.toRadians(z)-zRotation;
        
        rotateBy(rX, rY, rZ);
    }
    
    public void rotateBy(float x, float y, float z){
        xRotation+= x;
        yRotation+= y;
        zRotation+= z;
        
        phoneGeometry.setLocalRotation(new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1));
        phoneGeometry.rotate(xRotation, yRotation, zRotation);
    }
    
    private RotateAxis rotateAxis;

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public void didChangeRotation(final float x, final float y, final float z) {
        this.enqueue(new Callable<Boolean>() {

            public Boolean call() throws Exception {
                setRotation(x, y, z);
                return true;
            }
        });
        
    }
    enum RotateAxis{
        X_AXIS, Y_AXIS, Z_AXIS, UNDEFINED
    }
    
    private int axis(RotateAxis axis){
        if(axis==this.rotateAxis){
            return 1;
        }
    return 0;
    }
    
    public void setSceneDelegate(SceneDelegate pSceneDelegate){
        this.sceneDelegate= pSceneDelegate;
    }
    
    public SceneDelegate getSceneDelegate(){
        return sceneDelegate;
    }
    
}

