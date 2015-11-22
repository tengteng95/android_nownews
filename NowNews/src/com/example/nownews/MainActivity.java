package com.example.nownews;


import com.example.nownews.ui.MsgFragment;
import com.example.nownews.ui.MsgSendFragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	final int MESSAGE = 0;// 消息页
	final int SEND = 1;// 发布页
	FrameLayout frameLayout;// Fragment页面
	int currentPage = MESSAGE;// 当前页
	int SelectPage = MESSAGE;// 用户选中的页面

	Fragment fragment[] = new Fragment[2];
	MsgFragment msgFragment;
	MsgSendFragment msgSendFragment;
	
	TextView txt_msg,txt_send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		frameLayout = (FrameLayout) findViewById(R.id.main_fragmentContainer);
		txt_msg=(TextView) findViewById(R.id.main_txt_msg);
		txt_send =(TextView) findViewById(R.id.main_txt_send);

		msgFragment = new MsgFragment();
		msgSendFragment = new MsgSendFragment();
		//添加到数组中
		fragment[0]=msgFragment;
		fragment[1]=msgSendFragment;

		FragmentManager fManager = getSupportFragmentManager();
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.add(R.id.main_fragmentContainer, fragment[0])
		        .add(R.id.main_fragmentContainer, fragment[1])
				.hide(fragment[1]).commit();

	}



	Drawable drawable1,drawable2;
	public void doBottomClick(View view) {
		switch (view.getId()) {
		case R.id.main_txt_msg:
			SelectPage = MESSAGE;
			changeFragment(currentPage, SelectPage);
			currentPage = SelectPage;
			drawable1=getResources().getDrawable(R.drawable.message1);
			drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
			drawable2=getResources().getDrawable(R.drawable.send2);
			drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
			txt_msg.setCompoundDrawables(null, drawable1, null, null);
			txt_send.setCompoundDrawables(null, drawable2, null, null);	
			break;
		case R.id.main_txt_send:
			SelectPage = SEND;
			changeFragment(currentPage, SelectPage);
			currentPage = SelectPage;
			drawable1=getResources().getDrawable(R.drawable.message2);
			drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
			drawable2=getResources().getDrawable(R.drawable.send1);
			drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
			txt_msg.setCompoundDrawables(null, drawable1, null, null);
			txt_send.setCompoundDrawables(null, drawable2, null, null);	
			break;
		default:
			break;
		}
	}

	void changeFragment(int curPage, int selectPage) {
		if (curPage == selectPage) {
			return;
		}
		FragmentTransaction transaction;
		transaction = getSupportFragmentManager().beginTransaction();
		
		switch (selectPage) {
		case MESSAGE:
			transaction.hide(fragment[1]).show(fragment[0]).commit();
			break;
		case SEND:
			transaction.hide(fragment[0]).show(fragment[1]).commit();
		default:
			break;
		}
	}
}
