package com.htt.commonadapter;

import java.util.List;

import android.content.Context;
import android.support.v4.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected List<T> list;
	protected Context context;
	protected int layoutId;//布局文件id
	
	public CommonAdapter(Context context,List<T> list, int layoutId) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.list=list;
		this.layoutId=layoutId;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//进一步的抽取，使得子类需要实现的代码量达到最小
		//获取ViewHodler
		CommonViewHolder holder = CommonViewHolder.getInstance(context, convertView, 
				parent, layoutId, position);
		//中间的view的修改工作留给用户完成
		convert(holder,getItem(position));
		return holder.getConvertView();
	}
	
	/**
	 * 进行子View的修改工作，如TextView设置Text，IamgeView设置Img等等
	 * @param holder CommonViewHolder对象，你可以通过他的getViewById来获得相应的子View
	 * @param data Bean对象
	 */
	public abstract void convert(CommonViewHolder holder, T data);
	
	/**
	 * 当要显示的item的数据集发生变化时，需要重新设置list，并更新界面
	 * @param list
	 */
	public void setList(List<T> list)
	{
		this.list=list;
		this.notifyDataSetChanged();
	}

}
