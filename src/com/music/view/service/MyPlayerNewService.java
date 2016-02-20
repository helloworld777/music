package com.music.view.service;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.music.service.IMediaService;
import com.music.utils.ConstantUtil;
import com.music.utils.DeBug;
import com.music.utils.Mp3Util_New;

/**
 * �������ַ���
 * 
 * @author luyuanwei
 * 
 */
@SuppressLint("HandlerLeak") 
public class MyPlayerNewService extends Service {
	
	private MediaPlayer mediaPlayer; // ý�岥��������
	/**
	 * �ļ�����·��
	 */
	private String playPath; // �����ļ�·��
	@SuppressWarnings("unused")
	private int msg; // ������Ϣ
	/**
	 * ��ǰ���Ž���
	 */
	private int currentTime; // ��ǰ���Ž���
	private int duration; // ���ų���

	private static String TAG="MyPlayerService";
	
	private Mp3Util_New mp3Util;
	private Handler handler = new Handler() {

		@SuppressLint("HandlerLeak") @Override
		public void handleMessage(Message msg) {
			/**
			 * ��������
			 */
			if (msg.what == 1) {
				if (mediaPlayer != null) {
					// �õ���ǰ���ŵĽ���,���͹㲥��ȥ
					if(mediaPlayer.isPlaying()){
						currentTime = mediaPlayer.getCurrentPosition();
						Intent intent = new Intent();
						intent.setAction(ConstantUtil.MUSIC_CURRENT);
						intent.putExtra("currentTime", currentTime);
						sendBroadcast(intent);
						/**
						 * ÿ��һ���Ӱѵ�ǰ���ŵ����ֲ���ʱ��͵�ǰ����
						 */
						handler.sendEmptyMessageDelayed(1, 1000);
					}
					
				}
			}
			/**
			 * ��ʽ���
			 */
			if (msg.what == 2) {
				if (mediaPlayer != null) {
					// �õ���ǰ���ŵĽ���,���͹㲥��ȥ
					if(mediaPlayer.isPlaying()){
						currentTime = mediaPlayer.getCurrentPosition();
						Intent intent = new Intent();
						intent.setAction(ConstantUtil.LRC_CURRENT);
						intent.putExtra("currentTime", currentTime);
						sendBroadcast(intent);
						/**
						 * ÿ�������Ӱѵ�ǰ���ŵ����ֲ���ʱ��͵�ǰ����
						 */
						handler.sendEmptyMessageDelayed(2, 100);
					}
					
				}
			}
			
		}

	};
	@Override
	public void onCreate() {
		super.onCreate();
		DeBug.d(this, "MyPlayerService..................onCreate");
		mediaPlayer = new MediaPlayer();
		mp3Util=Mp3Util_New.getDefault();
		/**
		 * �������ֲ������ʱ�ļ�����
		 */
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mp3Util.nextMusic(true);
				
			}
		});
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		DeBug.d(this, "MyPlayerService..................onStartCommand");
		/**�Ƿ����Զ�������������Ŀؼ���������Ϣ*/
		if(intent==null){
			return 0;
		}
