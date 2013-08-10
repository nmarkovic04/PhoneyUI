/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import model.AnimateFunction;
import model.AnimationManager;
import model.preset.Preset;
import model.preset.PresetModel;

/**
 *
 * @author nikolamarkovic
 */
public class PresetsTableModel extends DefaultTableModel{

    private PresetModel model= PresetModel.getInstance();
    
    @Override
    public int getRowCount() {
        return PresetModel.getInstance().getPresetCount();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Preset preset= model.get(rowIndex);
        switch(columnIndex){
            case 0:
                return String.valueOf(rowIndex+1);
            case 1:
                return String.valueOf(Math.toDegrees(preset.getTransformData().getRotationX()));
            case 2:
                return String.valueOf(Math.toDegrees(preset.getTransformData().getRotationY()));
            case 3:
                return String.valueOf(Math.toDegrees(preset.getTransformData().getRotationZ()));
            case 4:
                return String.valueOf(preset.getDuration()/1000.0f);
            case 5:
                return String.valueOf(preset.getFunction().getType());
        
        }
        
        return "";
    }
    
    public void setValueAt(Object aValue, int row, int column) {
        Preset preset= model.get(row);
        switch(column){
            case 1:{
                preset.getTransformData().setRotationX(Math.toRadians(Double.valueOf(aValue.toString())));
                break;
            }
            case 2:{
                preset.getTransformData().setRotationY(Math.toRadians(Double.valueOf(aValue.toString())));
                break;
            }
            case 3:{
                preset.getTransformData().setRotationZ(Math.toRadians(Double.valueOf(aValue.toString())));
                break;
            }
            case 4:{
                String val= (String)aValue;
                float dur= Float.valueOf(val);
                preset.setDuration(dur*1000);
                break;
            }
            case 5:{
                AnimateFunction function= AnimationManager.getInstance().getAnimateFunctionByName(aValue.toString());
                preset.setFunction(function);
                break;
            }
        }
    }
}
