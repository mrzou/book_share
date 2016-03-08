package com.aqbook.activity.fragment;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aqbook.R;
import com.aqbook.activity.MainActivity;
import com.aqbook.activity.MipcaActivityCapture;
import com.aqbook.activity.SignUpActivity;
import com.aqbook.activity.entity.CustomProgress;
import com.aqbook.activity.entity.EditTextWatcher;
import com.aqbook.activity.entity.PublicMethod;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TwoFragmnet extends Fragment {

	private final static int SCANNIN_GREQUEST_CODE = 1;
	View view;
	
	private boolean if_success;
	private RequestQueue mQueue;  
	private Activity activity;
	
	ArrayList<String> bookArray;
	private TextView textView1, textView2, textView3;
	private ImageView bookImg;
	TextView textViewOfAddbook;
	
	private EditText editText;
	private Button button;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.activity = getActivity();
		mQueue = Volley.newRequestQueue(activity);
		// 设置activity的布局
		view = inflater.inflate(R.layout.fragment_two, null);
		TextView textViewIcon = (TextView) view.findViewById(R.id.add_book_book);
		textViewOfAddbook = (TextView) view.findViewById(R.id.add_book_icon);
		Typeface iconfont = Typeface.createFromAsset(activity.getAssets(), "icon_little.ttf");
		
		textViewIcon.setTypeface(iconfont);
		textViewOfAddbook.setTypeface(iconfont);
		
		textViewOfAddbook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences token = getActivity().getSharedPreferences("token", 0);
				if(token.getString("token", "").equals("")){
					PublicMethod.makeAlertDialog(getActivity(), null, "还没登陆?").show();
				}else{
					Intent intent = new Intent();
					intent.setClass(activity, MipcaActivityCapture.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
				}
			}
		});
		editText = (EditText) view.findViewById(R.id.enjoyText);
		button = (Button) view.findViewById(R.id.comeOutBook);
		onclickToAddBookListener(button);
		editText.addTextChangedListener(new EditTextWatcher(button));
		return view;
	}
	
	public void onclickToAddBookListener(Button submitButton){
		submitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				showProgressDialog();
				SharedPreferences token = getActivity().getSharedPreferences("token", 0);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", bookArray.get(1));
				map.put("author", bookArray.get(2));
				map.put("image_url", bookArray.get(0));
				map.put("description", bookArray.get(3));
				JSONObject jsonObject = new JSONObject(map);
				Map<String, Object> allMap = new HashMap<String, Object>();    
				allMap.put("book", jsonObject);
				allMap.put("user_id", token.getString("user_id", ""));
				JSONObject jsonObjectAll = new JSONObject(allMap); 
				// TODO Auto-generated method stub
				JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST,"http://192.168.1.106:3000/manager_books", jsonObjectAll,  
				        new Response.Listener<JSONObject>() {  
				            @Override  
				            public void onResponse(JSONObject response) {  
				            	try {
				            		if(response.getString("state").equals("success")){
				            			if_success = true;
				            		}else{
				            			if_success = false;
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
					mQueue.add(jsonObjectRequest);
			}
			
		});
	}
	//展示黑影
	private void showProgressDialog() {
		final CustomProgress dialog = CustomProgress.show(getActivity(), "发布中...", true, null); 
		new Handler().postDelayed(new Runnable(){  
	        public void run(){    
	            //等待10000毫秒后销毁此页面，并提示登陆成功  
	            dialog.cancel();  
	            if(if_success){
	            	Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();  
	            	
	            	getActivity().finish();
	            	Intent intent = new Intent(getActivity(), MainActivity.class);
	            	startActivity(intent);
	            	MainActivity.setIndexPager(1);
	            }else{
	            	Toast.makeText(getActivity(), "发布的书重复!", 2).show();
	            }
	        }  
	    }, 2500);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == -1){
			/*	Bundle bundle = data.getExtras();
			
				mTextView.setText(bundle.getString("result"));*/
				Bundle bundle = data.getExtras();
				sendHttpRequest(bundle.getString("result"));
			}
			break;
		}
    }	
	public void sendHttpRequest(String ibsn){
		StringRequest stringRequest = new StringRequest("http://api.douban.com/book/subject/isbn/"+ibsn,  
                new Response.Listener<String>() {  
                    @Override  
                    public void onResponse(String response) {  
						try {
							setBookMessage(response);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }  
                }, new Response.ErrorListener() {  
                    @Override  
                    public void onErrorResponse(VolleyError error) {  
                        Log.e("TAG", error.getMessage(), error);  
                    }  
                });
		int socketTimeout = 30000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		stringRequest.setRetryPolicy(policy);
		mQueue.add(stringRequest);
	}
	@SuppressLint("ResourceAsColor")
	public void setBookMessage(String bookMessage) throws IOException, ParserConfigurationException, SAXException{
		LinearLayout scanBookLinearLayout = (LinearLayout) view.findViewById(R.id.scan_book_id);
        scanBookLinearLayout.removeViewAt(0);
        scanBookLinearLayout.setPadding(50, 20, 40, 10);
        
        bookArray = xmlStringToDocument(bookMessage);
        final ImageView bookImg = new ImageView(activity);
        bookImg.setLayoutParams(new LayoutParams(270, 360));
        bookImg.setBackgroundColor(R.color.white);
        
        ImageRequest imageRequest = new ImageRequest(bookArray.get(0), new Response.Listener<Bitmap>() {  
            @Override  
            public void onResponse(Bitmap response) {  
                bookImg.setImageBitmap(response);  
            }  
        },270, 360, Config.RGB_565, new Response.ErrorListener() {  
            @Override  
            public void onErrorResponse(VolleyError error) {  
            }  
        });  
        int socketTimeout = 50000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		imageRequest.setRetryPolicy(policy);
        mQueue.add(imageRequest);
        
        LinearLayout bookIntroduce = setBookIntroduce();
        scanBookLinearLayout.addView(bookImg);
        scanBookLinearLayout.addView(bookIntroduce);
        editText.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        button.setEnabled(false);
	}
	public LinearLayout setBookIntroduce(){
		LinearLayout linearLayout = new LinearLayout(activity);
		linearLayout.setLayoutParams(new LayoutParams(370, 560));
		linearLayout.setOrientation(1);
//		书名
		textView1 = new TextView(activity);
		textView1.setLayoutParams(new LayoutParams(300, 45));
		textView1.setText(bookArray.get(1));
//		作者
		textView2 = new TextView(activity);
		textView2.setLayoutParams(new LayoutParams(300, 45));
		textView2.setText(bookArray.get(2));
//		书的介绍
		textView3 = new TextView(activity);
		textView3.setLayoutParams(new LayoutParams(400, 270));
		textView3.setText(bookArray.get(3));
//		发布图书的按钮
		
		linearLayout.addView(textView1);
		linearLayout.addView(textView2);
		linearLayout.addView(textView3);
		
		return linearLayout;
	}
//	解析xml字符串
	@TargetApi(Build.VERSION_CODES.FROYO) public ArrayList<String> xmlStringToDocument(String xmlString) throws IOException, ParserConfigurationException, SAXException {
		
		ArrayList<String> newBookArray = new ArrayList<String>();
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		InputSource is=new InputSource(new StringReader(xmlString));
		org.w3c.dom.Document dom=builder.parse(is);
		newBookArray.add(dom.getElementsByTagName("link").item(2).getAttributes().getNamedItem("href").getNodeValue());
		newBookArray.add(dom.getElementsByTagName("title").item(0).getTextContent());
		newBookArray.add(dom.getElementsByTagName("name").item(0).getTextContent());
		newBookArray.add(dom.getElementsByTagName("summary").item(0).getTextContent());
		return newBookArray;
	}
}