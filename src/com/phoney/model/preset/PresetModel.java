/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.model.preset;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.phoney.util.Settings;

/**
 *
 * @author nikolamarkovic
 */
public class PresetModel {
    private static PresetModel instance= new PresetModel();
    private List<Preset> presets;
    private List<ModelChangeObserver> observers;
    
    private PresetModel() { 
        presets= new LinkedList<Preset>();
        observers= new LinkedList<ModelChangeObserver>();
    }
    
    public int getPresetCount(){
        return presets.size();
    }
    
    public Preset get(int index){
        return presets.get(index);
    }
    
    public void addPreset(Preset preset){
        presets.add(preset);
        notifyOnModelChange();
    }
    
    public void removePreset(Preset preset){
        presets.remove(preset);
        notifyOnModelChange();;
    }
    
    public void removePresetAtIndex(int index){
        presets.remove(index);
        notifyOnModelChange();;
    }
    
    public static PresetModel getInstance(){
        return instance;
    }
    
    public void addObserver(ModelChangeObserver observer){
        observers.add(observer);
    }
    
    public void removeObserver(ModelChangeObserver observer){
        observers.remove(observer);
    }
    
    public void notifyOnModelChange(){
        for(ModelChangeObserver observer : observers){
            observer.onModelChange(this);
        }
    }
    
    public void saveToFile(File file){
        ObjectMapper mapper= new ObjectMapper();
        try {
            mapper.writeValue(file, presets);
            
        } catch (IOException ex) {
            Logger.getLogger(PresetModel.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public boolean loadFromFile(File file){
        ObjectMapper mapper= new ObjectMapper();
        try {
            presets= mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Preset.class));
            for(Preset preset : presets){
                preset.getTransformData().calculateQuaternion();
            }
            
            notifyOnModelChange();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PresetModel.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return false;
    }
}
