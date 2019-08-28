//package com.nahuo.quicksale.oldermodel;
//
//import com.easemob.chat.EMContact;
//
//public class ChatUserModel extends EMContact {
//	private int unreadMsgCount;
//	private String header;
//
//
//	public String getHeader() {
//		return header;
//	}
//
//	public void setHeader(String header) {
//		this.header = header;
//	}
//
//	public int getUnreadMsgCount() {
//		return unreadMsgCount;
//	}
//
//	public void setUnreadMsgCount(int unreadMsgCount) {
//		this.unreadMsgCount = unreadMsgCount;
//	}
//
//	@Override
//	public int hashCode() {
//		return 17 * getUsername().hashCode();
//	}
//
//	@Override
//	public boolean equals(Object o) {
//		if (o == null || !(o instanceof ChatUserModel)) {
//			return false;
//		}
//		return getUsername().equals(((ChatUserModel) o).getUsername());
//	}
//
//	@Override
//	public String toString() {
//		return nick == null ? username : nick;
//	}
//}
