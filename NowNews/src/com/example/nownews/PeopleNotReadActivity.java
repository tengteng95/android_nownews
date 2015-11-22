package com.example.nownews;

import java.util.ArrayList;
import java.util.List;

import com.example.nownews.beans.User;
import com.git.xlistview.XListView;
import com.htt.commonadapter.CommonAdapter;
import com.htt.commonadapter.CommonViewHolder;
import com.htt.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PeopleNotReadActivity extends Activity {
	
	XListView listview;
	List<User> list= new ArrayList<User>();
	PeopleNotReadAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_not_read);
		
		Intent intent=getIntent();
		Bundle bundle = intent.getExtras();
		ArrayList datalist=bundle.getParcelableArrayList("list");//获取未读列表
		list=(List<User>)datalist.get(0);
		listview=(XListView) findViewById(R.id.activity_people_not_read_listview);
		initListView();
	}
	
	private void initListView()
	{
		adapter= new PeopleNotReadAdapter(PeopleNotReadActivity.this, list, R.layout.item_people_not_read_list);
		listview.setAdapter(adapter);
	}
	
	
	private void stopRefreshOrLoadMore()
	{
		listview.stopRefresh();
		listview.stopLoadMore();
		listview.setRefreshTime("刚刚");
	}
	
	class PeopleNotReadAdapter extends CommonAdapter<User>
	{

		public PeopleNotReadAdapter(Context context, List<User> list, int layoutId) {
			super(context, list, layoutId);
		}

		@Override
		public void convert(CommonViewHolder holder, User data) {
			holder.setText(R.id.item_people_not_read_txt_username, data.getNickname());
//			      .setImageURL_displayImage(R.id.item_people_not_read_avatar, data.get)
		}
		
	}
	
	

}
