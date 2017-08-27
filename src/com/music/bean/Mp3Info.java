package com.music.bean;

import com.music.utils.MediaUtil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

@SuppressLint("DefaultLocale")
@SuppressWarnings("rawtypes")
public class Mp3Info extends MusicBaseInfo implements Comparable {
	private long id; //
	private String album; //
	private String displayName; //
	private long size; //
	private String url; //

	private String songName;
	private String songer;
	/**
	 */
	private String downUrl;
	private String lrcTitle; //
	private String lrcSize; //
	private String titlepinyin;//
	@SuppressWarnings("unused")
	private String fisrtPinYin;
	/**
	 */
	private byte[] bitMap;

	private Bitmap bitmap;

	public String decode, encode;
	public String picUrl;
	public Mp3Info() {
		super();
	}

	public Mp3Info(int id, String title, String album, int albumId, String displayName, String artist, int duration, long size, String url, String lrcTitle, String lrcSize) {
		super();
		this.id = id;
		this.title = title;
		this.album = album;
		this.albumId = albumId;
		this.displayName = displayName;
		this.artist = artist;
		this.duration = duration;
		this.size = size;
		this.url = url;
		this.lrcTitle = lrcTitle;
		this.lrcSize = lrcSize;
	}

	@Override
	public String toString() {
		return "Mp3Info [id=" + id + ", title=" + title + ", album=" + album + ", albumId=" + albumId + ", displayName=" + displayName + ", artist=" + artist + ", duration="
				+ duration + ", size=" + size + ", url=" + url + ", lrcTitle=" + lrcTitle + ", lrcSize=" + lrcSize + "]";
	}

	@Override
	public boolean equals(Object o) {

		Mp3Info mp3Info = (Mp3Info) o;

		return this.title.equals(mp3Info.getTitle());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		setTitlepinyin(MediaUtil.toHanyuPinYin(title));
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLrcTitle() {
		return lrcTitle;
	}

	public void setLrcTitle(String lrcTitle) {
		this.lrcTitle = lrcTitle;
	}

	public String getLrcSize() {
		return lrcSize;
	}

	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setTitlepinyin(String titlepinyin) {

		this.titlepinyin = titlepinyin;
	}

	public String getTitlepingyin() {
		return this.titlepinyin;
	}

	@Override
	public int compareTo(Object another) {
		Mp3Info mp3Info = (Mp3Info) another;
		int i = 0;
		boolean flag = false;
		for (; i < this.titlepinyin.length(); i++) {
			if (i < mp3Info.titlepinyin.length()) {
				if (titlepinyin.charAt(i) != mp3Info.titlepinyin.charAt(i)) {
					break;
				} else {
					continue;
				}
			} else {
				flag = true;
				break;

			}

		}
		if (i == titlepinyin.length()) {
			return -1;
		}
		if (flag) {
			return 1;
		}
		return this.titlepinyin.charAt(i) - mp3Info.titlepinyin.charAt(i);
	}

	public String getFisrtPinYin() {
		return getTitlepingyin().substring(0, 1).toUpperCase();
	}

	public void setFisrtPinYin(String fisrtPinYin) {
		this.fisrtPinYin = fisrtPinYin;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getSonger() {
		return songer;
	}

	public void setSonger(String songer) {
		this.songer = songer;
	}

	public byte[] getBitMap() {
		return bitMap;
	}

	public void setBitMap(byte[] bitMap) {
		this.bitMap = bitMap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}