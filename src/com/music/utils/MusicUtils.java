package com.music.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Files.FileColumns;
import android.text.TextUtils;

import com.music.bean.AlbumInfo;
import com.music.bean.ArtistInfo;
import com.music.bean.FolderInfo;
import com.music.bean.MusicInfo;
import com.music.db.AlbumInfoDao;
import com.music.db.ArtistInfoDao;
import com.music.db.FavoriteInfoDao;
import com.music.db.FolderInfoDao;
import com.music.db.MusicInfoDao;
import com.music.service.IConstants;

/**
 * ��ѯ����ҳ��Ϣ����ȡ����ͼƬ��
 * 
 * @author longdw(longdawei1988@gmail.com)
 * 
 */
public class MusicUtils implements IConstants {

	public List<MusicInfo> getMusicInfos() {
		return musicInfos;
	}

	public void setMusicInfos(List<MusicInfo> musicInfos) {
		this.musicInfos = musicInfos;
	}

	private static String[] proj_music = new String[] {
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
			MediaStore.Audio.Media.DURATION };

	private static String[] proj_album = new String[] { Albums.ALBUM,
			Albums.NUMBER_OF_SONGS, Albums._ID, Albums.ALBUM_ART };

	private static String[] proj_artist = new String[] {
			MediaStore.Audio.Artists.ARTIST,
			MediaStore.Audio.Artists.NUMBER_OF_TRACKS };

	private static String[] proj_folder = new String[] { FileColumns.DATA };

	public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
	public static final int FILTER_DURATION = 1 * 60 * 1000;// 1����
	// private static final BitmapFactory.Options sBitmapOptionsCache = new
	// BitmapFactory.Options();
	// private static final BitmapFactory.Options sBitmapOptions = new
	// BitmapFactory.Options();
	// private static final HashMap<Long, Bitmap> sArtCache = new HashMap<Long,
	// Bitmap>();
	// @SuppressWarnings("unused")
	// private static final Uri sArtworkUri = Uri
	// .parse("content://media/external/audio/albumart");

	static {
		// sBitmapOptionsCache.inPreferredConfig = Bitmap.Config.RGB_565;
		// sBitmapOptionsCache.inDither = false;
		//
		// sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		// sBitmapOptions.inDither = false;
	}

	// ������Ϣ���ݿ�
	private MusicInfoDao mMusicInfoDao;
	// ר����Ϣ���ݿ�
	private AlbumInfoDao mAlbumInfoDao;
	// ������Ϣ���ݿ�
	private ArtistInfoDao mArtistInfoDao;
	// �ļ�����Ϣ���ݿ�
	private FolderInfoDao mFolderInfoDao;
	// �ҵ��ղ���Ϣ���ݿ�
	private FavoriteInfoDao mFavoriteDao;

	private List<AlbumInfo> albumInfos = new ArrayList<AlbumInfo>();
	private List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
	private static MusicUtils musicUtils = null;
	private Context mContext;

	private MusicUtils(Context context) {
		mContext = context;
		// initData();
	}

	public static void init(Context context) {
		musicUtils = new MusicUtils(context);

	}

	public void initData() {

		queryAlbums(mContext);
	}

	public static MusicUtils getDefault() {
		return musicUtils;
	}

	public List<MusicInfo> queryFavorite(Context context) {
		if (mFavoriteDao == null) {
			mFavoriteDao = new FavoriteInfoDao(context);
		}
		return mFavoriteDao.getMusicInfo();
	}

	/**
	 * ��ȡ������Ƶ�ļ����ļ�����Ϣ
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("NewApi")
	public List<FolderInfo> queryFolder(Context context) {
		if (mFolderInfoDao == null) {
			mFolderInfoDao = new FolderInfoDao(context);
		}
		// if (mFolderInfoDao.hasData()) {
		// return mFolderInfoDao.getFolderInfo();
		// }
		Uri uri = MediaStore.Files.getContentUri("external");
		ContentResolver cr = context.getContentResolver();
		StringBuilder mSelection = new StringBuilder(FileColumns.MEDIA_TYPE
				+ " = " + FileColumns.MEDIA_TYPE_AUDIO + " and " + "("
				+ FileColumns.DATA + " like'%.mp3' or " + Media.DATA
				+ " like'%.wma')");
		// ��ѯ��䣺������.mp3Ϊ��׺����ʱ������1���ӣ��ļ���С����1MB��ý���ļ�
		// if(sp.getFilterSize()) {
		mSelection.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		// }
		// if(sp.getFilterTime()) {
		mSelection.append(" and " + Media.DURATION + " > " + FILTER_DURATION);
		// }
		mSelection.append(") group by ( " + FileColumns.PARENT);
		List<FolderInfo> list = getFolderList(cr.query(uri, proj_folder,
				mSelection.toString(), null, null));
		// mFolderInfoDao.saveFolderInfo(list);
		return list;
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @param context
	 * @return
	 */
	public List<ArtistInfo> queryArtist(Context context) {
		if (mArtistInfoDao == null) {
			mArtistInfoDao = new ArtistInfoDao(context);
		}
		// if (mArtistInfoDao.hasData()) {
		// return mArtistInfoDao.getArtistInfo();
		// }
		Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
		d("queryArtist.....................uri:"+uri.toString());
		List<ArtistInfo> list = getArtistList(cr.query(uri, proj_artist, null,
				null, MediaStore.Audio.Artists.NUMBER_OF_TRACKS + " desc"));
//		mArtistInfoDao.saveArtistInfo(list);
		return list;
	}

