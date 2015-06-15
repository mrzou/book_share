package com.aqbook.activity;

import java.util.ArrayList;
import java.util.List;

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

public class MainActivity extends FragmentActivity {

	private ViewPager viewPager;// 页卡内容
	private List<Fragment> fragments;// Tab页面列表
	private int selectedColor, unSelectedColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView() {
		selectedColor = getResources()
				.getColor(R.color.tab_title_pressed_color);
		unSelectedColor = getResources().getColor(
				R.color.tab_title_normal_color);

		InitTextView();
		InitViewPager();
	}

	/**
	 * 初始化Viewpager页
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		fragments = new ArrayList<Fragment>();
		fragments.add(new OneFragment());
		fragments.add(new TwoFragmnet());
		fragments.add(new ThreeFragment());
		fragments.add(new FourFragment());
		viewPager.setAdapter(new CustomeFragmentPagerAdapter(getSupportFragmentManager(), fragments));
		viewPager.setCurrentItem(0);                   //初始化page的fragment
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		
		makeMenuOfBottom(R.id.linearLayout1, "书架", 0);
		makeMenuOfBottom(R.id.linearLayout2, "发书", 1);
		makeMenuOfBottom(R.id.linearLayout3, "消息", 2);
		makeMenuOfBottom(R.id.linearLayout4, "我", 3);
	}

	private void makeMenuOfBottom(int linearLayoutId, String text, int index){
		Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
		LinearLayout linearLayout = (LinearLayout) findViewById(linearLayoutId);
		
		linearLayout.setOnClickListener(new MyOnClickListener(index));
//		设置主页底下菜单的图片和文字
		((TextView) linearLayout.getChildAt(0)).setTypeface(iconfont);
		((TextView) linearLayout.getChildAt(1)).setText(text);
//		设置初始颜色
		if(index==0){
			((TextView) linearLayout.getChildAt(0)).setTextColor(selectedColor);
			((TextView) linearLayout.getChildAt(1)).setTextColor(selectedColor);
		}else{
			((TextView) linearLayout.getChildAt(0)).setTextColor(unSelectedColor);
			((TextView) linearLayout.getChildAt(1)).setTextColor(unSelectedColor);
		}
	}
	/**
	 * 头标点击监听
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
	 * 为选项卡绑定监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			int i;
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
//			设置菜单的颜色的变化
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
			viewPager.setCurrentItem(index, false);
		}
	}
	@Override  
	protected void onResume() {  
	    super.onResume();
	    viewPager.setCurrentItem(0, false);
	}  

}