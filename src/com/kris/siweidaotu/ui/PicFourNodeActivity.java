package com.kris.siweidaotu.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import android.R.integer;
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
import android.widget.ImageView;
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

public class PicFourNodeActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private ImageView pic_one_node_iv;
	private ImageView pic_two_node_iv;
	private ImageView pic_three_node_iv;
	private ImageView pic_four_node_iv;
	private ImageView pic_five_node_iv;
	private UnScrollGridView node_gv;

	private Context mContext;
	/** 1:开始训练 2:记忆中 3：提交答案 */
	private int memoryType = 1;
	private int selectNode = -1;
	private int selectItem = -1;

	public static Map<Integer,Integer> selectMap = new HashMap<Integer,Integer>();
	private List<Integer> nodeData = new ArrayList<Integer>();
	private List<Integer> randomIntList = new ArrayList<Integer>();
	private NodeStrAdapter nodeStrAdapter = null;
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	
	private long startMemoryTime = 0;  
	private long endMemoryTime = 0; 
	private long completeTime = 0;
	private int rightNum = 0;
	private int errorNum = 0;
	private int totalNode = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetBodyView(R.layout.activity_picture_four_node, true);
		setBtnLeftOnClickListener(this);
		mContext = this;

		setBtnLeftBackground(R.drawable.back_red);
		setBtnLeftVisiable(true);
		setBtnLeftOnClickListener(this);
		setTitleText("图片通关-4个节点");
		setBtnRightBackground(R.drawable.circular_bead_border_white);
		setBtnRightVisiable(true);
		setBtnRightOnClickListener(this);
		setBtnRightText("开始训练");

		
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
		pic_one_node_iv = (ImageView) findViewById(R.id.pic_one_node_iv);
		pic_two_node_iv = (ImageView) findViewById(R.id.pic_two_node_iv);
		pic_three_node_iv = (ImageView) findViewById(R.id.pic_three_node_iv);
		pic_four_node_iv = (ImageView) findViewById(R.id.pic_four_node_iv);
		pic_five_node_iv = (ImageView) findViewById(R.id.pic_five_node_iv);

		imageViewList.add(pic_one_node_iv);
		imageViewList.add(pic_two_node_iv);
		imageViewList.add(pic_three_node_iv);
		imageViewList.add(pic_four_node_iv);
		imageViewList.add(pic_five_node_iv);
		
		node_gv = (UnScrollGridView) findViewById(R.id.node_gv);
		node_gv.setOnItemClickListener(this);

		pic_one_node_iv.setOnClickListener(this);
		pic_two_node_iv.setOnClickListener(this);
		pic_three_node_iv.setOnClickListener(this);
		pic_four_node_iv.setOnClickListener(this);
		pic_five_node_iv.setOnClickListener(this);
		
		for (int i = 0; i < imageViewList.size(); i++) {
			imageViewList.get(i).getDrawable().setLevel(110);
		}
	}

	@SuppressLint("NewApi")
	private void initData() {
		selectNode = -1;
		selectItem = -1;
		
		int[] initRandom = RandomNumUtil.GetRandomSequence(totalNode,109);
		randomIntList.clear();
		for (int i = 0; i < initRandom.length; i++) {
			randomIntList.add(initRandom[i]);
			imageViewList.get(i).getDrawable().setLevel(randomIntList.get(i));
		}

		nodeData.clear();
		int[] intRandom = RandomNumUtil.GetRandomSequence(totalNode);
		for (int i = 0; i < intRandom.length; i++) {
			nodeData.add(i,randomIntList.get(intRandom[i]));
		}

		if (nodeStrAdapter == null) {
			nodeStrAdapter = new NodeStrAdapter(this, nodeData);
			node_gv.setAdapter(nodeStrAdapter);
		} else {
			nodeStrAdapter.notifyDataSetChanged();
		}
		
		for (int i = 0; i < imageViewList.size(); i++) {
			imageViewList.get(i).setBackground(getResources().
					getDrawable(R.drawable.border_gray));
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
			
		case R.id.pic_one_node_iv:
			if (memoryType == 3) {
				selectNode = 0;
				selectTextView(selectNode);
			}
			
			break;

		case R.id.pic_two_node_iv:
			if (memoryType == 3) {
				selectNode = 1;
				selectTextView(selectNode);
			}
			
			break;

		case R.id.pic_three_node_iv:
			if (memoryType == 3) {
				selectNode = 2;
				selectTextView(selectNode);
				
			}
			break;

		case R.id.pic_four_node_iv:
			if (memoryType == 3) {
				selectNode = 3;
				selectTextView(selectNode);
			}
			break;

		case R.id.pic_five_node_iv:
			if (memoryType == 3) {
				selectNode = 4;
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
		for (int i = 0; i < imageViewList.size(); i++) {
			if (index == i) {
				if (imageViewList.get(i).getDrawable().getLevel() != 110) {
					imageViewList.get(i).setBackground(getResources().
							getDrawable(R.drawable.border_blue));
				}else{
					imageViewList.get(i).setBackground(getResources().
							getDrawable(R.drawable.border_red));
				}
			}else{
				imageViewList.get(i).setBackground(getResources().
						getDrawable(R.drawable.border_gray));
			}
			
		}
		
		if (selectItem >= 0) {
			imageViewList.get(selectNode).getDrawable().setLevel(nodeData.get(selectItem));
			imageViewList.get(selectNode).setBackground(getResources().
					getDrawable(R.drawable.border_gray));
			selectMap.put(selectNode, selectItem);
			selectNode = -1;
			selectItem = -1;
			nodeStrAdapter.notifyDataSetChanged();
		}
		
	}
	
	@SuppressLint("NewApi")
	private void memoryEnd() {
		for (int i = 0; i < imageViewList.size(); i++) {
			imageViewList.get(i).getDrawable().setLevel(110);
			imageViewList.get(i).setBackground(getResources().
					getDrawable(R.drawable.border_gray));
		}
	}
	
	
	@SuppressLint("NewApi")
	private void checkResult() {
		rightNum = 0;
		errorNum = 0;
        for (int i = 0; i < imageViewList.size(); i++) {
			if (randomIntList.get(i) == imageViewList.get(i).getDrawable().getLevel()) {
				rightNum++;
//				imageViewList.get(i).setText(imageViewList.get(i).getText().toString()+"√");
			}else{
				errorNum++;
				imageViewList.get(i).setBackground(getResources().
						getDrawable(R.drawable.border_red));
			}
		}
		if (rightNum == imageViewList.size()) {
			PassDialog(this, 1);
		}else{
			PassDialog(this, 2);
		}
	}

	public class NodeStrAdapter extends BaseAdapter {
		private Context mContext = null;
		private List<Integer> data;

		public NodeStrAdapter(Context context, List<Integer> data) {
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
						R.layout.item_select_node_pic, parent, false);
				holder.node_picture_iv = (ImageView) convertView
						.findViewById(R.id.node_picture_iv);
				convertView.setTag(holder);
			} else
				holder = (ViewHolder) convertView.getTag();

			holder.node_picture_iv.getDrawable().setLevel(data.get(position));
			if (selectItem == position) {
				holder.node_picture_iv.setBackground(
						getResources().getDrawable(R.drawable.border_red));
			}else{
				holder.node_picture_iv.setBackground(
						getResources().getDrawable(R.drawable.border_gray));
			}
			
			for (int i = 0; i < selectMap.size(); i++) {
				if (selectMap.get(i) == position) {
					holder.node_picture_iv.getDrawable().setLevel(110);
				}
			}
			return convertView;
		}

		class ViewHolder {
			ImageView node_picture_iv;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("onItemClick selectNode="+selectNode);
		selectItem = position;
		if (selectNode >= 0) {
			imageViewList.get(selectNode).getDrawable().setLevel(nodeData.get(selectItem));
			imageViewList.get(selectNode).setBackground(getResources().
					getDrawable(R.drawable.border_gray));
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
		TextView dialog_text4_tv = (TextView) window
				.findViewById(R.id.dialog_text4_tv);
		TextView confirm_tv = (TextView) window
				.findViewById(R.id.confirm_tv);
		TextView cancel_tv = (TextView) window
				.findViewById(R.id.cancel_tv);
		
		if (type == 1) {
			dialog_text1_tv.setText("恭喜"+LocalDataHelper.
					getInstance(mContext).getUserName());
			dialog_text2_tv.setText("通过[图片导图-4个节点]考试");
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

