package com.nahuo.quicksale.oldermodel.json;

import com.nahuo.quicksale.common.Const;

public class JPostFee {
    /**运费类型 @see {@link Const.PostFeeType}*/
    private int PostFeeTypeID;
    /**类型名*/
    private String PostFeeTypeName;
    /**默认邮费*/
    private double DefaultPostFee;
    /**多少起免运费*/
    private double FreePostFeeAmount;
    
    
    public JPostFee(){}
    
    
    
    public JPostFee(int postFeeTypeID) {
        PostFeeTypeID = postFeeTypeID;
    }



    public JPostFee(int postFeeTypeID, double defaultPostFee, double freePostFeeAmount) {
        PostFeeTypeID = postFeeTypeID;
        DefaultPostFee = defaultPostFee;
        FreePostFeeAmount = freePostFeeAmount;
    }



    public int getPostFeeTypeID() {
        return PostFeeTypeID;
    }
    public void setPostFeeTypeID(int postFeeTypeID) {
        PostFeeTypeID = postFeeTypeID;
    }
    public String getPostFeeTypeName() {
        return PostFeeTypeName;
    }
    public void setPostFeeTypeName(String postFeeTypeName) {
        PostFeeTypeName = postFeeTypeName;
    }
    public double getDefaultPostFee() {
        return DefaultPostFee;
    }
    public void setDefaultPostFee(double defaultPostFee) {
        DefaultPostFee = defaultPostFee;
    }
    public double getFreePostFeeAmount() {
        return FreePostFeeAmount;
    }
    public void setFreePostFeeAmount(double freePostFeeAmount) {
        FreePostFeeAmount = freePostFeeAmount;
    }
    
    

}
