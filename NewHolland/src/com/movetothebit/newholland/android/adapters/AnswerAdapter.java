package com.movetothebit.newholland.android.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.model.AnswerItem;

public class AnswerAdapter  extends BaseAdapter implements SpinnerAdapter{
    private Activity activity;
    private List<AnswerItem> list; 

    public AnswerAdapter(Activity activity, List<AnswerItem> list){
        this.activity = activity;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public AnswerItem getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

    View spinView;
    if( convertView == null ){
        LayoutInflater inflater = activity.getLayoutInflater();
        spinView = inflater.inflate(R.layout.spinner_layout, null);
    } else {
         spinView = convertView;
    }
    TextView t1 = (TextView) spinView.findViewById(R.id.row);
   
    t1.setText(String.valueOf(list.get(position).getValue()));
  
    return spinView;
    }
}