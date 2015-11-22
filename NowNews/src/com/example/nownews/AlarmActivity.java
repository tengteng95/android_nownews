package com.example.nownews;

import java.io.IOException;
import java.lang.reflect.Field;

import com.htt.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

public class AlarmActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        final Ringtone r = RingtoneManager.getRingtone(AlarmActivity.this,notification);
//        setRingtoneRepeat(r);
//        r.play();
        //final MediaPlayer mediaPlayer =new MediaPlayer();
//        AssetManager assetManager=getResources().getAssets();
//        assetManager.
//        mediaPlayer.setDataSource(AlarmActivity.this,R.raw.skycity);
        final MediaPlayer mediaPlayer=MediaPlayer.create(AlarmActivity.this, R.raw.skycity);
        try {
			mediaPlayer.prepare();
			mediaPlayer.setLooping(true);//设置成循环播放
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        mediaPlayer.start();
        
        
        Intent intent=getIntent();
        String content=intent.getStringExtra("content");
        if(Utils.IsStringNull(content))
        {//空串
        	content="时间到了！";
        }
        //显示对话框
        AlertDialog dialog=new AlertDialog.Builder(AlarmActivity.this).
            setTitle("闹钟").//设置标题
            setMessage(content).//设置内容
            setPositiveButton("知道了", new OnClickListener(){//设置按钮
                public void onClick(DialogInterface dialog, int which) {
                    AlarmActivity.this.finish();//关闭Activity
//                    r.stop();
                    mediaPlayer.stop();
                    
                }
            }).create();
        dialog.setCancelable(false);
        dialog.show();
        
        
    }
    
    private void setRingtoneRepeat(Ringtone ringtone){
        Class<Ringtone> clazz = Ringtone.class;
        try {
            Field audio = clazz.getDeclaredField("mAudio");
            audio.setAccessible(true);
            MediaPlayer target = (MediaPlayer) audio.get(ringtone);
            target.setLooping(true);
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    

}
