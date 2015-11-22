package com.example.nownews.adapter;

import java.util.List;

import com.example.nownews.R;
import com.example.nownews.beans.UnFinishedBeans;
import com.example.nownews.beans.User;
import com.htt.commonadapter.CommonAdapter;
import com.htt.commonadapter.CommonViewHolder;
import com.htt.utils.Utils;

import android.content.Context;
import android.widget.TextView;

public class Msg_not_complete_Adapter extends CommonAdapter<UnFinishedBeans>{

	User cuUser;
	public Msg_not_complete_Adapter(Context context, List<UnFinishedBeans> list, int layoutId,User user) {
		super(context, list, layoutId);
		this.cuUser=user;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void convert(CommonViewHolder holder, UnFinishedBeans data) {
		//如果未读，设置背景为黄色；如果已读，设置背景为蓝色
		TextView content= holder.getViewById(R.id.item_msg_list_unfinished_txt_content);
		holder.setText(R.id.item_msg_list_unfinished_txt_content, data.getContent())//设置消息内容
		      .setText(R.id.item_msg_list_unfinished_txt_name, data.getName())//设置发布者姓名
		      //设置头像
		      .setImageURL_displayImage(R.id.item_msg_list_unfinished_img_avatar, data.getAvatar())
		      .setText(R.id.item_msg_list_unfinished_txt_date, data.getDate());
		
		if(false==readOrNot(data.getUnread_list()))
		{//未读
			content.setBackgroundDrawable(Utils.getDrawable(context, R.drawable.unfished_yellow));
		}else{
			content.setBackgroundDrawable(Utils.getDrawable(context, R.drawable.unfinished_blue));
		}
		
	}
	
	/**
	 * 如果未读，返回false
	 * @param list
	 * @return
	 */
	private boolean readOrNot(List<User> list)
	{
		if(list==null){
			return false;
		}
		//默认已读
		boolean flag=true;
		User beans;
		for(int i=0;i<list.size();i++)
		{
			beans=list.get(i);
			if(beans.getUid().equals(cuUser.getUid()))
			{
				flag=false;
				break;
			}
		}
		return flag;
	}
	/**
	 * 根据传入的boolean值，重置text的背景色
	 * @param readOrNot
	 */
	public void updateTextbackground(boolean readOrNot,int position)
	{
	}

}
