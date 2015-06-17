package com.aqbook.activity.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aqbook.R;
import com.aqbook.activity.entity.ListViewItem;

public class ListViewAdapter extends BaseAdapter {
	ArrayList<ListViewItem> apk_list;
	LayoutInflater inflater;
	Context context;

	public ListViewAdapter(Context oneFragment, ArrayList<ListViewItem> apk_list, Context context) {
		this.apk_list = apk_list;
		this.inflater = LayoutInflater.from(oneFragment);
		this.context = context;
	}

	public void onDateChange(ArrayList<ListViewItem> apk_list) {
		this.apk_list = apk_list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apk_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return apk_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ListViewItem entity = apk_list.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_layout, null);
			setPicture(convertView);
			holder.book_title = (TextView) convertView
					.findViewById(R.id.item3_booktitle);
			holder.book_author = (TextView) convertView
					.findViewById(R.id.item3_bookauthor);
			holder.book_info = (TextView) convertView
					.findViewById(R.id.item3_bookinfo);
			holder.recom_reson = (TextView) convertView
					.findViewById(R.id.item3_recomm_reason);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.book_title.setText(entity.getTitle());
		holder.book_author.setText(entity.getAuthor());
		holder.book_info.setText(entity.getInfo());
		holder.recom_reson.setText(entity.getReason());
		return convertView;
	}
	public void setPicture(View view){
		Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "logup.ttf");
		TextView iconBook = (TextView)view.findViewById(R.id.iconf_book);
		TextView user_name = (TextView)view.findViewById(R.id.user_name);
		iconBook.setTypeface(iconfont);
		user_name.setText("15018633076");
	}

	class ViewHolder {
		TextView book_title;
		TextView book_author;
		TextView book_info;
		TextView recom_reson;
	}
}
