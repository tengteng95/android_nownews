package com.example.nownews.adapter;

import java.util.List;

import com.example.nownews.R;
import com.example.nownews.beans.Beans;
import com.example.nownews.beans.FinishedBeans;
import com.htt.commonadapter.CommonAdapter;
import com.htt.commonadapter.CommonViewHolder;

import android.content.Context;

public class Msg_complete_Adapter extends CommonAdapter<FinishedBeans> {

	public Msg_complete_Adapter(Context context, List<FinishedBeans> list, int layoutId) {
		super(context, list, layoutId);
	}

	@Override
	public void convert(CommonViewHolder holder, FinishedBeans data) {
		//需要设置的有头像、用户名、时间、内容，
		holder.setText(R.id.item_msg_list_unfinished_txt_content, data.getContent())//内容
		  .setText(R.id.item_msg_list_unfinished_txt_name, data.getName())//用户名
	      .setImageURL_displayImage(R.id.item_msg_list_unfinished_img_avatar, data.getAvatar())//头像
		  .setText(R.id.item_msg_list_unfinished_txt_date, data.getDate());
		
	}

}
