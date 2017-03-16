package com.kris.siweidaotu.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kris.siweidaotu.R;
import com.kris.siweidaotu.data.Const;
import com.kris.siweidaotu.helper.LocalDataHelper;
import com.kris.siweidaotu.ui.base.BaseActivity;
import com.kris.siweidaotu.ui.view.SelfDialog;
import com.kris.siweidaotu.ui.view.UnScrollGridView;
import com.kris.siweidaotu.util.ActivityUtil;
import com.kris.siweidaotu.util.DateUtil;
import com.kris.siweidaotu.util.RandomNumUtil;
import com.kris.siweidaotu.util.TimeUtil;
import com.umeng.analytics.MobclickAgent;

public class TextEightNodeActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private TextView text_one_node_tv;
	private TextView text_two_node_tv;
	private TextView text_three_node_tv;
	private TextView text_four_node_tv;
	private TextView text_five_node_tv;
	private TextView text_six_node_tv;
	private TextView text_seven_node_tv ;
	private TextView text_eight_node_tv;
	private TextView text_nine_node_tv;
	
	private UnScrollGridView node_gv;

	private Context mContext;
	/** 1:开始训练 2:记忆中 3：提交答案 */
	private int memoryType = 1;
	
	private String wordGroup[];
	private int selectNode = -1;
	private int selectItem = -1;

	public static Map<Integer,Integer> selectMap = new HashMap<Integer,Integer>();
	private List<String> nodeData = new ArrayList<String>();
	private List<String> tempData = new ArrayList<String>();
	private NodeStrAdapter nodeStrAdapter = null;
	private List<TextView> textViewList = new ArrayList<TextView>();
	
	private long startMemoryTime = 0;  
	private long endMemoryTime = 0; 
	private long completeTime = 0;
	private int rightNum = 0;
	private int errorNum = 0;
	private int totalNode = 9;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetBodyView(R.layout.activity_text_eight_node, true);
		setBtnLeftOnClickListener(this);
		mContext = this;

		setBtnLeftBackground(R.drawable.back_red);
		setBtnLeftVisiable(true);
		setBtnLeftOnClickListener(this);
		setTitleText("文字通关-8个节点");
		setBtnRightBackground(R.drawable.circular_bead_border_white);
		setBtnRightVisiable(true);
		setBtnRightOnClickListener(this);
		setBtnRightText("开始训练");

		InputStream inputStream = getResources().openRawResource(
				R.raw.word_group);

		String wordGroupStr = getFileString(inputStream);
		wordGroup = wordGroupStr.split("\n");

		initViews();
