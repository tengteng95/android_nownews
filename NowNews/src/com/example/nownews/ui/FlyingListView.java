package com.example.nownews.ui;

import java.util.List;

import com.example.nownews.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;



public class FlyingListView extends ListView implements OnTouchListener,OnGestureListener{

	private GestureDetector gestureDetector;
	private Button deleteButton;
	private int selectItem;//是在哪一个item上进行的滑动操作
	private int is_delButton_show=0;//删除按钮是否已经显示
	//private OnItemDeleteListener onItemDeleteListener;
	
	public FlyingListView(Context context) {
		this(context, null);//调用另一个构造方法
	}
	public FlyingListView(Context context, AttributeSet attrs) {
		this(context, attrs,0);//调用另一个构造方法
	}
	public FlyingListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		gestureDetector = new GestureDetector(getContext(),this);
		setOnTouchListener(this);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		if (0==is_delButton_show) {//如果没有显示
			selectItem = pointToPosition((int)e.getX(), (int)e.getY());
		}
		return true;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
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
		//如果删除按钮没有显示，并且手势滑动符合我们的条件
		//此处可以根据需要进行手势滑动的判断，如限制左滑还是右滑，我这里是左滑右滑都可以
		if (0==is_delButton_show && Math.abs(velocityX) > Math.abs(velocityY)) {
			//因为在使用listView之前无法确定btn的id，所以最好不要采用写死的方法，这样非常不灵活哦！
			
			View view =this.getChildAt(selectItem);
			//view.findViewById(R.id.delButton);
		

		}else{
			setOnTouchListener(this);
		}
		
		return false;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		//得到当前触摸的item
		selectItem = pointToPosition((int)event.getX(), (int)event.getY());
		//如果删除按钮已经显示，那么隐藏按钮，异常按钮在当前位置的绘制
		if (is_delButton_show==1) {
			deleteButton.setVisibility(GONE);
			selectItem=0;//没有显示
//			btnHide(btnDelete);
//			viewGroup.removeView(btnDelete);
//			btnDelete = null;
//			isDeleteShow = false;
			return false;
		}else{
			//如果按钮没显示，则触发手势事件
			//由此去触发GestureDetector的事件，可以查看其源码得知，onTouchEvent中进行了手势判断，调用onFling
			return gestureDetector.onTouchEvent(event);
		}
	}

}
