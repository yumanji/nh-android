package com.movetothebit.newholland.android.adapters;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.model.InscriptionData;
import com.movetothebit.newholland.android.ui.DetailActivity;
import com.movetothebit.newholland.android.ui.FormActivity;

public class DataAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private List<InscriptionData> mList;

	private Context mContext;
	private boolean[] isView;
	
	public DataAdapter(Activity activity,List<InscriptionData> list) {
	
		this.mContext = activity.getApplicationContext();
		this.mInflater = LayoutInflater.from(mContext);
		this.mList = list;
		isView = new boolean[getCount()];
		Arrays.fill(isView, Boolean.FALSE);
		
	}
	
	public void setList(List<InscriptionData> list){
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
//		boolean animation = false;
		if (convertView == null) {
		
				convertView = mInflater.inflate(R.layout.inscription_data_layout, null);
				
				holder = new ListContent();	
				holder.baseLayout = (LinearLayout)convertView.findViewById(R.id.baseLayout);
				holder.nextButton = (Button) convertView.findViewById(R.id.nextButton);
				holder.date = (TextView) convertView.findViewById(R.id.dateText);
				holder.place = (TextView) convertView.findViewById(R.id.placeText);
				holder.machineType = (TextView) convertView.findViewById(R.id.machineTypeText);
				holder.brand = (TextView) convertView.findViewById(R.id.brandText);
				holder.commercialMode = (TextView) convertView.findViewById(R.id.modelText);
				holder.commercialModeComp = (TextView) convertView.findViewById(R.id.modelCompText);
				holder.salesmanName = (TextView) convertView.findViewById(R.id.salesnameText);	
				
				holder.dealerName = (TextView) convertView.findViewById(R.id.dealerText);			
				
				convertView.setTag(holder);
				

		} else {
			
			holder = (ListContent) convertView.getTag();
			
		}
		
		final InscriptionData item = mList.get(position);	

		if(item.getFillData()==1&&item.getNameClient().equals("")&&item.getLastnameClient().equals("")){
			holder.baseLayout.setBackgroundResource(R.drawable.order_edit_back);
		}else{
			holder.baseLayout.setBackgroundResource(R.drawable.order_back);
		}
		
		holder.machineType.setText(item.getMachineType());		
		holder.brand.setText(item.getBrand());
		holder.place.setText(item.getPopulation()+" ("+item.getProvince()+")");
		holder.commercialMode.setText(item.getCommercialModel());
		holder.commercialModeComp.setText(item.getModeloComparable());
		holder.dealerName.setText(item.dealerName);
		holder.date.setText(item.getMonth()+" - " +item.getYear());
		holder.salesmanName.setText(item.getSalesmanName());
		
		holder.nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, DetailActivity.class);
				Bundle extra = new Bundle();
				extra.putInt("index", position);
				i.putExtras(extra);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(i);
				
			}
		});
		
//		if(isView[position]!=true){
//			Animation show =AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
//			convertView.setAnimation(show);
//			isView[position]=true;
//		}
		
					
		return convertView;
	}
	
	class ListContent {	
		LinearLayout baseLayout;
		Button nextButton;
		TextView machineType;
		TextView brand;
		TextView commercialMode;
		TextView commercialModeComp;
		TextView dealerName;
		TextView hp;
		
		TextView date;		
		TextView place;
			
		TextView salesmanName;
	}



}
