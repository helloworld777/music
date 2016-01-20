package com.music.utils;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.music.bean.Mp3Info;
import com.music.service.MyPlayerService;

public class Mp3Util {
	private static final String TAG = "Mp3Util";

	private static Mp3Util mp3Util = null;

	private MediaUtil mediaUtil;
	
	private boolean isSortByTime=false;
	/**
	 * ���еĸ���
	 */
	private List<Mp3Info> mp3Infos = null;

	public List<Mp3Info> getMp3Infos() {
		return mp3Infos;
	}
	@SuppressWarnings("unchecked")
	public void addMp3(Mp3Info mp3Info){
		
		if(!mp3Infos.contains(mp3Info)){
			mp3Infos.add(mp3Info);
			Collections.sort(mp3Infos);
		}
		
	}
	private int index;
//	public static Mp3Util getInstance(Context context) {
//		if (mp3Util == null) {
//			mp3Util = new Mp3Util(context);
//		}
//		return mp3Util;
//	}
	public static Mp3Util getInstance() {
		return mp3Util;
	}
	public static Mp3Util getDefault() {
		return mp3Util;
	}
	public static void init(Context context){
		mp3Util = new Mp3Util(context);
	}
	
	private Context context;
	/**
	 * ��ǰ���ڲ��ŵĸ�����
	 */
	private Mp3Info currentMp3Info;
	/**
	 * �����Ѳ��ŵ�ʱ��
	 */
	private int currentTime;
	/**
	 * ��ǰ����״̬
	 */
	private boolean isPlaying;

	/**
	 * ��������
	 */
	private int playType;

	/**
	 * ��ǰ���Ÿ��������и����е�����
	 */
	private int listPosition;
	/**
	 * �Ƿ���ʾ���
	 */
	private boolean isShowLrc;

	private Mp3Util(Context context) {
		this.context = context;
		init();
	}
	
	
	private void init() {
		mediaUtil = new MediaUtil();
		initCurrentMusicInfo(context);
		isPlaying=false;
		isSortByTime=false;
		isShowLrc=false;
		currentTime=0;
		mp3Infos=mediaUtil.sortMp3InfosByTitle(context);
		currentMp3Info = mp3Infos.get(listPosition);
		Log.d("Mp3Util",currentMp3Info.getDisplayName());
		Log.d("Mp3Util","listPosition:"+listPosition);
	}
	/**
	 * Ӧ�ó����˳�ʱ���浱ǰ���ŵ�������Ϣ
	 */
	public void saveCurrentMusicInfo(Context context){
		Log.i("Mp3Util", "����ֵ:listPosition="+listPosition);
		SharedPreHelper.setIntValue(context, "listPosition", listPosition);
		SharedPreHelper.setIntValue(context, "playType", playType);
	}
	/**
	 * Ӧ�ó����˳�ʱ���浱ǰ���ŵ�������Ϣ
	 */
	public void initCurrentMusicInfo(Context context){
		
		listPosition=SharedPreHelper.getIntValue(context, "listPosition", 0);
		playType=SharedPreHelper.getIntValue(context, "playType", 9);
		Log.i("Mp3Util", "��ȡֵ:listPosition="+listPosition);
//		playType=9;

	}
	public void sortMp3InfosByTitle(){
		mp3Infos.clear();
		mp3Infos.addAll(mediaUtil.sortMp3InfosByTitle(context));
		setSortByTime(false);
		listPosition=mp3Infos.indexOf(currentMp3Info);
	}
	public void sortMp3InfosByTime(){
		mp3Infos.clear();
		mp3Infos.addAll(mediaUtil.getMp3Infos(context));
		listPosition=mp3Infos.indexOf(currentMp3Info);
		setSortByTime(true);
	}
	
