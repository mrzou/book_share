package com.aqbook.activity;

import java.util.ArrayList;
import java.util.List;

import com.aqbook.activity.entity.NetworkStateService;
import com.aqbook.activity.fragment.FourFragment;
import com.aqbook.activity.fragment.OneFragment;
import com.aqbook.activity.fragment.ThreeFragment;
import com.aqbook.activity.fragment.TwoFragmnet;
import com.aqbook.R;
import com.aqbook.activity.adapter.CustomeFragmentPagerAdapter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
//import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity{

	private ViewPager viewPager;// 椤靛崱鍐呭
	private List<Fragment> fragments;// Tab椤甸潰鍒楄〃
	private int selectedColor, unSelectedColor;
	private String indexId = "0";
	private static int indexPager = 0;
	
	public static void setIndexPager(int index){
		indexPager = index;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		Intent i=new Intent(this, NetworkStateService.class);  
		startService(i);
		initView();
	}

	private void initView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);

		InitTextView();
		InitViewPager(0);
	}

	/**
	 * 鍒濆鍖朧iewpager椤�
	 */
	private void InitViewPager(int page) {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();
		fragments.add(new OneFragment());
		fragments.add(new TwoFragmnet());
		fragments.add(new ThreeFragment());
		fragments.add(new FourFragment());
		viewPager.setAdapter(new CustomeFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setCurrentItem(page);                   //鍒濆鍖杙age鐨刦ragment
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 鍒濆鍖栧ご鏍�
	 * 
	 */
	private void InitTextView() {
		
		makeMenuOfBottom(R.id.linearLayout1, "发布", 0);
		makeMenuOfBottom(R.id.linearLayout2, "添加", 1);
		makeMenuOfBottom(R.id.linearLayout3, "消息", 2);
		makeMenuOfBottom(R.id.linearLayout4, "个人", 3);
	}

	private void makeMenuOfBottom(int linearLayoutId, String text, int index){
		Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
		
		LinearLayout linearLayout = (LinearLayout) findViewById(linearLayoutId);
		linearLayout.setOnClickListener(new MyOnClickListener(index));
//		璁剧疆涓婚〉搴曚笅鑿滃崟鐨勫浘鐗囧拰鏂囧瓧
		((TextView) linearLayout.getChildAt(0)).setTypeface(iconfont);
		((TextView) linearLayout.getChildAt(1)).setText(text);
//		璁剧疆鍒濆棰滆壊
		if(index==0){
			((TextView) linearLayout.getChildAt(0)).setTextColor(selectedColor);
			((TextView) linearLayout.getChildAt(1)).setTextColor(selectedColor);
		}else{
			((TextView) linearLayout.getChildAt(0)).setTextColor(unSelectedColor);
			((TextView) linearLayout.getChildAt(1)).setTextColor(unSelectedColor);
		}
	}
	/**
	 * 澶存爣鐐瑰嚮鐩戝惉
	 */
	private class MyOnClickListener implements OnClickListener {
		
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index, true);
		}
	}

	/**
	 * 涓洪�椤瑰崱缁戝畾鐩戝惉鍣�
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		
		@Override
		public void onPageScrollStateChanged(int index) {
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageSelected(int index) {
			int i;
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
//			璁剧疆鑿滃崟鐨勯鑹茬殑鍙樺寲
			for(i=0; i<4; i++){
				if(i<index || i>index){
					TextView textView1 = (TextView) ((LinearLayout) linearLayout.getChildAt(i)).getChildAt(0);
					TextView textView2 = (TextView) ((LinearLayout) linearLayout.getChildAt(i)).getChildAt(1);
					textView1.setTextColor(unSelectedColor);
					textView2.setTextColor(unSelectedColor);
				}else{
					TextView textView1 = (TextView) ((LinearLayout) linearLayout.getChildAt(index)).getChildAt(0);
					TextView textView2 = (TextView) ((LinearLayout) linearLayout.getChildAt(index)).getChildAt(1);
					textView1.setTextColor(selectedColor);
					textView2.setTextColor(selectedColor);
				}
			}
			indexPager = index;
			viewPager.setCurrentItem(index, false);
		}
	}
	@Override  
	protected void onResume() {  
	    super.onResume();
	    viewPager.setCurrentItem(indexPager);
	    indexPager = 0;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        bundle.getString("result");
        indexPager = Integer.parseInt(bundle.getString("id"));
    }
}