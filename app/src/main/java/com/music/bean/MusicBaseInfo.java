package com.music.bean;

public interface  MusicBaseInfo {
	/**
	 * ����ID
	 * @return
	 */
	public long getId();
	/**
	 * ��������
	 * @return
	 */
	public String getTitle();
	/**
	 * 
	 * @return ����ר��ID
	 */
	public long getAlbumId();
	/**
	 * 
	 * @return �����ĸ���
	 */
	public String getArtist();
	
	/**
	 * 
	 * @return ����ʱ��
	 */
	public long getDuration();
	
	/**
	 * 
	 * @return ����·��
	 */
	public String getUrl();

}
