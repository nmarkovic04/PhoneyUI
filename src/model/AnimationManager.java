/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.LinkedList;
import java.util.List;
import static model.AnimateFunction.AnimateFunctionType;
/**
 *
 * @author nikolamarkovic
 */
public class AnimationManager {
    private List<AnimateFunction> functions;
  
    
    private static AnimationManager instance= new AnimationManager();
    
    private AnimationManager() { 
        initFunctions();
    }
    
    private void initFunctions(){
        functions= new LinkedList<AnimateFunction>();
        
        AnimateFunction linearFunction= new AnimateFunction(AnimateFunctionType.Linear, new Animator() {

            public float value(float currentTime, float duration) {
                return currentTime/duration;
            }
        });
        functions.add(linearFunction);
        
        // cubic ease out
        AnimateFunction cubicEaseOutFunction= new AnimateFunction(AnimateFunctionType.EaseOut, new Animator() {

            public float value(float currentTime, float duration) {
                currentTime /= duration;
                currentTime--;
                return (currentTime*currentTime*currentTime + 1) ;
            }
        });
        functions.add(cubicEaseOutFunction);
        
        // cubic ease in
        AnimateFunction cubicEaseInFunction= new AnimateFunction(AnimateFunctionType.EaseIn, new Animator() {

            public float value(float currentTime, float duration) {
                currentTime /= duration;
                return currentTime*currentTime*currentTime;
            }
        });
        functions.add(cubicEaseInFunction);
        
        // cubic ease in out
//        AnimateFunction cubicEaseInOutFunction= new AnimateFunction(AnimateFunctionType.EaseInOut, new Animator() {
//
//            public float value(float currentTime, float duration) {
//                
//                currentTime /= duration/2;
//                if (currentTime < 1) return 1/2*currentTime*currentTime*currentTime;
//                currentTime -= 2;
//                return 1/2*(currentTime*currentTime*currentTime + 2);
//            }
//        });
//        functions.add(cubicEaseInOutFunction);
    }
    
    public static AnimationManager getInstance(){
        return instance;
    }
    
    public Animator getAnimator(AnimateFunctionType type){
        for(AnimateFunction function : functions){
            if(type==function.getType()){
                return function.getAnimator();
            }
           
        }
         return null;
    }
    
    public AnimateFunction getAnimateFunctionByName(String name){
        for(AnimateFunction function : functions){
            if(function.getType().getName().equals(name)) {
                return function;
            }
        }
        return null;
    }
    
    public AnimateFunction getAnimateFunction(AnimateFunctionType type){
        System.out.println("Get Animator for "+type.getName());
        for(AnimateFunction function : functions){
            if(function.getType()==type) {
                return function;
            }
        }
        return null;
    }
 
    public int getFunctionCount(){
        return functions.size();
    }
    
    public List<String> getFunctionNames(){
        List<String> names= new LinkedList<String>();
        for(AnimateFunction function : functions){
            names.add(function.getType().getName());
        }
        return names;
    }
}
