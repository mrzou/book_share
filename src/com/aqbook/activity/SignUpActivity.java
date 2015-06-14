package com.aqbook.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity<signUpButton> extends Activity {
	
	private RequestQueue signUpQueue;
	private ProgressDialog mProgressDialog;
	
	EditText phonEditText;
	EditText passwordEditText;
	Button signUpButton;
	Button button;
	EditText passwordConfirmEditText;
	
	private static String APPKEY = "806cb6d938b4";
	private static String APPSECRET = "5c627357ef651ecc7657974e4a235101";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sign_up);
		initSendMessageSdk();
		
		signUpQueue = Volley.newRequestQueue(this);
		phonEditText = (EditText) findViewById(R.id.phone_edit);
		passwordEditText = (EditText) findViewById(R.id.password_edit);
		passwordConfirmEditText = (EditText) findViewById(R.id.confirm_password_edit);
		signUpButton = (Button) findViewById(R.id.sign_up);
		button = (Button) findViewById(R.id.sendValidCode);
		
		TextView textView1 = (TextView) findViewById(R.id.return_main_activity);
		TextView textView2 = (TextView) findViewById(R.id.scan_book);
		Typeface iconfont = Typeface.createFromAsset(getAssets(), "icon_little.ttf");
		Typeface iconfont1 = Typeface.createFromAsset(getAssets(), "logup.ttf");
		textView1.setTypeface(iconfont);
		textView2.setTypeface(iconfont1);
		
		setPhoneLostFocus();
		validatePassword();
	}
	//验证密码长度及是否一致
	public void validatePassword(){
		passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(!arg1){
					if(passwordEditText.length()<6||passwordEditText.length()>12){
						Toast.makeText(SignUpActivity.this, "密码长度不符合", Toast.LENGTH_SHORT).show();
						if(signUpButton.isEnabled()){signUpButton.setEnabled(false);}
					}else{
						signUpButton.setEnabled(true);
					}
				}
			}
			
		});
		passwordConfirmEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(!arg1){
					if(!passwordConfirmEditText.getText().toString().equals(passwordEditText.getText().toString())){
						Toast.makeText(SignUpActivity.this, "确认密码不一样", Toast.LENGTH_SHORT).show();
						if(signUpButton.isEnabled()){signUpButton.setEnabled(false);}
					}else{
						signUpButton.setEnabled(true);
					}
				}
			}
			
		});
	}
	//电话失去焦点是监听是否有一致的号码
	public void setPhoneLostFocus(){
		phonEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {  
		    @Override  
		    public void onFocusChange(View v, boolean hasFocus) {  
		        if(hasFocus){//获得焦点  
		            //在这里可以对获得焦点进行处理  
		        }else if(phonEditText.getText().toString().length()==11){//失去焦点
		        	validatePhoneNumber(true);
		        }else{
		        	button.setEnabled(false);
		        	Toast.makeText(SignUpActivity.this, "手机号长度不符合", 1).show();
		        	if(signUpButton.isEnabled()){signUpButton.setEnabled(false);}
		        }
		    }             
		});
	}
	//到后台去验证手机号码是否重复
	public void validatePhoneNumber(final boolean state){
		
		button.setEnabled(true);
    	Map<String, Object> phoneNumber = new HashMap<String, Object>();    
		phoneNumber.put("phone_number", phonEditText.getText().toString());    
		JSONObject jsonObject = new JSONObject(phoneNumber); 
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST,"http://192.168.1.106:3000/validate_messages/validatePhoneNumber", jsonObject,  
	        new Response.Listener<JSONObject>() {  
	            @Override  
	            public void onResponse(JSONObject response) {  
	            	signUpButton = (Button) findViewById(R.id.sign_up);
	            	try {
						if(response.get("state").equals("error")){
							Toast.makeText(SignUpActivity.this, "手机号已注册", Toast.LENGTH_SHORT).show();
							button.setEnabled(false);
							if(signUpButton.isEnabled()){signUpButton.setEnabled(false);}
						}else{
							button.setEnabled(true);
							signUpButton.setEnabled(true);
							if(!state){
								SMSSDK.getVerificationCode("86",phonEditText.getText().toString());	
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }  
	        }, new Response.ErrorListener() {  
	            @Override  
	            public void onErrorResponse(VolleyError error) {  
	                Log.v("TAG", error.getMessage(), error);  
	            }  
	        });  
		int socketTimeout = 30000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonObjectRequest.setRetryPolicy(policy);
		signUpQueue.add(jsonObjectRequest);
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
	               }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
	            	 //获取验证码成功
	               }else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
	               //返回支持发送验证码的国家列表
	               }
              }
            } 
        }; 
        SMSSDK.registerEventHandler(eh); //注册短信回调
	}
	//点击验证码按钮
	public void sendValidCode(View view){
		Log.v("TAG", "aaaaaaaaaaaa");
        String phString=phonEditText.getText().toString();
        if(phString.length()==11){
        	Log.v("TAG", "bbbbbbbbbbbbbb");
        	button.setEnabled(true);
        	validatePhoneNumber(false);
		}else{
			Toast.makeText(this, "电话长度不正确", 1).show();
			button.setEnabled(false);
		}
	}
	//注册
	public void clickToSignUp(View view) throws TimeoutException {
		EditText acount_name = (EditText) findViewById(R.id.acount_edit);
		EditText validEdit = (EditText) findViewById(R.id.valid_edit);
		if(acount_name.getText().toString().equals("")){
			Toast.makeText(this, "用户名不能为空", 1).show();
			signUpButton.setEnabled(false);
		}else if(!validEdit.getText().toString().equals("")){
			validateCode(phonEditText.getText().toString());
			
		}else{
			Toast.makeText(SignUpActivity.this, "验证码不能为空", 1).show();
		}
	}
	
	//发送确认的验证码
	public void validateCode(String number) throws TimeoutException{
		EditText validEdit = (EditText) findViewById(R.id.valid_edit);
		if(TextUtils.isEmpty(validEdit.getText().toString())){
			Toast.makeText(this, "验证码不能为空", 1).show();
			if(signUpButton.isEnabled()){signUpButton.setEnabled(false);}
		}else{
			signUpButton.setEnabled(true);
			String params = "appkey=806cb6d938b4&phone="+number+"&zone=86"+"&code="+validEdit.getText().toString();
			
			StringRequest stringRequest = new StringRequest("https://api.sms.mob.com/sms/verify?"+params, 
					new Response.Listener<String>() {  
			            @Override  
			            public void onResponse(String response) {  
			            	try {
								JSONObject stringToJson = new JSONObject(response);
								if(!stringToJson.get("status").toString().equals("200")){
									Toast.makeText(SignUpActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
								}else{
									excuteSignUp();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            }  
			        }, new Response.ErrorListener() {  
			            @Override  
			            public void onErrorResponse(VolleyError error) {  
			                Log.v("TAG", error.getMessage(), error);  
			            }  
			        });
			int socketTimeout = 3000;//30 seconds - change to what you want
			RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			stringRequest.setRetryPolicy(policy);
			synchronized (signUpQueue) { signUpQueue.add(stringRequest); }
		}
	}
	//验证码正确后执行的注册功能
	public void excuteSignUp(){
		showProgressDialog();
		EditText acount_name = (EditText) findViewById(R.id.acount_edit);
		signUpButton.setEnabled(true);
		EditText password_confirm = (EditText)findViewById(R.id.confirm_password_edit);
		Map<String, Object> map = new HashMap<String, Object>();    
		map.put("phone_number", phonEditText.getText().toString());    
		map.put("password", passwordEditText.getText().toString());    
		map.put("password_confirmation", password_confirm.getText().toString());    
		map.put("name", acount_name.getText().toString());    
		JSONObject jsonObject = new JSONObject(map); 
		Map<String, Object> allMap = new HashMap<String, Object>();    
		allMap.put("user", jsonObject);
		JSONObject jsonObjectAll = new JSONObject(allMap); 
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST,"http://192.168.1.106:3000/users.json", jsonObjectAll,  
			new Response.Listener<JSONObject>() {  
				@Override  
				public void onResponse(JSONObject response) {  
					try {
						if(response.get("state").toString().equals("error")){
							Toast.makeText(SignUpActivity.this, "注册失败!", 1).show();
						}else{
							mProgressDialog.cancel();
							Toast.makeText(SignUpActivity.this, "注册成功!", 1).show();
							String token = response.get("token").toString();
							String user_id = response.get("user_id").toString();
							saveTokenToPreference(token, user_id);
							SignUpActivity.this.onBackPressed();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(SignUpActivity.this, "注册错误!", 1).show();
					}  
				}  
			}, new Response.ErrorListener() {  
				@Override  
				public void onErrorResponse(VolleyError error) {  
					Log.v("TAG", error.getMessage(), error);  
				}  
		});
		int socketTimeout = 30000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonObjectRequest.setRetryPolicy(policy);
		signUpQueue.add(jsonObjectRequest);
	}
	//保存token的消息
	public void saveTokenToPreference(String token, String user_id){
		SharedPreferences.Editor editor = getSharedPreferences("token", MODE_PRIVATE).edit();
		editor.putString("token", token);
		editor.putString("user_id", user_id);
		editor.commit();
	}
	// 开启注册进度条
	private void showProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("注册中...");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();
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
			if (result == SMSSDK.RESULT_COMPLETE) {
				//短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
//					textView2.setText("提交验证码成功");
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
					Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
//					textView2.setText("验证码已经发送");
				}
				new CountDownTimer(60000, 1000) {

				     public void onTick(long millisUntilFinished) {
				    	 button.setEnabled(false);
				         button.setText("重新获取("+String.valueOf(millisUntilFinished / 1000)+"s)");
				     }

				     public void onFinish() {
				         button.setText("重新获取");
				         button.setEnabled(true);
				     }
				  }.start();
			}else{
				Toast.makeText(getApplicationContext(), "手机号码错误!", 2).show();
			}
			
		}
		
	};
	//点击布局空白处可去掉Edittext的焦点，同时收缩输入键盘
	//@Override
    public boolean onTouchEvent(MotionEvent event) {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.sigUpAllLayout);
	    InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
	    //login_auto_account.setCursorVisible(false);
	    linearLayout.setFocusable(true);
        linearLayout.setFocusableInTouchMode(true);
        linearLayout.requestFocus();
	    imm.hideSoftInputFromWindow(phonEditText.getWindowToken(), 0); 
        return super.onTouchEvent(event);
    }
}
