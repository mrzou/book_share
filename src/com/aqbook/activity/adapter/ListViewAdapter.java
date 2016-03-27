package com.aqbook.activity.adapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.aqbook.R;
import com.aqbook.activity.entity.ListViewItem;

public class ListViewAdapter extends BaseAdapter {
	ArrayList<ListViewItem> apk_list;
	LayoutInflater inflater;
	Context context;
	private RequestQueue mQueue;

	public ListViewAdapter(Context oneFragment, ArrayList<ListViewItem> apk_list, Context context) {
		this.apk_list = apk_list;
		this.inflater = LayoutInflater.from(oneFragment);
		this.context = context;
		this.mQueue = Volley.newRequestQueue(context);
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
			holder.book_title = (TextView) convertView.findViewById(R.id.item3_booktitle);
			holder.book_author = (TextView) convertView.findViewById(R.id.item3_bookauthor);
			holder.book_info = (TextView) convertView.findViewById(R.id.item3_bookinfo);
			holder.image_uri = (ImageView) convertView.findViewById(R.id.item3_apkiv);
			holder.recom_reson = (TextView) convertView.findViewById(R.id.item3_recomm_reason);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.book_title.setText(entity.getTitle());
		holder.book_author.setText(entity.getAuthor());
		holder.book_info.setText(entity.getInfo());
		holder.recom_reson.setText(entity.getReason());
		setItemImage(holder.image_uri, entity.getPicture());
		return convertView;
	}
	
	public void setItemImage(final ImageView imageView, String pic_url){
		ImageRequest imageRequest = new ImageRequest(pic_url, new Response.Listener<Bitmap>() {  
            @Override  
            public void onResponse(Bitmap response) {  
                imageView.setImageBitmap(response);  
            }  
        },270, 360, Config.RGB_565, new Response.ErrorListener() {  
            @Override  
            public void onErrorResponse(VolleyError error) {  
            	imageView.setImageResource(R.drawable.error); 
            }  
        });  
        int socketTimeout = 50000;//30 seconds - change to what you want
		RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		imageRequest.setRetryPolicy(policy);
        mQueue.add(imageRequest);
	}
	
	public void setPicture(View view){
		Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "logup.ttf");
		TextView iconBook = (TextView)view.findViewById(R.id.iconf_book);
		iconBook.setTypeface(iconfont);
		TextView user_name = (TextView)view.findViewById(R.id.user_name);
		user_name.setText("15018633076");
	}

	class ViewHolder {
		TextView book_title;
		TextView book_author;
		TextView book_info;
		ImageView image_uri;
		TextView recom_reson;
	}
}
