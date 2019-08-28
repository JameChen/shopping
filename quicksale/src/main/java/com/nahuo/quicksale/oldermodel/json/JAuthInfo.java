package com.nahuo.quicksale.oldermodel.json;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.nahuo.quicksale.common.Const;

public class JAuthInfo implements Serializable{
    private static final long serialVersionUID = -2459631240654086937L;
    @Expose
    private String CardNum;// 银行卡号
    @Expose
    private String RealName;
    @Expose
    private String Statu;// 身份认证审核状态
    @Expose
    private int StatuID;//身份认证审核状态id
    @Expose
    private String ReviewTime;
    @Expose
    private String FrontPic;
    @Expose
    private String BackPic;
    @Expose
    private String HeadPic;
    @Expose
    private String Message;//驳回信息
    
    private boolean isManualAdd;//手动添加数据
    
    public JAuthInfo(){}
    public static JAuthInfo getNotCommitInstance() {
        JAuthInfo obj = new JAuthInfo();
        obj.setStatu(Const.IDAuthState.NOT_COMMIT);
        obj.setManualAdd(true);
        return obj;
    }

    public String getCardNum() {
        return CardNum;
    }

    public void setCardNum(String cardNum) {
        CardNum = cardNum;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getStatu() {
        if(Statu.equals("未审核")){
            return Const.IDAuthState.CHECKING;
        }
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }

    public String getReviewTime() {
        return ReviewTime;
    }

    public void setReviewTime(String reviewTime) {
        ReviewTime = reviewTime;
    }

    public String getFrontPic() {
        return FrontPic;
    }

    public void setFrontPic(String frontPic) {
        FrontPic = frontPic;
    }

    public String getBackPic() {
        return BackPic;
    }

    public void setBackPic(String backPic) {
        BackPic = backPic;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String headPic) {
        HeadPic = headPic;
    }

    public int getStatuID() {
        return StatuID;
    }

    public void setStatuID(int statuID) {
        StatuID = statuID;
    }
    public boolean isManualAdd() {
        return isManualAdd;
    }
    public void setManualAdd(boolean isManualAdd) {
        this.isManualAdd = isManualAdd;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }




    

}
