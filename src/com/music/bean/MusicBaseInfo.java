package com.music.bean;

public class  MusicBaseInfo {
	

	public long getSongId() {
		return songId;
	}
	public void setSongId(long songId) {
		this.songId = songId;
	}
	public long getAlbumId() {
		return albumId;
	}
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	public int isFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	public final static String KEY_MUSIC= "music";
	
	public static final String KEY_ID = "_id";
	public static final String KEY_SONG_ID = "songid";
	public static final String KEY_ALBUM_ID = "albumid";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_MUSIC_NAME = "musicname";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DATA = "data";
	public static final String KEY_FOLDER = "folder";
	public static final String KEY_MUSIC_NAME_KEY = "musicnamekey";
	public static final String KEY_ARTIST_KEY = "artistkey";
	public static final String KEY_FAVORITE = "favorite";
	/** ���ݿ��е�_id */
	public int _id = -1;	//
	public long songId;	//����ID
	public long albumId;//ר��ID
	public long duration;	//ʱ��
	public String title;	//���ֱ���
	public String artist;	//����
	public String folder;	//�ļ���
	public String playPath;//����·��
	public String musicName;
	public String musicNameKey;
	public String data;
	public String artistKey;
	/** 0��ʾû���ղ� 1��ʾ�ղ� */
	public int favorite; //
	
	

}
