package com.aqbook.activity.entity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NetworkStateService extends Service{

	private static final String tag="tag";
	private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();  
                if(info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    Log.v("TAG", name);
                    if(!name.equals("WIFI")){
                    	Toast.makeText(getApplicationContext(), "当前环境得工作在局域网环境!", 1).show();
                    }else{
                    	Log.v("TAG", "mobie");
                    	WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);  
                    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
                    	Toast.makeText(getApplicationContext(), "当前环境为wifi环境", 1).show();
                    }
                    //doSomething()
                } else {
                	Toast.makeText(getApplicationContext(), "没有可用网络", 2).show();
                  //doSomething()
                }
            }
        }
    };
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
}
