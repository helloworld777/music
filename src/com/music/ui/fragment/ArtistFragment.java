package com.music.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SectionIndexer;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lu.library.adapter.ViewHolder;
import com.music.bean.ArtistInfo;
import com.music.helpers.StringHelper;
import com.music.lu.R;
import com.music.model.MusicModel;
import com.music.ui.activity.LocalMusicActivity;
import com.lu.library.adapter.LuAdapter;
import com.music.ui.service.IConstants;
import com.music.ui.widget.indexablelistview.IndexableListView;
import com.music.utils.LogUtil;

import java.util.List;

/**
 *所有歌手
 */
public class ArtistFragment extends BaseFragment {

	private static final String TAG = "ArtistFragment";

	private ArtistAdapter listAdapter;
	@ViewInject(value = R.id.listview)
	private IndexableListView mMusiclist; //

	private  List<ArtistInfo> artistInfos;
	private MusicListItemClickListener musicListItemClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		musicListItemClickListener = new MusicListItemClickListener();

		LogUtil.i(TAG, "onCreate");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_song, container,
				false);
		initViewWidget(view);
		LogUtil.i(TAG, "onCreateView");
		return view;
	}

	/**
	 */
	private void initViewWidget(View view) {


		long start=System.currentTimeMillis();
		mMusiclist = (IndexableListView) view.findViewById(R.id.listview);

		mMusiclist.setFastScrollEnabled(true);
		mMusiclist.setOnItemClickListener(musicListItemClickListener);


//		artistInfos = MusicUtils.getDefault().queryArtist(getActivity());
		artistInfos = MusicModel.getInstance().queryArtist(getActivity());
		listAdapter = new ArtistAdapter(getActivity(),artistInfos,R.layout.item_listview_album);


		mMusiclist.addFooterView(getFoodView(getString(R.string.arities_size,artistInfos.size())));
		mMusiclist.setShow(false);
		mMusiclist.setAdapter(listAdapter);


		System.out.println("artist widget:"+(System.currentTimeMillis()-start)/1000.0);
	}

	class MusicListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// playMusic(position);
			((LocalMusicActivity)getActivity()).changeFragment(IConstants.START_FROM_ARTIST,artistInfos.get(position));
		}

	}

	class ArtistAdapter extends LuAdapter<ArtistInfo> implements SectionIndexer {


		public ArtistAdapter(Context context, List<ArtistInfo> datas,
				int mItemLayoutId) {
			super(context, datas, mItemLayoutId);
		}
		private String mSections = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		@Override
		public Object[] getSections() {
			String[] sections = new String[mSections.length()];
			for (int i = 0; i < mSections.length(); i++)
				sections[i] = String.valueOf(mSections.charAt(i));
			return sections;
		}

		@Override
		public int getPositionForSection(int section) {

			int result = -1;
			String s = mSections.substring(section, section + 1);
			for (int i = 0; i < artistInfos.size(); i++) {
				String string = StringHelper.getPingYin(artistInfos.get(i).artist_name).substring(0, 1);

				if (string.equalsIgnoreCase(s)) {
					result = i;
					break;
				}
			}
			return result;
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public void convert(ViewHolder helper, int position) {
			// TODO Auto-generated method stub
			ArtistInfo artistInfo=getItem(position);

			helper.setString(R.id.tv_album_name, artistInfo.artist_name);
			helper.setString(R.id.tv_number_of_songs, artistInfo.number_of_tracks+"首歌曲");
			helper.getView(R.id.tv_artist).setVisibility(View.GONE);


			helper.getView(R.id.iv_album_bg).setVisibility(View.GONE);

			helper.getView(R.id.catalog).setVisibility(View.GONE);
		}

	}
}
