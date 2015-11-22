package com.example.nownews.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nownews.AlarmReceiver;
import com.example.nownews.MainActivity;
import com.example.nownews.MyApplication;
import com.example.nownews.R;
import com.example.nownews.adapter.ViewPagerAdapter;
import com.example.nownews.beans.User;
import com.example.nownews.ui.TopBar.TopBarListener;
import com.htt.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

/**
 * 消息发布界面Activity
 * @author huangtengteng
 *
 */
public class MsgSendActivity extends Activity implements TopBarListener,OnClickListener{

	TopBar topbar;
	EditText input;
	ProgressBar progressBar;
	TextView txt_progressbar;
	MyApplication application;
	
	DatePicker datePicker;
	TimePicker timePicker;
	
	ImageView img_clock,img_topIt;//设置闹钟，设置置顶
	Button btn_apply_clock;
	
	int selected_year,selected_month,selected_day;
	int selected_hour,selected_min;
	
	boolean isClockSet=false;//是否设定了闹钟
	boolean isClockChanged=false;//闹钟时间是否被改变
	String selected_clock;//设定好的时钟
	long v_selected_clock;
	
	boolean isTopItSet=false;//是否设置了置顶
	AlarmManager alarmManager;
	
	ViewPager viewPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send);
		alarmManager=(AlarmManager) MsgSendActivity.this.getSystemService(Context.ALARM_SERVICE);
		progressBar=(ProgressBar) findViewById(R.id.send_activity_progressbar);
		txt_progressbar=(TextView) findViewById(R.id.send_activity_txt_progrssbar);
		
		application = (MyApplication)getApplication();
		topbar=(TopBar) findViewById(R.id.send_activity_topbar);
		topbar.setTopBarListener(this);
		input=(EditText) findViewById(R.id.send_activity_et_input);
		
		img_clock=(ImageView) findViewById(R.id.activity_send_img_clock);
		img_topIt=(ImageView) findViewById(R.id.activity_send_img_top_it);
		//btn_apply_clock=(Button) findViewById(R.id.activity_send_btn_apply_clock);
		img_clock.setOnClickListener(this);
		img_topIt.setOnClickListener(this);
