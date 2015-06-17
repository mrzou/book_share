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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OneFragment extends Fragment implements ILoadListener{
	
	private View view;
	ListViewAdapter adapter;
	LoadListView listview;
	
	ArrayList<ListViewItem> apk_list = new ArrayList<ListViewItem>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fargment_one, null);
		showListView(apk_list, true);
		
		return this.view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);    
	}
	
	private void showListView(ArrayList<ListViewItem> apk_list, boolean loadData) {
		if (loadData) {
			apk_list = apk_list.isEmpty() ? getData():apk_list;      //切换fragment时会重复加载该类，但是apk_list的值不变
			listview = (LoadListView) view.findViewById(R.id.fragment1_listview);
			listview.setInterface(this);
			adapter = new ListViewAdapter(getActivity(), apk_list, getActivity());
			listview.setAdapter(adapter);
		} else {
			adapter.onDateChange(apk_list);
		}
	}

	private ArrayList<ListViewItem> getData() {
		for (int i = 0; i < 10; i++) {
			ListViewItem entity = new ListViewItem();
			entity.setTitle("android疯狂讲义");
			entity.setAuthor("李刚");
			entity.setInfo("计算机便携化是未来的发展趋势，而Android作为最受欢迎的手机、平板电脑操作之一，其发展的上升势头是势不可当的。而Android应用选择了");
			entity.setReason("好好好");
			apk_list.add(entity);
		}
		return apk_list;
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
