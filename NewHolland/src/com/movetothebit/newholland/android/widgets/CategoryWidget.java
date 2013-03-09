package com.movetothebit.newholland.android.widgets;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movetothebit.newholland.android.R;

public class CategoryWidget extends RelativeLayout{
	
	
	public ImageView image;
	public int drawable;
	public int imageBackground;
	public String title;
	private TextView titleText;
	private ImageView cat;
	
	



	public CategoryWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		LayoutInflater  mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate (R.layout.category_item_layout,this,true);  
        
        image = (ImageView)this.findViewById(R.id.back);         
        titleText = (TextView)this.findViewById(R.id.catTitle);        
        cat = (ImageView) this.findViewById(R.id.catImage);

        
	}
	
	public void setData(int drawable,
			String title, int imageBackground){
		this.drawable = drawable;
		this.title = title;
		this.imageBackground = imageBackground;
		
		titleText.setText(title);	        
	    cat.setImageResource(drawable);
	    image.setImageResource(imageBackground);
	  
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int orientation = getResources().getConfiguration().orientation;
//		int measure = 0;
//		if(orientation == Configuration.ORIENTATION_LANDSCAPE){
//			measure = heightMeasureSpec;			
//		}else if(orientation == Configuration.ORIENTATION_PORTRAIT){
//			measure = widthMeasureSpec;
//			
//		}
//		 
//		super.onMeasure(measure, measure);
//
//	}


	

}
