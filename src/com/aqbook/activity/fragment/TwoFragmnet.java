package com.aqbook.activity.fragment;

import com.aqbook.R;
import com.aqbook.activity.EnjoyBookActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class TwoFragmnet extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置activity的布局
		View view = inflater.inflate(R.layout.fragment_two, null);
		TextView textView1 = (TextView) view.findViewById(R.id.add_book_book);
		TextView textViewOfAddbook = (TextView) view.findViewById(R.id.add_book_icon);
		Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "icon_little.ttf");
		
		textView1.setTypeface(iconfont);
		textViewOfAddbook.setTypeface(iconfont);
		
		textViewOfAddbook.setOnClickListener(new OnClickListener() {
			   
			   @Override
			   public void onClick(View textView) {
				   Intent toAddBookIntent = new Intent(getActivity(), EnjoyBookActivity.class);
				   startActivity(toAddBookIntent);
			   }
		});
		return view;
	}
}