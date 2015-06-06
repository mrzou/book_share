package com.aqbook.activity.fragment;

import java.util.ArrayList;

import com.aqbook.R;
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

public class OneFragment extends Fragment implements ILoadListener{
	
	private View view;
	ArrayList<ListViewItem> apk_list = new ArrayList<ListViewItem>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("TAG", "OneFragment.java");
		this.view = inflater.inflate(R.layout.fargment_one, null);
		getData();
		showListView(apk_list, true);
		return this.view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);    
	}
	
	ListViewAdapter adapter;
	LoadListView listview;
	
	private void showListView(ArrayList<ListViewItem> apk_list, boolean loadData) {
		if (loadData) {
			listview = (LoadListView) view.findViewById(R.id.fragment1_listview);
			listview.setInterface(this);
			adapter = new ListViewAdapter(getActivity(), apk_list);
			listview.setAdapter(adapter);
		} else {
			adapter.onDateChange(apk_list);
		}
	}

	private void getData() {
		for (int i = 0; i < 10; i++) {
			ListViewItem entity = new ListViewItem();
			entity.setName("hello");
			entity.setInfo("this is about hello");
			entity.setDes("descript");
			apk_list.add(entity);
		}
	}
	private void getLoadData() {
		for (int i = 0; i < 2; i++) {
			ListViewItem entity = new ListViewItem();
			entity.setName("loaddata");
			entity.setInfo("data for load");
			entity.setDes("about loaddata");
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
