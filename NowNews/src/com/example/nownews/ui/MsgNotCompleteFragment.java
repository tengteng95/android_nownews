package com.example.nownews.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nownews.MyApplication;
import com.example.nownews.R;
import com.example.nownews.adapter.Msg_not_complete_Adapter;
import com.example.nownews.beans.UnFinishedBeans;
import com.example.nownews.beans.User;
import com.git.xlistview.XListView;
import com.git.xlistview.XListView.IXListViewListener;
import com.htt.utils.Utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.sax.ElementListener;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MsgNotCompleteFragment extends Fragment implements IXListViewListener, OnClickListener {

	Context context;
	XListView listView;
	TextView txt_no_msg;
	List<UnFinishedBeans> itemlist = new ArrayList<UnFinishedBeans>();
	Msg_not_complete_Adapter adapter;
	ProgressBar progressBar;
	TextView txt_progressbar;
	SQLiteDatabase db;
	MyApplication myApplication;
	User currentUser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		context = getContext();
		myApplication = (MyApplication) getActivity().getApplication();
		currentUser = myApplication.gerCurrentUser();
		View view = inflater.inflate(R.layout.fragment_msg_not_complete, container, false);
		listView = (XListView) view.findViewById(R.id.msg_not_complete_listView);
		txt_no_msg = (TextView) view.findViewById(R.id.msg_not_complete_no_message);
		txt_no_msg.setOnClickListener(this);

		progressBar = (ProgressBar) view.findViewById(R.id.msg_not_complete_progressbar);
		txt_progressbar = (TextView) view.findViewById(R.id.msg_not_complete_txt_progressbar);
		// 初始化listView，会首先从数据库中获取数据，并显示，不至于每次进入程序都要进行一段等待时间
		initListView();
		// 从服务器获取数据
		getDataFromServer();

		return view;
	}

	void initListView() {
		// 设置adapter
		adapter = new Msg_not_complete_Adapter(getContext(), itemlist, R.layout.item_msg_list_unfinished,currentUser);
		listView.setAdapter(adapter);
		// 从数据库中获取元素
		itemlist = getItemListFromDb();
		if (itemlist.size() != 0) {
			listView.setVisibility(View.VISIBLE);
			txt_no_msg.setVisibility(View.GONE);
			adapter.setList(itemlist);
		} else {
			listView.setVisibility(View.GONE);
			txt_no_msg.setVisibility(View.VISIBLE);
		}

		listView.setXListViewListener(this);
		// 为listView设置长按显示菜单
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "确认已读");
				menu.add(0, 1, 1, "删除");

			}
		});
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
			listView.setVisibility(View.GONE);
			txt_no_msg.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int position = menuInfo.position - 1;// 当前被选中的item的位置,需要减1(因为headerView存在)
		switch (item.getItemId()) {
		case 0:// 点击了确认已读
				// 向服务器发出确认通知，同时需要更新本地数据库，将其从未读列表中删除
			setRead(position);
			Utils.showToast(context, position + "clicked");
			
			break;
		case 1:
			Utils.showToast(context, "click delete");
			break;
		default:
			Utils.showToast(context, "error click");
			break;
		}
		return super.onContextItemSelected(item);
	}

	List<UnFinishedBeans> getBeansList() {
		final List<UnFinishedBeans> list = new ArrayList<UnFinishedBeans>();
		JsonObjectRequest request = new JsonObjectRequest(Utils.GET_MESSAGE_UNFINISHED, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int code = response.getInt("code");
							String msg = response.getString("message");
							if (0 == code) {// 成功
								JSONArray array = response.getJSONArray("results");
								JSONObject object;
								String mid, authorname, content, avatar;
								String time, notifytime;
								long v_time, v_notifytime;
								int isOnTop;
								UnFinishedBeans beans;
								for (int i = 0; i < array.length(); i++) {
									object = array.getJSONObject(i);
									mid = object.getString("mid");
									authorname = object.getString("authorname");
									content = object.getString("content");
									// 现在的服务器端返回的数据中没有avatar项
									// avatar =object.getString("avatar");
									avatar = "http://a.hiphotos.baidu.com/image/pic/item/bd3eb13533fa828bd6ea96e2ff1f4134960a5ae1.jpg";
									v_time = object.getLong("lastModifiedTime");
									v_notifytime = object.getLong("notifyTime");
									time = Utils.getDateFromTimeMillis(v_time);
									notifytime = Utils.getDateFromTimeMillis(v_notifytime);
									isOnTop = (true == object.getBoolean("isOnTop")) ? 1 : 0;
									// 获取未读用户列表
									List<User> unread_list = getRead_Or_NOt_List(object, "unreadList");
									List<User> read_list = getRead_Or_NOt_List(object, "readList");
									beans = new UnFinishedBeans();
									beans.setMid(mid);
									beans.setName(authorname);
									beans.setContent(content);
									beans.setAvatar(avatar);
									beans.setDate(time);
									beans.setNotifytime(notifytime);
									beans.setIsOnTop(isOnTop);
									beans.setUnread_list(unread_list);
									beans.setRead_list(read_list);
									list.add(beans);
								}
								progressBar.setVisibility(View.GONE);
								txt_progressbar.setVisibility(View.GONE);
								saveDataIntoDb(list);// 将获取到的信息存入数据库
								itemlist = list;
								if (itemlist != null && itemlist.size() > 0) {// 接收到了数据

									adapter.setList(itemlist);
									listView.setVisibility(View.VISIBLE);
									txt_no_msg.setVisibility(View.GONE);
								} else {
									// 没有接收到数据
									listView.setVisibility(View.GONE);
									txt_no_msg.setVisibility(View.VISIBLE);
								}
							}else if(code==4)
							{//成功，但是没有任何数据
								listView.setVisibility(View.GONE);
								txt_no_msg.setVisibility(View.VISIBLE);
							}
							else {
								// 输出错误信息
								Utils.showToast(context, "从服务器获取未完成消息列表失败：" + msg);
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
						Utils.showToast(context, "MsgNotComplete从服务器获取数据失败！");
						progressBar.setVisibility(View.GONE);
						txt_progressbar.setVisibility(View.GONE);
					}
				});

		progressBar.setVisibility(View.VISIBLE);
		txt_progressbar.setVisibility(View.VISIBLE);
		MyApplication.getRequestQueue().add(request);
		return list;

	}

	/**
	 * 获取已读或未读用户列表，传入的第二个参数要么是unreadList，要么是readList
	 * 
	 * @param object
	 * @param isRead
	 * @return
	 */
	List<User> getRead_Or_NOt_List(JSONObject object, String isRead) {
		List<User> list = new ArrayList<User>();
		try {
			JSONArray array = object.getJSONArray(isRead);
			JSONObject jsonObject;
			User user;
			String uid, nickname;
			for (int i = 0; i < array.length(); i++) {
				jsonObject = array.getJSONObject(i);
				uid = jsonObject.getString("uid");
				nickname = jsonObject.getString("nickname");
				user = new User(uid, "weishezhi", "weishezhi", nickname);
				list.add(user);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<UnFinishedBeans> getItemListFromDb() {
		// 如果不存在数据库msg.db，则创建，否则直接打开
		if (db == null) {
			db = context.openOrCreateDatabase(Utils.DB_NAME, Context.MODE_PRIVATE, null);
		}
		// 如果不存在“未处理通知表”，则创建该表，该表中含有三个元素：_id, content,
		// name,avatar,time,（未读列表，已读列表）
		/**
		 * _id mid authorname content avatar time(发布时间) notifytime（通知时间）isOnTop
		 * unreadlist readlist
		 */
		OpenOrCreateMsgNotCompleteTable(db);
		// 从数据库中选取最新的20条数据，这里暂时实现成选取所有数据
		Cursor c = db.rawQuery("select * from " + Utils.TABLE_MSG_NOT_COMPLETE, null);

		List<UnFinishedBeans> list = new ArrayList<UnFinishedBeans>();
		UnFinishedBeans bean;
		String content, name, avatar, time;
		// 遍历结果，并将其添加到list中,bean需要的属性：发布者姓名，头像，内容，时间
		if (c != null) {
			while (c.moveToNext()) {
				content = c.getString(c.getColumnIndex("content"));
				name = c.getString(c.getColumnIndex("authorname"));
				avatar = c.getString(c.getColumnIndex("avatar"));
				time = c.getString(c.getColumnIndex("time"));
				// avatar=Utils.getSaveString(avatar);
				bean = new UnFinishedBeans();
				bean.setContent(content);
				bean.setName(name);
				bean.setDate(time);
				list.add(bean);
			}
			c.close();
		}
		// Toast.makeText(context, "MsgNotComplete从数据库中读取了" + list.size() +
		// "条消息", Toast.LENGTH_LONG).show();
		// Log.i("msg", "MsgNotComplete从数据库中读取了" + list.size() + "条消息");
		if (list == null || list.size() == 0) {// 从数据库中没有读到元素，ListView
												// 隐藏，显示“暂没有任何消息”的文本
			listView.setVisibility(View.GONE);
			txt_no_msg.setVisibility(View.VISIBLE);
		}
		return list;
	}

	private void saveDataIntoDb(List<UnFinishedBeans> list) {
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
		OpenOrCreateMsgNotCompleteTable(db);
		String mid, authorname, content, avatar, time, notifytime, unreadList, readList;
		int isOnTop;

		int insertnum = 0;
		// 执行插入语句
		for (int i = 0; i < list.size(); i++) {
			mid = list.get(i).getMid();
			authorname = list.get(i).getName();
			content = list.get(i).getContent();
			avatar = list.get(i).getAvatar();
			time = list.get(i).getDate();
			notifytime = list.get(i).getNotifytime();
			/** list转成String来存储，使用uid:nickname的格式，不同用户之间用‘，’隔开 **/
			unreadList = UserListToString(list.get(i).getUnread_list());
			readList = UserListToString(list.get(i).getRead_list());

			isOnTop = list.get(i).getIsOnTop();
			Cursor cursor = db.rawQuery(
					"select * from " + Utils.TABLE_MSG_NOT_COMPLETE + " where mid=" + Utils.saveTextIntoDb(mid), null);
			if (cursor.getCount() == 0) {// 没有插入过才插入
				db.execSQL("insert into " + Utils.TABLE_MSG_NOT_COMPLETE
						+ "(mid,authorname,content,avatar,time,notifytime,isOnTop,unreadList,readList)" + " values("
						+ Utils.saveTextIntoDb(mid) + "," + Utils.saveTextIntoDb(authorname) + ","
						+ Utils.saveTextIntoDb(content) + "," + Utils.saveTextIntoDb(avatar) + ","
						+ Utils.saveTextIntoDb(time) + "," + Utils.saveTextIntoDb(notifytime) + "," + isOnTop + ","
						+ Utils.saveTextIntoDb(unreadList) + "," + Utils.saveTextIntoDb(readList) + ")");

				insertnum++;
			}

		}
		Toast.makeText(context, "MsgNotComplete已经成功保存入数据库" + insertnum + "项", Toast.LENGTH_LONG).show();

		Log.i("msg", "MsgNotComplete已经成功保存入数据库" + list.size() + "项");
	}

	String UserListToString(List<User> list) {
		String result = "";
		User user;
		String uid, nickname;
		for (int i = 0; i < list.size(); i++) {
			user = list.get(i);
			uid = user.getUid();
			nickname = user.getNickname();
			if (i != list.size() - 1) {
				result += uid + ":" + nickname + ",";
			} else// 最后一条数据后面不加逗号分隔
			{
				result += uid + ":" + nickname;
			}
		}
		return result;
	}

	@Override
	public void onRefresh() {
		// 下拉刷新，从服务器获取数据
		getDataFromServer();
	}

	@Override
	public void onLoadMore() {
		Utils.showToast(context, "暂无更多消息");
	}

	@Override
	public void onClick(View v) {
		// txt_no_msg的点击事件
		getDataFromServer();
	}

	private void stopRefreshOrLoadMore() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	private void OpenOrCreateMsgNotCompleteTable(SQLiteDatabase db) {
		if (db == null) {
			Utils.showToast(context, "创建表失败！传入的数据库为空！");
			return;
		}
		db.execSQL("create table if not exists " + Utils.TABLE_MSG_NOT_COMPLETE
				+ " (_id integer primary key autoincrement," + " mid text not null," + " authorname text not null,"
				+ " content text not null," + " avatar text not null," + " time text not null,"
				+ " notifytime text not null," + " isOnTop integer," + " unreadList text not null,"
				+ " readList text not null," + "unique (mid) " + ")");

		// Cursor c = db.rawQuery("select * from " +
		// Utils.TABLE_MSG_NOT_COMPLETE, null);
		// if (c != null) {
		// String[] names = c.getColumnNames();
		// String string = "";
		//
		// for (int i = 0; i < names.length; i++) {
		// string += names[i] + " ";
		// }
		// Utils.showToast(context, string);
		// }
	}

	/**将该信息设置为已读**/
	private void setRead(int position)
	{
		JSONObject object =new JSONObject();
		try {
			object.put("uid",currentUser.getUid());
			UnFinishedBeans beans=adapter.getItem(position);
			object.put("mid",beans.getMid() );
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request =new JsonObjectRequest(Utils.READ_URL, object,
				new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				int code=-100;
				String msg;
				try {
					code = response.getInt("code");
					msg=response.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(code==0)
				{
					Utils.showToast(context, "更新为已读成功！");
				}else{
					Utils.showToast(context, "更新失败:message");
				}
						
			}
				
			} , new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
					}
				});
		
		MyApplication.getRequestQueue().add(request);
	}
}
