package com.music.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.music.bean.MusicInfo;
import com.music.db.AlbumInfoDao;
import com.music.db.ArtistInfoDao;
import com.music.db.FavoriteInfoDao;
import com.music.db.FolderInfoDao;
import com.music.db.MusicInfoDao;
import com.music.lu.R;
import com.music.service.IConstants;
import com.music.service.IMediaService;

/**
 * ��ҳ����
 * ����չʾ������ļ���ģ��
 * ����Ҫע��Ƕ�׵�����ViewPager
 * @author longdw(longdawei1988@gmail.com)
 *
 */
@SuppressLint("InflateParams")
public class MainFragment extends Fragment implements IConstants{

	private GridView mGridView;
	private MyGridViewAdapter mAdapter;
	protected IMediaService mService;

	
	private MusicInfoDao mMusicDao;
	private FolderInfoDao mFolderDao;
	private ArtistInfoDao mArtistDao;
	private AlbumInfoDao mAlbumDao;
	private FavoriteInfoDao mFavoriteDao;
//	public UIManager //mUIManager;
	
//	private MusicTimer mMusicTimer;
	@SuppressWarnings("unused")
	private MusicPlayBroadcast mPlayBroadcast;
//	private MainBotto//mUIManager mBotto//mUIManager;
//	private SlidingDrawerManager mSdm;
	@SuppressWarnings("unused")
	private RelativeLayout  mMainLayout;
	@SuppressWarnings("unused")
	private LinearLayout mBottomLayout;
	@SuppressWarnings("unused")
	private Bitmap defaultArtwork;
//	private ServiceManager mServiceManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMusicDao = new MusicInfoDao(getActivity());
		mFolderDao = new FolderInfoDao(getActivity());
		mArtistDao = new ArtistInfoDao(getActivity());
		mAlbumDao = new AlbumInfoDao(getActivity());
		mFavoriteDao = new FavoriteInfoDao(getActivity());
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frame_main, container, false);
		mGridView = (GridView) view.findViewById(R.id.gridview);
		mAdapter = new MyGridViewAdapter();
		
		mMainLayout = (RelativeLayout) view
				.findViewById(R.id.main_layout);
//		mMainLayout.setOnTouchListener(this);
//		mBottomLayout = (LinearLayout) view.findViewById(R.id.bottomLayout);

		//MusicApp.mServiceManager.connectService();
		//MusicApp.mServiceManager.setOnServiceConnectComplete(this);

		mGridView.setAdapter(mAdapter);
		
		//mUIManager = new UIManager(getActivity(), view);
		//mUIManager.setOnRefreshListener(this);
		
//		mSdm = new SlidingDrawerManager(getActivity(), mServiceManager, view);
	//	mBottomUIManager = new MainBotto//mUIManager(getActivity(), view);
	//	mMusicTimer = new MusicTimer(mSdm.mHandler, mBotto//mUIManager.mHandler);
	//	mSdm.setMusicTimer(mMusicTimer);
		
