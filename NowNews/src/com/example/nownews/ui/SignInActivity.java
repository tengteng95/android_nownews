package com.example.nownews.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nownews.MainActivity;
import com.example.nownews.MyApplication;
import com.example.nownews.R;
import com.example.nownews.beans.User;
import com.htt.commonadapter.CommonAdapter;
import com.htt.commonadapter.CommonViewHolder;
import com.htt.utils.Utils;
import com.htt.view.DropEditText;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SignInActivity extends Activity implements OnClickListener{
	
	DropEditText dt_username;
	EditText et_password;
	Button btn_signin;
	Button btn_register,btn_deleteDb;
	CommonAdapter<User> adapter;
	SQLiteDatabase db;
	ProgressBar progressbar;
	TextView txt_progressbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_activity);
		
		dt_username = (DropEditText) findViewById(R.id.signin_activity_drop_edit_username);
		initDropEdit();//为dropEdit添加下拉选项
		et_password=(EditText) findViewById(R.id.signin_activity_et_password);
		btn_signin=(Button) findViewById(R.id.signin_activity_btn_sign_in);
		btn_register=(Button) findViewById(R.id.signin_activity_btn_register);
		
		btn_signin.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		
		btn_deleteDb=(Button) findViewById(R.id.signin_activity_btn_deletedb);
		btn_deleteDb.setOnClickListener(this);
		
		progressbar=(ProgressBar) findViewById(R.id.signin_activity_progressbar);
		txt_progressbar=(TextView) findViewById(R.id.signin_activity_txt_progressbar);
	}
	
	/**
	 * 初始化DropEdit，使其支持下拉显示近期登陆用户
	 */
	private void initDropEdit()
	{
		final List<User> userlist =getRecentUserListFromDb();//获取近期登陆用户信息表
		adapter = new CommonAdapter<User>(SignInActivity.this, userlist, R.layout.item_simple_user) {
			
			@Override
			public void convert(CommonViewHolder holder, final User data) {
				//修改用户名
				holder.setText(R.id.item_user_username, data.getUsername());
				//设置删除点击事件
				ImageView img_delete = holder.getViewById(R.id.item_user_delete);
				img_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						userlist.remove(data);
						//还需要从数据库中删除该用户记录，以免下次还会读出
						deleteFromDb(data.getUsername());
						adapter.setList(userlist);//更新界面
					}
				});
			}
		};
		dt_username.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		String username,password;
		switch (v.getId()) {
		case R.id.signin_activity_btn_sign_in://登陆
			username = dt_username.getText().toString();
			password = et_password.getText().toString();
			progressbar.setVisibility(View.VISIBLE);
			txt_progressbar.setVisibility(View.VISIBLE);
			isMatch(username,password);
			break;
		case R.id.signin_activity_btn_register://注册
			startActivity(new Intent(SignInActivity.this,RegisterActivity.class));
			break;
		case R.id.signin_activity_btn_deletedb:
			db = SignInActivity.this.openOrCreateDatabase(Utils.DB_NAME,SignInActivity.this.MODE_PRIVATE,null);
			db.execSQL("drop table "+Utils.TABLE_MSG_NOT_COMPLETE);
			db.execSQL("drop table "+Utils.TABLE_RECENT_USER);
	
			Utils.showToast(SignInActivity.this,"数据库已删除" );
			break;
		default:
			break;
		}
		
	}
	
	/**
	 *判断输入的用户名和密码是否匹配
	 * @param username
	 * @param password
	 * @return
	 */
	boolean isMatch(final String username,final String password)
	{
		final MyApplication application = (MyApplication) getApplication();
		// 第二个参数是JsonObject类型，如果该参数为null，表示get请求，否则使用Post请求，这里我们要使用Post请求。
		JSONObject object = new JSONObject();
		try {
			object.put("username", username);
			object.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(Utils.SIGN_IN_URL, object, 
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int code=response.getInt("code");
							String message=response.getString("message");
							String uid;
							if(0==code)
							{//code为零表示匹配成功，
								uid=response.getString("uid");//获取用户的唯一标识uid
								//设置当前用户，跳转到主界面
								application.setCurrentUser(new User(uid, username, password, username));//设置当前用户
								insertUserIntoRecent(uid,username);
								startActivity(new Intent(SignInActivity.this,MainActivity.class));
								
								Utils.showToast(SignInActivity.this, "登陆成功");
							}else{//输出错误信息
								Utils.showToast(SignInActivity.this, "登录失败:"+message);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						progressbar.setVisibility(View.GONE);
						txt_progressbar.setVisibility(View.GONE);
					}
				}, new ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError error) {
						Utils.showToast(SignInActivity.this, "与服务器的通信异常！");
						progressbar.setVisibility(View.GONE);
						txt_progressbar.setVisibility(View.GONE);
					}
					
				});
		MyApplication.getRequestQueue().add(request);// 将其添加到请求队列
		return false;
		
	}
	
	/**
	 * 将用户插入到近期登陆列表中
	 * @param username
	 */
	private void insertUserIntoRecent(String uid,String username){
		SQLiteDatabase db = openOrCreateDatabase(Utils.DB_NAME, MODE_PRIVATE, null);
		//将用户名插入表中
		openOrCreateRecentTable(db);
		db.execSQL("insert into "+Utils.TABLE_RECENT_USER+"(uid,username,password,nickname)"
		         +" values(" + Utils.saveTextIntoDb(uid)+","
		         + Utils.saveTextIntoDb(username)+","
		         +Utils.saveTextIntoDb("")+","
		         +Utils.saveTextIntoDb("")+")");
	}
	
	/**
	 *从近期登陆用户表中删除指定用户名
	 * @param username
	 */
	private void deleteFromDb(String username)
	{
		SQLiteDatabase db = openOrCreateDatabase(Utils.DB_NAME, MODE_PRIVATE, null);
		openOrCreateRecentTable(db);
		db.execSQL("delete from "+Utils.TABLE_RECENT_USER+" where username="+"'"+username +"'");
	}
	
	/**
	 * 从数据库中读取近期登陆列表
	 * @return
	 */
	List<User> getRecentUserListFromDb()
	{
		List<User> userList = new ArrayList<User>();
		//打开数据库
		SQLiteDatabase db = openOrCreateDatabase(Utils.DB_NAME, MODE_PRIVATE, null);
		//（如果不存在）创建近期登陆用户信息表
		openOrCreateRecentTable(db);
		Cursor c =db.rawQuery("select * from "+Utils.TABLE_RECENT_USER,null);
		String username;
		User user;
		if(null!=c)
		{
			while(c.moveToNext())
			{
				username = c.getString(c.getColumnIndex("username"));
				user = new User(null, username, null, null);
				userList.add(user);
			}
			c.close();
		}
		return userList;
	}
	
	private void  openOrCreateRecentTable(SQLiteDatabase db) {
		if(db==null)
		{
			Utils.showToast(SignInActivity.this, "创建最近登录用户表失败：数据库没有初始化");
			return ;
		}
		db.execSQL("create table if not exists "+ Utils.TABLE_RECENT_USER+" (_id integer "+
				"primary key autoincrement, uid text not null, username text not null , "+
						"password text not null, nickname not null)");
	}
	
	

}
