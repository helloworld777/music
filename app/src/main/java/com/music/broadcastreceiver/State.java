package com.music.broadcastreceiver;

import android.content.Intent;

/**
 * �յ���̨����������Ϣ�����״̬
 * @author Administrator
 *
 */
public interface State {
	void playMusicState();
	void currentState(Intent intent);
	void duration(Intent intent);
	void pauseMusicState();
}
