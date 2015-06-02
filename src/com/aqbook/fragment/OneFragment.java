package com.aqbook.fragment;

import java.util.ArrayList;

import com.aqbook.R;
import com.aqbook.adapter.ListViewAdapter;
import com.aqbook.entity.ListViewItem;
import com.aqbook.view.LoadListView;
import com.aqbook.view.LoadListView.ILoadListener;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OneFragment extends Fragment implements ILoadListener{
	
	private View view;
	ArrayList<ListViewItem> apk_list = new ArrayList<ListViewItem>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view = inflater.inflate(R.layout.fargment_one, null);
		getData();
		showListView(apk_list);
		return this.view;
	}
	
	ListViewAdapter adapter;
	LoadListView listview;
	
	private void showListView(ArrayList<ListViewItem> apk_list) {
		if (adapter == null) {
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
				//��ȡ������
				getLoadData();
				//����listview��ʾ��
				showListView(apk_list);
				//֪ͨlistview�������
				listview.loadComplete();
			}
		}, 2000);
	}
}
