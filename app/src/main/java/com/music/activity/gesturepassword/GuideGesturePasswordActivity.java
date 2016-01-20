package com.music.activity.gesturepassword;

import com.music.MusicApplication;
import com.music.lu.R;
import com.music.widget.lockpatternview.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * �û�����������������
 * 
 * @author jgduan
 * 
 *         ���û����ν���Ӧ��ʱ��ʾ
 * 
 */
public class GuideGesturePasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ���ò���
		setContentView(R.layout.activity_gesturepassword_guide);

		// �ڲ������ҵ������������밴ť��Ϊ��󶨵���¼�������
		findViewById(R.id.gesturepwd_guide_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						// ��������������
						MusicApplication.getInstance().getLockPatternUtils().clearLock();
						// ָ��Intent��תĿ��
						Intent intent = new Intent(
								GuideGesturePasswordActivity.this,
								CreateGesturePasswordActivity.class);
						// ���µ�Activity
						startActivity(intent);
						// ������ǰActivity
						finish();

					}
				});
	}

}
