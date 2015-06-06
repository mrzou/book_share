package com.aqbook.activity;

import java.util.ArrayList;
import java.util.List;

import com.aqbook.activity.fragment.FourFragment;
import com.aqbook.activity.fragment.OneFragment;
import com.aqbook.activity.fragment.ThreeFragment;
import com.aqbook.activity.fragment.TwoFragmnet;
import com.aqbook.R;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
//import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	private ViewPager viewPager;// 页卡内容
//	private ImageView imageView;// 动画图片
	private TextView book_case, out_book, comment_message, self_message;// 选项名称
	private TextView book_casep, out_bookp, comment_messagep, self_messagep;// 选项名称
	private List<Fragment> fragments;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int screenH;// 屏幕高度度
	private int selectedColor, unSelectedColor;
	/** 页卡总数 **/
	private static final int pageSize = 4;

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

//		InitImageView();
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
		viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
				fragments));
		viewPager.setCurrentItem(0);                   //初始化page的fragment
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 * 
	 */
	private void InitTextView() {
		Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
		
		LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
		LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.linearLayout3);
		LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.linearLayout4);
		book_case = (TextView) findViewById(R.id.tab_1);
		book_casep = (TextView) findViewById(R.id.tab_1p);
		book_casep.setTypeface(iconfont);
		out_book = (TextView) findViewById(R.id.tab_2);
		out_bookp = (TextView) findViewById(R.id.tab_2p);
		out_bookp.setTypeface(iconfont);
		comment_message = (TextView) findViewById(R.id.tab_3);
		comment_messagep = (TextView) findViewById(R.id.tab_3p);
		comment_messagep.setTypeface(iconfont);
		self_message = (TextView) findViewById(R.id.tab_4);
		self_messagep = (TextView) findViewById(R.id.tab_4p);
		self_messagep.setTypeface(iconfont);

		book_case.setTextColor(selectedColor);
		out_book.setTextColor(unSelectedColor);
		comment_message.setTextColor(unSelectedColor);
		self_message.setTextColor(unSelectedColor);
		book_casep.setTextColor(selectedColor);
		out_bookp.setTextColor(unSelectedColor);
		comment_messagep.setTextColor(unSelectedColor);
		self_messagep.setTextColor(unSelectedColor);

		book_case.setText("书架");
		out_book.setText("发书");
		comment_message.setText("消息");
		self_message.setText("我");
        
		linearLayout1.setOnClickListener(new MyOnClickListener(0));
		linearLayout2.setOnClickListener(new MyOnClickListener(1));
		linearLayout3.setOnClickListener(new MyOnClickListener(2));
		linearLayout4.setOnClickListener(new MyOnClickListener(3));
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

			switch (index) {
			case 0:
				book_case.setTextColor(selectedColor);
				out_book.setTextColor(unSelectedColor);
				comment_message.setTextColor(unSelectedColor);
				self_message.setTextColor(unSelectedColor);
				book_casep.setTextColor(selectedColor);
				out_bookp.setTextColor(unSelectedColor);
				comment_messagep.setTextColor(unSelectedColor);
				self_messagep.setTextColor(unSelectedColor);
				break;
			case 1:
				out_book.setTextColor(selectedColor);
				book_case.setTextColor(unSelectedColor);
				comment_message.setTextColor(unSelectedColor);
				self_message.setTextColor(unSelectedColor);
				out_bookp.setTextColor(selectedColor);
				book_casep.setTextColor(unSelectedColor);
				comment_messagep.setTextColor(unSelectedColor);
				self_messagep.setTextColor(unSelectedColor);
				break;
			case 2:
				comment_message.setTextColor(selectedColor);
				book_case.setTextColor(unSelectedColor);
				out_book.setTextColor(unSelectedColor);
				self_message.setTextColor(unSelectedColor);
				comment_messagep.setTextColor(selectedColor);
				book_casep.setTextColor(unSelectedColor);
				out_bookp.setTextColor(unSelectedColor);
				self_messagep.setTextColor(unSelectedColor);
				break;
			case 3:
				comment_message.setTextColor(unSelectedColor);
				book_case.setTextColor(unSelectedColor);
				out_book.setTextColor(unSelectedColor);
				self_message.setTextColor(selectedColor);
				comment_messagep.setTextColor(unSelectedColor);
				book_casep.setTextColor(unSelectedColor);
				out_bookp.setTextColor(unSelectedColor);
				self_messagep.setTextColor(selectedColor);
				break;
			}
			
			viewPager.setCurrentItem(index);
		}

	}

	/**
	 * 为选项卡绑定监听器
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int index) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int index) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* index, screenH, screenH);// 显然这个比较简洁，只有一行代码。
			currIndex = index;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
//			imageView.startAnimation(animation);

			switch (index) {
			case 0:
				book_case.setTextColor(selectedColor);
				out_book.setTextColor(unSelectedColor);
				comment_message.setTextColor(unSelectedColor);
				self_message.setTextColor(unSelectedColor);
				book_casep.setTextColor(selectedColor);
				out_bookp.setTextColor(unSelectedColor);
				comment_messagep.setTextColor(unSelectedColor);
				self_messagep.setTextColor(unSelectedColor);
				break;
			case 1:
				out_book.setTextColor(selectedColor);
				book_case.setTextColor(unSelectedColor);
				comment_message.setTextColor(unSelectedColor);
				self_message.setTextColor(unSelectedColor);
				out_bookp.setTextColor(selectedColor);
				book_casep.setTextColor(unSelectedColor);
				comment_messagep.setTextColor(unSelectedColor);
				self_messagep.setTextColor(unSelectedColor);
				break;
			case 2:
				comment_message.setTextColor(selectedColor);
				book_case.setTextColor(unSelectedColor);
				out_book.setTextColor(unSelectedColor);
				self_message.setTextColor(unSelectedColor);
				comment_messagep.setTextColor(selectedColor);
				book_casep.setTextColor(unSelectedColor);
				out_bookp.setTextColor(unSelectedColor);
				self_messagep.setTextColor(unSelectedColor);
				break;
			case 3:
				comment_message.setTextColor(unSelectedColor);
				book_case.setTextColor(unSelectedColor);
				out_book.setTextColor(unSelectedColor);
				self_message.setTextColor(selectedColor);
				comment_messagep.setTextColor(unSelectedColor);
				book_casep.setTextColor(unSelectedColor);
				out_bookp.setTextColor(unSelectedColor);
				self_messagep.setTextColor(selectedColor);
				break;
			}
			viewPager.setCurrentItem(index);
		}
	}

	/**
	 * 定义适配器
	 */
	class myPagerAdapter extends FragmentPagerAdapter {
		
		private List<Fragment> fragmentList;
		public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
			super(fm);
			this.fragmentList = fragmentList;
		}

		/**
		 * 得到每个页面
		 */
		@Override
		public Fragment getItem(int arg0) {
			return (fragmentList == null || fragmentList.size() == 0) ? null
					: fragmentList.get(arg0);
		}

		/**
		 * 每个页面的title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return null;
		}

		/**
		 * 页面的总个数
		 */
		@Override
		public int getCount() {
			return fragmentList == null ? 0 : fragmentList.size();
		}
	}

}