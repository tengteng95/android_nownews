package com.example.nownews.beans;

import java.io.Serializable;

/**
 * 当前在线用户信息
 * @author huangtengteng
 *
 */
public class User implements Serializable {
	String uid;
	String username;
	String password;
	String nickname;
	
	public User(String uid, String username, String password ,String nickname) {
		this.uid=uid;
		this.username=username;
		this.password=password;
		this.nickname=nickname;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
