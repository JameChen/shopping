package com.nahuo.live.xiaozhibo.im;

/**
 * 消息体类
 */
public class TCChatEntity {
	
	private String grpSendName;
	private String context;
	private int  type;
	private String receiveId="";

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public TCChatEntity() {
		// TODO Auto-generated constructor stub
	}
	


	public String getSenderName() {
		return grpSendName != null ? grpSendName : "";
	}

	public void setSenderName(String grpSendName) {
		this.grpSendName = grpSendName;
	}
		


	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TCChatEntity)) return false;

		TCChatEntity that = (TCChatEntity) o;

		if (getType() != that.getType()) return false;
		if (grpSendName != null ? !grpSendName.equals(that.grpSendName) : that.grpSendName != null)
			return false;
		return getContext() != null ? getContext().equals(that.getContext()) : that.getContext() == null;

	}

	@Override
	public int hashCode() {
		int result = grpSendName != null ? grpSendName.hashCode() : 0;
		result = 31 * result + (getContext() != null ? getContext().hashCode() : 0);
		result = 31 * result + getType();
		return result;
	}
}
