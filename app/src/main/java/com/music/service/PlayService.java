package com.music.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * �������ַ���
 * 
 * @author luyuanwei
 * 
 */
@SuppressLint("HandlerLeak")
public class PlayService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}/*

	private MediaPlayer mediaPlayer; // ý�岥��������
	private String path; // �����ļ�·��
	private int msg; // ������Ϣ
	*//**
	 * ��ǰ���Ž���
	 *//*
	private int currentTime; // ��ǰ���Ž���
	private int duration; // ���ų���

	private static String TAG = "PlayService";

	private Mp3Util mp3Util;
	private Handler handler = new Handler() {

		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			*//**
			 * ��������
			 *//*
			if (msg.what == 1) {
				if (mediaPlayer != null) {
					// �õ���ǰ���ŵĽ���,���͹㲥��ȥ
					if (mediaPlayer.isPlaying()) {
						currentTime = mediaPlayer.getCurrentPosition();
						Intent intent = new Intent();
						intent.setAction(ConstantUtil.MUSIC_CURRENT);
						intent.putExtra("currentTime", currentTime);
						sendBroadcast(intent);
						*//**
						 * ÿ��һ���Ӱѵ�ǰ���ŵ����ֲ���ʱ��͵�ǰ����
						 *//*
						handler.sendEmptyMessageDelayed(1, 1000);
					}

				}
			}
			*//**
			 * ��ʽ���
			 *//*
			if (msg.what == 2) {
				if (mediaPlayer != null) {
					// �õ���ǰ���ŵĽ���,���͹㲥��ȥ
					if (mediaPlayer.isPlaying()) {
						currentTime = mediaPlayer.getCurrentPosition();
						Intent intent = new Intent();
						intent.setAction(ConstantUtil.LRC_CURRENT);
						intent.putExtra("currentTime", currentTime);
						sendBroadcast(intent);
						*//**
						 * ÿ�������Ӱѵ�ǰ���ŵ����ֲ���ʱ��͵�ǰ����
						 *//*
						handler.sendEmptyMessageDelayed(2, 100);
					}

				}
			}

		}

	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "PlayerService ������");
		mediaPlayer = new MediaPlayer();
		mp3Util = Mp3Util.getDefault();
		EventBus.getDefault().register(this);
		*//**
		 * �������ֲ������ʱ�ļ�����
		 *//*
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				if (mp3Util.getPlayType() == AppConstant.PlayerMsg.PLAYING_REPEAT) { // ����ѭ��
					mediaPlayer.start();
					return;
				}
				Intent intent = new Intent(ConstantUtil.MUSIC_NEXT_PLAYER);
				sendBroadcast(intent);
			}
		});
		;
	}


	@Subscriber(tag = TagUtil.TAG_ACTION_RESUME)
	private void resume() {
		if (!mp3Util.isPlaying()) {
			mediaPlayer.start();
			mp3Util.setPlaying(true);
		}
		Intent intent = new Intent();
		intent.setAction(ConstantUtil.MUSIC_DURATION);
		duration = mediaPlayer.getDuration();
		intent.putExtra("duration", duration);
		sendBroadcast(intent);
		handler.sendEmptyMessage(1);
		handler.sendEmptyMessage(2);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Subscriber(tag = TagUtil.TAG_ACTION_PAUSE)
	public void pause() {
		if (null != mediaPlayer && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			mp3Util.setPlaying(false);
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PAUSE));
		}

	}

	*//**
	 * �ӵ�ǰʱ��currentTime����ʼ����
	 * 
	 * @param currentTime
	 *//*
	private void play(int currentTime) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mp3Util.setPlaying(true);
			if (currentTime > 0) {
				mediaPlayer.seekTo(currentTime);
				if (!mp3Util.isPlaying()) {
					sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
				}
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				return;
			}
			Intent intent = new Intent();
			intent.setAction(ConstantUtil.MUSIC_DURATION);// �����ֳ��ȸ��¶���

			duration = mediaPlayer.getDuration();
			intent.putExtra("duration", duration);
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
			sendBroadcast(intent);
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	};

	private void sendSomeBroadcast() {
		Intent intent = new Intent();
		intent.setAction(ConstantUtil.MUSIC_DURATION);// �����ֳ��ȸ��¶���
		duration = mediaPlayer.getDuration();
		intent.putExtra("duration", duration);
		sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
		sendBroadcast(intent);
		handler.sendEmptyMessage(1);
		handler.sendEmptyMessage(2);
	}

	*//**
	 * �ӵ�ǰʱ��currentTime����ʼ����
	 * 
	 * @param currentTime
	 *//*
	@Subscriber(tag = TagUtil.TAG_ACTION_PLAY_BY_INDEX)
	private void playByIndex(int index) {
		System.out.println("�����¼�....index:" + index);
		try {
			mp3Util.setIndex(index);
			path = mp3Util.getMp3Infos().get(index).getUrl();
			play(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	
	@Subscriber(tag = TagUtil.TAG_ACTION_PLAY_BY_PATH)
	private void playByPath(Mp3Info mp3Info) {
		try {
			mp3Util.setCurrentMp3Info(mp3Info);
			play(mp3Info.getUrl());
			sendSomeBroadcast();
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	@Subscriber(tag = TagUtil.TAG_ACTION_NEXT)
	private void next(){
		
	}
	@Subscriber(tag = TagUtil.TAG_ACTION_PRE)
	private void pre(){
		
	}
	private void play(String path) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.start();
			post(TagUtil.TAG_STATE_PLAY);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	*//**
	 * ������Ϣ
	 * 
	 * @param tag
	 *//*
	private void post(String tag) {
		EventBus.getDefault().post(mp3Util.getCurrentMp3Info(), tag);
	}

	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		EventBus.getDefault().unregister(this);
	}
*/}
