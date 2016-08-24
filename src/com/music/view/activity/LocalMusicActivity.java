package com.music.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.bean.AlbumInfo;
import com.music.bean.ArtistInfo;
import com.music.bean.FolderInfo;
import com.music.bean.MusicBaseInfo;
import com.music.bean.UserManager;
import com.music.broadcastreceiver.MyBroadcastReceiver;
import com.music.broadcastreceiver.State;
import com.music.lu.R;
import com.music.service.IConstants;
import com.music.utils.ApplicationUtil;
import com.music.utils.AsyncTaskUtil;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.utils.LogUtil;
import com.music.utils.MediaUtil;
import com.music.utils.Mp3Util_New;
import com.music.utils.MusicUtils;
import com.music.utils.PhotoUtils;
import com.music.view.MyNotification;
import com.music.view.PlayPauseDrawable;
import com.music.view.SplashScreen;
import com.music.view.fragment.LocalMusicFragment;
import com.music.view.fragment.MusicListFragment;
import com.music.view.gesturepressword.UnlockGesturePasswordActivity;
import com.music.view.widget.CircularImage;
import com.music.view.widget.MusicTimeProgressView;
import com.music.widget.slidingmenu2.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 *
 */
@SuppressLint("HandlerLeak")
@ContentView(value = R.layout.activity_localmusic)
public class LocalMusicActivity extends BaseFragmentActivity implements
		IConstants {

	public interface OnBackListener {
		public abstract void onBack();
	}

	public static final String TAG = null;
	public static final int REQUESTCODE_LOGIN = 0;
	private static final int LOCK_PASSWORD = 1;
	public Fragment contentFragment;

	private SplashScreen mSplashScreen;

	private boolean isHome = true;

	@ViewInject(value = R.id.tv_title)
	private TextView tv_title;

	@ViewInject(value = R.id.tv_music_Artist)
	private TextView tv_music_Artist;

	@ViewInject(value = R.id.tv_music_time)
	private TextView tv_music_CurrentTime;

	@ViewInject(value = R.id.tv_music_title)
	private TextView tv_music_title;

	@ViewInject(value = R.id.tv_mobile)
	private TextView tv_mobile;

	@ViewInject(value = R.id.btn_playing2)
	private ImageButton btn_musicPlaying;

	private MyNotification myNotification;

	@ViewInject(value = R.id.iv_music_album)
	private ImageView iv_music_album;

	@ViewInject(value = R.id.slidingMenu)
	private com.music.widget.slidingmenu.SlidingMenu slidingMenu;

	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(value = R.id.iv_search)
	private ImageView iv_search;

	@ViewInject(value = R.id.musicTimeProgressView)
	private MusicTimeProgressView musicTimeProgressView;

	/** 圆形头像类 */
	@ViewInject(value = R.id.iv_header)
	private CircularImage iv_header;
	@ViewInject(value = R.id.tv_username)
	private TextView tv_username;

	@SuppressWarnings("unused")
	private PlayPauseDrawable playPauseDrawable;
	private int playColor = 0XFFE91E63;
	private int pauseColor = 0XFFffffff;
	private int dwableDuaration = 200;
	private MusicBaseInfo currentMp3Info;
	private Mp3Util_New mp3Util;

	private MusicListFragment musicListFragment = null;
	private FragmentTransaction transaction;
	private LocalMusicFragment localMusicFragment;
	private MyBroadcastReceiver myBroadcastReceiver;
	@SuppressWarnings("unused")
	private SlidingMenu mSlidingMenu;
	/**
	 * 拍照传过来的照片路径
	 */
	protected String userHeaderImg;

	private boolean isFirst = true;

	private boolean unLockSuccess = true;

	// private boolean
	@TargetApi(19)
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		LogUtil.d(this, "onCreate.................");
		mSplashScreen = new SplashScreen(this);
		mSplashScreen.show(R.drawable.image_splash_background_new,
				SplashScreen.SLIDE_UP);

		mp3Util = Mp3Util_New.getDefault();
		playPauseDrawable = new PlayPauseDrawable(30, playColor, pauseColor,
				dwableDuaration);
		long start1 = SystemClock.currentThreadTimeMillis();
		initData();
		long start2 = SystemClock.currentThreadTimeMillis();
		DeBug.d(this, "initData:" + (start2 - start1) / 1000.0 + " s");
		registerReceiver();
		long start3 = SystemClock.currentThreadTimeMillis();
		DeBug.d(this, "registerReceiver:" + (start3 - start2) / 1000.0 + " s");
		// handler.sendEmptyMessageDelayed(0, 500);
		initUmeng();
		long start4 = SystemClock.currentThreadTimeMillis();
		DeBug.d(this, "initUmeng:" + (start4 - start3) / 1000.0 + " s");

		
//		tintManager.setTintColor(Color.argb(153, 255, 00, 00));
	}

	private void initUmeng() {
		// umeng push
		PushAgent.getInstance(this).onAppStart();
		// 开启推送并设置注册的回调处理
		PushAgent.getInstance(this).enable(new IUmengRegisterCallback() {

			@Override
			public void onRegistered(String registrationId) {
				DeBug.d(LocalMusicActivity.this,
						"onRegistered....................registrationId:"
								+ registrationId);
			}
		});
		// UmengUpdateAgent.update(this);
		// UpdateConfig.setDebug(true);
	}

	/**
	 * click ARTIST ,ALBUM,FOLDER jump to musiclistfragment
	 * 
	 * @param flag
	 * @param object
	 */
	public void changeFragment(int flag, Object object) {
		if (musicListFragment == null) {
			musicListFragment = new MusicListFragment();
		}

		musicListFragment.initData(flag, object, this);
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.id_frame, musicListFragment);
		transaction.addToBackStack(null);
		transaction.commit();

		isHome = false;
		String title = "";
		switch (flag) {
		case START_FROM_ARTIST:
			ArtistInfo artistInfo = (ArtistInfo) object;
			title = artistInfo.artist_name;
			break;
		case START_FROM_ALBUM:
			AlbumInfo albumInfo = (AlbumInfo) object;
			title = albumInfo.album_name;
			break;
		case START_FROM_FOLDER:
			FolderInfo folderInfo = (FolderInfo) object;
			title = folderInfo.folder_name;
			break;
		case START_FROM_FAVORITE:
			title = "我喜欢的";
			break;
		}
		tv_title.setText(title);
	}

	private void initData() {
		loadDataAsyncTaskUtil.execute("");
	}

	private AsyncTaskUtil loadDataAsyncTaskUtil = new AsyncTaskUtil(
			new AsyncTaskUtil.IAsyncTaskCallBack() {
				final long start = System.currentTimeMillis();

				@Override
				public Object doInBackground(String... arg0) {
					MusicUtils.getDefault().queryMusic(getApplication(),
							START_FROM_LOCAL);
					MusicUtils.getDefault().queryAlbums(getApplication());
					MusicUtils.getDefault().queryArtist(getApplication());
					MusicUtils.getDefault().queryFolder(getApplication());
					return null;
				}

				@Override
				public void onPostExecute(Object result) {
					long end = System.currentTimeMillis();
					int speedTime = (int) ((end - start));
					DeBug.d(LocalMusicActivity.this,
							"..........onPostExecute, speedTime:" + speedTime);
					if (speedTime < 3000) {
						try {
							Thread.sleep(3000 - speedTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mSplashScreen.removeSplashScreen();
					initWidgetData();
					resetPlayState();
				}
			});

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.i(getClass(), "onActivityResult..............resultCode="
				+ resultCode);
		switch (requestCode) {
		case REQUESTCODE_LOGIN:
			loginResult(data);
			break;

		case LOCK_PASSWORD:
			unLockSuccess = (data != null);

			LogUtil.i(getClass(), "..............unLockSuccess="
					+ unLockSuccess);
			if (!unLockSuccess) {
				exit();
			}
			break;
		default:
			UserManager.getInstance().chooseHeaderImg(requestCode, resultCode,
					data, this, iv_header, userHeaderImg);
			break;
		}

	}

	private void loginResult(Intent data) {
		if (data == null) {
			DialogUtil.showToast(getApplicationContext(), "登陆失败");
			return;
		}
		DialogUtil.showToast(getApplicationContext(), "登录成功");
		String username = UserManager.getInstance().getUserBean().getUsername();
		tv_username.setText(username);
		String userHeaderUrl = UserManager.getInstance().getUserBean()
				.getHeadPath();

		// UserManager.getInstance().downUserHeader(userHeader,iv_header);
		ImageLoader.getInstance().displayImage(userHeaderUrl, iv_header);
	}

	private void resetPlayState() {

		currentMp3Info = mp3Util.getCurrentMp3Info();
		tv_music_title.setText(currentMp3Info.getTitle());
		tv_music_Artist.setText(currentMp3Info.getArtist());
		Bitmap bmp = MediaUtil.getArtwork(getApplicationContext(),
				currentMp3Info.getSongId(), currentMp3Info.getAlbumId(), true,
				true);
		iv_music_album.setImageBitmap(bmp);

		DeBug.d(this,
				"currentMp3Info.getDuration():" + currentMp3Info.getDuration());
		musicTimeProgressView.setMaxProgress(currentMp3Info.getDuration());
	}

	private void initWidgetData() {
		if (localMusicFragment == null) {
			localMusicFragment = new LocalMusicFragment();
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.id_frame, localMusicFragment)
				.commitAllowingStateLoss();
		tv_mobile.setText(android.os.Build.MODEL);
		myNotification = new MyNotification(this);
	}

	@OnClick({ R.id.iv_back, R.id.btn_next2, R.id.btn_playing2,
			R.id.music_about_layout, R.id.rl_setting, R.id.rl_exit,
			R.id.iv_header, R.id.iv_search })
	public void onclick(View view) {

		switch (view.getId()) {
		case R.id.iv_back:
			back();
			break;
		case R.id.btn_playing2:
			Mp3Util_New.getDefault().playMusic();
			break;
		case R.id.btn_next2:
			Mp3Util_New.getDefault().nextMusic(false);
			break;
		case R.id.rl_setting:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.music_about_layout:
			startActivity(PlayerActivity.class);
			break;
		case R.id.rl_exit:
			showExitDialog();
			break;
		case R.id.iv_header:
			chooseHeader();
			break;
		case R.id.iv_search:
			startActivity(SearchMusicActivity.class);
			break;
		default:
			break;
		}

	}

	private void chooseHeader() {
		if (!UserManager.isLogin()) {
			DialogUtil.showToast(getApplicationContext(), "您还未登陆,请先登陆");
			Intent intent2 = new Intent(this, LoginActivity.class);
			startActivityForResult(intent2, REQUESTCODE_LOGIN);
		} else {
			chooseHeaderImgDialog();
		}
	}

	private void back() {
		if (!isHome) {
			transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(musicListFragment);
			transaction.commit();
			tv_title.setText("本地音乐");
			isHome = true;
		} else {
			slidingMenu.toggle();
			if (slidingMenu.isOpen()) {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_forward);
			} else {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_back);
			}
		}
	}

	/**
	 * 选择头像
	 */
	private void chooseHeaderImgDialog() {
		DialogUtil.showAlertDialog(this, "头像选择", new String[] { "拍照", "本地" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							userHeaderImg = PhotoUtils
									.takePicture(LocalMusicActivity.this);
							break;
						case 1:
							PhotoUtils.selectPhoto(LocalMusicActivity.this);
							break;
						default:
							break;
						}

						DialogUtil.closeAlertDialog();

					}
				});
	}

	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	//
	// if (!mp3Util.isPlaying()) {
	// DeBug.d(LocalMusicActivity.this, "............animatePlay");
	// playPauseDrawable.animatePlay();
	// } else {
	// DeBug.d(LocalMusicActivity.this, "............animatePause");
	// playPauseDrawable.animatePause();
	// }
	//
	// };
	// };

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isHome) {
				tv_title.setText("本地音乐");
				isHome = true;
			} else {
				DeBug.d(TAG, "moveTaskToBack.........................");
				ApplicationUtil.setAppToBack(this, 1);
				moveTaskToBack(false);
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtil.i(getClass(), "onResume..............unLockSuccess:"
				+ unLockSuccess);
		LogUtil.i(getClass(),
				"onResume..............ApplicationUtil.getAppLockState(this):"
						+ ApplicationUtil.getAppLockState(this));
		LogUtil.i(getClass(),
				"onResume..............ApplicationUtil.getAppToBack(this):"
						+ ApplicationUtil.getAppToBack(this));
		if (ApplicationUtil.getAppLockState(this) == 1) {

			if (isFirst) {
				Intent intent = new Intent(this,
						UnlockGesturePasswordActivity.class);
				startActivityForResult(intent, LOCK_PASSWORD);
				isFirst = false;
			} else {
				if (ApplicationUtil.getAppToBack(this) == 1) {
					Intent intent = new Intent(this,
							UnlockGesturePasswordActivity.class);
					startActivityForResult(intent, LOCK_PASSWORD);
				}
			}
		}
	}

	/**
	 * 注册广播接收器
	 */
	private void registerReceiver() {

		regisgerPlayStateReceiver();

	}

	/**
	 * 注册歌曲播放状态接收器
	 */
	private void regisgerPlayStateReceiver() {
		State state = new MyMainState();
		IntentFilter filter = new IntentFilter();
		myBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		registerReceiver(myBroadcastReceiver, filter);
	}

	/**
	 * 当前播放状体类
	 * 
	 * 
	 */
	class MyMainState implements State {

		@Override
		public void currentState(Intent intent) {
			int currentTime = intent.getIntExtra("currentTime", -1);
			mp3Util.setCurrentTime(currentTime);
			tv_music_CurrentTime.setText(MediaUtil.formatTime(currentTime));
			musicTimeProgressView.setCurrentProgress(currentTime);
		}

		@Override
		public void duration(Intent intent) {
			Log.i(TAG, "duration()");
			resetPlayState();
			myNotification.reset();
		}

		@Override
		public void pauseMusicState() {
			// playPauseDrawable.animatePlay();
			btn_musicPlaying
					.setImageResource(R.drawable.img_button_notification_play_play);
			myNotification.setPlayImageState(false);
		}

		@Override
		public void playMusicState() {
			// playPauseDrawable.animatePause();
			btn_musicPlaying
					.setImageResource(R.drawable.img_button_notification_play_pause);
			myNotification.setPlayImageState(true);

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.action_exit:
			showExitDialog();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showExitDialog() {
		DialogUtil.showExitAlertDialog(this,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						exit();
					}
				});
	}

	/**
	 * 应用程序退出
	 */
	public void exit() {
		unregisterReceiver();
		ApplicationUtil.setAppToBack(this, 1);
		mp3Util.saveCurrentMusicInfo(this);
		myNotification.cancel();
		mp3Util.unBindService();
		finish();
		System.exit(0);

		// onDestroy();
	};

	public void unregisterReceiver() {
		unregisterReceiver(myBroadcastReceiver);
		myNotification.unregisterReceiver();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			DeBug.d(TAG, "slidingMenu.isOpen()=" + slidingMenu.isOpen());
			if (slidingMenu.isOpen()) {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_forward);
			} else {
				iv_back.setImageResource(R.drawable.ic_common_title_bar_back);
			}
		}
		return super.onTouchEvent(event);
	}
}
