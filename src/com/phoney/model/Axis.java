package com.phoney.model;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;


/**
 *
 * @author Nikola
 */
public class Axis {
 
    private int ID;
    private Geometry geometry;
    private Material material;
    private Axis homie;
    private boolean outter;
    /**
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * @return the material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @param material the material to set
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(int ID) {
        this.ID = ID;
        
        setOutter(this.ID<0);
    }

    /**
     * @return the homie
     */
    public Axis getHomie() {
        return homie;
    }

    /**
     * @param homie the homie to set
     */
    public void setHomie(Axis homie) {
        this.homie = homie;
    }

    /**
     * @return the outter
     */
    public boolean isOutter() {
        return outter;
    }

    /**
     * @param outter the outter to set
     */
    public void setOutter(boolean outter) {
        this.outter = outter;
    }
    
    
    
}
