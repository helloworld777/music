package com.music.ui.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lu.library.util.SPUtil;
import com.music.Constant;
import com.music.bean.BgBean;
import com.music.lu.R;
import com.lu.library.adapter.LuAdapter;
import com.lu.library.adapter.ViewHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 切换背景界面
 */
@ContentView(value = R.layout.activity_changebg)
public class ChangeBgActivity extends BaseActivity {

	@ViewInject(value = R.id.gridview)
	private GridView gridView;

	private LuAdapter<BgBean> adapter;
	private List<BgBean> bgBeans;

	@ViewInject(value = R.id.iv_search)
	private ImageView iv_search;
	@ViewInject(value = R.id.iv_more)
	private ImageView iv_more;
	@ViewInject(value = R.id.iv_back)
	private ImageView iv_back;

	@ViewInject(value = R.id.tv_title)
	private TextView tv_title;

	private int checkedId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initWidget();
	}

	private void initWidget() {
		iv_more.setVisibility(View.GONE);
		iv_search.setVisibility(View.GONE);
		tv_title.setText(R.string.title_change_bg);

		adapter = new LuAdapter<BgBean>(this, bgBeans,
				R.layout.item_changebg) {

			@Override
			public void convert(ViewHolder arg0, BgBean arg1) {

				arg0.setImage(R.id.img_bg, arg1.getBitmap());
				if (!arg1.isChecked()) {
					arg0.getView(R.id.img_checked)
							.setVisibility(View.INVISIBLE);
				} else {
					arg0.getView(R.id.img_checked).setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void convert(ViewHolder helper, int position) {

			}
		};

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				for (int i = 0; i < bgBeans.size(); i++) {
					bgBeans.get(i).setChecked(i==position?true:false);
				}
				SPUtil.put(BG_INDEX_KEY, (position));
				BgBean.setCurrentBitmap(bgBeans.get(position).getBitmap());
				sendBroadcast(new Intent(Constant.CHANGED_BG));
				adapter.notifyDataSetChanged();
			}
		});

	}

	private void initData() {

		checkedId = (int) SPUtil.get( BG_INDEX_KEY,0);
		bgBeans = new ArrayList<BgBean>();
		AssetManager assetManager = getAssets();

		InputStream inputStream=null;
		try {
			String[] paths = assetManager.list("bgs");
			// Bitmap[] bitmaps=new Bitmap[paths.length];
			for (int i = 0; i < paths.length; i++) {
				inputStream = assetManager.open("bgs/" + paths[i]);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				bgBeans.add(new BgBean(bitmap, checkedId==i?true:false));

			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@OnClick({ R.id.iv_back })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_back:
			finish();
			break;

		default:
			break;
		}
	}

}
