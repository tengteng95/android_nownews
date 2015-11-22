package com.example.nownews.beans;

import java.util.List;

public class UnFinishedBeans extends HObject {
	/**
	 * HObject: Bean基类，内包含消息内容content(String类型)、name（发布者名称）、 avatar(发布者头像)
	 * date（消息最后修改时间）
	 */
	String mid;// 消息标志id
	String notifytime;//通知时间
	int isOnTop;

	List<User> unread_list;// 未读用户列表
	List<User> read_list;// 已读用户列表

	public List<User> getUnread_list() {
		return unread_list;
	}

	public void setUnread_list(List<User> unread_list) {
		this.unread_list = unread_list;
	}

	public List<User> getRead_list() {
		return read_list;
	}

	public void setRead_list(List<User> read_list) {
		this.read_list = read_list;
	}
	
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public int getIsOnTop() {
		return isOnTop;
	}

	public void setIsOnTop(int isOnTop) {
		this.isOnTop = isOnTop;
	}

	public String getNotifytime() {
		return notifytime;
	}

	public void setNotifytime(String notifytime) {
		this.notifytime = notifytime;
	}
	

}
