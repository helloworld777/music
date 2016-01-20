package com.music;

import java.util.ArrayList;
import java.util.List;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.music.db.MusicInfoDao;
import com.music.fragment.BusEvent;
import com.music.fragment.ClickEvent;
import com.music.fragment.LocalMusicFragment;
import com.music.fragment.MainFragment;
import com.music.fragment.MainFragment.GridViewItemClick;
import com.music.lu.R;
import com.music.lu.utils.AsyncTaskUtil;
import com.music.lu.utils.DialogUtil;
import com.music.lu.utils.MusicUtils;
import com.music.service.IConstants;
import com.music.widget.slidingmenu.SlidingMenu;

/**
 * ���࣬�״ν���Ӧ�ûᵽ����
 * �����ṩ����ҳMainFragment����ʾ�Ͳ໬MenuFragment����ʾ
 *
 */
@SuppressLint("HandlerLeak")
public class MainContentActivity extends BaseFragmentActivity implements IConstants,GridViewItemClick {

	public static final String ALARM_CLOCK_BROADCAST = "alarm_clock_broadcast";
	public SlidingMenu mSlidingMenu;
	private List<OnBackListener> mBackListeners = new ArrayList<OnBackListener>();
	public Fragment contentFragment;

	private Handler mHandler;
	private MusicInfoDao mMusicDao;
	private SplashScreen mSplashScreen;
//	private int mScreenWidth;
	
	public interface OnBackListener {
		public abstract void onBack();
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);


		setContentView(R.layout.activity_maincontent);
		
		
		
		
		mSplashScreen = new SplashScreen(this);
		mSplashScreen.show(R.drawable.image_splash_background_new,
				SplashScreen.SLIDE_LEFT);
		// set the Above View
		contentFragment = new MainFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_main, contentFragment).commit();

		// configure the SlidingMenu
//		mSlidingMenu = new SlidingMenu(this);
//		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//		mSlidingMenu.setMode(SlidingMenu.RIGHT);
//		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
//		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
//		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		mSlidingMenu.setFadeDegree(0.35f);
//		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//		mSlidingMenu.setMenu(R.layout.frame_menu);
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.frame_menu, new MenuFragment()).commit();

		mMusicDao = new MusicInfoDao(this);
//		mHandler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				mSplashScreen.removeSplashScreen();
//			}
//		};

		initData();
		
	}

	private void initSDCard() {/*
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(1000);// ����������ȼ�
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);// sd�������룬���Ѿ�����
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);// sd�����ڣ�����û�й���
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);// sd�����Ƴ�
		intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);// sd����Ϊ
															// USB�������洢���������ر����
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);// sd���Ѿ���sd����۰γ������ǹ��ص㻹û���
		// intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);// ��ʼɨ��
		// intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);// ɨ�����
		intentFilter.addDataScheme("file");
		registerReceiver(sdCardReceiver, intentFilter);// ע���������
		*/
		EventBus.getDefault().register(this);
	}
	@Subscriber()
	public void onEvent(ClickEvent position){
		Looper.prepare();
		DialogUtil.showToast(this, "postion:"+position.msg);
		Looper.loop();
	}
	private void initData() {
		
		new AsyncTaskUtil(new com.music.lu.utils.AsyncTaskUtil.IAsyncTaskCallBack() {
			
			@Override
			public void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				mSplashScreen.removeSplashScreen();
			}
			
			@Override
			public Object doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				
				if (!mMusicDao.hasData()) {
					MusicUtils.queryMusic(MainContentActivity.this,
							START_FROM_LOCAL);
					MusicUtils.getDefault().queryAlbums(MainContentActivity.this);
					MusicUtils.queryArtist(MainContentActivity.this);
					MusicUtils.queryFolder(MainContentActivity.this);
				}else{
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}
		}).execute("");
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				if (mMusicDao.hasData()) {
//					// ��������ݾ͵�������ת
//					mHandler.sendMessageDelayed(mHandler.obtainMessage(), 3000);
//				} else {
//					MusicUtils.queryMusic(MainContentActivity.this,
//							START_FROM_LOCAL);
//					MusicUtils.queryAlbums(MainContentActivity.this);
//					MusicUtils.queryArtist(MainContentActivity.this);
//					MusicUtils.queryFolder(MainContentActivity.this);
//					mHandler.sendEmptyMessage(1);
//				}
//			}
//		}).start();
	}

//	public void registerBackListener(OnBackListener listener) {
//		if (!mBackListeners.contains(listener)) {
//			mBackListeners.add(listener);
//		}
//	}
//
//	public void unRegisterBackListener(OnBackListener listener) {
//		mBackListeners.remove(listener);
//	}

