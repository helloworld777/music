package com.music.view.gesturepressword;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.lu.R;
import com.music.utils.ApplicationUtil;
import com.music.utils.DeBug;
import com.music.utils.DialogUtil;
import com.music.view.MusicApplication;
import com.music.view.activity.BaseActivity;
import com.music.widget.lockpatternview.LockPatternUtils;
import com.music.widget.lockpatternview.LockPatternView;
import com.music.widget.lockpatternview.LockPatternView.Cell;
@ContentView(R.layout.activity_gesturepassword_unlock)
public class UnlockGesturePasswordActivity extends BaseActivity {
	/** �м����ͼ�� **/
	private LockPatternView mLockPatternView;
	/** ����������� **/
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	/** ��ʱ�� **/
	private CountDownTimer mCountdownTimer = null;
	/** Handler **/
	private Handler mHandler = new Handler();
	/** �����ı� **/
	private TextView mHeadTextView;
	private Animation mShakeAnim;

	@ViewInject(R.id.gesturepwd_unlock_forget)
	private TextView gesturepwd_unlock_forget;
	/**
	 * ������ʾ��Ϣ
	 * 
	 * @param message
	 */
	private void showToast(CharSequence message) {
		DialogUtil.showToast(this, message);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		long start=SystemClock.currentThreadTimeMillis();
		super.onCreate(savedInstanceState);
		DeBug.d(this, "onCreate........");
		// ���ò���
//		setContentView(R.layout.activity_gesturepassword_unlock);

		
		
//		if (ApplicationUtil.getAppLockState(this) != 1) {
//			startActivity(LocalMusicActivity.class);
//			finish();
//			return;
//		}
		// ����id�ڲ������ҵ��ؼ�����
		mLockPatternView = (LockPatternView) this
				.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
		
		DeBug.d(this, ".......onCreate:"+(SystemClock.currentThreadTimeMillis()-start)/1000.0+" s");
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		// �ж��Ƿ���������������,���û����,��ת�����ý���
		// if (!App.getInstance().getLockPatternUtils().savedPatternExists()) {
		// startActivity(new Intent(this, GuideGesturePasswordActivity.class));
		// finish();
		// }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// �жϼ�ʱ�������Ƿ�Ϊ��
		if (mCountdownTimer != null)// ��Ϊ��
			mCountdownTimer.cancel();// ȡ����ʱ��
	}
	@OnClick({R.id.gesturepwd_unlock_forget})
	public void viewClick(View view){
		DeBug.d(this, "...........viewClick");
		switch (view.getId()) {
		case R.id.gesturepwd_unlock_forget:
			
			startActivity(new Intent(this,CreateGesturePasswordActivity.class));
//			finish();
			break;

		default:
			break;
		}
	}
	/**
	 * ������Ƶ�ͼ��,�ָ�����ʼ״̬
	 */
	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};
	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {
		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}
		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}
		public void onPatternDetected(List<LockPatternView.Cell> pattern) {

			if (pattern == null)// �ж�pattern�Ƿ�Ϊ��
				return;

			// �жϽ����Ƿ�ɹ�
			if (MusicApplication.getInstance().getLockPatternUtils()
					.checkPattern(pattern)) {// �ɹ�

				// ���õ�ǰģʽΪ��ȷ��ģʽ
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Correct);

				// ����Intent��תĿ��
				// Intent intent = new
				// Intent(UnlockGesturePasswordActivity.this,
				// GuideGesturePasswordActivity.class);
				// // ���µ�Activity
				// startActivity(intent);
				showToast("�����ɹ�");
				ApplicationUtil.setAppToBack(
						UnlockGesturePasswordActivity.this, 0);
				setResult(0, new Intent());
				// ������ǰ��Activity
				finish();

			} else {// δ�ɹ�

				// ���õ�ǰģʽΪ����ģʽ
				mLockPatternView
						.setDisplayMode(LockPatternView.DisplayMode.Wrong);

				// �ж����볤��
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {// ���볤�ȴﵽ���Ҫ��

					// ͳ������������
					mFailedPatternAttemptsSinceLastTimeout++;
					// ͳ��ʣ��Ľ�������
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
							- mFailedPatternAttemptsSinceLastTimeout;
					// �ж�ʣ��Ľ�������
					if (retry >= 0) {
						if (retry == 0)// ���ʣ���������0,֪ͨ�û�30�������
							showToast("����5��������룬��30�������");
						mHeadTextView.setText("������󣬻�����������" + retry + "��");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}

				} else {// ���볤��δ�ﵽҪ��
					showToast("���볤�Ȳ�����������");
				}

				// �ж�����������
				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {// ��������ﵽ�������
					// ֪ͨ���ý�������30��,30������û��5�ν�������
					mHandler.postDelayed(attemptLockout, 2000);

				} else {// �������δ�ﵽ�������
					// ֪ͨ������Ƶ�ͼ��,�ָ�����ͼ��״̬
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		private void patternInProgress() {
		}

		public void onPatternCellAdded(List<Cell> pattern) {
			// TODO Auto-generated method stub

		}
	};

	Runnable attemptLockout = new Runnable() {

		@Override
		public void run() {
			// ����Ѿ����Ƶ�ͼ��
			mLockPatternView.clearPattern();
			// �����м�ͼ������
			mLockPatternView.setEnabled(false);
			// ʹ�ü�ʱ�����ͼ�ʱ
			mCountdownTimer = new CountDownTimer(
					LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					// �����ȥ������
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {// �������0
						// ÿ��һ����¶����ı���Ϣ
						mHeadTextView.setText(secondsRemaining + " �������");
					} else {// ����ʱ����
						mHeadTextView.setText("�������������");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish() {
					// �����м��������
					mLockPatternView.setEnabled(true);
					// ��������������
					mFailedPatternAttemptsSinceLastTimeout = 0;
				}
			}.start();
		}
	};

}
