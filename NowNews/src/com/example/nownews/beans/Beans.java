package com.example.nownews.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Beans extends HObject implements Parcelable {
	//继承自HObject
	// HObject:Bean基类，内包含消息内容content(String类型)、name（发布者名称）、avatar(发布者头像) 
	//date（消息最后修改时间）
	public Beans(String content,String avatar)
	{
		this.content=content;
		this.avatar=avatar;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
