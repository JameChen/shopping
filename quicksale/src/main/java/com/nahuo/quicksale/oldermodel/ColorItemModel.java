package com.nahuo.quicksale.oldermodel;

public class ColorItemModel {

    private boolean isCheck;
    private ColorModel color;
    private boolean isSystem;
    public ColorItemModel() {

    }

    public ColorItemModel(boolean isCheck, ColorModel color) {
	this.isCheck = isCheck;
	this.color = color;
    }

    public boolean isCheck() {
	return isCheck;
    }

    public void setCheck(boolean isCheck) {
	this.isCheck = isCheck;
    }

    public ColorModel getColor() {
	return color;
    }

    public void setColor(ColorModel color) {
	this.color = color;
    }
    
    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof SizeItemModel && this.color != null){
            
            ColorItemModel cm = (ColorItemModel) o;
            if(cm.getColor() == null){
                return false;
            }
            if(this.isSystem == cm.isSystem() && this.color.getName().equals(cm.getColor().getName())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
