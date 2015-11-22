package com.example.nownews.ui;

import com.example.nownews.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MsgFragment extends Fragment {
	
	FrameLayout frameLayout;
	
	MsgNotCompleteFragment fragment1 ;
	MsgCompleteFragment fragment2 ;
	Fragment fragments[]= new Fragment[2];
	
	final int NOTCOMPLETE=0;
	final int COMPLETE=1;
	int curPage=NOTCOMPLETE;
	int selectPage= COMPLETE;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view =inflater.inflate(R.layout.fragment_msg, container,false);

		TextView txt_not_complete = (TextView) view.findViewById(R.id.fragment_msg_txt_not_complete);
		TextView txt_complete = (TextView)view.findViewById(R.id.fragment_msg_txt_complete);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.fragment_msg_txt_not_complete:
					selectPage=NOTCOMPLETE;
					changFragment(curPage, selectPage);
					curPage=selectPage;
					break;
				case R.id.fragment_msg_txt_complete:
					selectPage=COMPLETE;
					changFragment(curPage, selectPage);
					curPage=selectPage;
					break;
				default:
					break;
				}
			}
		};
		
		txt_complete.setOnClickListener(listener);
		txt_not_complete.setOnClickListener(listener);
		
		MsgNotCompleteFragment fragment1 = new MsgNotCompleteFragment();
		MsgCompleteFragment fragment2 = new MsgCompleteFragment();
		fragments[0]=fragment1;
		fragments[1]=fragment2;
		
		FragmentManager fManager = getChildFragmentManager();
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.add(R.id.fragment_msg_framelayout,fragments[0])
		  .add(R.id.fragment_msg_framelayout,fragments[1])
		  .hide(fragments[1])
		  .commit();
		return view;
		
	}
	
	void changFragment(int curPage,int selectPage)
	{
		if(curPage==selectPage)
		{
			return ;
		}
		
		FragmentManager fManager = getChildFragmentManager();
		FragmentTransaction transaction =fManager.beginTransaction();
		switch (selectPage) {
		case NOTCOMPLETE:
			transaction.hide(fragments[1]).show(fragments[0]).commit();
			break;
		case COMPLETE:
			transaction.hide(fragments[0] ).show(fragments[1]).commit();
			break;
		default:
			break;
		}
	}

}