	/**
	 * ��ȡר����Ϣ
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AlbumInfo> queryAlbums(Context context) {
		if (mAlbumInfoDao == null) {
			mAlbumInfoDao = new AlbumInfoDao(context);
		}

		if (albumInfos.size() > 0) {
			return albumInfos;
		}
		// SPStorage sp = new SPStorage(context);

		Uri uri = Albums.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();
		StringBuilder where = new StringBuilder(Albums._ID
				+ " in (select distinct " + Media.ALBUM_ID
				+ " from audio_meta where (1=1 ");

		// if(sp.getFilterSize()) {
		where.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		// }
		// if(sp.getFilterTime()) {
		where.append(" and " + Media.DURATION + " > " + FILTER_DURATION);
		// }
		where.append("))");

		// if (mAlbumInfoDao.hasData()) {
		// return mAlbumInfoDao.getAlbumInfo();
		// } else {
		// Media.ALBUM_KEY ��ר����������
		albumInfos = getAlbumList(cr.query(uri, proj_album, where.toString(),
				null, Media.ALBUM_KEY));
		// mAlbumInfoDao.saveAlbumInfo(list);
		Collections.sort(albumInfos);
		return albumInfos;
		// }
	}

	/**
	 * 
	 * @param context
	 * @param from
	 *            ��ͬ�Ľ������Ҫ����ͬ�Ĳ�ѯ
	 * @return
	 */
	public List<MusicInfo> queryMusic(Context context, int from) {
		return queryMusic(context, null, null, from);
	}