//		initData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initViews() {
		text_one_node_tv = (TextView) findViewById(R.id.text_one_node_tv);
		text_two_node_tv = (TextView) findViewById(R.id.text_two_node_tv);
		text_three_node_tv = (TextView) findViewById(R.id.text_three_node_tv);
		text_four_node_tv = (TextView) findViewById(R.id.text_four_node_tv);
		text_five_node_tv = (TextView) findViewById(R.id.text_five_node_tv);
		text_six_node_tv = (TextView) findViewById(R.id.text_six_node_tv);
		text_seven_node_tv = (TextView) findViewById(R.id.text_seven_node_tv);
		text_eight_node_tv = (TextView) findViewById(R.id.text_eight_node_tv);
		text_nine_node_tv = (TextView) findViewById(R.id.text_nine_node_tv);
		
		textViewList.add(text_one_node_tv);
		textViewList.add(text_two_node_tv);
		textViewList.add(text_three_node_tv);
		textViewList.add(text_four_node_tv);
		textViewList.add(text_five_node_tv);
		textViewList.add(text_six_node_tv);
		textViewList.add(text_seven_node_tv);
		textViewList.add(text_eight_node_tv);
		textViewList.add(text_nine_node_tv);
		
		node_gv = (UnScrollGridView) findViewById(R.id.node_gv);
		node_gv.setOnItemClickListener(this);

		text_one_node_tv.setOnClickListener(this);
		text_two_node_tv.setOnClickListener(this);
		text_three_node_tv.setOnClickListener(this);
		text_four_node_tv.setOnClickListener(this);
		text_five_node_tv.setOnClickListener(this);
		text_six_node_tv.setOnClickListener(this);
		text_seven_node_tv.setOnClickListener(this);
		text_eight_node_tv.setOnClickListener(this);
		text_nine_node_tv.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	private void initData() {

		int[] initRandom = RandomNumUtil.GetRandomSequence(totalNode,999);
		tempData.clear();
		for (int i = 0; i < initRandom.length; i++) {
			tempData.add(wordGroup[initRandom[i]]);
			textViewList.get(i).setText(tempData.get(i));
		}
		
		nodeData.clear();
		int[] intRandom = RandomNumUtil.GetRandomSequence(totalNode);
		for (int i = 0; i < intRandom.length; i++) {
			nodeData.add(i,tempData.get(intRandom[i]));
		}

		if (nodeStrAdapter == null) {
			nodeStrAdapter = new NodeStrAdapter(this, nodeData);
			node_gv.setAdapter(nodeStrAdapter);
		} else {
			nodeStrAdapter.notifyDataSetChanged();
		}

		
		for (int i = 0; i < textViewList.size(); i++) {
			textViewList.get(i).setBackground(getResources().
					getDrawable(R.drawable.circular_bead_border_pink));
		}
		for (int i = 0; i < totalNode; i++) {
			selectMap.put(i, -1);
		}

	}

	public static String getFileString(InputStream inputStream) {
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer("");
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_topbar_left:
			finish();
			break;

		case R.id.btn_topbar_right:
			if (memoryType == 1) {
				startMemoryTime = System.currentTimeMillis();
				initData();
				memoryType = 2;
				setBtnRightText("记忆完毕");
			}else if (memoryType == 2) {
				endMemoryTime = System.currentTimeMillis();  
				memoryEnd();
				node_gv.setVisibility(View.VISIBLE);
				setBtnRightText("提交答案");
				memoryType = 3;
			} else if (memoryType == 3) {
				completeTime = System.currentTimeMillis(); 
				node_gv.setVisibility(View.GONE);
				checkResult();
				setBtnRightText("开始训练");
				memoryType = 1;
			}
			break;

		case R.id.text_one_node_tv:
			if (memoryType == 3) {
				selectNode = 0;
				selectTextView(selectNode);
			}
			break;

		case R.id.text_two_node_tv:
			if (memoryType == 3) {
				selectNode = 1;
				selectTextView(selectNode);
			}
			
			break;

		case R.id.text_three_node_tv:
			if (memoryType == 3) {
				selectNode = 2;
				selectTextView(selectNode);
			}
			
			break;

		case R.id.text_four_node_tv:
			if (memoryType == 3) {
				selectNode = 3;
				selectTextView(selectNode);
			}
			
			break;

		case R.id.text_five_node_tv:
			if (memoryType == 3) {
				selectNode = 4;
				selectTextView(selectNode);
			}
			break;
		case R.id.text_six_node_tv:
			if (memoryType == 3) {
				selectNode = 5;
				selectTextView(selectNode);
			}
			break;
		case R.id.text_seven_node_tv:
			if (memoryType == 3) {
				selectNode = 6;
				selectTextView(selectNode);
			}
			break;
		case R.id.text_eight_node_tv:
			if (memoryType == 3) {
				selectNode = 7;
				selectTextView(selectNode);
			}
			break;
		case R.id.text_nine_node_tv:
			if (memoryType == 3) {
				selectNode = 8;
				selectTextView(selectNode);
			}
			break;
			
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("TextFourNodeActivity  onDestroy");
	}

	@SuppressLint("NewApi")
	private void selectTextView(int index) {
		for (int i = 0; i < textViewList.size(); i++) {
			if (index == i ) {
				if (!TextUtils.isEmpty(textViewList.get(i).getText().toString())) {
					textViewList.get(i).setBackground(getResources().
							getDrawable(R.drawable.circular_bead_border_blue));
				}else{
					textViewList.get(i).setBackground(getResources().
							getDrawable(R.drawable.circular_bead_border_pink));
				}
			}else{
				if (!TextUtils.isEmpty(textViewList.get(i).getText().toString())) {
					textViewList.get(i).setBackground(getResources().
							getDrawable(R.drawable.circular_bead_border_pink));
				}else{
					textViewList.get(i).setBackground(getResources().
							getDrawable(R.drawable.circular_bead_border_white));
				}
			}
		}
		
		if (selectItem >= 0) {
			textViewList.get(selectNode).setText(nodeData.get(selectItem));
			textViewList.get(selectNode).setBackground(getResources().
					getDrawable(R.drawable.circular_bead_border_pink));
			selectMap.put(selectNode, selectItem);
			selectNode = -1;
			selectItem = -1;
			nodeStrAdapter.notifyDataSetChanged();
		}
		
	}
	
	@SuppressLint("NewApi")
	private void memoryEnd() {
		for (int i = 0; i < textViewList.size(); i++) {
			textViewList.get(i).setText("");
			textViewList.get(i).setBackground(getResources().
					getDrawable(R.drawable.circular_bead_border_white));
		}
	}
	
	
	private void checkResult() {
		rightNum = 0;
		errorNum = 0;
        for (int i = 0; i < textViewList.size(); i++) {
			if (tempData.get(i).equals(textViewList.get(i).getText().toString())) {
				rightNum++;
				textViewList.get(i).setText(textViewList.get(i).getText().toString()+"√");
			}else{
				errorNum++;
				textViewList.get(i).setText(textViewList.get(i).getText().toString()+"×"+
						"\n"+tempData.get(i)+"√");
			}
		}
		if (rightNum == textViewList.size()) {
			PassDialog(this, 1);
		}else{
			PassDialog(this, 2);
		}
		
	}

	public class NodeStrAdapter extends BaseAdapter {
		private Context mContext = null;
		private List<String> data;

		public NodeStrAdapter(Context context, List<String> data) {
			mContext = context;
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_select_node, parent, false);
				holder.node_string_tv = (TextView) convertView
						.findViewById(R.id.node_string_tv);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.node_string_tv.setText(data.get(position));
			if (selectItem == position) {
				holder.node_string_tv.setBackground(
						getResources().getDrawable(R.drawable.border_red));
			}else{
				holder.node_string_tv.setBackground(
						getResources().getDrawable(R.drawable.border_gray));
			}
			for (int i = 0; i < selectMap.size(); i++) {
				if (selectMap.get(i) == position) {
					holder.node_string_tv.setText("");
				}
			}

			return convertView;
		}

		class ViewHolder {
			TextView node_string_tv;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectItem = position;
		if (selectNode >= 0) {
			textViewList.get(selectNode).setText(nodeData.get(position));
			textViewList.get(selectNode).setBackground(getResources().
					getDrawable(R.drawable.circular_bead_border_pink));
			selectMap.put(selectNode, selectItem);
			selectNode = -1;
			selectItem = -1;
		}
		
		nodeStrAdapter.notifyDataSetChanged();
	}
	
	
	public void PassDialog(final Context mContext,final int type) {

		final SelfDialog dialogHint = new SelfDialog(mContext, R.style.SelfDialog);
		// 设置它的ContentView
		Window window = dialogHint.getWindow();
		window.setContentView(R.layout.dialog_pass);
		WindowManager.LayoutParams params = window.getAttributes();
//		params.width = LayoutParams.FILL_PARENT;
		params.y = 0; // 设置y坐标
		window.setAttributes(params);
		/** 设置不能点击取消 */
		dialogHint.setCancelable(false);

		long totalUseTime = (completeTime - startMemoryTime)/1000;
		long memoryTime = (endMemoryTime - startMemoryTime)/1000;
		if (memoryTime == 0) {
			memoryTime = 1;
		}
		Date completeDate =  new Date(completeTime); 
		String completeDateStr = DateUtil.format(completeDate, DateUtil.yyyy_MM_dd_HH_mm_ss);
		
		TextView dialog_text1_tv = (TextView) window
		.findViewById(R.id.dialog_text1_tv);
		TextView dialog_text2_tv = (TextView) window
				.findViewById(R.id.dialog_text2_tv);
		TextView dialog_text3_tv = (TextView) window
				.findViewById(R.id.dialog_text3_tv);
		TextView confirm_tv = (TextView) window
				.findViewById(R.id.confirm_tv);
		TextView cancel_tv = (TextView) window
				.findViewById(R.id.cancel_tv);
		TextView dialog_text4_tv = (TextView) window
				.findViewById(R.id.dialog_text4_tv);
		
		if (type == 1) {
			dialog_text1_tv.setText("恭喜"+LocalDataHelper.
					getInstance(mContext).getUserName());
			dialog_text2_tv.setText("通过[文字导图-4个节点]考试");
			dialog_text4_tv.setText("记忆用时:"+memoryTime+"s,总用时:"+totalUseTime+"s");
			dialog_text3_tv.setText("通关时间:"+completeDateStr);
		}else if (type ==2) {
			dialog_text1_tv.setText("很抱歉"+LocalDataHelper.
					getInstance(mContext).getUserName());
			dialog_text2_tv.setText("挑战失败,错误数:"+errorNum);
			dialog_text4_tv.setText("记忆用时:"+memoryTime+"s,总用时:"+totalUseTime+"s");
			dialog_text3_tv.setText("挑战时间:"+completeDateStr);
		}
		
	
		confirm_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMemoryTime = System.currentTimeMillis();
				initData();
				memoryType = 2;
				setBtnRightText("记忆完毕");
				dialogHint.dismiss();
			}
		});
		
		cancel_tv.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogHint.dismiss();
			}
		});
		
		dialogHint.show();
	}

}
