package com.aqbook.activity.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aqbook.R;
import com.aqbook.activity.MainActivity;
import com.aqbook.activity.SignUpActivity;
import com.aqbook.activity.entity.CustomProgress;
import com.aqbook.activity.entity.PublicMethod;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FourFragment extends Fragment implements OnClickListener{

	View view;
	Button signOutView;
	TextView signUpButton;
	Button signInButton;
	private boolean if_success = false;
	private RequestQueue signInQueue;
	private boolean anotherUser = false;
	
	LayoutInflater inflatera;
	ViewGroup containera;
	Bundle savedInstanceStatea;
	
	public FourFragment(){
		
	}
	
	public FourFragment(boolean anotherUser){
		this.anotherUser = anotherUser;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflatera = inflater;
		containera = container;
		savedInstanceStatea = savedInstanceState;
		
		signInQueue = Volley.newRequestQueue(getActivity());
		testIfToken(inflater);
		return view;
	}
	//登陆注册按钮
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.logUpUser:
			Intent intent = new Intent(getActivity(), SignUpActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_login:
			userLogin();
			break;
		}
	}
	//手机　密码检测
	public void userLogin(){
		String userPhone = ((EditText) view.findViewById(R.id.log_in_acount_edit)).getText().toString();
		String userPassword = ((EditText) view.findViewById(R.id.log_in_password_edit)).getText().toString();
		int passwordLength = userPassword.length();
		if(userPhone.length() != 11){
			Toast.makeText(getActivity(), "电话号码不正确", 1).show();
		}else if(passwordLength<6||passwordLength>13){
			Toast.makeText(getActivity(), "密码长度错误", 1).show();
		}else{
			userToLogin(userPhone, userPassword);
		}
	}
	//提交到后台
	public void userToLogin(String userPhone, String userPassword){
		Map<String, Object> map = new HashMap<String, Object>();    
		map.put("phone_number", userPhone);    
		map.put("password", userPassword);    
		JSONObject jsonObject = new JSONObject(map); 
		Map<String, Object> allMap = new HashMap<String, Object>();    
		allMap.put("user", jsonObject);
		JSONObject jsonObjectAll = new JSONObject(allMap);
		if(PublicMethod.isNetworkConnected(getActivity())){
			makeDialog();
			JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST,"http://192.168.1.106:3000/users/sign_in", jsonObjectAll,  
				new Response.Listener<JSONObject>() {  
					@Override  
					public void onResponse(JSONObject response) {  
						try {
							if(!response.get("state").toString().equals("error")){
								SharedPreferences.Editor editor = getActivity().getSharedPreferences("token", 0).edit();
								editor.putString("token", response.get("token").toString());
								editor.putString("user_id", response.get("user_id").toString());
								editor.commit();
								if_success = true;
							}else{
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if_success = false;
						}  
					}  
				}, new Response.ErrorListener() {  
					@Override  
					public void onErrorResponse(VolleyError error) {  
						if_success = false;
					}  
			});
			int socketTimeout = 30000;//30 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			jsonObjectRequest.setRetryPolicy(policy);
			signInQueue.add(jsonObjectRequest);
		}else{
			Toast.makeText(getActivity(), "网络没有连接", 1).show();
		}
	}
	public void makeDialog(){
		final CustomProgress dialog = CustomProgress.show(getActivity(), "登录中...", true, null); 
		new Handler().postDelayed(new Runnable(){  
	        public void run(){    
	            //等待10000毫秒后销毁此页面，并提示登陆成功  
	            dialog.cancel();  
	            if(if_success){
	            	Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();  
	            	
	            	getActivity().finish();
	            	Intent intent = new Intent(getActivity(), MainActivity.class);
	            	intent.putExtra("id", 3);
	            	startActivity(intent);
	            	if(anotherUser){
	            		MainActivity.setIndexPager(2);
	            	}else{
	            		MainActivity.setIndexPager(3);
	            	}
	            }else{
	            	Toast.makeText(getActivity(), "登陆失败!", 1).show();
	            }
	        }  
	    }, 2500); 
	}
	//根据是否有token决定是否显示用户信息
	public void testIfToken(LayoutInflater inflater){
		SharedPreferences token = getActivity().getSharedPreferences("token", 0);
		if(token.getString("token", "").equals("")){
			view = inflater.inflate(R.layout.fragment_four, null);
			signUpButton = (TextView) view.findViewById(R.id.logUpUser);
			signInButton = (Button) view.findViewById(R.id.btn_login);
			signInButton.setOnClickListener(this);
			signUpButton.setOnClickListener(this);
		}else{
			view = inflater.inflate(R.layout.personal_message, null);
			setPicture();
		}
	}
	public void setPicture(){
		TextView already_come_out = (TextView) view.findViewById(R.id.already_come_out);
		TextView collect_book = (TextView) view.findViewById(R.id.collect_book);
		TextView goto_already_come_out = (TextView) view.findViewById(R.id.goto_already_come_out);
		TextView goto_collect_book = (TextView) view.findViewById(R.id.goto_collect_book);
		signOutView = (Button) view.findViewById(R.id.sign_out);
		Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "icon_little.ttf");
		Typeface messageIcont = Typeface.createFromAsset(getActivity().getAssets(), "message.ttf");
		Typeface sign_out = Typeface.createFromAsset(getActivity().getAssets(), "sign_out.ttf");
		already_come_out.setTypeface(messageIcont);
		collect_book.setTypeface(messageIcont);
		goto_already_come_out.setTypeface(iconfont);
		goto_collect_book.setTypeface(iconfont);
		signOutView.setTypeface(sign_out);
		signOutListener();
	}
	//注册登出事件
	public void signOutListener(){
		signOutView.setOnClickListener(new OnClickListener(){
	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				confirmToLogOut();
			}
		});
	}
	public void confirmToLogOut(){
		PublicMethod.makeDialog(getActivity(), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = getActivity().getSharedPreferences("token", 0).edit();
				editor.putString("token", "");
				editor.putString("user_id", "");
				editor.commit();
				Toast.makeText(getActivity(), "用户退出成功", 1).show();
				
				getActivity().finish();
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				MainActivity.setIndexPager(3);
			}
		}, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
		}).show();
	}
	@Override
	public void onDetach(){
		super.onDetach();
		Log.v("TAG", "onDetach");
	}
}
