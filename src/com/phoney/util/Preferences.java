/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikolamarkovic
 */
public final class Preferences implements Serializable{
    
    private static final String KEY_PORT= "Port";
    
    private static final transient String filename= "phoney_config.properties";
    private int port= 28000;
    
    private static transient Preferences instance= new Preferences();
    
    private Preferences() { 
        load();
    }
    
    public synchronized static Preferences getInstance() {
       
        return instance;
    }
    
    public void save(){
        Properties properties= new Properties();
        try {
            properties.put(KEY_PORT, String.valueOf(getPort()));
            properties.store(new FileOutputStream(filename), null);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void load(){
        Properties properties= new Properties();
        try {
            properties.load(new FileInputStream(filename));
            String portValue= (String)properties.get(KEY_PORT);
            if(portValue!=null) {
                setPort(Integer.valueOf(portValue));
            }
        } catch (IOException ex) {
            // file not found
            
            // lets make one
            save();
            
        }
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
        save();
    }
    
    
}
