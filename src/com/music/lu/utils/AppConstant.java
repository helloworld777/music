package com.music.lu.utils;

/**
 * Ӧ�ó�����
 * @author wwj
 *
 */
public class AppConstant {
	/**
	 * ƥ����
	 */
	public final static int MATCH_LRC_COMPLETED=0;
	
	
//	public enum PlayType{
//		PLAYING_QUEUE,PLAYING_SHUFFLE,PLAYING_REPEAT
//	}
	public class PlayerMsg {
		/**
		 * ��ʼ����
		 */
		public static final int PLAY_MSG = 1;		//
		/**
		 * ��ͣ
		 */
		public static final int PAUSE_MSG = 2;		//��ͣ
		/**
		 * ֹͣ
		 */
		public static final int STOP_MSG = 3;		//ֹͣ
		/**
		 * ����
		 */
		public static final int CONTINUE_MSG = 4;	//����
		/**
		 * ��һ��
		 */
		public static final int PRIVIOUS_MSG = 5;	//��һ��
		/**
		 * ��һ��
		 */
		public static final int NEXT_MSG = 6;		//��һ��
		/**
		 * ���ȸı�
		 */
		public static final int PROGRESS_CHANGE = 7;//���ȸı�
		/**
		 * ��������
		 */
		public static final int PLAYING_MSG = 8;	//��������
		
		/**˳�򲥷�*/
		public static final int PLAYING_QUEUE=9;		//˳�򲥷�
		/**�������*/
		public static final int PLAYING_SHUFFLE=10;		//
		/**��������*/
		public static final int PLAYING_REPEAT=11;		//
		
	}
	/**
	 * //֪ͨ����������Ϣ
	 */
	public static final String NOTIFICATION_PLAY_PAUSE="com.action.lu.play_pause";
	/**
	 * //֪ͨ�������Ĳ�����һ����Ϣ
	 */
	public static final String NOTIFICATION_NEXT="com.action.lu.next";
	/**
	 * 
	 * ��һ��
	 */
	public static final String MUSIC_NEXT = "com.wwj.action.MUSIC_NEXT";
}
