package mygame;

import GUI.CameraDelegate;
import GUI.ControlsPanel;
import scene.FirstPersonCameraScene;
import scene.FixedCameraScene;
import GUI.PresetsPanel;
import GUI.TopCameraControlsPanel;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.collision.CollisionResult;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.Callable;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.preset.PresetModel;
import scene.Scene;
import util.Settings;

public class Main extends SimpleApplication implements CameraDelegate{
    private final String IM_MOUSE_LEFT_CLICK = "IM_MOUSE_LEFT_CLICK";
    private final String IM_MOUSE_MOVE = "IM_MOUSE_MOVE";
    
    private FirstPersonCameraScene fpCameraScene;
    private FixedCameraScene fixedCameraScene;
    private Spatial sceneModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Main app = new Main();

            }
        });

    }

    public Main() {
        init();
    }

    private Scene scene;
    
    private void init() {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("DummyMotion");
        settings.setResolution(Settings.WIDTH, Settings.HEIGHT);
        settings.setSamples(1);
        
        setSettings(settings);
        createCanvas();
        setDisplayStatView(false);
        setDisplayFps(false);
        setShowSettings(false);
        setPauseOnLostFocus(false);
        
        fpCameraScene= new FirstPersonCameraScene(this, (JmeCanvasContext)getContext());
        fixedCameraScene= new FixedCameraScene(this, (JmeCanvasContext)getContext());
        
        scene= fixedCameraScene;
        
        JFrame frame = new JFrame("DummyMotion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JPanel bottomPanel= new JPanel();
        
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(getControlsPanel(), BorderLayout.CENTER);
        bottomPanel.add(new PresetsPanel(), BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout());
        panel.add(fpCameraScene.getCanvas(), BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        JPanel topContainer= new JPanel();
        topContainer.setLayout(new BorderLayout());
        topContainer.add(getMenuBar(), BorderLayout.NORTH);
        topContainer.add(getTopCameraControlsPanel(), BorderLayout.CENTER);
        panel.add(topContainer, BorderLayout.NORTH);
        
        frame.setContentPane(panel);

        frame.pack();
        frame.setVisible(true);
        
        
        startCanvas();
    }
    
    private JMenuBar menuBar;
    private TopCameraControlsPanel topCameraControlsPanel;
    
    private TopCameraControlsPanel getTopCameraControlsPanel(){
        if(topCameraControlsPanel==null){
            topCameraControlsPanel= new TopCameraControlsPanel();
            topCameraControlsPanel.setDelegate(this);
        }
        return topCameraControlsPanel;
    }
    
    private JMenuBar getMenuBar(){
        if(menuBar==null){
            menuBar= new JMenuBar();
            JMenu fileMenu= new JMenu("File");
            JMenuItem openItem= new JMenuItem("Open");
            openItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    onOpen();
                }
            });
            
            JMenuItem saveItem= new JMenuItem("Save");
            saveItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    onSave();
                }
            });
            
            JMenuItem exitItem= new JMenuItem("Exit");
            exitItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    onExit();
                }
            });
            
            JMenuItem newItem= new JMenuItem("New");
            newItem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    onNew();
                }
            });
            
            fileMenu.add(newItem);
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.add(exitItem);
            
            menuBar.add(fileMenu);
        }
        return menuBar;
    }
    
    private void onOpen(){
        JFileChooser fc= new JFileChooser();
         FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON file", "json");
        fc.setFileFilter(filter);
        
        if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
            if(Settings.sceneChanged){
                int result= JOptionPane.showConfirmDialog(null, "Unsaved changes will be lost. Would you like to save the current scene?", 
                        "Save", JOptionPane.YES_NO_CANCEL_OPTION);
                
                switch(result){
                    case JOptionPane.YES_OPTION:
                    {
                        onSave();
                        openSceneFromFile(fc.getSelectedFile());
                    }
                    case JOptionPane.NO_OPTION:
                    {
                        openSceneFromFile(fc.getSelectedFile());
                        return;
                    }
                    case JOptionPane.CANCEL_OPTION:
                    {
                        return;
                    }
                }
            }else{
                openSceneFromFile(fc.getSelectedFile());
            }
        }
    }
    
    private void onNew(){
        
    }
    
    private void onSave(){
        if(Settings.currentFile!=null){
            PresetModel.getInstance().saveToFile(Settings.currentFile);
        }else{
            JFileChooser fc= new JFileChooser();
            if(fc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                Settings.currentFile= fc.getSelectedFile();
                PresetModel.getInstance().saveToFile(Settings.currentFile);
            }
        }
    
    }
    
    private void onExit(){
        System.exit(0);
    }
    
    private void openSceneFromFile(File f){
        if(PresetModel.getInstance().loadFromFile(f)){
            Settings.currentFile= f;
        }
    }
    
    private ControlsPanel controlsPanel;
    private JTextField zAxisField;

    private JPanel getControlsPanel() {
        if (controlsPanel == null) {
            controlsPanel = new ControlsPanel();
            
            
        }
        return controlsPanel;
    }

    @Override
    public void simpleInitApp() {
        Settings.WIDTH= fixedCameraScene.getCanvas().getWidth();
        Settings.HEIGHT= fixedCameraScene.getCanvas().getHeight();
        fpCameraScene.init();
        fixedCameraScene.init();
        
        setupModel();
        setupLight();
        setupInput();
        
    }
    
    private void setupInput(){
        // Mouse move
        getInputManager().addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        getInputManager().addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        getInputManager().addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        getInputManager().addMapping(IM_MOUSE_MOVE, new MouseAxisTrigger(MouseInput.AXIS_Y, false));

        getInputManager().addListener(new AnalogListener() {
            public void onAnalog(String name, float value, float tpf) {

                if (scene.isDragging()) {
                    scene.onMouseDrag();
                } else {
                    scene.onMouseMove();
                }


            }
        }, IM_MOUSE_MOVE);
        
        
        // Mouse click
        getInputManager().addMapping(IM_MOUSE_LEFT_CLICK, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
        getInputManager().addListener(new ActionListener() {
            public void onAction(String name, boolean isPressed, float tpf) {
                scene.onMouseClick(name, isPressed, tpf);
            }
        }, IM_MOUSE_LEFT_CLICK);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        if(scene!=null)
            scene.update();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
        
    }

    private void setupModel() {
        assetManager.registerLocator("town.zip", ZipLocator.class);
       sceneModel = assetManager.loadModel("main.scene");
       sceneModel.setLocalScale(2f);
       rootNode.attachChild(sceneModel);
    }

    private void setupLight() {
         AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);
 
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }

    public void didChangeCamera(final boolean firstPersonCamera) {
        enqueue(new Callable<Boolean> () {

            public Boolean call(){
                Scene oldScene= scene;
                scene= (firstPersonCamera)?fpCameraScene:fixedCameraScene;

                oldScene.willDisappear();
                scene.willAppear();

                scene.show();

                oldScene.didDisappear();
                scene.didAppear();
                return Boolean.TRUE;
                }
             });
        
        
    }
  
}
