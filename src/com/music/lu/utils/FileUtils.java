package com.music.lu.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

/**
 *
 */
public class FileUtils {
	private static String SDCardRoot=Environment.getExternalStorageDirectory()
	.getAbsolutePath() + File.separator;
	
	
	private static final String TAG="FileUtils";
	/**
	 * Ӧ�ó���·��
	 */
	
	private static String dataPath=SDCardRoot+"lu";
	/**
	 * ����·��
	 */
	private static String downPath=dataPath+File.separator+"download";
	/**
	 * ͼƬ·��
	 */
	private static String imgPath=dataPath+File.separator+"img";
	
	/**
	 * ͼƬ·��
	 */
	private static String lrcPath=dataPath+File.separator+"lrc";
	
	private static String crashPath=dataPath+File.separator+"crash";
	/**
	 * ���ص�ַ
	 * @return
	 */
	public static String downPath(){
		createDirFile(dataPath);
		createDirFile(downPath);
		return downPath;
	}
	/**
	 * �����쳣��ַ
	 * @return
	 */
	public static String crashPath(){
		createDirFile(dataPath);
		createDirFile(crashPath);
		return crashPath;
	}
	/**
	 * ���ص�ַ
	 * @return
	 */
	public static String lrcPath(){
		createDirFile(dataPath);
		createDirFile(lrcPath);
		return lrcPath;
	}
	/**
	 *  ͼƬ��ַ
	 * @return
	 */
	public static String imgPathPath(){
		createDirFile(dataPath);
		createDirFile(imgPath);
		return imgPath;
	}
	
	/**
	 * �ж�SD�Ƿ����
	 * 
	 * @return
	 */
	@SuppressLint("NewApi") 
	public static boolean isSdcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)||Environment.isExternalStorageRemovable()) {
			return true;
		}
		return false;
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param path
	 *            Ŀ¼·��
	 */
	public static void createDirFile(String path) {
		
		
		
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	@SuppressLint("NewApi") 
	public static File getDiskCacheDir(Context context,String uniqueName){
	
		String cachePath;
		if(isSdcardExist()){
			cachePath=context.getExternalCacheDir().getPath();
		}else{
			cachePath=context.getCacheDir().getPath();
		}
		MyLog.d(TAG, "cachePath:"+cachePath);
		
		return new File(cachePath+File.separator+uniqueName);
	}
	
	
	/**
	 * �����ļ�
	 * 
	 * @param path
	 *            �ļ�·��
	 * @return �������ļ�
	 */
	public static File createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
		}
		return file;
	}

	/**
	 * ɾ���ļ���
	 * 
	 * @param folderPath
	 *            �ļ��е�·��
	 */
	public static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete();
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param path
	 *            �ļ���·��
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
			}
		}
	}

	/**
	 * ��ȡ�ļ���Uri
	 * 
	 * @param path
	 *            �ļ���·��
	 * @return
	 */
	public static Uri getUriFromFile(String path) {
		File file = new File(path);
		return Uri.fromFile(file);
	}

	/**
	 * �����ļ���С
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "δ֪��С";
		if (size < 1024) {
			fileSizeString = df.format((double) size) + "B";
		} else if (size < 1048576) {
			fileSizeString = df.format((double) size / 1024) + "K";
		} else if (size < 1073741824) {
			fileSizeString = df.format((double) size / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) size / 1073741824) + "G";
		}
		return fileSizeString;
	}
	
}
