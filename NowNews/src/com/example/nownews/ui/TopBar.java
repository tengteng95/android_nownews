package com.example.nownews.ui;

import com.example.nownews.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBar extends RelativeLayout {

	private Button leftButton,rightButton;
	private TextView title;
	private String leftButton_text;
	private Drawable leftButton_drawable;
	
	private String title_text;
	private int title_textSize;
	private int title_textColor;
	
	private String rightButton_text;
	private Drawable rightButton_drawable;
	
	private LayoutParams layoutParams_left, layoutParams_title, layoutParams_right;
	private TopBarListener listener;
	public TopBar(Context context) {
		this(context,null);
	}
	
	public TopBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
		leftButton_text=typedArray.getString(R.styleable.TopBar_leftButton_text);
		leftButton_drawable=typedArray.getDrawable(R.styleable.TopBar_leftButton_background);
		
		title_text=typedArray.getString(R.styleable.TopBar_Title_text);
		title_textColor=typedArray.getColor(R.styleable.TopBar_Title_textColor, 0);
		//怎么提取大小属性的属性值？？
		title_textSize=(int) typedArray.getDimension(R.styleable.TopBar_Title_textSize, 20);
		
		rightButton_text = typedArray.getString(R.styleable.TopBar_rightButton_text);
		rightButton_drawable=typedArray.getDrawable(R.styleable.TopBar_rightButton_background);
		
		
		leftButton = new Button(context);
		rightButton = new Button(context);
		title = new TextView(context);
		
		//左边Button的设置
		leftButton.setText(leftButton_text);
		leftButton.setBackgroundDrawable(leftButton_drawable);
		//leftButton.setBackground(leftButton_drawable);
		leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onLeftButtonClicked();
				}
				
			}
		});
		
		//中间title
		title.setText(title_text);
		title.setTextSize(title_textSize);
		title.setTextColor(title_textColor);
		
		//右边button
		rightButton.setText(rightButton_text);
		rightButton.setBackgroundDrawable(rightButton_drawable);
		rightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null)
				{
					listener.onRightButtonClicked();
				}
			}
		});
		
		layoutParams_left = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams_left.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
		this.addView(leftButton, layoutParams_left);
		
		layoutParams_right = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams_right.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
		this.addView(rightButton, layoutParams_right);
		
		layoutParams_title = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams_title.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
		this.addView(title, layoutParams_title);
		
		typedArray.recycle();
	}
	
	public interface TopBarListener{
		void onLeftButtonClicked();
		void onRightButtonClicked();
	}
	
	public void setTopBarListener(TopBarListener listener)
	{
		this.listener=listener;
	}

}
