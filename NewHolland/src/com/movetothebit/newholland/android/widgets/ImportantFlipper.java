package com.movetothebit.newholland.android.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.movetothebit.newholland.android.R;

public class ImportantFlipper extends RelativeLayout{
	
	
	public ViewFlipper viewFlipper;
	public int drawable;
	public String title;
	private TextView titleText;
	private ImageView cat;
	
	private int[] imageID ;



	public ImportantFlipper(final Context context, AttributeSet attrs) {
		super(context, attrs);
	
		LayoutInflater  mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.flipper_layout,this, true);  
        
        viewFlipper = (ViewFlipper) this.findViewById(R.id.flipper);         
        titleText = (TextView)this.findViewById(R.id.catTitle); 
        
        cat = (ImageView) this.findViewById(R.id.catImage);
        
        cat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = "http://www.newholland.es/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				context.startActivity(i);

				
			}
		});
     
       
        
	}
	
	public void setData(int drawable,
			String title, int[] imageID){
		this.drawable = drawable;
		this.title = title;
		this.imageID = imageID;
		 titleText.setText(title);
	    titleText.setVisibility(View.INVISIBLE);
//	        cat.setImageDrawable(getContext().getResources().getDrawable(drawable));
	       
	        for (int i = 0; i < imageID.length; i++)  
	        { 
	            ImageView image = new ImageView(getContext());  
	            image.setImageResource(imageID[i]);  
	            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            viewFlipper.addView(image, new LayoutParams(  
	                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	        }

	}
	

	public void startFlipp(){
		viewFlipper.setFlipInterval(2500);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getContext(),  
	         R.anim.push_left_in));  
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.push_left_out));  
		viewFlipper.startFlipping();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int orientation = getResources().getConfiguration().orientation;
		if(orientation == Configuration.ORIENTATION_LANDSCAPE){
			super.onMeasure(heightMeasureSpec, heightMeasureSpec);

			
		}else if(orientation == Configuration.ORIENTATION_PORTRAIT){
			super.onMeasure(widthMeasureSpec, widthMeasureSpec);

			
		}		 
		    
	}


}
