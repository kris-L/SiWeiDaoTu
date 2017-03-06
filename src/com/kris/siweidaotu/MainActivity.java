package com.kris.siweidaotu;

import com.kris.siweidaotu.R;
import com.kris.siweidaotu.data.Const;
import com.kris.siweidaotu.ui.SettingActivity;
import com.kris.siweidaotu.ui.TextFourNodeActivity;
import com.kris.siweidaotu.ui.base.BaseActivity;
import com.kris.siweidaotu.util.ActivityUtil;
import com.kris.siweidaotu.util.DialogManage;
import com.kris.siweidaotu.util.TimeUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener {

	
	private TextView text_exam_tv;
	private TextView picture_exam_tv;
	private TextView setting_tv;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	    initData();
	}
	
    private void initViews(){
    	text_exam_tv = (TextView) findViewById(R.id.text_exam_tv);
    	picture_exam_tv = (TextView) findViewById(R.id.picture_exam_tv);   	
    	setting_tv = (TextView) findViewById(R.id.setting_tv);
    	
    	text_exam_tv.setOnClickListener(this);
    	picture_exam_tv.setOnClickListener(this);
    	setting_tv.setOnClickListener(this);
    }
    
    private void initData() {
    	
    }
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (TimeUtil.isFastClick()) {
			return;
		}
		switch (v.getId()) {
		case R.id.text_exam_tv:
//			Intent intent_four = new Intent(this, TextFourNodeActivity.class);
//			startActivity(intent_four);
			DialogManage.selectNodeDialog(this, Const.TEXT_EXAM_TYPE);
			
			break;
			
		case R.id.picture_exam_tv:
			DialogManage.selectNodeDialog(this, Const.PICTURE_EXAM_TYPE);
			
			break;
			
		case R.id.setting_tv:
			Intent intent_setting = new Intent(this, SettingActivity.class);
			startActivity(intent_setting);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("MainActivity  onDestroy");
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
