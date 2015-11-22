package com.example.nownews.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.nownews.PeopleNotReadActivity;
import com.example.nownews.R;
import com.example.nownews.beans.SendBeans;
import com.htt.commonadapter.CommonAdapter;
import com.htt.commonadapter.CommonViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Msg_send_Adapter extends CommonAdapter<SendBeans>{

	public Msg_send_Adapter(Context context, List<SendBeans> list, int layoutId) {
		super(context, list, layoutId);
	}

	@Override
	public void convert(CommonViewHolder holder, final SendBeans data) {
		//需要设置内容，多少人未读，时间这几项
		//时间和多少人未读暂未设置
		holder.setText(R.id.item_send_list_txt_content, data.getContent())
		      .setText(R.id.item_list_send_txt_people_not_read, data.getUnread_list().size()+"人未读")
		      .setText(R.id.item_send_list_txt_date,data.getDate());
		
		TextView txt_not_read=holder.getViewById(R.id.item_list_send_txt_people_not_read);
		txt_not_read.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,PeopleNotReadActivity.class);
				//将未读列表放入
				Bundle bundle = new Bundle();
				ArrayList list =new ArrayList();
				list.add(data.getUnread_list());
				bundle.putParcelableArrayList("list", list);
				intent.putExtras(bundle);
				context.startActivity(intent);
//				bundle.putSerializable("list", (Serializable) data.getUnread_list());
			}
		});
		      
	}

}
