package com.music.utils;

/**
 *  ����������
 *
 */
public interface  ConstantUtil {
	/**
	 * ���¶���
	 */
	public static final String UPDATE_ACTION = "com.wwj.action.UPDATE_ACTION"; // ���¶���
	
	/**
	 * ��ǰ���ָı䶯��
	 */
	public static final String MUSIC_CURRENT = "com.wwj.action.MUSIC_CURRENT"; // ��ǰ���ָı䶯��
	/**
	 * ����ʱ���ı䶯��
	 */
	public static final String MUSIC_DURATION = "com.wwj.action.MUSIC_DURATION"; // ����ʱ���ı䶯��
	/**
	 * �����ظ��ı䶯��
	 */
	public static final String REPEAT_ACTION = "com.wwj.action.REPEAT_ACTION"; // �����ظ��ı䶯��
	
	
	
	
	/**
	 * 
	 * ������һ��
	 */
	public static final String MUSIC_NEXT_PLAYER = "com.action.MUSIC_NEXT";
	/**
	 * 
	 * ��һ��
	 */
	public static final String MUSIC_PRE = "com.wwj.action.MUSIC_PRE";
	/**
	 * 
	 * ����
	 */
	public static final String MUSIC_PLAYER = "com.wwj.action.MUSIC_PLAYER";
	
	public static final String SHUFFLE_ACTION = "com.wwj.action.SHUFFLE_ACTION"; // ����������Ŷ���
	/**
	 * ��ͣ
	 */
	public static final String MUSIC_PAUSE="com.wwj.action.MUSIC_PAUSE";	//������ͣ
	
	/**
	 * ��ʸ��ĵĶ���
	 */
	public static final String LRC_CURRENT="com.lu.lrc.current";
	public static final String CHANGED_BG = "com.lu.changedgb";
	
	
	/**
	 * 
	 */
	public static final String AUTOMATIC_DOWN_LRC="AUTOMATIC_DOWN_LRC";
	public static final String LISTENER_DOWN="LISTENER_DOWN";
	public static final String SCREEN_SHOT="SCREEN_SHOT";
//	/**
//	 * ��ȡ��Ļ��С
//	 * @param context
//	 * @return
//	 */
//	public static int[] getScreen(Context context) {
//		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		Display display = windowManager.getDefaultDisplay();
//		DisplayMetrics outMetrics = new DisplayMetrics();
//		display.getMetrics(outMetrics);
//		return new int[] {(int) (outMetrics.density * outMetrics.widthPixels),
//				(int)(outMetrics.density * outMetrics.heightPixels)
//		};
//	}
}
