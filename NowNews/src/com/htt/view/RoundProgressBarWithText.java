package com.htt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RoundProgressBarWithText extends RelativeLayout {

	ProgressBar progressBar;
	TextView textView;
	Animation animation;
	Context context;
	public RoundProgressBarWithText(Context context) {
		this(context, null);
	}
	
	public RoundProgressBarWithText(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}
	
	public RoundProgressBarWithText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context=context;
		
		//progressBar = new ProgressBar(context, null, android.R.style.Widget_DeviceDefault_Light_ProgressBar_Smalls);
	}
	
	private void initView(){
		
	}
	
	private void initAnimation()
	{
		if(animation == null){
			animation = new AlphaAnimation(1.0f, 0.0f);
			animation.setFillAfter(false);
			animation.setDuration(1000);
			animation.setRepeatCount(Animation.INFINITE);//无限重复
			animation.setRepeatMode(Animation.REVERSE);
		}
	}

}