	public Mp3Info getCurrentMp3Info() {
		return currentMp3Info;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * ָ����������������λ�õ����ַ��Ͳ��ŷ���
	 * 
	 * @param listPosition
	 *            Ҫ�������ֵ�����λ��
	 */
	public void playMusic(int listPosition) {
		this.isPlaying = true;
		this.listPosition = listPosition;
		this.currentMp3Info = mp3Infos.get(listPosition);
		sendService(AppConstant.PlayerMsg.PLAY_MSG,0);
	};
	private void sendService(int msg,int progress){
		
		Log.i("Mp3Util", "listPosition="+listPosition);
		Log.i("Mp3Util", "currentMp3Info="+currentMp3Info.getDisplayName());
		Intent intent = new Intent(context, MyPlayerService.class);
		intent.putExtra("MSG", msg);
		intent.putExtra("url", currentMp3Info.getUrl());
		intent.putExtra("progress", progress);
		context.startService(intent);
	}
	/**
	 * ��һ��
	 */
	public void previous_music() {
		switch (playType) {
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			listPosition = (listPosition > 0 ? listPosition - 1 : mp3Infos
					.size() - 1);
			break;
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			listPosition = randomNum();
			break;
		}
		currentMp3Info = mp3Infos.get(listPosition);
		sendService(AppConstant.PlayerMsg.PLAY_MSG,0);
	}

	/**
	 * �������仯ʱ���ø÷������ͷ���
	 * 
	 * @param progress
	 */
	public void audioTrackChange(int progress) {
		sendService(AppConstant.PlayerMsg.PROGRESS_CHANGE,progress);
	}

	/**
	 * ���Ͳ��Ż���ͣ���ַ��� ���ǵ�ǰ�ǲ���״̬,����ͣ.��֮��Ȼ
	 * 
	 * 
	 */
	public void playMusic() {
		int MSG = 0;
		if (isPlaying()) { // ������ڲ��ţ�������ͣ��Ϣ
			MSG = AppConstant.PlayerMsg.PAUSE_MSG;
			isPlaying = false;
		} else {
			
			if(currentTime>0){
				MSG = AppConstant.PlayerMsg.PLAYING_MSG;
			}else{
				MSG = AppConstant.PlayerMsg.PLAY_MSG;
			}
			
			isPlaying = true;
		}
		Log.d(TAG, "listpostion:"+listPosition);
		Log.d(TAG, "currentTime:"+currentTime);
		sendService(MSG,0);
	}
	/**
	 * ���Ͳ��Ż���ͣ���ַ��� ���ǵ�ǰ�ǲ���״̬,����ͣ.��֮��Ȼ
	 * 
	 * 
	 */
	public void playMusic(Mp3Info mp3Info) {
		currentMp3Info=mp3Info;
		sendService(AppConstant.PlayerMsg.PLAY_MSG,0);
	}

	/**
	 * ���ݲ������ͷ�����һ�׸������ŷ���
	 */
	public void nextMusic() {
		Log.i(TAG, "nextMusic()");
		switch (playType) {
		case AppConstant.PlayerMsg.PLAYING_SHUFFLE:
			listPosition = randomNum();
			break;
		case AppConstant.PlayerMsg.PLAYING_REPEAT:
		case AppConstant.PlayerMsg.PLAYING_QUEUE:
			listPosition = (listPosition + 1 >= mp3Infos.size() ? 0
					: listPosition + 1);
			break;

		}
		playMusic(listPosition);
	}
	/**
	 * ���һ��
	 */
	public void randomPlay(){
		listPosition = randomNum();
		playMusic(listPosition);
	}
	/**
	 * �ı䲥������
	 */
	public void changePlayType() {
		if ((playType + 1) > AppConstant.PlayerMsg.PLAYING_REPEAT) {
			playType = AppConstant.PlayerMsg.PLAYING_QUEUE;
		} else {
			playType++;
		}
	}

	/**
	 * �����и��������������һ���������
	 * 
	 * @return
	 */
	private int randomNum() {
		return (int) (mp3Infos.size() * Math.random());
	}

	public void setCurrentMp3Info(Mp3Info currentMp3Info) {
		this.currentMp3Info = currentMp3Info;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public int getPlayType() {
		return playType;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

	public int getListPosition() {
		return listPosition;
	}

	public void setListPosition(int listPosition) {
		this.listPosition = listPosition;
	}
	public int getAllMp3Size(){
		return this.mp3Infos.size();
	}

	public boolean isSortByTime() {
		return isSortByTime;
	}

	public void setSortByTime(boolean isSortByTime) {
		this.isSortByTime = isSortByTime;
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}
	public boolean isShowLrc() {
		return isShowLrc;
	}
	public void setShowLrc(boolean isShowLrc) {
		this.isShowLrc = isShowLrc;
	}
}
