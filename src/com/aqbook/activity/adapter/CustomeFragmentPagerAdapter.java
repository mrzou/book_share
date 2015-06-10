package com.aqbook.activity.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 定义适配器
 */
public class CustomeFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> fragmentList;
	public CustomeFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
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
