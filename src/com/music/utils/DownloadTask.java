package com.music.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import android.util.Log;

public class DownloadTask extends Thread {
	@SuppressWarnings("unused")
	private int blockSize, downloadSizeMore, fileSize, downloadedSize;
	@SuppressWarnings("unused")
	private int threadNum = 5;
	String urlStr, threadNo, fileName;
	@SuppressWarnings("unused")
	private Handler handler;
	public DownloadTask(String urlStr, int threadNum, String fileName) {
		this.urlStr = urlStr;
		this.threadNum = threadNum;
		this.fileName = fileName;
	}
	public DownloadTask(String urlStr, String fileName,Handler handler) {
		this.urlStr = urlStr;
		this.fileName = fileName;
		this.handler=handler;
	}

	@Override
	public void run() {/*
		FileDownloadThread[] fds = new FileDownloadThread[threadNum];
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			// ��ֹ����-1
			InputStream in = conn.getInputStream();
			// ��ȡ�����ļ����ܴ�С
			fileSize = conn.getContentLength();
			Log.i("bb", "======================fileSize:" + fileSize);
			// ����ÿ���߳�Ҫ���ص�������
			blockSize = fileSize / threadNum;
			// ���������ٷֱȼ������
			downloadSizeMore = (fileSize % threadNum);
			File file = new File("/sdcard/lu/music/",fileName);
			for (int i = 0; i < threadNum; i++) {
				Log.i("bb", "======================i:" + i);
				// �����̣߳��ֱ������Լ���Ҫ���صĲ���
				FileDownloadThread fdt = new FileDownloadThread(url, file, i
						* blockSize, (i + 1) * blockSize - 1);
				fdt.setName("Thread" + i);
				fdt.start();
				fds[i] = fdt;
			}
			boolean finished = false;
			while (!finished) {
				// �Ȱ������������㶨
				downloadedSize = downloadSizeMore;
				finished = true;
				for (int i = 0; i < fds.length; i++) {
					downloadedSize += fds[i].getDownloadSize();
					if (!fds[i].isFinished()) {
						finished = false;
					}
				}
				if(finished){
					 handler.sendEmptyMessage(0);
				}
				
				// �߳���ͣһ��
//				sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	*/}

	class FileDownloadThread extends Thread {
		private static final int BUFFER_SIZE = 1024;
		private URL url;
		private File file;
		private int startPosition;
		private int endPosition;
		private int curPosition;
		// ��ʶ��ǰ�߳��Ƿ��������
		private boolean finished = false;
		private int downloadSize = 0;

		public FileDownloadThread(URL url, File file, int startPosition,
				int endPosition) {
			this.url = url;
			this.file = file;
			this.startPosition = startPosition;
			this.curPosition = startPosition;
			this.endPosition = endPosition;
		}

		@Override
		public void run() {
			BufferedInputStream bis = null;
			RandomAccessFile fos = null;
			byte[] buf = new byte[BUFFER_SIZE];
			URLConnection con = null;
			try {
				con = url.openConnection();
				con.setAllowUserInteraction(true);
				// ���õ�ǰ�߳����ص���ֹ��
				con.setRequestProperty("Range", "bytes=" + startPosition + "-"
						+ endPosition);
				Log.i("bb", Thread.currentThread().getName() + "  bytes="
						+ startPosition + "-" + endPosition);
				// ʹ��java�е�RandomAccessFile ���ļ����������д����
				fos = new RandomAccessFile(file, "rw");
				// ����д�ļ�����ʼλ��
				fos.seek(startPosition);
				bis = new BufferedInputStream(con.getInputStream());
				// ��ʼѭ����������ʽ��д�ļ�
				while (curPosition < endPosition) {
					int len = bis.read(buf, 0, BUFFER_SIZE);
					if (len == -1) {
						break;
					}
					fos.write(buf, 0, len);
					curPosition = curPosition + len;
					if (curPosition > endPosition) {
						downloadSize += len - (curPosition - endPosition) + 1;
					} else {
						downloadSize += len;
					}
				}
				// ���������Ϊtrue
				this.finished = true;
				bis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public boolean isFinished() {
			return finished;
		}

		public int getDownloadSize() {
			return downloadSize;
		}
	}
}
