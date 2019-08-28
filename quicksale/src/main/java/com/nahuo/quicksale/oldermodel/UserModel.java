package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class UserModel implements Serializable {

    private static final long serialVersionUID = -2492773240079820333L;

    
    @Expose
    private int UserID;// 用户ID（酷有ID）
    @Expose
    private String UserName;// 用户名
    @Expose
    private String Email;// 邮箱
    @Expose
    private String Mobile;// 电话号码
    @Expose
    private String QQ;//qq
    @Expose
    private String Signature = "";//签名
    @Expose
    private String Statu;//账号状态
	@Expose
	private int StatuID;//账号状态
    @Expose
    private int AllItemCount = 0;//所有商品数量
    @Expose
    private int AllAgentCount = 0;//所有代理数量
    @Expose
    private int AllVendorCount = 0;//所有代理数量
    @Expose
    private boolean HasSecurityQst;//是否设置了安全问题
    @Expose
    private boolean IsBindBank;//是否绑定银行
    @Expose
    private boolean HasPaymentPassword;//是否设置支付密码
    @Expose
    private double ItemRaise;//零售加价率
    @Expose
    private boolean IsRetailPriceEnabled;//零售价是否可修改
	@Expose
	private String NickName;//;新的用户名
    
    private String logo;//用户logo

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public boolean isIsRetailPriceEnabled() {
        return IsRetailPriceEnabled;
    }
    public void setIsRetailPriceEnabled(boolean isRetailPriceEnabled) {
    	IsRetailPriceEnabled = isRetailPriceEnabled;
    }
	public double getItemRaise() {
        return ItemRaise;
    }
    public void setItemRaise(double itemRaise) {
    	ItemRaise = itemRaise;
    }
    
	public boolean isHasSecurityQst() {
        return HasSecurityQst;
    }
    public void setHasSecurityQst(boolean hasSecurityQst) {
        HasSecurityQst = hasSecurityQst;
    }
    public boolean isIsBindBank() {
        return IsBindBank;
    }
    public void setIsBindBank(boolean isBindBank) {
        IsBindBank = isBindBank;
    }
    public boolean isHasPaymentPassword() {
        return HasPaymentPassword;
    }
    public void setHasPaymentPassword(boolean hasPaymentPassword) {
        HasPaymentPassword = hasPaymentPassword;
    }
    public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getStatu() {
		return Statu;
	}
	public void setStatu(String statu) {
		Statu = statu;
	}
	public int getAllItemCount() {
		return AllItemCount;
	}
	public void setAllItemCount(int allItemCount) {
		AllItemCount = allItemCount;
	}
	public int getAllAgentCount() {
		return AllAgentCount;
	}
	public void setAllAgentCount(int allAgentCount) {
		AllAgentCount = allAgentCount;
	}
	public int getAllVendorCount() {
		return AllVendorCount;
	}
	public void setAllVendorCount(int allVendorCount) {
		AllVendorCount = allVendorCount;
	}


	public int getStatuID() {
		return StatuID;
	}

	public void setStatuID(int statuID) {
		StatuID = statuID;
	}

}
