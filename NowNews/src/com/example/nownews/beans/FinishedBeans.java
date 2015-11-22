package com.example.nownews.beans;

public class FinishedBeans extends HObject {
	/**
	 * HObject: Bean基类，内包含消息内容content(String类型)、name（发布者名称）、 avatar(发布者头像)
	 * date（消息最后修改时间）
	 */
	String mid;// 消息标志id

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}
	
}
