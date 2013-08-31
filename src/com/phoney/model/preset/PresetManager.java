package com.phoney.model.preset;

import com.jme3.math.Quaternion;
import com.phoney.model.RotationModel;
import com.phoney.model.TransformData;
import com.phoney.util.Settings;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author nikolamarkovic
 */
public class PresetManager {
    
    private static final PresetManager instance= new PresetManager();
    
    private int currentPresetIndex;
    private Preset currentPreset;
    
    private Timer timer;
    
    private PresetManager() { }
    
    public void play(){
        if(PresetModel.getInstance().getPresetCount()<2){
            return;
        }
         
        currentPresetIndex= 0;
        Settings.sceneRotationEnabled= false;
        scheduleNextAnimation();
        
    }
    
    private void scheduleNextAnimation(){
        timer= new Timer();
        
        timer.scheduleAtFixedRate(new AnimationTask(PresetModel.getInstance().get(currentPresetIndex),
                                                    PresetModel.getInstance().get(currentPresetIndex+1)), 
                                                    0, 
                                                    1000/Settings.animationFPS);
    }
    
    private void playNextAnimation(){
        currentPresetIndex++;
        
        scheduleNextAnimation();
    }
    
    public void resume(){
        if(PresetModel.getInstance().getPresetCount()==0){
            return;
        }
    }
    
    public void pause(){
        if(PresetModel.getInstance().getPresetCount()==0){
            return;
        }
    }
    
    public void stop(){
        if(PresetModel.getInstance().getPresetCount()==0){
            return;
        }
        
        if(timer!=null){
            timer.cancel();
            timer= null;
        }
        
        Settings.sceneRotationEnabled= true;
        currentPresetIndex= 0;
    }
  
    public void animationDidFinish(){
        if(currentPresetIndex==PresetModel.getInstance().getPresetCount()-2){
            if(Settings.loop){
                play();
            }else{
                timer= null;
                currentPresetIndex= 0;
                Settings.sceneRotationEnabled= true;
            }
        }else{
            playNextAnimation();
        }
    }
  
    public static PresetManager getInstance(){
        return instance;
    }

    private class AnimationTask extends TimerTask {
        private Preset sourcePreset;
        private Preset destinationPreset;
        
        private long startTime= -1;
        public AnimationTask(Preset sourcePreset, Preset destinationPreset) {
            this.sourcePreset= sourcePreset;
            this.destinationPreset= destinationPreset;
        }

        Quaternion q1, q2;
        
        @Override
        public void run() {
            if(startTime<0){ 
                startTime= new Date().getTime();
            }
            
            long diff= new Date().getTime()-startTime;
            float duration= destinationPreset.getDuration();
            
            if(diff>duration){
                timer.cancel();
                PresetManager.getInstance().animationDidFinish();
                return;
            }
            
            if(q1==null && q2==null){
                q1= new Quaternion();
                q2= new Quaternion();
            }
            
            q1.set(sourcePreset.getTransformData().getQuaternion());
            q2.set(destinationPreset.getTransformData().getQuaternion());
            
            q1.slerp(q2, destinationPreset.getFunction().value(diff, duration));
            
            RotationModel.getInstance().setTransformData(this, new TransformData(q1));
        }
    }
}
