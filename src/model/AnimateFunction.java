/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author nikolamarkovic
 */
public class AnimateFunction {
      
    public enum AnimateFunctionType{
        
        Linear("Linear"), EaseOut("EaseOut"), EaseIn("EaseIn"), EaseInOut("EaseInOut");
        
        private String name;
        AnimateFunctionType(String name){
            this.name= name;
        }
        
        public String getName(){
            return this.name;
        }
        
        @JsonIgnore static AnimateFunctionType getTypeNamed(String name){
            if(name.equals("Linear")){
                return AnimateFunctionType.Linear;
            }else if(name.equals("EaseOut")){
                return AnimateFunctionType.EaseIn;
            }else if(name.equals("EaseIn")){
                return AnimateFunctionType.EaseOut;
            }else if(name.equals("EaseInOut")){
                return EaseInOut;
            }
            
            return null;
        }
    }
    private AnimateFunctionType type;
    @JsonIgnore private Animator animator;
    
    public AnimateFunction() { 
    }
    
    public AnimateFunction(AnimateFunctionType type, Animator animator){
        this.type= type;
        this.animator= animator;
    }
    
    public float value(float currentTime, float duration){
        return animator.value(currentTime, duration);
    }

    /**
     * @return the type
     */
    public AnimateFunctionType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(AnimateFunctionType type) {
        this.type = type;//AnimationManager.AnimateFunctionType.getTypeNamed(type.getName());
//        setAnimator(AnimationManager.getInstance().getAnimator(this.type));
    }
    
    public Animator getAnimator(){
        return animator;
    }
    
    public void setAnimator(Animator animator){
        this.animator= animator;
    }
   
    
}
