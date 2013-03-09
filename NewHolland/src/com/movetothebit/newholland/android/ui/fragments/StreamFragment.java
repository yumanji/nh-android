package com.movetothebit.newholland.android.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.movetothebit.newholland.android.BaseFragment;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.adapters.StreamAdapter;
import com.movetothebit.newholland.android.model.Item;
import com.movetothebit.newholland.android.utils.RemoteFacade;

public class StreamFragment extends BaseFragment{

	private ListView list;
	private StreamAdapter adapter;
	private List<Item> items = new ArrayList<Item>();
	private View root;
	
	 public static StreamFragment newInstance(String string) {
	        StreamFragment fragment = new StreamFragment();
	        Bundle extras  = new Bundle();
	        extras.putString("data", string);
	        fragment.setArguments(extras);
	        return fragment;
	    }
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.stream_layout, container, false);
		
		list =(ListView)root.findViewById(R.id.list);
		adapter = new StreamAdapter(getSherlockActivity(), items);
		list.setAdapter(adapter);
		
		new GetListTask().execute(getArguments().getString("data"));
		
		return root;
	}



	
	
	private class GetListTask extends AsyncTask<String, Void, Void> {
		

		
	


		@Override
		protected Void doInBackground(String... params) {
			items = RemoteFacade.parseItems(getActivity(), params[0]);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			if (items != null && items.size() != 0) {
				adapter.setList(items);
				adapter.notifyDataSetChanged();
			}else{
				LinearLayout ll = (LinearLayout)root.findViewById(R.id.emptyList);
				ll.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}		
	}	
	

}
