package com.example.nownews;

import com.htt.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AlarmActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        if(Utils.IsStringNull(content))
        {//空串
        	content="时间到了！";
        }
        //显示对话框
        new AlertDialog.Builder(AlarmActivity.this).
            setTitle("闹钟").//设置标题
            setMessage(content).//设置内容
            setPositiveButton("知道了", new OnClickListener(){//设置按钮
                public void onClick(DialogInterface dialog, int which) {
                    AlarmActivity.this.finish();//关闭Activity
                }
            }).create().show();
        
        
    }
    
    

}
