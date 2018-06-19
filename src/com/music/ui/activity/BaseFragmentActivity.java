package com.music.ui.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.music.MusicApplication;
import com.music.annotation.ComputeTimeUtil;
import com.music.bean.MessageEvent;
import com.music.lu.R;
import com.music.utils.ApplicationUtil;
import com.music.utils.DeBug;
import com.music.utils.SystemBarTintManager;
import com.music.utils.screen.ScreenShotUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class BaseFragmentActivity extends AppCompatActivity {
	protected SystemBarTintManager tintManager;
	@SuppressLint("ResourceAsColor")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
		ViewUtils.inject(this);
		ComputeTimeUtil.inject(this);
		tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setNavigationBarTintEnabled(true);

		tintManager.setTintColor(getResources().getColor(R.color.transparent));

	}
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void messageEventBus(MessageEvent event){
		handleMessage(event);
	}

	protected void handleMessage(MessageEvent event) {
	}

	/**
	 * Sets the entire activity-wide theme.
	 */
	private void setTheme() {
		//Set the UI theme.
		if (MusicApplication.getInstance().getCurrentTheme()==MusicApplication.DARK_THEME) {
			setTheme(R.style.AppTheme);
		} else {
//			setTheme(R.style.AppThemeLight);
		}

	}
	protected void startActivity(Class<?> class1) {
		startActivity(new Intent(this, class1));
	}
	protected void startActivity(Class<?> clazz,View sharedElement,String sharedElementName) {
		if (Build.VERSION.SDK_INT>21){
			ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, sharedElement, sharedElementName);
			startActivity(new Intent(this, clazz),options.toBundle());
		}else{
			startActivity(clazz);
		}

	}

	protected void changeFont(ViewGroup root) {
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/a.ttf");
		for (int i = 0, count = root.getChildCount(); i < count; i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				DeBug.d(this, v.toString());
				((TextView) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFont((ViewGroup) v);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
//		ViewGroup root = (ViewGroup) this.getWindow().getDecorView();
//		changeFont(root);
		if(ApplicationUtil.getYaoYiYao(this)){
			ScreenShotUtil.getInstance().registerShakeToScrShot(this);
		}
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		EventBus.getDefault().unregister(this);
	}
}
