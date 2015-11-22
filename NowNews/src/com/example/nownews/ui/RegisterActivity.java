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
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RegisterActivity extends Activity implements OnClickListener {

	
	DropEditText drop_edit_username;
	EditText  et_password;
	TextView txt_register;
	String username, password;
	
	ProgressBar progressBar;
	TextView txt_progressbar;
	
	TextView txt_error_log;
	CommonAdapter<User> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);

		progressBar=(ProgressBar) findViewById(R.id.register_activity_progressbar);
		txt_progressbar=(TextView) findViewById(R.id.register_activity_txt_progrssbar);
		txt_register = (TextView) findViewById(R.id.register_activity_txt_register);
		txt_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		//et_username = (EditText) findViewById(R.id.register_activity_et_username);
		et_password = (EditText) findViewById(R.id.register_activity_et_password);

		txt_register.setOnClickListener(this);

		txt_error_log = (TextView) findViewById(R.id.register_activity_txt_error_log);
		
		drop_edit_username=(DropEditText) findViewById(R.id.register_activity_drop_edit_username);

		
		
	}

	
	/**
	 * 将用户插入到近期登陆列表中
	 * @param username
	 */
	private void insertUserIntoRecent(String uid,String username){
		SQLiteDatabase db = openOrCreateDatabase(Utils.DB_NAME, MODE_PRIVATE, null);
		//将用户名插入表中
		db.execSQL("insert into "+Utils.TABLE_RECENT_USER+"(uid,username,password,nickname)"
		         +" values(" + Utils.saveTextIntoDb(uid)+","
		         + Utils.saveTextIntoDb(username)+","
		         +Utils.saveTextIntoDb("")+","
		         +Utils.saveTextIntoDb("")+")");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_activity_txt_register:
			username = drop_edit_username.getText().toString();
			password = et_password.getText().toString();
			// 如果两者都不为空
			if (false == Utils.IsStringNull(username) && false == Utils.IsStringNull(password)) {
				// 进一步判断用户名格式是否正确：只能有字母和数字组成，且长度应在[6,16]之间
				if ((true == username.matches("[a-zA-Z0-9]+")) && username.length() >= 6 && username.length() <= 16) { // 还需进一步判断密码的格式是否正确
					if ((true == password.matches("[a-zA-Z0-9]+")) && password.length() >= 6
							&& password.length() <= 16) {// 格式均符合要求，可以向服务器发起注册请求
						register(username, password,username);//最后一项为nickname,默认等于username

					}

				} else {
					Utils.showToast(this, "用户名格式不正确！请输入6到16位的仅有字母和数字组成的用户名！");
				}

			} else {
				Utils.showToast(RegisterActivity.this, "用户名或密码不能为空！");
			}

			break;

		default:
			break;
		}

	}

	/**
	 * 向服务器发出注册请求
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private void register(final String username, final String password, final String nickname) {
		final MyApplication application = (MyApplication) getApplication();
		// 第二个参数是JsonObject类型，如果该参数为null，表示get请求，否则使用Post请求，这里我们要使用Post请求。
		JSONObject object = new JSONObject();
		try {
			object.put("username", username);
			object.put("password", password);
			object.put("nickname", nickname);// 默认用户名和nickname一致
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JsonObjectRequest request = new JsonObjectRequest(Utils.REGISTER_URL, object, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				int code =-100;
				String message=null;
				String uid =null;
				//提取服务器返回状态码和message信息
				try {
					code = response.getInt("code");
					message = response.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if(code == 0)
				{   //处理成功
					// 注册成功以后，跳转到主界面,不要忘了设置当前用户，并且还要将用户插入近期登陆表中
					try {
						uid =response.getString("uid");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					User user = new User(uid, username, password, nickname);
					application.setCurrentUser(user);//设置当前用户
					insertUserIntoRecent(username,uid);//插入近期登陆表
					startActivity(new Intent(RegisterActivity.this,MainActivity.class));
				}else{//显示出错原因
					Utils.showToast(RegisterActivity.this, message);
				}
				//隐藏进度条
				progressBar.setVisibility(View.GONE);
				txt_progressbar.setVisibility(View.GONE);

			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// 注册失败，给出失败原因
//				if (error.networkResponse.statusCode != 200) {
//					Utils.showToast(RegisterActivity.this, "注册失败,用户名已存在");
//				}
				Utils.showToast(RegisterActivity.this,"注册失败！" );
				//隐藏进度条
				progressBar.setVisibility(View.GONE);
				txt_progressbar.setVisibility(View.GONE);
			}
		});
		//将进度条设置为可见
		progressBar.setVisibility(View.VISIBLE);
		txt_progressbar.setVisibility(View.VISIBLE);
		MyApplication.getRequestQueue().add(request);// 将其添加到请求队列
	}
	
}
