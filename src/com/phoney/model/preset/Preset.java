/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.model.preset;

import com.phoney.animation.AnimateFunction;
import com.phoney.animation.AnimateFunction.AnimateFunctionType;
import com.phoney.animation.AnimationManager;
import com.phoney.model.TransformData;
/**
 *
 * @author nikolamarkovic
 */
public class Preset {
    
    private TransformData transformData;
    private float duration;
    private AnimateFunction function;
    
    public Preset(){
        function= Preset.defaultFunction();
    }
    
    public Preset(TransformData pTransformData, float pDuration, AnimateFunction pFunction){
        this.transformData= new TransformData(pTransformData);
        this.duration= pDuration;
        this.function= pFunction;
    }

    public Preset(TransformData pTransformData, float pDuration){
        this(pTransformData, pDuration, Preset.defaultFunction());
    }
    
    public Preset(TransformData pTransformData){
        this(pTransformData, Preset.defaultDuration());
    }

    /**
     * @return the duration
     */
    public float getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * @return the function
     */
    public AnimateFunction getFunction() {
        return function;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(AnimateFunction function) {
        this.function = function;
    }
    
    public static AnimateFunction defaultFunction(){
        return AnimationManager.getInstance().getAnimateFunction(AnimateFunctionType.Linear);
    }
    
    public static float defaultDuration(){
        return 2000.0f;
    }

    /**
     * @return the transformData
     */
    public TransformData getTransformData() {
        return transformData;
    }

    /**
     * @param transformData the transformData to set
     */
    public void setTransformData(TransformData transformData) {
        this.transformData = transformData;
    }
}
