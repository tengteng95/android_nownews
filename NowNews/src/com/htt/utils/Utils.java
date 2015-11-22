package com.htt.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.example.nownews.beans.User;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.widget.Toast;

public class Utils {

	public static final String DB_NAME="msg.db";
	/**
	 * 每个用户都有一个已完成表，一个未完成表，和一个用户发送消息表
	 * 此外，对于所有用户共用一个近期登陆用户信息表
	 */
	
	//未完成信息表的名称
	public static final String TABLE_MSG_NOT_COMPLETE="table_msgNotcomplete";
	//已完成信息表的名称
	public static final String TABLE_MSG_COMPLETE="table_msgcomplete";
	//近期登陆用户信息表
	public static final String TABLE_RECENT_USER="table_userRecent";
	//用户发送的消息表
	public static final String TABLE_USER_SEND="table_user_send";
	public static final String TABLE_TEST="table_test";
	
	public static String getTableName(String tablename,User currentUser)
	{
		String result;
		if(tablename.equals(TABLE_MSG_NOT_COMPLETE)
				|| tablename.equals(TABLE_MSG_COMPLETE)
				|| tablename.equals(TABLE_USER_SEND))
		{
			result=tablename+currentUser.getUsername();
		}else if(tablename.equals(TABLE_RECENT_USER))
		{//近期登陆用户是所有用户共享的表
			result=tablename;
		}else{
			return null;
		}
		return result;
	}
	//注册
	public static final String REGISTER_URL="http://10.12.67.141/api/user/register";
	//登陆
	public static final String SIGN_IN_URL="http://10.12.67.141/api/user/login";
	//发送消息URL
	public static final String SEND_MESSAGE_URL="http://10.12.67.141/api/message";
	//获取所有消息
	public static final String GET_MESSAGE_URL="http://10.12.67.141/api/message";
	public static final String GET_MESSAGE_UNFINISHED="http://10.12.67.141/api/message/unfinished";
	public static final String GET_MESSAGE_FINISHED="http://10.12.67.141/api/message/finished";
	//获取用户发送的所有消息的列表的URL
	public static final String GET_USER_SEND_LIST_URL="http://10.12.67.141/api/user/message";
	
	public static final String UNREAD_URL="http://10.12.67.141/api/message/unread";
	public static final String READ_URL="http://10.12.67.141/api/message/read";
	
	//把上一次下载过的数据保存进数据库，关闭应用再进入时，不必重新请求连接
	public static SQLiteDatabase create(Context context ,String dbname)
	{
		return context.openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
	}
	
	public static void createTable(SQLiteDatabase db,String tablename)
	{
		
	}
	
	
	/**
	 * 判断网络是否可用
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	         if (mNetworkInfo != null) {  
	             return mNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
	 }
	
	/**
	 * 显示Toast消息
	 * @param context
	 * @param string
	 */
	public static void  showToast(Context context, String string)
	{
		Toast.makeText(context, string, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 判断一个字符串是否为空串
	 * @param string
	 * @return
	 */
	public static boolean IsStringNull(String string){
		if(string!=null && !string.equals(""))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 返回用单引号引起后的字符串，用以存入数据库
	 * @param source
	 * @return
	 */
	public static String saveTextIntoDb(String source)
	{
		return "'"+source+"'";
	}
	
	/**
	 * 生成指定格式的时间串并返回 类似：2015-11-14T16:57:52.352Z
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @return
	 */
	public static String getDateString(int year,int month,int day, int hour,int min)
	{
		String date = year+"-"+month+"-"+day
				        +"T"+hour+":"+min+":"+"00.000"+"Z";
		return date;
	}
	
	/**
	 * 按照指定格式解析时间串，并返回
	 * @param date
	 * @return
	 */
	public static int[] parseDateString(String date)
	{
		int []datearray = new int[5];
		String[] temp = date.split("-");
		if(temp.length!=3)
		{
			return null;
		}else{
			datearray[0]=Integer.parseInt(temp[0]);//获取“年”信息
			datearray[1]=Integer.parseInt(temp[1]);//获取“月”信息
		}
		
		String[] temp2 =temp[2].split("T");
		if(temp2.length!=2)
		{
			return null;
		}
		datearray[2]=Integer.parseInt(temp2[0]);//获取“日”信息
		String temp3[]=temp2[1].split(":");
		if(temp3.length!=3)
		{
			return null;
		}
		datearray[3]=Integer.parseInt(temp3[0]);
		datearray[4]=Integer.parseInt(temp3[1]);
		return datearray;
		
	}
	
	public static Drawable getDrawable(Context context, int drawable_id)
	{
		Drawable drawable=context.getResources().getDrawable(drawable_id);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		return drawable;
	}
	
	public static String getDateFromTimeMillis(long time)
	{
		Date date= new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分");
		return formatter.format(date);
	}
	
	public static long getMillis(int year,int month,int day,int hourofday ,int min)
	{
		long result=0;
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hourofday);
		calendar.set(Calendar.MINUTE, min);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		result=calendar.getTimeInMillis();
		Log.i("time", getDateFromTimeMillis(result));
		Log.i("time", "result:"+result);
		return result;
		
	}
	

	
	
}
