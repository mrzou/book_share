package com.aqbook.activity.fragment;

import com.aqbook.R;
import com.aqbook.activity.SignUpActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class FourFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_four, null);
		TextView textView = (TextView) view.findViewById(R.id.logUpUser);
		textView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("TAG", "excute clickToLogUp");
				Intent intent = new Intent(getActivity(), SignUpActivity.class);
				startActivity(intent);
				
			}
			
		});
		return view;
	}
	
	public void clickToLogUp(View view){
	}
}
