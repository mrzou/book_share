package com.aqbook.activity.adapter;

import java.util.List;

import com.aqbook.R;
import com.aqbook.activity.fragment.FourFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 定义适配器
 */
public class CustomeFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> fragmentList;
	private Context context;
	public CustomeFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
		this.context = context;
	}

	/**
	 * 得到每个页面
	 */
	@Override
	public Fragment getItem(int arg0) {
		Log.v("TAG", "arg0="+String.valueOf(arg0));
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
	@Override
	public void destroyItem (ViewGroup container, int position, Object object){
		Log.v("TAG", "position = "+position);
	}
}
