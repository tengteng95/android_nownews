package com.example.nownews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
    	String content=intent.getStringExtra("content");//获取提醒内容
        Intent i=new Intent(context, AlarmActivity.class);
        i.putExtra("content", content);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
