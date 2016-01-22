package com.music.view.activity;

import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.bean.LyricSentence;
import com.music.bean.MusicBaseInfo;
import com.music.broadcastreceiver.MyBroadcastReceiver;
import com.music.broadcastreceiver.State;
import com.music.lrc.LyricDownloadManager;
import com.music.lrc.LyricLoadHelper;
import com.music.lrc.LyricLoadHelper.LyricListener;
import com.music.lrc.LyricView;
import com.music.lu.R;
import com.music.model.LyricModel;
import com.music.utils.AppConstant;
import com.music.utils.AsyncTaskUtil;
import com.music.utils.AsyncTaskUtil.IAsyncTaskCallBack;
import com.music.utils.ConstantUtil;
import com.music.utils.DialogUtil;
import com.music.utils.ImageUtil;
import com.music.utils.MediaUtil;
import com.music.utils.Mp3Util_New;
import com.music.view.animator.ActivityAnimator;
import com.music.view.widget.RoundImageView;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

/**
 * �������Ž�����
 * 
 * 
 */
@SuppressLint("NewApi")
@ContentView(value = R.layout.activity_play_layout)
public class PlayerActivity extends BaseActivity implements
		LyricView.LyricViewClickListener {
	private final static String TAG = "PlayerActivity";

	@ViewInject(value = R.id.musicTitle)
	private TextView musicTitle;
	@ViewInject(value = R.id.musicArtist)
	private TextView musicArtist;

	@ViewInject(value = R.id.previous_music)
	private ImageButton previousBtn; // ��һ��

	@ViewInject(value = R.id.repeat_music)
	private ImageButton repeatBtn; // �ظ�������ѭ����ȫ��ѭ����

	@ViewInject(value = R.id.play_music)
	private ImageButton playBtn; // ���ţ����š���ͣ��

	@ViewInject(value = R.id.next_music)
	private ImageButton nextBtn; // ��һ��

	@ViewInject(value = R.id.sb_progress)
	private SeekBar music_progressBar; // ��������

	@ViewInject(value = R.id.ib_back)
	private ImageButton ib_back;

	@ViewInject(value = R.id.img_share)
	private ImageButton img_share;

	@ViewInject(value = R.id.iv_music_album)
	private RoundImageView iv_music_album;
	
	@ViewInject(value = R.id.iv_needle)
	private ImageView iv_needle;

	@ViewInject(value = R.id.tv_current_progress)
	private TextView tv_current_progress; // ��ǰ�������ĵ�ʱ��

	@ViewInject(value = R.id.tv_final_progress)
	private TextView tv_finalProgress; // ����ʱ��

	@ViewInject(value = R.id.rl_disc)
	private RelativeLayout rl_disc;

	@ViewInject(value = R.id.ll_lrc)
	private LinearLayout ll_lrc;
	
	private int currentTime; // ��ǰ��������ʱ��

	private MyBroadcastReceiver playerBroadcastReceiver;


	@ViewInject(value = R.id.lyricView)
	private LyricView lyricView;

	private Mp3Util_New mp3Util;
	private MusicBaseInfo currentMp3Info;
	@ViewInject(value = R.id.ll_bg)
	private LinearLayout ll_bg;

	private LyricDownloadManager manager;
	private LyricLoadHelper loadHelper;
	
	private ObjectAnimator animatorPlay;
	private RotateAnimation animatorNeedlePlay;
	private RotateAnimation animatorNeedlePause;

//	private Map<String, List<LyricSentence>> lycsList=new HashMap<String,List<LyricSentence>>();
	private AsyncTaskUtil asyncTaskUtil;
	private LyricModel lyricModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp3Util = Mp3Util_New.getDefault();
		
		lyricModel=new LyricModel();
		manager = new LyricDownloadManager(this);
		loadHelper = new LyricLoadHelper();
		loadHelper.setLyricListener(new MyLyricListener());
		asyncTaskUtil = new AsyncTaskUtil();
		
		initViewData();
		setViewOnclickListener();
		setPlayType();
		registerReceiver();
		findMp3Lrc(true);
	}

	private class MyLyricListener implements LyricListener {

		@Override
		public void onLyricLoaded(List<LyricSentence> lyricSentences,
				int indexOfCurSentence) {
			Log.i(TAG, "���سɹ�");
			lyricModel.putLyric(mp3Util.getCurrentMp3Info().getTitle(), lyricSentences);
			lyricView.setLyricSentences(lyricSentences,true);
			lyricView.setLoadLrc(LyricView.LRC_LOADED);
		}

		@Override
		public void onLyricSentenceChanged(int indexOfCurSentence) {

		}

	}

	/**
	 * ���ظ��
	 */
	private void findMp3Lrc(boolean isInit) {
		String title=mp3Util.getCurrentMp3Info().getTitle();
		if(lyricModel.isCache(title)){
			lyricView.setLyricSentences(lyricModel.getLyricSentences(title),isInit);
			lyricView.invalidate();
			return;
		}
		asyncTaskUtil.setIAsyncTaskCallBack(new IAsyncTaskCallBack() {
			String lrcPath = null;
			@Override
			public void onPostExecute(Object result) {
				// û�м��ص����
				if (lrcPath == null) {
					lyricView.setLyricSentences(null,false);
					lyricView.setLoadLrc(LyricView.NO_LRC);
					Log.d(TAG, "�������ʧ��..");
				}
				lyricView.invalidate();
			}
			@Override
			public Object doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				currentMp3Info = mp3Util.getCurrentMp3Info();
				String songName = currentMp3Info.getTitle();
				String songer = currentMp3Info.getArtist();
				// �鿴����
				lrcPath = lyricModel.findLocalLrc(songer, songName);
				if (lrcPath == null) {
					// ����û�У��������
					lrcPath = manager.searchLyricFromWeb(songName, songer);
					Log.d(TAG, "������ظ��..");
				}
				if (lrcPath != null) {
					// �������
					loadHelper.loadLyric(lrcPath);
				}

				return null;
			}
		});
		asyncTaskUtil.execute("");

	}


	private void initAnimation() {
		if (animatorPlay == null) {
			animatorPlay = ObjectAnimator.ofFloat(iv_music_album, "rotation", 0,
					360);
			animatorPlay.setDuration(20 * 1000);
			animatorPlay.setRepeatCount(-1);

			if(mp3Util.isPlaying()){
				animatorPlay.start();
			}
		}
		
		if(animatorNeedlePlay==null){
			animatorNeedlePlay=new RotateAnimation(0, -20, 0, 0);
			animatorNeedlePlay.setDuration(3*1000);
			animatorNeedlePlay.setFillAfter(true);
			if(!mp3Util.isPlaying()){
				iv_needle.startAnimation(animatorNeedlePlay);
			}
		}
		
		if(animatorNeedlePause==null){
			animatorNeedlePause=new RotateAnimation(-20, 0, 0, 0);
			animatorNeedlePause.setDuration(3*1000);
			animatorNeedlePause.setFillAfter(true);
		}
		
		
	}

	private void setViewOnclickListener() {
		music_progressBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListenerImpl());
		lyricView.setLyricViewClickListener(this);

	}

	@OnClick({ R.id.play_music, R.id.next_music, R.id.previous_music,
			R.id.repeat_music, R.id.ib_back, R.id.ib_changebg,
			R.id.img_favourite, R.id.img_share, R.id.rl_disc, R.id.ll_lrc,
			R.id.ib_play_list })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.play_music: // �������
			mp3Util.playMusic();
			break;
		case R.id.next_music: // �����һ��
			next_music();
			break;
		case R.id.previous_music: // �����һ��
			previous_music();
			break;
		case R.id.repeat_music: // �������˳��
			changePlayType();
			break;
		case R.id.ib_back:
			finishAnimator();
			break;
		case R.id.ib_changebg:
			startActivity(new Intent(this, ChangeBgActivity.class));
			break;
		case R.id.img_favourite:
			break;
		case R.id.img_share:
			umengShareMusic();
			break;
		case R.id.rl_disc:
			mp3Util.setShowLrc(true);
			rl_disc.setVisibility(View.GONE);
			ll_lrc.setVisibility(View.VISIBLE);
			loadLrc(false);
			break;
		case R.id.ll_lrc:
			mp3Util.setShowLrc(false);
			rl_disc.setVisibility(View.VISIBLE);
			ll_lrc.setVisibility(View.GONE);
			break;
		case R.id.ib_play_list:
			finishAnimator();
			break;
		default:
			break;
		}

	}
	/**
	 * umeng share api
	 */
	private void umengShareMusic() {
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"1104335219", "J68iUn08AUZwHWrJ");
		qZoneSsoHandler.addToSocialSDK();
		UMQQSsoHandler qHandler = new UMQQSsoHandler(this, "1104335219",
				"J68iUn08AUZwHWrJ");
		qHandler.addToSocialSDK();

		UMSocialService service = UMServiceFactory
				.getUMSocialService("com.umeng.share");

		service.setShareContent("hehehe");
		service.openShare(this, false);
	}

	/**
	 * ���ݲ������� ���ñ���ͼ
	 * 
	 */
	private void setPlayType() {
		int drawableId = 0;
		String typeString = " ";
		switch (mp3Util.getPlayType()) {
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			drawableId = R.drawable.play_icn_loop;
			typeString = "˳�򲥷�";
			break;
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
			drawableId = R.drawable.play_icn_one;
			typeString = "����ѭ��";
			break;
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			drawableId = R.drawable.play_icn_shuffle;
			typeString = "�������";
			break;
		default:
			drawableId = R.drawable.playing_queue;
			break;
		}
		repeatBtn.setBackgroundResource(drawableId);

		DialogUtil.showToast(this, typeString);
	}

	/**
	 * �ı䲥������
	 */
	private void changePlayType() {
		mp3Util.changePlayType();
		setPlayType();

	}

	/**
	 * ��һ��
	 */
	public void previous_music() {
		mp3Util.previous_music();
		// loadLrc();
	}

	/**
	 * ��һ��
	 */
	public void next_music() {
		mp3Util.nextMusic(false);

	}

	/**
	 * ���ظ��
	 */
	private void loadLrc(boolean isInit) {

		if (mp3Util.isShowLrc()) {
			lyricView.setLoadLrc(LyricView.LRC_LOADIGN);
			findMp3Lrc(isInit);
		}

	}

	/**
	 * ��ʼ���ؼ�����
	 */
	public void initViewData() {
		initAnimation();
		currentMp3Info = mp3Util.getCurrentMp3Info();
		musicTitle.setText(currentMp3Info.getTitle());
		musicArtist.setText(currentMp3Info.getArtist());

		music_progressBar.setMax((int) currentMp3Info.getDuration());
		music_progressBar.setProgress(mp3Util.getCurrentTime());

		tv_current_progress.setText(MediaUtil.formatTime(mp3Util
				.getCurrentTime()));
		tv_finalProgress.setText(MediaUtil.formatTime(mp3Util
				.getCurrentMp3Info().getDuration()));

		if (mp3Util.isPlaying()) {
			playBtn.setBackgroundResource(R.drawable.btn_pause);
			animatorPlay.start();
		} else {
			playBtn.setBackgroundResource(R.drawable.btn_play);
			iv_music_album.clearAnimation();
		}

		Bitmap bmp = MediaUtil
				.getArtwork(getApplicationContext(), currentMp3Info.getSongId(),
						currentMp3Info.getAlbumId(), true, true);
	
		
		Bitmap bmpBg= MediaUtil
				.getArtworkOriginal(getApplicationContext(), currentMp3Info.getSongId(),
						currentMp3Info.getAlbumId(), true, false);
		
		iv_music_album.setImageBitmap(bmp);
//		ll_bg.setBackground(ImageUtil.bitmapToDrawable(ImageUtil.blurBitmap(bmp, this)));
		ll_bg.setBackground(ImageUtil.bitmapToDrawable(ImageUtil.blurBitmap(bmpBg, this)));
		

	}

	/**
	 * �������ı��¼�������
	 * 
	 * @author luyuanwei
	 * 
	 */
	private class OnSeekBarChangeListenerImpl implements
			OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			switch (seekBar.getId()) {
			case R.id.sb_progress:
				if (fromUser) {
					sb_progressChange(progress); // �û����ƽ��ȵĸı�
				}
				break;
			}
		}
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	}

	/**
	 * �������仯ʱ���ø÷������ͷ���
	 * 
	 * @param progress
	 */
	public void sb_progressChange(int progress) {
//		mp3Util.sb_progressChange(progress);
		mp3Util.audioTrackChange(progress);
	}

	// ע��㲥������
	private void registerReceiver() {
		State state = new MyState();

		IntentFilter filter = new IntentFilter();
		playerBroadcastReceiver = new MyBroadcastReceiver(state, filter);
		registerReceiver(playerBroadcastReceiver, filter);

		IntentFilter filterlrc = new IntentFilter();
		filterlrc.addAction(ConstantUtil.LRC_CURRENT);
		registerReceiver(LrcBroadcastReceiver, filterlrc);
	}

	/**
	 * ���ݲ�������״̬�ı�����״̬
	 * 
	 * @author Administrator
	 * 
	 */
	class MyState implements State {
		@Override
		public void playMusicState() {
			playBtn.setBackgroundResource(R.drawable.btn_pause);

			if (animatorPlay.isPaused()) {
				animatorPlay.resume();
			} else {
				animatorPlay.start();
			}
			iv_needle.startAnimation(animatorNeedlePause);
		}

		@Override
		public void currentState(Intent intent) {
			currentTime = intent.getIntExtra("currentTime", -1);
//			DeBug.d(PlayerActivity.this, "currentState currentTime:"+currentTime);
			mp3Util.setCurrentTime(currentTime);

			tv_current_progress.setText(MediaUtil.formatTime(currentTime));
			music_progressBar.setProgress(currentTime);				 
		}

		@Override
		public void duration(Intent intent) {
			
			int duration = intent.getIntExtra("duration", -1);
			Log.i(TAG, "duration:"+duration); 
			music_progressBar.setMax(duration);
			tv_finalProgress.setText(MediaUtil.formatTime(duration));
			musicTitle.setText(mp3Util.getCurrentMp3Info().getTitle());
			musicArtist.setText(mp3Util.getCurrentMp3Info().getArtist());
			initViewData();
			lyricView.clear();
			loadLrc(true);
		}

		@Override
		public void pauseMusicState() {
			playBtn.setBackgroundResource(R.drawable.btn_play);
			animatorPlay.pause();
			
			iv_needle.startAnimation(animatorNeedlePlay);
		}

	}

	@SuppressLint("NewApi")
	private void unregisterReceiver() {
		if (playerBroadcastReceiver != null) {
			unregisterReceiver(playerBroadcastReceiver);
		}
		unregisterReceiver(LrcBroadcastReceiver);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}
	/**
	 * ��ʾ��ʵĿؼ����շ���������Ϣ,ÿ0.1sˢ��һ��
	 */
	private BroadcastReceiver LrcBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mp3Util.isShowLrc()) {
				int currentTime = intent.getIntExtra("currentTime", -1);
//				DeBug.d(PlayerActivity.this, "currentTime:"+currentTime);
				lyricView.updateindex(currentTime);
			}
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if(event.getAction()==KeyEvent.KEYCODE_BACK){
			finishAnimator();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * finish��Ķ���
	 */
	private void finishAnimator() {
		finish();
		ActivityAnimator animator = new ActivityAnimator();
		try {
			animator.getClass()
					.getMethod(animator.randomAnimator(), Activity.class)
					.invoke(animator, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void lyricViewClick() {
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

}
