package com.kris.siweidaotu.ui; 

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TextView;

import com.kris.siweidaotu.R;
import com.kris.siweidaotu.ui.base.BaseActivity;
import com.kris.siweidaotu.util.TimeUtil;

public class TextFourNodeActivity extends BaseActivity implements OnClickListener {
	

	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        baseSetBodyView(R.layout.activity_text_four_node, true);
		setBtnLeftOnClickListener(this);
		
		
		mContext = this;
		
		System.out.println("1212");
		
		initViews();
	    initData();
	}
	
    private void initViews(){
    	
    	
    	
    }
    
    private void initData() {
    	
    }
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_topbar_left:
			finish();
			break;

		default:
			break;
		}
		
	}

}
