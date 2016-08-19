package com.music.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.content.Context;
import android.text.TextUtils;

import com.music.utils.DeBug;
import com.music.utils.FileUtils;

/**
 * �������
 * 
 * @author longdw(longdawei1988@gmail.com)
 * 
 */
public class LyricDownloadManager {
	private static final String TAG = LyricDownloadManager.class
			.getSimpleName();
	public static final String GB2312 = "GB2312";
	public static final String UTF_8 = "utf-8";
	private final int mTimeOut = 10 * 1000;
	private LyricXMLParser mLyricXMLParser = new LyricXMLParser();
	private URL mUrl = null;
	private int mDownloadLyricId = -1;
	private final static String BAIDU_MUSIC_URL = "http://box.zhangmen.baidu.com/x?op=12&count=1&title=";
	private final static String BAIDU_LRC_URL = "http://box.zhangmen.baidu.com/bdlrc/";
	@SuppressWarnings("unused")
	private Context mContext = null;

	public LyricDownloadManager(Context c) {
		mContext = c;
	}

	/*
	 * ���ݸ������͸�����ȡ�øø��XML��Ϣ�ļ� ���ظ�ʱ���·��
	 */
	public String searchLyricFromWeb(String musicName, String singerName) {
		DeBug.d(TAG, "����ǰ��������:" + musicName + ",������:" + singerName);

		if (TextUtils.isEmpty(musicName) || TextUtils.isEmpty(singerName)) {
			return "";
		}
		// ������������Ǻ��֣���ô��Ҫ���б���ת��
		try {

			musicName = URLEncoder.encode(musicName, UTF_8);
			singerName = URLEncoder.encode(singerName, UTF_8);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}

		// �ٶ����ֺе�API
		String strUrl = BAIDU_MUSIC_URL + musicName + "$$" + singerName
				+ "$$$$";

		// ����URL
		try {
			mUrl = new URL(strUrl);
			DeBug.d(TAG, "�����ȡ�����Ϣ��URL��" + mUrl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			HttpURLConnection httpConn = (HttpURLConnection) mUrl
					.openConnection();
			httpConn.setReadTimeout(mTimeOut);
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				DeBug.d(TAG, "http����ʧ��");
				return null;
			}
			httpConn.connect();
			DeBug.d(TAG, "http���ӳɹ�");

			// ���ٶ����ֺеķ��ص����������ݸ��Զ����XML����������������ʵ�����ID
			mDownloadLyricId = mLyricXMLParser.parseLyricId(httpConn
					.getInputStream());
			httpConn.disconnect();
		} catch (IOException e1) {
			e1.printStackTrace();
			DeBug.d(TAG, "http��������IO�쳣");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			DeBug.d(TAG, "XML��������");
			return null;
		}
		return fetchLyricContent(musicName, singerName);
	}

	/**
	 * ���ݸ������ID����ȡ�����ϵĸ���ı�����
	 * 
	 * @param musicInfo
	 */
	private String fetchLyricContent(String musicName, String singerName) {
		if (mDownloadLyricId == -1) {
			DeBug.d(TAG, "δָ���������ID");
			return null;
		}
		BufferedReader br = null;
		StringBuilder content = null;
		String temp = null;
		String lyricURL = BAIDU_LRC_URL + mDownloadLyricId / 100 + "/"
				+ mDownloadLyricId + ".lrc";
		DeBug.d(TAG, "��ʵ���ʵ���ص�ַ:" + lyricURL);

		try {
			mUrl = new URL(lyricURL);
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		}

		// ��ȡ����ı��������ַ�������
		try {
			// ������������
			br = new BufferedReader(new InputStreamReader(mUrl.openStream(),
					GB2312));
			if (br != null) {
				content = new StringBuilder();
				// ���л�ȡ����ı�
				while ((temp = br.readLine()) != null) {
					content.append(temp);
					DeBug.d(TAG, "<Lyric>" + temp);
				}
				br.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			DeBug.d(TAG, "��ʻ�ȡʧ��");
		}

		try {
			musicName = URLDecoder.decode(musicName, UTF_8);
			singerName = URLDecoder.decode(singerName, UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (content != null) {
			// ��鱣���Ŀ¼�Ƿ��Ѿ�����

			// String folderPath =
			// PreferenceManager.getDefaultSharedPreferences(
			// mContext).getString(SettingFragment.KEY_LYRIC_SAVE_PATH,
			// Constant.LYRIC_SAVE_FOLDER_PATH);
			String folderPath = FileUtils.lrcPath();

			File savefolder = new File(folderPath);
			if (!savefolder.exists()) {
				savefolder.mkdirs();
			}
			String savePath = folderPath + File.separator + singerName + "-"
					+ musicName + ".lrc";
			// String savePath = folderPath + File.separator + musicName +
			// ".lrc";
			DeBug.d(TAG, "��ʱ���·��:" + savePath);

			saveLyric(content.toString(), savePath);

			return savePath;
		} else {
			return null;
		}

	}

	/** ����ʱ��浽���أ�д������� */
	private void saveLyric(String content, String filePath) {
		// ���浽����
		File file = new File(filePath);
		try {
			OutputStream outstream = new FileOutputStream(file);
			OutputStreamWriter out = new OutputStreamWriter(outstream);
			out.write(content);
			out.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
			DeBug.d(TAG, "���ź��������д�����ʱ������IO����");
		}
		DeBug.d(TAG, "��ʱ���ɹ�");
	}
}