//		btn_apply_clock.setOnClickListener(this);
		
		viewPager=(ViewPager) findViewById(R.id.activity_send_viewPager);
		initViewPager();
		
		init();//为时间选择添加监听
	}
	
	private void initViewPager()
	{
		List<View> viewlist=new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(MsgSendActivity.this);
		View view1=inflater.inflate(R.layout.viewpager_datepicker, null);
		View view2=inflater.inflate(R.layout.viewpager_timepicker, null);
		viewlist.add(view1);
		viewlist.add(view2);
		viewPager.setAdapter(new ViewPagerAdapter(viewlist));
		
		datePicker=(DatePicker) view1.findViewById(R.id.activity_send_datapicker);
		timePicker=(TimePicker) view2.findViewById(R.id.activity_send_timepicker);
		btn_apply_clock=(Button) view2.findViewById(R.id.activity_send_btn_apply_clock);
		btn_apply_clock.setOnClickListener(this);
		
	}
	private void init()
	{
		Calendar calendar=Calendar.getInstance();
		int year =calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
		datePicker.init(year, month, dayOfMonth, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				selected_year=year;
				selected_month=monthOfYear+1;//monthOfYear取值为0-11，加1后才是真正的月份
				selected_day=dayOfMonth;
				isClockChanged=true;
			}
		});
		
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				selected_hour=hourOfDay;
				selected_min=minute;
				isClockChanged=true;
			}
		});
	}
	@Override
	public void onLeftButtonClicked() {
		this.finish();//返回上一个Activity
	}
	
	@Override
	public void onRightButtonClicked() {
		//点击了发布按钮
		User user=application.gerCurrentUser();
		if(null==user)
		{
			Utils.showToast(MsgSendActivity.this, "没有设置当前用户！！！");
			return;
		}
		/**
		 *      content: String, *
				notifyTime: Date：格式2015-11-19T08:13:24.073Z
				isOnTop: Boolean, (false)
				uid: String, *
				nickname: String, *
				expireTime: Date,
		 */
		String content=input.getText().toString();
		String nickname = user.getNickname();
		if(nickname==null)
		{
			nickname = user.getUsername();
		}
		String uid=user.getUid();
		long notifytime = 0;
		if(isClockSet==true){//设置了闹钟
			//notifytime=selected_clock;
			notifytime=v_selected_clock;
			
		}
		JSONObject object = new JSONObject();
		try {
			object.put("content", content);
			object.put("uid", uid);
			object.put("nickname", nickname);
			if(isClockSet==true)
			{
			    object.put("notifyTime", notifytime);
			    setClock(v_selected_clock);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		JsonObjectRequest request = new JsonObjectRequest(Utils.SEND_MESSAGE_URL, object, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							int code = response.getInt("code");
							String message = response.getString("message");
							if(0==code){
								//发布成功
								Utils.showToast(MsgSendActivity.this, "发布成功!");
								//发布成功以后，跳转到主界面
								startActivity(new Intent(MsgSendActivity.this,MainActivity.class));
								Log.i("msg", "发布成功！");
							}else{
								//发布失败，给出提示信息
								Utils.showToast(MsgSendActivity.this, "发布失败："+message);
								Log.i("msg", "发布失败！");
							}
						} catch (JSONException e) {
							e.printStackTrace();
							Log.i("msg", "发布：catchException！");
						}
						//隐藏
						progressBar.setVisibility(View.GONE);
						txt_progressbar.setVisibility(View.GONE);
						Log.i("msg", "发布1111");
					}
				}, new ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {
						Utils.showToast(MsgSendActivity.this, "发布失败！");
						//隐藏
						progressBar.setVisibility(View.GONE);
						txt_progressbar.setVisibility(View.GONE);
						Log.i("msg", "发布errorlistener");
					}
					
				});
		//显示进度条
		progressBar.setVisibility(View.VISIBLE);
		txt_progressbar.setVisibility(View.VISIBLE);
		MyApplication.getRequestQueue().add(request);// 将其添加到请求队列
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_send_img_clock://点击了设置闹钟按钮
			if(isClockSet==false)
			{//如果之前没有设置,显示时间和日期选择器，应用按钮文本设置为“应用"
				btn_apply_clock.setText("应用");
				showClockUI();
			}else{//如果之前已经设置，应用按钮文本设置为“取消”
				btn_apply_clock.setText("取消");
				showClockUI();
			}
			break;
		case R.id.activity_send_img_top_it://点击了设置置顶
			if(isTopItSet==false)
			{//之前没有设置，点击以后变为紫色，表示设置了
				isTopItSet=true;
				//img_topIt.setImageDrawable(getResources().getDrawable(R.drawable.top_it_purple));
				img_topIt.setImageDrawable(Utils.getDrawable(MsgSendActivity.this,R.drawable.top_it_purple));
			}else{//之前已经设置，点击以后恢复蓝色，取消设置
				isTopItSet=false;
				//img_topIt.setImageDrawable(getResources().getDrawable(R.drawable.top_it));
				img_topIt.setImageDrawable(Utils.getDrawable(MsgSendActivity.this,R.drawable.top_it));
			}
			break;
			
		case R.id.activity_send_btn_apply_clock:
			if(isClockSet==false)
			{//当前还未设置闹钟，此时点击的是"应用"按钮，
				isClockSet=true;//设定了时钟
				selected_year=datePicker.getYear();
				selected_month=datePicker.getMonth()+1;
				selected_day=datePicker.getDayOfMonth();
				selected_hour=timePicker.getCurrentHour();
				selected_min=timePicker.getCurrentMinute();
				//获取指定格式的时间串
				selected_clock=Utils.getDateString(selected_year, selected_month, selected_day, selected_hour, selected_min);
				Log.i("time", "闹钟时间:"+selected_clock);
				v_selected_clock=Utils.getMillis(selected_year, selected_month-1, selected_day, selected_hour, selected_min);
				Log.i("time", selected_year+","+(selected_month-1)+","+selected_day+","+selected_hour+","+selected_min);
				//img_clock.setImageDrawable(getResources().getDrawable(R.drawable.clock_purple));
				img_clock.setImageDrawable(Utils.getDrawable(MsgSendActivity.this,R.drawable.clock_purple));
				hideClockUI();
				
//				setClock(v_selected_clock);
			}else{//当前已经设置了闹钟，此时点击的是"取消"按钮或者"修改"按钮
				isClockSet=false;
				//img_clock.setImageDrawable(getResources().getDrawable(R.drawable.clock));
				img_clock.setImageDrawable(Utils.getDrawable(MsgSendActivity.this,R.drawable.clock));
			}
			hideClockUI();

			break;
		default:
			break;
		}
		
	}
	
	private void showClockUI(){
		viewPager.setVisibility(View.VISIBLE);
	}
	
	private void hideClockUI(){
		viewPager.setVisibility(View.GONE);
	}
	
	@SuppressLint("NewApi")
	private void setClock(long millis)
	{
		Intent intent = new Intent(MsgSendActivity.this, AlarmReceiver.class); // 创建Intent对象
		intent.putExtra("content", input.getText().toString());//将当前输入框中的文字作为提醒内容
		PendingIntent pi = PendingIntent.getBroadcast(MsgSendActivity.this, 0, intent, 0); // 创建PendingIntent
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 包含新API的代码块
			Log.i("time", "闹钟时间:"+millis+"当前时间:"+System.currentTimeMillis());
			alarmManager.setExact(AlarmManager.RTC_WAKEUP, millis, pi); // 设置闹钟
		} else {
			// 包含旧的API的代码块
			alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pi); // 设置闹钟
		}

		Toast.makeText(MsgSendActivity.this, "闹钟设置成功", Toast.LENGTH_LONG).show();// 提示用户
	}
}
