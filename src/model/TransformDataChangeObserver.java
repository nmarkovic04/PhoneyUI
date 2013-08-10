/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author nikolamarkovic
 */
public interface TransformDataChangeObserver {
    void onTransformDataChanged(Object source, TransformData data);
}
