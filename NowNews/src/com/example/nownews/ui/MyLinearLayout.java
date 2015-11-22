package com.example.nownews.ui;

import com.example.nownews.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyLinearLayout extends LinearLayout implements OnTouchListener {
	ImageView img_avatar;
	TextView txt_content;
	TextView txt_delete ;
	Button test_btn ;
	
	TranslateAnimation translateAnimation1, translateAnimation2 ;
	public MyLinearLayout(Context context) {
		this(context, null);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	boolean once=false;
	static final int IMG_AVATAR_ID=2;
	static final int TXT_CONTENT_ID=3;
	static final int TXT_DELETE_ID=4;
	static final int TEST_BUTTON_ID=5;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		Log.i("onMeasure", "jjjj");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(once==false)
		{
			initView();
			once=true;
		}
	}
	
	/**
	 * 初始化界面元素
	 */
	void initView()
	{
		img_avatar = new ImageView(getContext());//发布者头像
		img_avatar.setId(IMG_AVATAR_ID);
		img_avatar.setImageResource(R.drawable.ic_launcher);
		
		txt_content = new TextView(getContext());//发布内容
		txt_content.setId(TXT_CONTENT_ID);
		txt_content.setText("content");
		
		txt_delete = new TextView(getContext());//删除按钮
		txt_delete.setId(TXT_DELETE_ID);
		
		test_btn = new Button(getContext());
		test_btn.setId(TEST_BUTTON_ID);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		this.addView(img_avatar, params);
		this.addView(txt_content,params);
		this.addView(txt_delete,params);
		this.addView(test_btn,params);
		
		this.setOnTouchListener(this);
		
	}
	
	GestureDetector mgestureDector = new GestureDetector(getContext(),new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			final int FLING_MIN_DISTANCE = 300, FLING_MIN_VELOCITX = 300;  
	        if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE &&
	        		Math.abs(velocityX) > FLING_MIN_VELOCITX) {  //从左往右
	        	
	        	if(translateAnimation1==null)
	        	{
	        		translateAnimation1 = new TranslateAnimation(0.0f, MyLinearLayout.this.getWidth(),0.0f,0.0f);
	        		translateAnimation1.setDuration(1000);
		    		translateAnimation1.setFillAfter(false);
		    		translateAnimation1.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
//							itemlist.remove(selectItem);
//				        	adapter.setList(itemlist);
						}
					});
	        	}
	        	MyLinearLayout.this.startAnimation(translateAnimation1);

	        }
	        else if(e1.getX() - e2.getX() > FLING_MIN_DISTANCE && 
	        		Math.abs(velocityX) > FLING_MIN_VELOCITX){//从右往左
	      
	        	if(translateAnimation2==null)
	        	{
	        		translateAnimation2 = new TranslateAnimation(0.0f, -MyLinearLayout.this.getWidth(),0.0f,0.0f);
	        		translateAnimation2.setDuration(1000);
		    		translateAnimation2.setFillAfter(false);
		    		translateAnimation2.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
//				        	itemlist.remove(selectItem);
//				        	adapter.setList(itemlist);
						}
						@Override
						public void onAnimationStart(Animation animation) {
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
						}
					});
	        	}
	        	MyLinearLayout.this.startAnimation(translateAnimation2);
	        }
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			Toast.makeText(getContext(), "down!!!", Toast.LENGTH_SHORT).show();
			return true;
		}
	});

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "click!!!", Toast.LENGTH_SHORT).show();
		return mgestureDector.onTouchEvent(event);
	}
	
	public ImageView getImgAvatar()
	{
		return img_avatar;
	}
}
