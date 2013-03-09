package com.movetothebit.newholland.android.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.movetothebit.newholland.android.BaseActivity;
import com.movetothebit.newholland.android.R;


public class WebActivity extends BaseActivity {

	
	private WebView webView;
	private WebSettings webSettings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.web_layout);
        
       
        webView = (WebView)this.findViewById(R.id.webView);
      
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); 
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new MyWebViewClient());
        
        webView.loadUrl(getIntent().getExtras().getString("url"));
      
    }
   
    
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (Uri.parse(url).getHost().equals("www.google.es")) {
//                // This is my web site, so do not override; let my WebView load the page
//                return false;
//            }
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
            return true;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
    
    
}
