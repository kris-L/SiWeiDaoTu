package com.kris.siweidaotu.ui;

import com.kris.siweidaotu.R;
import com.kris.siweidaotu.helper.LocalDataHelper;
import com.kris.siweidaotu.util.AlertDialogWin;
import com.kris.siweidaotu.util.DESUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

    private TextView textView1,registerCode_tv,textView3,textView4;
    private Button registerCopy,zhantieBtn,app_register_btn,registerBackBtn,button5;
    private EditText editText1;
    
    private Context mContext;
    
    private String yourCodeShow = "";
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_register);
        mContext = this;
        
        textView1 = (TextView) findViewById(R.id.registerShow);
        registerCode_tv = (TextView) findViewById(R.id.registerCode);
        registerCopy = (Button) findViewById(R.id.registerCopy);
        textView3 = (TextView) findViewById(R.id.registerText);
        editText1 = (EditText) findViewById(R.id.registerInput);
        zhantieBtn = (Button) findViewById(R.id.zhantie);
        app_register_btn = (Button) findViewById(R.id.app_register);
        registerBackBtn = (Button) findViewById(R.id.registerBack);
        button5 = (Button) findViewById(R.id.registerClear);
        textView4 = (TextView) findViewById(R.id.registerTiShi);
        textView4.setTextSize(15f);
        textView4.setTextColor(-16776961);
        textView4.setText("注册提示：\n注册资格请咨询，\nQQ：1832768043（记忆魅力）\nQQ：8716128（李博士）\n注册方法：将手机码复制后发给李博士或者记忆魅力，将获取的注册码，粘贴到注册码区，点击注册按钮。\n");
        textView1.setTextSize(20f);
        registerCode_tv.setTextSize(20f);
        textView3.setTextSize(20f);
        editText1.setTextSize(20f);
        registerCopy.setTextSize(20f);
        zhantieBtn.setTextSize(20f);
        app_register_btn.setTextSize(20f);
        registerBackBtn.setTextSize(20f);
        button5.setTextSize(20f);
        try {
            this.yourCodeShow = getShouJiInfo(((Activity)this));
        }
        catch(Exception v0) {
            v0.printStackTrace();
            AlertDialogWin.showAlertDialog(((Activity)this), "获取手机码异常", "没有正常获取手机码，请与管理员联系（QQ：31525874）\n"
                     + v0.getMessage() + v0.toString(), "确定");
        }

        registerCode_tv.setText(this.yourCodeShow);
        registerCopy.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                setClipboard(RegisterActivity.this, RegisterActivity.this.yourCodeShow);
                AlertDialogWin.showAlertDialog(RegisterActivity.this, "复制手机码", "手机码已经复制到剪贴板。", "确定");
            }
        });
        
        zhantieBtn.setOnClickListener(new OnClickListener() {
            @SuppressLint("NewApi")
			public void onClick(View arg0) {
            	ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
				String clipStr = clip.getText().toString(); // 粘贴
            	editText1.setText(clipStr);
//                registerOperation(RegisterActivity.this, editText1.getText()
//                        .toString(), RegisterActivity.this.yourCodeShow);
            }
        });
        
        app_register_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
            	registerOperation(RegisterActivity.this, editText1.getText()
                      .toString(), RegisterActivity.this.yourCodeShow);
            }
        });
        
        registerBackBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
            	finish();
            }
        });
        button5.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                editText1.setText("");
            }
        });
        
        String v2 = getShouJiInfo(((Activity)this));
        
//        registerBackBtn.setClickable(false);
//        registerBackBtn.setVisibility(8);
        
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.getMenuInflater().inflate(R.menu.register, menu);
//        return true;
//    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == 4) {
            this.finish();
        }

        return super.onKeyDown(keyCode, event);
    }

//    public boolean onOptionsItemSelected(MenuItem mi) {
//        switch(mi.getItemId()) {
//            case 2131296908: {
////                getCurScreen(((Activity)this));
//                break;
//            }
//        }
//
//        return 1;
//    }
    
    public String getShouJiInfo(Activity activity) {
        String v2 = "";
        String deviceId  = Secure.getString(activity.getWindow().getContext().getContentResolver(), 
                "android_id");
        System.out.println("deviceId="+deviceId);
        try {
            v2 = DESUtil.encryptDES(deviceId, "jiyiInte");
        }
        catch(Exception v0) {
            v0.printStackTrace();
            AlertDialogWin.showAlertDialog(activity, "加密异常", String.valueOf(v0.getMessage()) + v0.toString(), 
                    "确定");
        }
        return v2;
    }
    
    @SuppressLint("NewApi")
	public void setClipboard(Activity paramActivity, String paramString)
    {
      ((ClipboardManager)paramActivity.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("手机码", paramString));
    }
    
    
    public void registerOperation(Activity activity, String registerCode, String shoujiInfo) {
        if(registerCode.equals("")) {
            AlertDialogWin.showAlertDialog(activity, "注册错误提示", "未检测到输入的注册码", "确定");
        }
        else if(MatchedRegister(activity, registerCode, shoujiInfo).booleanValue()) {
        	LocalDataHelper.getInstance(mContext).setRegister(true);
            AlertDialogWin.showAlertDialog(activity, "注册完成！", "恭喜你注册成功！\n现在可以使用注册版功能。", "确定");
        }
        else {
        	LocalDataHelper.getInstance(mContext).setRegister(false);
            AlertDialogWin.showAlertDialog(activity, "注册失败！", "您输入的注册码不正确，请联系管理员重新获取。\nQQ: 1832768043（记忆魅力）\nQQ：8716128（李博士）\n", 
                    "确定");
        }
    }
    
    public Boolean MatchedRegister(Activity activity, String registerCode, String shoujiInfo) {
        Boolean v5;
        String v1 = "";
        try {
            v1 = DESUtil.decryptDES(registerCode, "jiyiJMBC");
        }
        catch(Exception v0) {
            v0.printStackTrace();
            AlertDialogWin.showAlertDialog(activity, "注册错误提示", "注册码分析异常： " + v0.getMessage() + v0.toString(), 
                    "确定");
        }

        String v2 = String.valueOf(shoujiInfo) + "jiyi8.cn";
        String v4 = "VIP" + shoujiInfo + "jiyi8.cn";
        String v3 = "SVIP" + shoujiInfo + "jiyi8.cn";
        if((v1.equals(v2)) || (v1.equals(v4))) {
        	
            v5 = true;
        }
        else if(v1.equals(v3)) {
        	
            v5 = true;
        } else {
            v5 = false;
        }

        return v5;
    }
    
    
}

