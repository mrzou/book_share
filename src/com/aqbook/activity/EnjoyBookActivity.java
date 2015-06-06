package com.aqbook.activity;

import com.aqbook.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class EnjoyBookActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_enjoy_book);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.addbook_custom_title);
		
		TextView textView1 = (TextView) findViewById(R.id.return_main_activity);
		TextView textView2 = (TextView) findViewById(R.id.scan_book);
		Typeface iconfont = Typeface.createFromAsset(getAssets(), "icon_little.ttf");
		textView1.setTypeface(iconfont);
		textView2.setTypeface(iconfont);
	}
}
