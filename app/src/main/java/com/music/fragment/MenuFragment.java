package com.music.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.music.MainContentActivity;
import com.music.SettingActivity;
import com.music.lu.R;
import com.music.service.IConstants;
import com.music.widget.slidingmenu2.SlidingMenu.OnOpenedListener;

/**
 * �໬Menu
 * �����ṩ����������ã������Ŀ��Ƶȼ�����
 * @author longdw(longdawei1988@gmail.com)
 *
 */
public class MenuFragment extends Fragment implements OnClickListener,
		IConstants, OnOpenedListener {

	private TextView mMediaCountTv;
	private TextView mScanTv, mPlayModeTv, mBackgroundTv, mSleepTv, mSettingTv,
			mExitTv;
	private MainContentActivity mMainActivity;

	private int mCurMode;
//	private ServiceManager mServiceManager;
	private static final String modeName[] = { "�б�ѭ��", "˳�򲥷�", "�������", "����ѭ��" };
	private int modeDrawable[] = { R.drawable.icon_list_reapeat,
			R.drawable.icon_sequence, R.drawable.icon_shuffle,
			R.drawable.icon_single_repeat };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.frame_menu_layout, container, false);
		initView(view);
//		mServiceManager = MusicApp.mServiceManager;

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		mMainActivity = (MainContentActivity) getActivity();
//		mMainActivity.mSlidingMenu.setOnOpenedListener(this);
	}

	private void initView(View view) {
		mMediaCountTv = (TextView) view.findViewById(R.id.txt_media_count);
		mScanTv = (TextView) view.findViewById(R.id.txt_scan);
		mPlayModeTv = (TextView) view.findViewById(R.id.txt_play_mode);
		mBackgroundTv = (TextView) view.findViewById(R.id.txt_background);
		mSleepTv = (TextView) view.findViewById(R.id.txt_sleep);
		mSettingTv = (TextView) view.findViewById(R.id.preference_text);
		mExitTv = (TextView) view.findViewById(R.id.txt_exit);

		mMediaCountTv.setOnClickListener(this);
		mScanTv.setOnClickListener(this);
		mPlayModeTv.setOnClickListener(this);
		mBackgroundTv.setOnClickListener(this);
		mSleepTv.setOnClickListener(this);
		mSettingTv.setOnClickListener(this);
		mExitTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_scan:
//			startActivityForResult(new Intent(getActivity(), MenuScanActivity.class), 1);
			break;
		case R.id.txt_play_mode:
			changeMode();
			break;
		case R.id.txt_background:
//			startActivity(new Intent(getActivity(), MenuBackgroundActivity.class));
			break;
		case R.id.preference_text:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		case R.id.txt_sleep:
//			mMainActivity.showSleepDialog();
			break;
		case R.id.txt_exit:
//			getActivity().finish();
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1) {
			//ˢ����ҳ����
//			((MainContentActivity)getActivity()).mMainFragment.refreshNum();
		}
	}

	private void changeMode() {
		mCurMode++;
		if (mCurMode > MPM_SINGLE_LOOP_PLAY) {
			mCurMode = MPM_LIST_LOOP_PLAY;
		}
//		mServiceManager.setPlayMode(mCurMode);
//		initPlayMode();
	}

	private void initPlayMode() {
		mPlayModeTv.setText(modeName[mCurMode]);
		Drawable drawable = getResources().getDrawable(modeDrawable[mCurMode]);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		mPlayModeTv.setCompoundDrawables(drawable, null, null, null);
	}

	@Override
	public void onOpened() {
//		mCurMode = mServiceManager.getPlayMode();
		initPlayMode();
	}
}
