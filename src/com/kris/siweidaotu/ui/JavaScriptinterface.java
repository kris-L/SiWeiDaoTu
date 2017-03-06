package com.kris.siweidaotu.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JavaScriptinterface {

    private Context mContext;
    
    /** Instantiate the interface and set the context */
    public JavaScriptinterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface  
    public void startBrowser(String url) {
    	System.out.println("url="+url);
        Uri content_url = Uri.parse(url); 
		Intent intent = new Intent(Intent.ACTION_VIEW,content_url);
		intent.setClassName("com.android.browser","com.android.browser.BrowserActivity"); 
		mContext.startActivity(intent);
    }
    
    @JavascriptInterface
	public void openweb(String url){
		Intent intent= new Intent();        
		intent.setAction("android.intent.action.VIEW");    
		Uri content_url = Uri.parse(url);   
		intent.setData(content_url);  
//		  intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
		mContext.startActivity(intent);
	}
    
    private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
    
}
