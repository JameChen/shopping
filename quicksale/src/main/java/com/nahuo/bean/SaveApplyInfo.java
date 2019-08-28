package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jame on 2018/1/2.
 */

public class SaveApplyInfo implements Serializable{

    private static final long serialVersionUID = -7139033011168388895L;
    /**
     * Message : 保存成功，本月该仓库还可以申请7次,商品最迟在01月02日发出
     * ApplyInfo : {"TypeName":"立即发"}
     */
    @Expose
    @SerializedName("Message")
    private String Message="";
    @Expose
    @SerializedName("ApplyInfo")
    private ApplyInfoBean ApplyInfo;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public ApplyInfoBean getApplyInfo() {
        return ApplyInfo;
    }

    public void setApplyInfo(ApplyInfoBean ApplyInfo) {
        this.ApplyInfo = ApplyInfo;
    }

    public static class ApplyInfoBean implements Serializable{
        private static final long serialVersionUID = -6251163695303404054L;
        /**
         * TypeName : 立即发
         */
        @Expose
        @SerializedName("Desc")
        String Desc="";

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        @Expose
        @SerializedName("TypeName")
        private String TypeName="";

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String TypeName) {
            this.TypeName = TypeName;
        }
    }
}
