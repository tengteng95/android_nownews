package com.example.nownews.ui;

import com.example.nownews.R;
import com.htt.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MyActivity extends Activity {

	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myactivity);
	}
	
	public void doClick(View v){
		context= MyActivity.this;
		SQLiteDatabase db=context.openOrCreateDatabase(Utils.DB_NAME+"_", Context.MODE_PRIVATE, null);
		db.execSQL("create table if not exists " + Utils.TABLE_TEST
				+"("
				+"_id integer primary key autoincrement, "
				+"username text not null"
			    + ")");
		String username="username";
		db.execSQL("insert into "+ Utils.TABLE_TEST +"(username)"+
				" values(" +Utils.saveTextIntoDb(username)+")");

	}
}
