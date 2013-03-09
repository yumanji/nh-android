package com.movetothebit.newholland.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.movetothebit.newholland.android.BaseFragmentActivity;
import com.movetothebit.newholland.android.R;
import com.movetothebit.newholland.android.adapters.CategoryPagerAdapter;
import com.movetothebit.newholland.android.helpers.AppHelper;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class CatalogActivity extends BaseFragmentActivity {
	
	CategoryPagerAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.category_layout);
		
		
		mAdapter = new CategoryPagerAdapter(getSupportFragmentManager());
	
		mPager = (ViewPager)findViewById(R.id.pager_search);
	    mPager.setAdapter(mAdapter);
	
	    mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
	    mIndicator.setViewPager(mPager);
	}
	


	
	
}
