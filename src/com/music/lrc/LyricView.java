package com.music.lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.music.bean.LyricSentence;
import com.music.lu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 显示歌词的View
 */
public class LyricView extends View {
	@SuppressWarnings("unused")
	private static final String TAG = "LyricView";
	/**
	 */
	public static final int NO_LRC = 0;
	/**
	 */
	public static final int LRC_LOADIGN = 1;
	/**
	 */
	public static final int LRC_LOADED = 2;
	/**
	 */
	private Paint mPaint;
	private float mX;
	/**
	 */
	private Paint mPathPaint;
	private int mY;
	private long currentDuringtime2;
	private float middleY;//
	private final int DY = 80;//

	private float drifty;//
	private int index = 0;
//	private float mTouchHistoryY;
	private int loadLrc = 0;

	private float drift_r = 0;
	@SuppressWarnings("unused")
	private Context mContext;

	public LyricView(Context context) {
		super(context);

		init(context);
	}

	public LyricView(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public LyricView(Context context, AttributeSet attr, int i) {
		super(context, attr, i);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		setFocusable(true);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(25);
		mPaint.setColor(Color.LTGRAY);
		mPaint.setTypeface(Typeface.SERIF);
		mPaint.setTextAlign(Paint.Align.CENTER);

		mPathPaint = new Paint();
		mPathPaint.setAntiAlias(true);
		mPathPaint.setTextSize(40);
		mPathPaint.setColor(Color.WHITE);
		mPathPaint.setTypeface(Typeface.SANS_SERIF);
		mPathPaint.setTextAlign(Paint.Align.CENTER);
	}

	public List<Map<String, String>> maps;

	public void setMaps(List<Map<String, String>> maps) {
		this.maps = maps;
	}

	public void setLyricLoadHelper(LyricLoadHelper loadHelper) {
	}

	private List<LyricSentence> lyricSentences = new ArrayList<LyricSentence>();

	public void setLyricSentences(List<LyricSentence> l) {
//		DeBug.d(this,"List<LyricSentence>:"+l.toString());
		if (l != null && lyricSentences.isEmpty()) {
			lyricSentences.addAll(l);
			index = 0;
		}
		loadLrc = LRC_LOADED;
	}

	public void reset() {
		index = 0;

	}

	/**
	 */
	public void clear() {
		index = 0;
		loadLrc = LRC_LOADIGN;
		lyricSentences.clear();
	}


	protected void onDraw(Canvas canvas) {
		drift_r = drifty;
		canvas.drawColor(0xEFeffff);
		if (index == -1)
			return;
//		isClick = false;
		if (lyricSentences == null || lyricSentences.size() == 0 || loadLrc == 0) {
//			isClick = true;
			canvas.drawText(getContext().getText(R.string.no_lrc).toString(), mX, middleY, mPathPaint);
			return;
		}
		if (loadLrc == 1) {
			canvas.drawText(getContext().getText(R.string.no_lrc).toString(), mX, middleY, mPathPaint);
			return;
		}

		if (index > lyricSentences.size()) {
			return;
		}
		canvas.drawText(lyricSentences.get(index).getContentText(), mX, middleY + drift_r, mPathPaint);

		float tempY = middleY + drift_r;
		for (int i = index - 1; i >= 0; i--) {
			tempY = tempY - DY;
			if (tempY < 0) {
				break;
			}
			canvas.drawText(lyricSentences.get(i).getContentText(), mX, tempY, mPaint);
		}
		tempY = middleY + drift_r;
		for (int i = index + 1; i < lyricSentences.size(); i++) {
			tempY = tempY + DY;
			if (tempY > mY) {
				break;
			}
			canvas.drawText(lyricSentences.get(i).getContentText(), mX, tempY, mPaint);
		}

	}

	/**
	 * @param w
	 *            Current width of this view.
	 * @param h
	 *            Current height of this view.
	 * @param ow
	 *            Old width of this view.
	 * @param oh
	 *            Old height of this view.
	 *
	 */
	protected void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w, h, ow, oh);
		mX = w * 0.5f;//
		mY = h;
		middleY = h * 0.5f;
	}

	/**
	 * @param CurrentPosition
	 */
	public void updateindex(int CurrentPosition) {

//		Log.i(TAG, "start................index=" + index);
		if (lyricSentences == null) {
			return;
		}
		// if (index < maps.size() - 1) {
		if (index < lyricSentences.size() - 1) {

			if (CurrentPosition >= (lyricSentences.get(index + 1).getStartTime())) {
				currentDuringtime2 = (lyricSentences.get(index + 1).getStartTime()) - (lyricSentences.get(index).getStartTime());
				index++;
				drifty = 0;
			} else if (index == 0) {
				currentDuringtime2 = (lyricSentences.get(index).getStartTime());
			} else if (CurrentPosition < (lyricSentences.get(index - 1).getStartTime())) {
				for (int i = 0, size = lyricSentences.size() - 1; i < size; i++) {
					if (CurrentPosition >= (lyricSentences.get(i).getStartTime()) && CurrentPosition < (lyricSentences.get(i + 1).getStartTime())) {
						currentDuringtime2 = (lyricSentences.get(i + 1).getStartTime()) - (lyricSentences.get(i).getStartTime());
						index = i;
						break;
					}

				}
			}

		}

		if (drifty > -40.0){
			if (currentDuringtime2 > 100) {
				drifty = (float) (drifty - 40.0 / (currentDuringtime2 / 100));
			} else {
				drifty = 0;
			}
		}

//		Log.i(TAG, "CurrentPosition:" + CurrentPosition + ",index:" + index+",drifty:"+drifty);
//		invalidate();
	}

	public boolean repair() {
		if (index <= 0) {
			index = 0;
			return false;
		}
		if (index > lyricSentences.size() - 1)
			index = lyricSentences.size() - 1;
		return true;
	}

	public int getLoadLrc() {
		return loadLrc;
	}

	public void setLoadLrc(int loadLrc) {
		this.loadLrc = loadLrc;
	}

	public interface LyricViewClickListener {
		void lyricViewClick();
	}
}
