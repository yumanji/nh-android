package com.movetothebit.newholland.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.movetothebit.newholland.android.ui.fragments.StreamFragment;

public class CategoryPagerAdapter extends FragmentPagerAdapter {

	 protected static final String[] CONTENT = new String[] {"TRACTORES","VENDIMIADORAS","COSECHADORAS","EMPACADORAS" };
	    private int mCount = CONTENT.length;

	    public CategoryPagerAdapter(FragmentManager fm) {
	        super(fm);
	    }

	    @Override
	    public Fragment getItem(int position) {
	    	return StreamFragment.newInstance(getFileData(position));
	    }

	    @Override
	    public int getCount() {
	        return mCount;
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	      return CategoryPagerAdapter.CONTENT[position % CONTENT.length];
	    }



	    public void setCount(int count) {
	        if (count > 0 && count <= 10) {
	            mCount = count;
	            notifyDataSetChanged();
	        }
	    }
	    public String getFileData(int position){
	    	String res = null;
	    	
	    	if(position==0){
	    		res = "tractores.xml";
	    	}else if(position==1){
	    		res = "vendimiadoras.xml";
	    	}else if(position==2){
	    		res = "cosechadoras.xml";
	    	}else if(position==3){
	    		res = "empacadoras.xml";
	    	}
	    	
	    	
	    	return res;
	    }


}
