package com.aqbook.activity.fragment;

import com.aqbook.R;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ThreeFragment extends Fragment {

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_three, null);
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
		return view;
	}
}
