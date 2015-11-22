package com.example.nownews;

import android.content.Context;
import android.view.GestureDetector;

public class MyGestureDetector extends GestureDetector{

	Context context;
	OnGestureListener listener;
	public MyGestureDetector(Context context, OnGestureListener listener) {
		super(context, listener);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.listener=listener;
	}
	
	
	

}