//	@Override
//	public void onBackPressed() {/*
//		if (mSlidingMenu.isMenuShowing()) {
//			mSlidingMenu.showContent();
//		} else {
//			if (mBackListeners.size() == 0) {
//				// super.onBackPressed();
//				// ��activity�е��� moveTaskToBack (boolean nonRoot)�������ɽ�activity
//				// �˵���̨��ע�ⲻ��finish()�˳���
//				// ����Ϊfalse����ֻ�е�ǰactivity��task����ָӦ�������ĵ�һ��activityʱ������Ч;
//				moveTaskToBack(true);
//			}
//			for (OnBackListener listener : mBackListeners) {
//				listener.onBack();
//			}
//		}
//	*/}

//	private final BroadcastReceiver sdCardReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {/*
//			String action = intent.getAction();
//			if (action.equals("android.intent.action.MEDIA_REMOVED")// ����δ����״̬
//					|| action.equals("android.intent.action.MEDIA_UNMOUNTED")
//					|| action.equals("android.intent.action.MEDIA_BAD_REMOVAL")
//					|| action.equals("android.intent.action.MEDIA_SHARED")) {
//				finish();
//				Toast.makeText(MainContentActivity.this, "SD������γ�����������û����ʼ��!",
//						Toast.LENGTH_SHORT).show();
//			}
//		*/}
//	};

	public void showSleepDialog() {/*

		if (MusicApp.mIsSleepClockSetting) {
			cancleSleepClock();
			Toast.makeText(getApplicationContext(), "��ȡ˯��ģʽ��",
					Toast.LENGTH_SHORT).show();
			return;
		}

		View view = View.inflate(this, R.layout.sleep_time, null);
		final Dialog dialog = new Dialog(this, R.style.lrc_dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		// lp.x = 100; // ��λ��X����
		// lp.y = 100; // ��λ��Y����
		lp.width = (int) (mScreenWidth * 0.7); // ���
		// lp.height = 400; // �߶�

		// ��Window��Attributes�ı�ʱϵͳ����ô˺���,����ֱ�ӵ�����Ӧ������Դ��ڲ����ĸ���,Ҳ������setAttributes
		// dialog.onWindowAttributesChanged(lp);
		dialogWindow.setAttributes(lp);

		dialog.show();

		final Button cancleBtn = (Button) view.findViewById(R.id.cancle_btn);
		final Button okBtn = (Button) view.findViewById(R.id.ok_btn);
		final EditText timeEt = (EditText) view.findViewById(R.id.time_et);
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == cancleBtn) {
					dialog.dismiss();
				} else if (v == okBtn) {
					String timeS = timeEt.getText().toString();
					if (TextUtils.isEmpty(timeS)
							|| Integer.parseInt(timeS) == 0) {
						Toast.makeText(getApplicationContext(), "������Ч��",
								Toast.LENGTH_SHORT).show();
						return;
					}
					setSleepClock(timeS);
					dialog.dismiss();
				}
			}
		};

		cancleBtn.setOnClickListener(listener);
		okBtn.setOnClickListener(listener);
	*/}

	/**
	 * ����˯������
	 * 
	 * @param timeS
	 */
	private void setSleepClock(String timeS) {/*
		Intent intent = new Intent(ALARM_CLOCK_BROADCAST);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				MainContentActivity.this, 0, intent, 0);
		// ����timeʱ��֮���˳�����
		int time = Integer.parseInt(timeS);
		long longTime = time * 60 * 1000L;
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC, System.currentTimeMillis() + longTime,
				pendingIntent);
		MusicApp.mIsSleepClockSetting = true;
		Toast.makeText(getApplicationContext(), "����"+timeS+"���Ӻ��˳����", Toast.LENGTH_SHORT)
				.show();
	*/}

	/**
	 * ȡ��˯������
	 */
	private void cancleSleepClock() {/*
		Intent intent = new Intent(ALARM_CLOCK_BROADCAST);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				MainContentActivity.this, 0, intent, 0);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(pendingIntent);
		MusicApp.mIsSleepClockSetting = false;
	*/}
	
	private BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			//�˳�����
			finish();
		}

	};

	@Override
	public void onDestroy() {
		super.onDestroy();
//		unregisterReceiver(sdCardReceiver);
//		unregisterReceiver(mAlarmReceiver);
////		MusicApp.mServiceManager.exit();
////		MusicApp.mServiceManager = null;
//		MusicUtils.clearCache();
//		cancleSleepClock();
//		System.exit(0);
	}

	@Override
	public void clickIndex(int position) {
		// TODO Auto-generated method stub
//		DialogUtil.showToast(this, "postion:"+position);
		switch (position) {
		case 1:
			contentFragment=new LocalMusicFragment();
			break;
		default:
			break;
		}
		
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.frame_main, contentFragment).commit();
	}

}
