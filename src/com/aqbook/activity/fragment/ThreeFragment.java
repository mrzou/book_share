package com.aqbook.activity.fragment;

import com.aqbook.R;
import com.aqbook.activity.MainActivity;
import com.aqbook.activity.entity.PublicMethod;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ThreeFragment extends Fragment {

	private View view;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		testIfLogIn(inflater);
		return view;
	}
	public View testIfLogIn(LayoutInflater inflater){
		SharedPreferences token = getActivity().getSharedPreferences("token", 0);
		if(token.getString("token", "").equals("")){
			view = inflater.inflate(R.layout.fragment_three_select, null);
			FourFragment anotherFragment = new FourFragment();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.id_content, anotherFragment);
		transaction.commit();
		}else{
			view = inflater.inflate(R.layout.fragment_three, null);
			setPicture();
		}
		Log.v("TAG", "Fragment3");
		return view;
	}
	
	public void setPicture(){
		TextView icon1 = (TextView) view.findViewById(R.id.message_l1_image);
		TextView icon2 = (TextView) view.findViewById(R.id.message_l2_image);
		TextView icon3 = (TextView) view.findViewById(R.id.message_l3_image);
		TextView arrow1 = (TextView) view.findViewById(R.id.forward_id1);
		TextView arrow2 = (TextView) view.findViewById(R.id.forward_id2);
		TextView arrow3 = (TextView) view.findViewById(R.id.forward_id3);
		Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "icon_little.ttf");
		arrow1.setTypeface(iconfont);
		arrow2.setTypeface(iconfont);
		arrow3.setTypeface(iconfont);
		icon1.setTypeface(iconfont);
		icon2.setTypeface(iconfont);
		icon3.setTypeface(iconfont);
	}
}