	/**
	 * 
	 * @param context
	 * @param selections
	 * @param selection
	 * @param from
	 *            local,artist,album,folder
	 * @return
	 */
	public List<MusicInfo> queryMusic(Context context, String selections,
			String selection, int from) {

		// if (musicInfos.size() > 0) {
		// return musicInfos;
		// }
		if (mMusicInfoDao == null) {
			mMusicInfoDao = new MusicInfoDao(context);
		}
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		ContentResolver cr = context.getContentResolver();

		StringBuffer select = new StringBuffer(" 1=1 ");
		select.append(" and " + Media.SIZE + " > " + FILTER_SIZE);
		select.append(" and " + Media.DURATION + " > " + FILTER_DURATION);
		if (!TextUtils.isEmpty(selections)) {
			select.append(selections);
		}
		switch (from) {
		case START_FROM_LOCAL:
			// if local database has data
			// if (mMusicInfoDao.hasData()) {
			// musicInfos = mMusicInfoDao.getMusicInfo();
			// } else {
			// query android media database
			long a = System.currentTimeMillis();
			Cursor cursor = cr.query(uri, proj_music, select.toString(), null,
					MediaStore.Audio.Media.ARTIST_KEY);
			musicInfos = getMusicList(cursor);
			d("query music spend time:" + (System.currentTimeMillis() - a)
					/ 1000.0 + " s");
			d("uri:" + uri.toString());
			mMusicInfoDao.saveMusicInfo(musicInfos);
			// }
			break;
		case START_FROM_ARTIST:
			if (mMusicInfoDao.hasData()) {
				musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
						START_FROM_ARTIST);
			} else {
				// return getMusicList(cr.query(uri, proj_music,
				// select.toString(), null,
				// MediaStore.Audio.Media.ARTIST_KEY));
			}
			break;
		case START_FROM_ALBUM:
			if (mMusicInfoDao.hasData()) {
				musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
						START_FROM_ALBUM);
			}
			break;
		case START_FROM_FOLDER:
			if (mMusicInfoDao.hasData()) {
				musicInfos = mMusicInfoDao.getMusicInfoByType(selection,
						START_FROM_FOLDER);
			}
			break;

		}
		return musicInfos;
	}

	private static void d(String msg) {
		DeBug.d(MusicUtils.class, msg);
	}

	public static ArrayList<MusicInfo> getMusicList(Cursor cursor) {
		if (cursor == null) {
			return null;
		}
		ArrayList<MusicInfo> musicList = new ArrayList<MusicInfo>();
		DeBug.d(MusicUtils.class,
				"getMusicList,local music,size:" + cursor.getCount());
		while (cursor.moveToNext()) {
			MusicInfo music = new MusicInfo();
			music.songId = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));
			music.albumId = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			music.duration = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));
			music.title = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE));
			music.artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			music.musicName = music.title;
			String filePath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			music.playPath = filePath;
			music.data = filePath;
			String folderPath = filePath.substring(0,
					filePath.lastIndexOf(File.separator));
			music.folder = folderPath;
			music.musicNameKey = StringUtil.getPingYin(music.title);
			music.artistKey = StringUtil.getPingYin(music.artist);
			musicList.add(music);
		}
		cursor.close();
		return musicList;
	}

	public List<AlbumInfo> getAlbumList(Cursor cursor) {
		List<AlbumInfo> list = new ArrayList<AlbumInfo>();
		d("album,size:" + cursor.getCount());
		while (cursor.moveToNext()) {
			AlbumInfo info = new AlbumInfo();
			info.album_name = cursor.getString(cursor
					.getColumnIndex(Albums.ALBUM));
			info.pinyin = StringUtil.getPingYin(info.album_name);
			info.album_id = cursor.getInt(cursor.getColumnIndex(Albums._ID));
			info.number_of_songs = cursor.getInt(cursor
					.getColumnIndex(Albums.NUMBER_OF_SONGS));
			info.album_path = cursor.getString(cursor
					.getColumnIndex(Albums.ALBUM_ART));
			list.add(info);
		}
		cursor.close();
		return list;
	}

	public List<ArtistInfo> getArtistList(Cursor cursor) {
		d("getArtistList:...........................size:"+cursor.getCount());
		List<ArtistInfo> list = new ArrayList<ArtistInfo>();
		while (cursor.moveToNext()) {
			ArtistInfo info = new ArtistInfo();
			info.artist_name = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
			info.number_of_tracks = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
			// info.number_of_tracks = cursor.getInt(cursor
			// .getColumnIndex(MediaStore.Audio.Artists.));

			list.add(info);
		}
		cursor.close();
		return list;
	}

	public static List<FolderInfo> getFolderList(Cursor cursor) {
		d("getFolderList,,,,,,,,,,,,,,,,size:" + cursor.getCount());
		List<FolderInfo> list = new ArrayList<FolderInfo>();
		while (cursor.moveToNext()) {
			FolderInfo info = new FolderInfo();
			String filePath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Files.FileColumns.DATA));
			info.folder_path = filePath.substring(0,
					filePath.lastIndexOf(File.separator));
			info.folder_name = info.folder_path.substring(info.folder_path
					.lastIndexOf(File.separator) + 1);
			list.add(info);
		}
		cursor.close();
		return list;
	}

	public static String makeTimeString(long milliSecs) {
		StringBuffer sb = new StringBuffer();
		long m = milliSecs / (60 * 1000);
		sb.append(m < 10 ? "0" + m : m);
		sb.append(":");
		long s = (milliSecs % (60 * 1000)) / 1000;
		sb.append(s < 10 ? "0" + s : s);
		return sb.toString();
	}

	/**
	 * ���ݸ�����ID��Ѱ�ҳ������ڵ�ǰ�����б��е�λ��
	 * 
	 * @param list
	 * @param id
	 * @return
	 */
	public static int seekPosInListById(List<MusicInfo> list, int id) {
		if (id == -1) {
			return -1;
		}
		int result = -1;
		if (list != null) {

			for (int i = 0; i < list.size(); i++) {
				if (id == list.get(i).songId) {
					result = i;
					break;
				}
			}
		}
		return result;
	}

	// public static void clearCache() {
	// sArtCache.clear();
	// }

	public List<AlbumInfo> getAlbumInfos() {
		return albumInfos;
	}

	public void setAlbumInfos(List<AlbumInfo> albumInfos) {
		this.albumInfos = albumInfos;
	}
}
