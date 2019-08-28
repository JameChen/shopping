package com.nahuo.quicksale.oldermodel;

public class SizeItemModel {

    private boolean isCheck;
    private SizeModel size;
    private boolean isSystem;

    public SizeItemModel() {

    }

    public SizeItemModel(boolean isCheck, SizeModel size) {
	this.isCheck = isCheck;
	this.size = size;
    }
    
    public boolean isCheck() {
	return isCheck;
    }

    public void setCheck(boolean isCheck) {
	this.isCheck = isCheck;
    }

    public SizeModel getSize() {
	return size;
    }

    public void setSize(SizeModel size) {
	this.size = size;
    }
    
    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof SizeItemModel && this.size != null){
            
            SizeItemModel cm = (SizeItemModel) o;
            if(cm.getSize() == null){
                return false;
            }
            if(this.isSystem == cm.isSystem() && this.size.getName().equals(cm.getSize().getName())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
