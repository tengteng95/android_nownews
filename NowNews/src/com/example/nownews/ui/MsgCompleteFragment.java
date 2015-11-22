package com.example.nownews.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nownews.MyApplication;
import com.example.nownews.R;
import com.example.nownews.adapter.Msg_complete_Adapter;

import com.example.nownews.beans.FinishedBeans;

import com.git.xlistview.XListView;
import com.git.xlistview.XListView.IXListViewListener;
import com.htt.utils.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MsgCompleteFragment extends Fragment implements IXListViewListener {

	Context context;
	View view;
	XListView listview;
	Msg_complete_Adapter adapter;
	List<FinishedBeans> itemlist = new ArrayList<FinishedBeans>();
	TextView txt_no_msg;
	SQLiteDatabase db;
	ProgressBar progressBar;
	TextView txt_progressbar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_msg_complete, container, false);
		context = getContext();
		listview = (XListView) view.findViewById(R.id.msg_complete_listView);
		txt_no_msg=(TextView) view.findViewById(R.id.msg_complete_txt_no_msg);
		
		progressBar=(ProgressBar) view.findViewById(R.id.msg_complete_progressbar);
		txt_progressbar=(TextView) view.findViewById(R.id.msg_complete_txt_progressbar);
		//初始化listView，从数据库中获取数据显示
		initListView();
		getDataFromServer();
		//服务器端还没有实现已完成消息存储
		// if(true==Utils.isNetworkConnected(context))
		// {//网络可用
		// //getBeansList();//从服务器获取数据
		// }else{
		// Utils.showToast(context, "当前网络不可用");
		// }
		return view;

	}

	void initListView() {
		itemlist = getItemListFromDb();
		if (itemlist.size() != 0) {
			if (adapter == null) {
				adapter = new Msg_complete_Adapter(getContext(), itemlist, R.layout.item_msg_list_unfinished);
				listview.setAdapter(adapter);
			}
			listview.setVisibility(View.VISIBLE);
			txt_no_msg.setVisibility(View.GONE);
		} else {
			listview.setVisibility(View.GONE);
			txt_no_msg.setVisibility(View.VISIBLE);
		}

		listview.setXListViewListener(this);
		// 为listView设置长按显示菜单
		listview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "完成");
				menu.add(0, 1, 1, "删除");

			}
		});
	}


	private List<FinishedBeans> getItemListFromDb() {
		// 如果不存在数据库msg.db，则创建，否则直接打开
		if (db == null) {
			db = context.openOrCreateDatabase(Utils.DB_NAME, Context.MODE_PRIVATE, null);
		}
		// 如果不存在“已处理通知表”，则创建该表
		/**
		 * _id mid authorname content avatar time(发布时间)
		 */
		OpenOrCreateMsgCompleteTable(db);
		// 从数据库中选取最新的20条数据，这里暂时实现成选取所有数据
		Cursor c = db.rawQuery("select * from " + Utils.TABLE_MSG_COMPLETE, null);

		List<FinishedBeans> list = new ArrayList<FinishedBeans>();
		FinishedBeans bean;
		String content, name, avatar, time;
		// 遍历结果，并将其添加到list中,bean需要的属性：发布者姓名，头像，内容，时间
		if (c != null) {
			while (c.moveToNext()) {
				content = c.getString(c.getColumnIndex("content"));
				name = c.getString(c.getColumnIndex("authorname"));
				avatar = c.getString(c.getColumnIndex("avatar"));
				time = c.getString(c.getColumnIndex("time"));
				// avatar=Utils.getSaveString(avatar);
				bean = new FinishedBeans();
				bean.setContent(content);
				bean.setName(name);
				bean.setDate(time);
				list.add(bean);
			}
			c.close();
		}
		//Toast.makeText(context, "从数据库中读取了" + list.size() + "条消息", Toast.LENGTH_LONG).show();
		if (list == null || list.size() == 0) {// 从数据库中没有读到元素，ListView
			// 隐藏，显示“暂没有任何消息”的文本
			listview.setVisibility(View.GONE);
			txt_no_msg.setVisibility(View.VISIBLE);
		}
		return list;
	}
	
	private void OpenOrCreateMsgCompleteTable(SQLiteDatabase db) {
		if (db == null) {
			Utils.showToast(context, "创建表失败！传入的数据库为空！");
			return;
		}
		 db.execSQL("create table if not exists " + Utils.TABLE_MSG_COMPLETE
				 +" (_id integer primary key autoincrement,"
				 +" mid text not null,"
				 +" authorname text not null,"
		         +" content text not null,"
				 +" avatar text not null,"
		         +" time text not null"
		         +")");
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	
	// 从服务器获取数据
		private void getDataFromServer() {
			if (true == Utils.isNetworkConnected(context)) {// 网络可用
				// 因为是异步操作的，所以这里返回的list是还没有来得及添加元素的list，即大小为0
				// 所以这里是错的
				// itemlist = getBeansList();// 从服务器获取数据
				getBeansList();
				stopRefreshOrLoadMore();

			} else {
				Utils.showToast(context, "当前网络不可用，检查网络连接");
				listview.setVisibility(View.GONE);
				txt_no_msg.setVisibility(View.VISIBLE);
			}
		}
		
		List<FinishedBeans> getBeansList() {
			final List<FinishedBeans> list = new ArrayList<FinishedBeans>();
			JsonObjectRequest request = new JsonObjectRequest(Utils.GET_MESSAGE_FINISHED, null, new Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						int code = response.getInt("code");
						String msg = response.getString("message");
						if (0 == code) {// 成功
							JSONArray array = response.getJSONArray("results");
							JSONObject object;
							String mid, authorname, content, avatar;
							String time;
							long v_time;
							FinishedBeans beans;
							for (int i = 0; i < array.length(); i++) {
								object = array.getJSONObject(i);
								mid = object.getString("mid");
								authorname = object.getString("authorname");
								content = object.getString("content");
								// 现在的服务器端返回的数据中没有avatar项
								// avatar =object.getString("avatar");
								avatar = "http://a.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828bd6ea96e2ff1f4134960a5ae1.jpg";
								v_time = object.getLong("lastModifiedTime");
								time=Utils.getDateFromTimeMillis(v_time);
		
								beans = new FinishedBeans();
								beans.setMid(mid);
								beans.setName(authorname);
								beans.setContent(content);
								beans.setAvatar(avatar);
								beans.setDate(time);
								list.add(beans);
							}
							progressBar.setVisibility(View.GONE);
							txt_progressbar.setVisibility(View.GONE);
							saveDataIntoDb(list);// 将获取到的信息存入数据库
							itemlist = list;
							if (itemlist != null && itemlist.size() > 0) {// 接收到了数据
								 
								adapter.setList(itemlist);
								listview.setVisibility(View.VISIBLE);
								txt_no_msg.setVisibility(View.GONE);
							} else {
								// 没有接收到数据
								listview.setVisibility(View.GONE);
								txt_no_msg.setVisibility(View.VISIBLE);
							}
						} else {
							// 输出错误信息
							Utils.showToast(context, "从服务器获取完成消息列表失败：" + msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progressBar.setVisibility(View.GONE);
					txt_progressbar.setVisibility(View.GONE);
				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Utils.showToast(context, "MsgComplete从服务器获取数据失败！");
					progressBar.setVisibility(View.GONE);
					txt_progressbar.setVisibility(View.GONE);
				}
			});

			progressBar.setVisibility(View.VISIBLE);
			txt_progressbar.setVisibility(View.VISIBLE);
			MyApplication.getRequestQueue().add(request);
			return list;

		}
		
		private void stopRefreshOrLoadMore()
		{
			listview.stopRefresh();
			listview.stopLoadMore();
			listview.setRefreshTime("刚刚");
		}
		
		private void saveDataIntoDb(List<FinishedBeans> list) {
			// 如果db为空，打开或创建数据库
			if (db == null) {
				db = context.openOrCreateDatabase(Utils.DB_NAME, Context.MODE_PRIVATE, null);
			}
			// 如果不存在未处理消息表，创建
			/**
			 * _id mid time(发布时间) notifytime（通知时间）isOnTop authorname content avatar
			 * unreadlist readlist
			 * 
			 */
			OpenOrCreateMsgCompleteTable(db);
			String mid, authorname, content, avatar, time;
			
			int insertnum=0;
			// 执行插入语句
			for (int i = 0; i < list.size(); i++) {
				mid = list.get(i).getMid();
				authorname = list.get(i).getName();
				content = list.get(i).getContent();
				avatar = list.get(i).getAvatar();
				time = list.get(i).getDate();
				Cursor cursor=db.rawQuery("select * from "+Utils.TABLE_MSG_COMPLETE
						       +" where mid="
						       +Utils.saveTextIntoDb(mid), null);
				if(cursor.getCount()==0)
				{//没有插入过才插入
					db.execSQL("insert into " + Utils.TABLE_MSG_COMPLETE
							+ "(mid,authorname,content,avatar,time)" + " values("
							+ Utils.saveTextIntoDb(mid) + "," + Utils.saveTextIntoDb(authorname) + ","
							+ Utils.saveTextIntoDb(content) + "," + Utils.saveTextIntoDb(avatar) + ","
							+ Utils.saveTextIntoDb(time) 
							 + ")");
					
					insertnum++;
				}

			}
			Toast.makeText(context, "MsgComplete已经成功保存入数据库" + insertnum + "项", Toast.LENGTH_LONG).show();

			Log.i("msg", "MsgComplete已经成功保存入数据库" + list.size() + "项");
		}



}
