package com.aqbook.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aqbook.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity {
	
	private RequestQueue signUpQueue;
	
	EditText phonEditText;
	
	private static String APPKEY = "806cb6d938b4";
	private static String APPSECRET = "5c627357ef651ecc7657974e4a235101";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sign_up);
		initSendMessageSdk();
		
		phonEditText = (EditText) findViewById(R.id.phone_edit);
		TextView textView1 = (TextView) findViewById(R.id.return_main_activity);
		TextView textView2 = (TextView) findViewById(R.id.scan_book);
		Typeface iconfont = Typeface.createFromAsset(getAssets(), "icon_little.ttf");
		Typeface iconfont1 = Typeface.createFromAsset(getAssets(), "logup.ttf");
		textView1.setTypeface(iconfont);
		textView2.setTypeface(iconfont1);
		
		findViewById(R.id.sign_up);
	}
	
	public void click_to_back(View view){
		this.onBackPressed();
	}
//	初始化短信通知的sdk
	public void initSendMessageSdk(){
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
        EventHandler eh=new EventHandler(){
 
            @Override
            public void afterEvent(int event, int result, Object data) {
            	
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
 
               if (result == SMSSDK.RESULT_COMPLETE) {
            	   
	               //回调完成
	               if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
	            	   Toast.makeText(SignUpActivity.this, "已发送验证码,请注意接收", Toast.LENGTH_SHORT).show();
	               }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
	            	 //获取验证码成功
	               }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
	               //返回支持发送验证码的国家列表
	               } 
              }else{                                                                 
            	  ((Throwable)data).printStackTrace(); 
              }
            } 
        }; 
        SMSSDK.registerEventHandler(eh); //注册短信回调
	}
	public void sendValidCode(View view){
		
        String phString=phonEditText.getText().toString();
		if(!TextUtils.isEmpty(phString)){
			SMSSDK.getVerificationCode("86",phonEditText.getText().toString());
		}else {
			Toast.makeText(this, "电话不能为空", 1).show();
		}

	}
//	注册
	public void clickToSignUp(View view) {
		validateCode(phonEditText.getText().toString());
		
		signUpQueue = Volley.newRequestQueue(this);
		Map<String, Object> map = new HashMap<String, Object>();    
		map.put("email", "12345678@x.com");    
		map.put("password", "12345678");    
		JSONObject jsonObject = new JSONObject(map); 
		Map<String, Object> allMap = new HashMap<String, Object>();    
		allMap.put("user", jsonObject);
		JSONObject jsonObjectAll = new JSONObject(allMap); 
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST,"http://192.168.1.106:3000/users/sign_in", jsonObjectAll,  
		        new Response.Listener<JSONObject>() {  
		            @Override  
		            public void onResponse(JSONObject response) {  
		                Log.d("TAG", response.toString());  
		            }  
		        }, new Response.ErrorListener() {  
		            @Override  
		            public void onErrorResponse(VolleyError error) {  
		                Log.e("TAG", error.getMessage(), error);  
		            }  
		        });  
		int socketTimeout = 30000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonObjectRequest.setRetryPolicy(policy);
		signUpQueue.add(jsonObjectRequest);
	}
	//发送确认的验证码
	public void validateCode(String number){
		EditText validEdit = (EditText) findViewById(R.id.valid_edit);
		if(TextUtils.isEmpty(validEdit.getText().toString())){
			Toast.makeText(this, "验证码不能为空", 1).show();
		}else{
			String params = "appkey=806cb6d938b4&phone="+number+"&zone=86"+"&code="+validEdit.getText().toString();
			
			StringRequest stringRequest = new StringRequest("https://api.sms.mob.com/sms/verify?"+params, 
					new Response.Listener<String>() {  
			            @Override  
			            public void onResponse(String response) {  
			                Log.v("TAG", response);  
			            }  
			        }, new Response.ErrorListener() {  
			            @Override  
			            public void onErrorResponse(VolleyError error) {  
			                Log.v("TAG", error.getMessage(), error);  
			            }  
			        });
			int socketTimeout = 30000;//30 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			stringRequest.setRetryPolicy(policy);
			signUpQueue.add(stringRequest);
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("TAG", "event="+event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
//					textView2.setText("提交验证码成功");
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
//					textView2.setText("验证码已经发送");
				}
			}
			
		}
		
	};


}
