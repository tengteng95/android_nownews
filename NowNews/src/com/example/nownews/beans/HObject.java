package com.example.nownews.beans;

/**
 * 
 * @author huangtengteng
 *Bean基类，内包含消息内容content(String类型)、name（发布者名称）、avatar(发布者头像)
 *date（消息最后修改时间）
 */
public  class HObject {
	
	String content;//消息内容
	String name;//发布者名称
	String avatar;//发布者头像
	String date;//消息最后修改时间
	
	
	public String getContent()
	{
		return content;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public void setContent(String content) {
		this.content = content;
	}
	
	

}
