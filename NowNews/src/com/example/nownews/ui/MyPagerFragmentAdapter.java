package com.example.nownews.ui;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class MyPagerFragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> viewlist;

	public MyPagerFragmentAdapter(FragmentManager fm,List<Fragment> viewlist) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.viewlist=viewlist;

	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return viewlist.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewlist.size();
	}

}