//		mPlayBroadcast = new MusicPlayBroadcast();
//		IntentFilter filter = new IntentFilter(BROADCAST_NAME);
//		filter.addAction(BROADCAST_NAME);
//		getActivity().registerReceiver(mPlayBroadcast, filter);
		
		
		refreshNum();
		return view;
	}

	private class MyGridViewAdapter extends BaseAdapter {

		private int[] drawable = new int[] { R.drawable.icon_local_music,
				R.drawable.icon_favorites, R.drawable.icon_folder_plus,
				R.drawable.icon_artist_plus, R.drawable.icon_album_plus };
		private String[] name = new String[] { "�ҵ�����", "�ҵ��", "�ļ���", "����",
				"ר��" };
		private int musicNum = 0, artistNum = 0, albumNum = 0, folderNum = 0, favoriteNum = 0;

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public void setNum(int music_num, int artist_num, int album_num,
				int folder_num, int favorite_num) {
			musicNum = music_num;
			artistNum = artist_num;
			albumNum = album_num;
			folderNum = folder_num;
			favoriteNum = favorite_num;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.item_mainfragment_gridview, null);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.gridview_item_iv);
				holder.nameTv = (TextView) convertView
						.findViewById(R.id.gridview_item_name);
				holder.numTv = (TextView) convertView
						.findViewById(R.id.gridview_item_num);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			switch (position) {
			case 0:// �ҵ�����
				holder.numTv.setText(musicNum + "");
				break;
			case 1:// �ҵ��
				holder.numTv.setText(favoriteNum + "");
				break;
			case 2:// �ļ���
				holder.numTv.setText(folderNum + "");
				break;
			case 3:// ����
				holder.numTv.setText(artistNum + "");
				break;
			case 4:// ר��
				holder.numTv.setText(albumNum + "");
				break;
			}

			convertView.setOnClickListener(new View.OnClickListener() {

				@SuppressWarnings("unused")
				@Override
				public void onClick(View v) {
					int from = -1;
					switch (position) {
					case 0:// �ҵ�����
						from = START_FROM_LOCAL;
						break;
					case 1:// �ҵ��
						from = START_FROM_FAVORITE;
						break;
					case 2:// �ļ���
						from = START_FROM_FOLDER;
						break;
					case 3:// ����
						from = START_FROM_ARTIST;
						break;
					case 4:// ר��
						from = START_FROM_ALBUM;
						break;
					}
//					mUIManager.setContentType(from);
					itemClick.clickIndex(position);
//					EventBus.getDefault().post(new ClickEvent(position));
				}
			});

			holder.iv.setImageResource(drawable[position]);
			holder.nameTv.setText(name[position]);

			return convertView;
		}

		private class ViewHolder {
			ImageView iv;
			TextView nameTv, numTv;
		}
	}
	
	public void onServiceConnectComplete(IMediaService service) {/*
		// service�󶨳ɹ���ִ�е�����
		refreshNum();
	*/}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		itemClick=(GridViewItemClick) activity;
	}
	private GridViewItemClick itemClick;
//	public void setItemClick(GridViewItemClick itemClick) {
//		this.itemClick=itemClick;
//	}
	public interface GridViewItemClick{
		public void clickIndex(int position);
	}
	public void refreshNum() {
		int musicCount = mMusicDao.getDataCount();
		int artistCount = mArtistDao.getDataCount();
		int albumCount = mAlbumDao.getDataCount();
		int folderCount = mFolderDao.getDataCount();
		int favoriteCount = mFavoriteDao.getDataCount();
		
		mAdapter.setNum(musicCount, artistCount, albumCount, folderCount, favoriteCount);
	}
	
	private class MusicPlayBroadcast extends BroadcastReceiver {

		@SuppressWarnings("unused")
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(BROADCAST_NAME)) {
				MusicInfo music = new MusicInfo();
				int playState = intent.getIntExtra(PLAY_STATE_NAME, MPS_NOFILE);
				int curPlayIndex = intent.getIntExtra(PLAY_MUSIC_INDEX, -1);
				Bundle bundle = intent.getBundleExtra(MusicInfo.KEY_MUSIC);
				if (bundle != null) {
					music = bundle.getParcelable(MusicInfo.KEY_MUSIC);
				}
				switch (playState) {
				case MPS_INVALID:// ���Ǻ����������ļ����ɲ���ֱ��������һ��
//					mMusicTimer.stopTimer();
//					mSdm.refreshUI(0, music.duration, music);
//					mSdm.showPlay(true);
//
//					mBotto//mUIManager.refreshUI(0, music.duration, music);
//					mBotto//mUIManager.showPlay(true);
					break;
				case MPS_PAUSE:
//					mMusicTimer.stopTimer();
//					mSdm.refreshUI(mServiceManager.position(), music.duration,
//							music);
//					mSdm.showPlay(true);
//
//					mBotto//mUIManager.refreshUI(mServiceManager.position(), music.duration,
//							music);
//					mBotto//mUIManager.showPlay(true);
//
//					mServiceManager.cancelNotification();
					break;
				case MPS_PLAYING:
//					mMusicTimer.startTimer();
//					mSdm.refreshUI(mServiceManager.position(), music.duration,
//							music);
//					mSdm.showPlay(false);
//
//					mBotto//mUIManager.refreshUI(mServiceManager.position(), music.duration,
//							music);
//					mBotto//mUIManager.showPlay(false);

					break;
				case MPS_PREPARE:
//					mMusicTimer.stopTimer();
//					mSdm.refreshUI(0, music.duration, music);
//					mSdm.showPlay(true);
//
//					mBotto//mUIManager.refreshUI(0, music.duration, music);
//					mBotto//mUIManager.showPlay(true);
//
//					// ��ȡ����ļ�
//					mSdm.loadLyric(music);
					break;
				}
			}
		}
	}

	public void onRefresh() {/*
		refreshNum();
	*/}

	int oldY = 0;
	
}
