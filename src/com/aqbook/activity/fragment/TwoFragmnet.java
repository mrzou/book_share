package com.aqbook.activity.fragment;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aqbook.R;
import com.aqbook.activity.MipcaActivityCapture;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.DocumentsContract.Document;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TwoFragmnet extends Fragment {

	private final static int SCANNIN_GREQUEST_CODE = 1;
	View view;
	
	private RequestQueue mQueue;  
	private Activity activity;
	
	TextView textView1;
	TextView textViewOfAddbook;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.activity = getActivity();
		mQueue = Volley.newRequestQueue(activity);
		// 设置activity的布局
		view = inflater.inflate(R.layout.fragment_two, null);
		textView1 = (TextView) view.findViewById(R.id.add_book_book);
		textViewOfAddbook = (TextView) view.findViewById(R.id.add_book_icon);
		Typeface iconfont = Typeface.createFromAsset(activity.getAssets(), "icon_little.ttf");
		
		textView1.setTypeface(iconfont);
		textViewOfAddbook.setTypeface(iconfont);
		
		textViewOfAddbook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(activity, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		return view;
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
		int socketTimeout = 3000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		stringRequest.setRetryPolicy(policy);
		mQueue.add(stringRequest);
	}
	@SuppressLint("ResourceAsColor")
	public void setBookMessage(String bookMessage) throws IOException, ParserConfigurationException, SAXException{
		LinearLayout scanBookLinearLayout = (LinearLayout) view.findViewById(R.id.scan_book_id);
        scanBookLinearLayout.removeViewAt(0);
        scanBookLinearLayout.setPadding(50, 20, 40, 10);
        
        ArrayList<String> bookArray = xmlStringToDocument(bookMessage);
        final ImageView bookImg = new ImageView(activity);
        bookImg.setLayoutParams(new LayoutParams(270, 360));
        bookImg.setBackgroundColor(R.color.white);
        ImageRequest imageRequest = new ImageRequest(  
                bookArray.get(0),  
                new Response.Listener<Bitmap>() {  
                    @Override  
                    public void onResponse(Bitmap response) {  
                        bookImg.setImageBitmap(response);  
                    }  
                }, 270, 360, Config.RGB_565, new Response.ErrorListener() {  
                    @Override  
                    public void onErrorResponse(VolleyError error) {  
                    }  
                });  
        mQueue.add(imageRequest);
        LinearLayout bookIntroduce = setBookIntroduce(bookArray);
        scanBookLinearLayout.addView(bookImg);
        scanBookLinearLayout.addView(bookIntroduce);
        view.findViewById(R.id.comeOutBook).setVisibility(View.VISIBLE);
        view.findViewById(R.id.enjoyText).setVisibility(View.VISIBLE);
	}
	public LinearLayout setBookIntroduce(ArrayList<String> booArray){
		LinearLayout linearLayout = new LinearLayout(activity);
		linearLayout.setLayoutParams(new LayoutParams(370, 560));
		linearLayout.setOrientation(1);
//		书名
		TextView textView1 = new TextView(activity);
		textView1.setLayoutParams(new LayoutParams(300, 45));
		textView1.setText(booArray.get(1));
//		作者
		TextView textView2 = new TextView(activity);
		textView2.setLayoutParams(new LayoutParams(300, 45));
		textView2.setText(booArray.get(2));
//		书的介绍
		TextView textView3 = new TextView(activity);
		textView3.setLayoutParams(new LayoutParams(400, 270));
		textView3.setText(booArray.get(3));
//		发布图书的按钮
		
		linearLayout.addView(textView1);
		linearLayout.addView(textView2);
		linearLayout.addView(textView3);
		
		return linearLayout;
	}
//	解析xml字符串
	public ArrayList<String> xmlStringToDocument(String xmlString) throws IOException, ParserConfigurationException, SAXException {
		
		ArrayList<String> bookArray = new ArrayList<String>();
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		InputSource is=new InputSource(new StringReader(xmlString));
		org.w3c.dom.Document dom=builder.parse(is);
		bookArray.add(dom.getElementsByTagName("link").item(2).getAttributes().getNamedItem("href").getNodeValue());
		bookArray.add(dom.getElementsByTagName("title").item(0).getTextContent());
		bookArray.add(dom.getElementsByTagName("name").item(0).getTextContent());
		bookArray.add(dom.getElementsByTagName("summary").item(0).getTextContent());
		Log.v("TAG",String.valueOf(bookArray));
		return bookArray;
	}
}