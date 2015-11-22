package com.htt.commonadapter;

import java.util.List;

import com.example.nownews.R;
import com.example.nownews.beans.Beans;

import android.content.Context;

public class MyAdapter extends CommonAdapter<Beans> {

	public MyAdapter(Context context, List<Beans> list,int layoutId)
	{
		super(context, list, layoutId);
	}
	

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		//使用万用ViewHolder
//		CommonViewHolder viewHolder= CommonViewHolder.getInstance(context, convertView,
//				parent, R.layout.item, position);
//		//获得TextView，修改数据
//		
//		TextView name = viewHolder.getViewById(R.id.item_name);
//		TextView nickname = viewHolder.getViewById(R.id.item_nickname);
//		name.setText(list.get(position).getName());
//		nickname.setText(list.get(position).getName());
//		//返回修改后的View视图
//		return viewHolder.getConvertView();
//		//return convertView;
//	}


	@Override
	public void convert(CommonViewHolder viewHolder, Beans data) {
		// TODO Auto-generated method stub
//		TextView name = viewHolder.getViewById(R.id.item_name);
//		TextView nickname = viewHolder.getViewById(R.id.item_nickname);
//		name.setText(data.getName());
//		nickname.setText(data.getNickname());
		
//		//进一步的简化
//		viewHolder.setText(R.id.item_name, data.getName())
//		  .setText(R.id.item_nickname, data.getNickname())
//		  .setImageURL_displayImage(R.id.item_img, "http://pic14.nipic.com/20110522/7411759_164157418126_2.jpg");
	}

}