//		msg = intent.getIntExtra("MSG", 0);
//		Log.i(TAG, "*********************msg=" + msg);
//		
//			// ��ȡ�ļ�·��
//			playPath = intent.getStringExtra("url");
//			Log.i(TAG, "*********************path=" + playPath);
//			switch (msg) {
//			case AppConstant.PlayerMsg.PLAY_MSG:
////				play(0);
//				musicPlay(0);
//				break;
//			case AppConstant.PlayerMsg.PAUSE_MSG:
//				musicPause();
//				break;
//			case AppConstant.PlayerMsg.PROGRESS_CHANGE:
//				currentTime = intent.getIntExtra("progress", -1);
//				musicPlay(currentTime);
//				break;
//			case AppConstant.PlayerMsg.PLAYING_MSG:
//				cotinuePlay();
//				break;
//			default:
//				break;
//			}
//		}
		
		return 1;
	}

	private void cotinuePlay() {
		
//		if(!mp3Util.isPlaying()){
		
		
			
			mediaPlayer.start();
			mp3Util.setPlaying(true);
			//���Ͳ��Ź㲥
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(2);
//		}
		
	}
	
	private IBinder mIBinder=new MediaServiceSub();
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		DeBug.d(this, "..................onBind");
		return mIBinder;
	}
	class MediaServiceSub extends IMediaService.Stub{

		@Override
		public int duration() throws RemoteException {
			return duration;
		}
		@Override
		public int position() throws RemoteException {
			return 0;
		}

		@Override
		public int getPlayState() throws RemoteException {
			return 0;
		}

		@Override
		public int getPlayMode() throws RemoteException {
			return 0;
		}

		@Override
		public void setPlayMode(int mode) throws RemoteException {
			
		}

		@Override
		public void sendPlayStateBrocast() throws RemoteException {
			
		}

		@Override
		public void exit() throws RemoteException {
//			System.exit(0);
			release();
//			stopSelf();
			
//			System.exit(0);
//			Intent service = new Intent(Mp3Util_New.playService);
//			MyPlayerNewService.this.onUnbind(service);
			
		}

		@Override
		public int getCurMusicId() throws RemoteException {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void play(String playPath) throws RemoteException {
			// TODO Auto-generated method stub
			MyPlayerNewService.this.playPath=playPath;
			musicPlay(0);
		}
		@Override
		public void pause() throws RemoteException {
			// TODO Auto-generated method stub
			musicPause();
		}
		@Override
		public void seekTo(int progress) throws RemoteException {
			// TODO Auto-generated method stub
			musicPlay(progress);
		}
		@Override
		public void cotinuePlay() throws RemoteException {
			// TODO Auto-generated method stub
			MyPlayerNewService.this.cotinuePlay();
		}
	}
	public void musicPause() {
		if (null != mediaPlayer && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			mp3Util.setPlaying(false);
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PAUSE));
		}

	}

	/**
	 * �ӵ�ǰʱ��currentTime����ʼ����
	 * 
	 * @param currentTime
	 */
	private void musicPlay(int postion) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(playPath);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mp3Util.setPlaying(true);
			
			Log.d(TAG, "postion:"+postion);
			if (postion > 0) {
				mediaPlayer.seekTo(postion);
				handler.sendEmptyMessage(1);
				handler.sendEmptyMessage(2);
				return;
			}
			Intent intent = new Intent();
			intent.setAction(ConstantUtil.MUSIC_DURATION);// �����ֳ��ȸ��¶���
			duration = mediaPlayer.getDuration();
			
			
			mp3Util.setDuration(duration);
			mp3Util.getCurrentMp3Info().setDuration(duration);
			Log.i(TAG, "duration:"+duration);
			intent.putExtra("duration", duration);
			sendBroadcast(intent);
			
			sendBroadcast(new Intent(ConstantUtil.MUSIC_PLAYER));
			
			handler.sendEmptyMessage(1);
			handler.sendEmptyMessage(2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
	class MyTask extends TimerTask{

		@Override
		public void run() {
			if (mediaPlayer != null) {
				// �õ���ǰ���ŵĽ���,���͹㲥��ȥ
				if(mp3Util.isPlaying()){
					currentTime = mediaPlayer.getCurrentPosition();
					Log.d(TAG, "MyTask currentTime:"+currentTime);
					/**
					 * ��ʸ��¹㲥
					 */
					if(mp3Util.isShowLrc()){
						Intent intentLrc = new Intent();
						intentLrc.setAction(ConstantUtil.LRC_CURRENT);
						intentLrc.putExtra("currentTime", currentTime);
						sendBroadcast(intentLrc);
					}
					
					/**
					 * �������ȹ㲥
					 */
					Intent intent = new Intent();
					intent.setAction(ConstantUtil.MUSIC_CURRENT);
					intent.putExtra("currentTime", currentTime);
					mp3Util.setCurrentTime(currentTime);
					sendBroadcast(intent);
				}
				
			}
		}
		
	}
	@Override
	public boolean onUnbind(Intent intent) {
		DeBug.d(this, "...........................onUnbind");
		
		release();
		return super.onUnbind(intent);
	}
	private void release(){
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		handler.removeMessages(1);
		handler.removeMessages(2);
		mp3Util.setCurrentTime(0);
	}
	@Override
	public void onDestroy() {
		DeBug.d(this, "...........................onDestroy");
		release();
//		timer.cancel();
		
		
	}
}
