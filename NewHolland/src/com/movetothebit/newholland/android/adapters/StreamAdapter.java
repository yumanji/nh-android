package com.movetothebit.newholland.android.adapters;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.helpers.AppHelper;
import com.movetothebit.newholland.android.model.Item;


public class StreamAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<Item> mList;

	private Context mContext;
	private boolean[] isView;
	
	public StreamAdapter(Context context,List<Item> list) {
	
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mList = list;
		isView = new boolean[getCount()];
		Arrays.fill(isView, Boolean.FALSE);
		
	}
	
	public void setList(List<Item> list){
		this.mList = list;	
		if(isView!= null){
			isView = new boolean[getCount()];	
			Arrays.fill(isView, Boolean.FALSE);
		}
		
		
        notifyDataSetChanged();		
	}
	
	@Override
	public int getCount() {
	
		return mList.size();
	}	

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}
	

	public View getView(final int position, View convertView,
			ViewGroup parent) {

		final ListContent holder;			
		boolean animation = false;
		if (convertView == null) {
		
				convertView = mInflater.inflate(R.layout.item_stream_layout, null);
				
				holder = new ListContent();
				holder.name = (TextView) convertView.findViewById(R.id.name);					
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				holder.price = (TextView) convertView.findViewById(R.id.price);

				holder.image = (ImageView) convertView.findViewById(R.id.image);
//				holder.add = (Button) convertView.findViewById(R.id.addButton);
				
				
				convertView.setTag(holder);
				

		} else {
			
			holder = (ListContent) convertView.getTag();
			
		}
		
		final Item item = mList.get(position);
		holder.name.setText(item.getName());
		holder.price.setText(item.getObs());
		holder.desc.setText(item.getDesc());
		System.out.println(item.getImageUrl());
		UrlImageViewHelper.setUrlDrawable(holder.image,item.getImageUrl(),R.drawable.ic_launcher);
		
//		holder.add.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				AppHelper.addOrder(mList.get(position));
//				
//			}
//		});
		
		if(isView[position]!=true){
			Animation show =AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
			convertView.setAnimation(show);
			isView[position]=true;
		}
		
					
		return convertView;
	}
	
	class ListContent {
		ImageView image;	
		Button add;
		TextView name;
		TextView price;
		TextView desc;
	
			
	}



}
