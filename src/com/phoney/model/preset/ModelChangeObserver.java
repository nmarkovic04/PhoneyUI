/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.phoney.model.preset;

import com.phoney.model.preset.PresetModel;

/**
 *
 * @author nikolamarkovic
 */
public interface ModelChangeObserver {
    void onModelChange(PresetModel model);
}
