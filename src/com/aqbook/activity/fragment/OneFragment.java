package com.aqbook.activity.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aqbook.R;
import com.aqbook.activity.SignUpActivity;
import com.aqbook.activity.adapter.ListViewAdapter;
import com.aqbook.activity.entity.ListViewItem;
import com.aqbook.activity.view.LoadListView;
import com.aqbook.activity.view.LoadListView.ILoadListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class OneFragment extends Fragment implements ILoadListener{
	
	private View view;
	ListViewAdapter adapter;
	LoadListView listview;
	
	private RequestQueue signUpQueue;
	
	ArrayList<ListViewItem> apk_list = new ArrayList<ListViewItem>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fargment_one, null);
		signUpQueue = Volley.newRequestQueue(getActivity());
		
		getListViewData(true);
		
		return this.view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);    
	}
	/*apk_list是传入listview中的全部数据
	loadData为真就是数据没有改变
	loadData为假数据就是刷新后的*/
	private void showListView(ArrayList<ListViewItem> apk_list, boolean loadData) {
		Log.v("TAG", String.valueOf(loadData));
		if (loadData) {
			Log.v("TAG", String.valueOf(apk_list));
			apk_list = this.apk_list;
			//apk_list = apk_list.isEmpty() ? getListViewData(true):apk_list;      //切换fragment时会重复加载该类，但是apk_list的值不变
			listview = (LoadListView) view.findViewById(R.id.fragment1_listview);
			listview.setInterface(this);
			adapter = new ListViewAdapter(getActivity(), apk_list, getActivity());
			listview.setAdapter(adapter);
		} else {
			Log.v("TAG", "datachange");
			adapter.onDateChange(apk_list);
		}
	}
	public void getListViewData(final boolean state){
		Log.v("TAG", "getData()");
		if(apk_list.isEmpty()){
			StringRequest stringRequest = new StringRequest("http://192.168.1.106:3000/manager_books", 
					new Response.Listener<String>() {  
			            @Override  
			            public void onResponse(String response) {  
			            	try {
								JSONObject stringToJson = new JSONObject(response);
								if(stringToJson.get("state").toString().equals("success")){
									setData(stringToJson.getJSONArray("book"));
									showListView(apk_list, state);
								}else{
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
			stringRequest.setRetryPolicy(policy);
			signUpQueue.add(stringRequest);
		}else{
			showListView(this.apk_list, true);
		}
	}
	public void setData(JSONArray array) throws JSONException {
		for (int i = 0; i < array.length(); i++) {
			ListViewItem entity = new ListViewItem();
			entity.setTitle(array.getJSONObject(i).get("title").toString());
			entity.setAuthor(array.getJSONObject(i).get("author").toString());
			entity.setInfo(array.getJSONObject(i).get("description").toString());
			entity.setPicture(array.getJSONObject(i).get("image_uri").toString());
			entity.setReason("好好好");
			apk_list.add(entity);
		}
	}
	private void getLoadData() {
		for (int i = 0; i < 2; i++) {
			ListViewItem entity = new ListViewItem();
			entity.setTitle("android疯狂讲义");
			entity.setAuthor("李刚");
			entity.setInfo("计算机便携化是未来的发展趋势，而Android作为最受欢迎的手机、平板电脑操作之一，其发展的上升势头是势不可当的。而Android应用选择了");
			entity.setReason("load data");
			apk_list.add(entity);
		}
	}
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getLoadData();
				showListView(apk_list, false);
				listview.loadComplete();
			}
		},2000);
	}
}
