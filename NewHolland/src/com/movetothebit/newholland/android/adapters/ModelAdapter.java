package com.movetothebit.newholland.android.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.model.ModelItem;

public class ModelAdapter extends  ArrayAdapter<ModelItem>{

	public Activity mActivity;
	public List<ModelItem> mObjects;
	
	public ModelAdapter(Activity activity, int textViewResourceId,
			List<ModelItem> objects) {
		super(activity.getApplicationContext(), textViewResourceId, objects);
		this.mActivity = activity;
		this.mObjects = objects;
	}
	
	@Override
	public View getDropDownView(int position, View convertView,
	ViewGroup parent) {
	
		View row = convertView;
	     if(row == null){
	    	 LayoutInflater inflater = mActivity.getLayoutInflater();
	         row = inflater.inflate(R.layout.spinner_layout, parent, false);
	     }
	     
	      TextView text1 = (TextView) row.findViewById(R.id.row);
	      text1.setText(mObjects.get(position).getValue());

	      return row;
	}

	@Override
	public ModelItem getItem(int position) {
	
		return super.getItem(position);
	}
	
	
}