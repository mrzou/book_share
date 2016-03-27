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
import com.aqbook.activity.adapter.ListViewAdapter;
import com.aqbook.activity.entity.ListViewItem;
import com.aqbook.activity.entity.PublicMethod;
import com.aqbook.activity.view.LoadListView;
import com.aqbook.activity.view.LoadListView.ILoadListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OneFragment extends Fragment implements ILoadListener{
	
	private View view;
	ListViewAdapter adapter;
	LoadListView listview;
	
	private int pageCount = 0;
	
	private RequestQueue signUpQueue;
	
	ArrayList<ListViewItem> apk_list = new ArrayList<ListViewItem>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fargment_one, null);
		signUpQueue = Volley.newRequestQueue(getActivity());
		boolean if_net = PublicMethod.isNetworkConnected(getActivity());
		if(!if_net){
			(view.findViewById(R.id.alert_net)).setVisibility(View.VISIBLE);
		}else{
			(view.findViewById(R.id.alert_net)).setVisibility(View.INVISIBLE);
		}
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
	private void setListViewAdapter(ArrayList<ListViewItem> apk_list, boolean loadData) {
		if (loadData) {
			listview = (LoadListView) view.findViewById(R.id.fragment1_listview);
			listview.setInterface(this);
			adapter = new ListViewAdapter(getActivity(), apk_list, getActivity());
			listview.setAdapter(adapter);
		} else {
			adapter.onDateChange(apk_list);
		}
	}
	public void getListViewData(final boolean state){
		Log.v("TAG", "getData()");
		if(apk_list.isEmpty() || state == false){
			StringRequest stringRequest = new StringRequest(PublicMethod.domainName + "/manager_books?start="+pageCount, 
					new Response.Listener<String>() {  
			            @Override  
			            public void onResponse(String response) {  
			            	try {
								JSONObject stringToJson = new JSONObject(response);
								if(stringToJson.get("state").toString().equals("success")){
									setData(stringToJson.getJSONArray("book"));
									if(state==false){                             //拖动屏幕加载的数据
										setListViewAdapter(apk_list, false);
										listview.loadComplete();
									}else{
										setListViewAdapter(apk_list, state);
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
			stringRequest.setRetryPolicy(policy);
			signUpQueue.add(stringRequest);
		}else{
			setListViewAdapter(this.apk_list, true);
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
			pageCount = apk_list.size();
		}
	}
	Handler handler = new Handler();
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getListViewData(false);
				//setListViewAdapter(apk_list, false);
				listview.loadComplete();
			}
		}, 2000);
	}
}
