package com.example.nownews;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.nownews.beans.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class MyApplication extends Application{

    static RequestQueue mQueue;
    static User currentUser;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mQueue = Volley.newRequestQueue(getApplicationContext());
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration  
                .createDefault(this);  
         
        //Initialize ImageLoader with configuration.  
        ImageLoader.getInstance().init(configuration);  
	}
	
	public static RequestQueue getRequestQueue()
	{
		return mQueue;
	}
	
	public  User gerCurrentUser()
	{
		return currentUser;
	}
	
	public  void setCurrentUser(User user)
	{
		currentUser=user;
	}
	
	
}
