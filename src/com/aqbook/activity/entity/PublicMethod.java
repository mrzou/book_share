package com.aqbook.activity.entity;

import com.aqbook.activity.MainActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class PublicMethod {
	//判断是否有网络
	public static String domainName = "http://android-ruby.herokuapp.com/";
    public static boolean isNetworkConnected(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null) {  
                return mNetworkInfo.isAvailable();  
            }  
        }  
        return false;  
    }
    //弹出窗口的封装
    public static AlertDialog.Builder makeDialog(final Context context, DialogInterface.OnClickListener sureListener, DialogInterface.OnClickListener cancelListener ){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("提示!");
		dialog.setMessage("确定退出?");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", sureListener);
		dialog.setNegativeButton("取消", cancelListener);
		return dialog;
    }
    //弹出窗口的封装
    public static AlertDialog.Builder makeAlertDialog(final Context context, DialogInterface.OnClickListener sureListener, String message){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("提示!");
		dialog.setMessage(message);
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", sureListener);
		return dialog;
    }
}
