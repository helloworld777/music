package com.music.view.activity;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.music.bean.UserBean;
import com.music.bean.UserManager;
import com.music.lu.R;
import com.music.utils.DialogUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.soexample.commons.Constants;
//import com.umeng.socialize.sensor.controller.UMShakeService;
//import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;

@ContentView(value = R.layout.activity_login)
public class LoginActivity extends Activity {

	protected static final String TAG = "LoginActivity";
	@ViewInject(value = R.id.et_username)
	private EditText et_username;
	@ViewInject(value = R.id.et_userpassword)
	private EditText et_userpassword;

	@ViewInject(value = R.id.tv_newuser)
	private TextView tv_newuser;

	private UserManager userManager;

	@SuppressWarnings("unused")
	private UserBean userBean;

	@ViewInject(value = R.id.btn_qzone)
	private ImageView btn_qzone;
	@ViewInject(value = R.id.btn_sina)
	private ImageView btn_sina;
	@ViewInject(value = R.id.btn_tencent)
	private ImageView btn_tencent;

	// ����ƽ̨��Controller, �����������SDK�����á������ȴ���
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constants.DESCRIPTOR);

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		userManager = UserManager.getInstance();
		userBean = userManager.getUserBean(this);

		setTitle("��½");

		initWidget();

		addQZoneQQPlatform();

	}

	private void addQZoneQQPlatform() {
		String appId = "1104335219";
		String appKey = "J68iUn08AUZwHWrJ";
		// ���QQ֧��, ��������QQ�������ݵ�target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.umeng.com");
		qqSsoHandler.addToSocialSDK();

		// ���QZoneƽ̨
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	private void initWidget() {

		// if(cb_username.isChecked()){
		// if(userBean!=null){
		// et_username.setText(userBean.getUsername());
		// }else{
		// et_username.setHint("�������û���");
		// }
		// }else{
		// et_username.setHint("�������û���");
		// }
		// if(cb_userpassword.isChecked()){
		// if(userBean!=null){
		// et_username.setText(userBean.getPasswrod());
		// }else{
		// et_userpassword.setHint("����������");
		// }
		// }else{
		// et_userpassword.setHint("����������");
		// }
		// if(userBean!=null){
		// et_username.setText(userBean.getUsername());
		// et_userpassword.setText(userBean.getPasswrod());
		// }else{
		// et_username.setHint("�������û���");
		// et_userpassword.setHint("����������");
		// }

		et_username.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	@OnClick({ R.id.btn_login, R.id.tv_newuser, R.id.btn_back,
			R.id.btn_tencent, R.id.btn_qzone, R.id.btn_sina, })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_login:
			String username = et_username.getText().toString();
			String userpasswrod = et_userpassword.getText().toString();
			login(username, userpasswrod);
			break;
		case R.id.tv_newuser:
			DialogUtil.showToast(this, "���û�ע��");
			break;
		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_tencent:
			login(SHARE_MEDIA.QQ);
			break;
		case R.id.btn_qzone:
			login(SHARE_MEDIA.QZONE);
			break;
		case R.id.btn_sina:
			login(SHARE_MEDIA.SINA);
			break;
		default:
			break;
		}
	}

	/**
	 * ��Ȩ�������Ȩ�ɹ������ȡ�û���Ϣ</br>
	 */
	private void login(final SHARE_MEDIA platform) {
		mController.doOauthVerify(this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
				DialogUtil.showToast(getApplicationContext(), "���ڵ�½...");
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				String uid = value.getString("uid");
				if (!TextUtils.isEmpty(uid)) {
					getUserInfo(platform);
				} else {
					Toast.makeText(LoginActivity.this, "��Ȩʧ��...",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}
		});
	}

	/**
	 * ��ȡ��Ȩƽ̨���û���Ϣ</br>
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(this, platform, new UMDataListener() {

			@Override
			public void onStart() {
				DialogUtil.showToast(getApplicationContext(), "��½�ɹ�,���ڻ�ȡ��Ϣ...");
			}

			@Override
			public void onComplete(int status, Map<String, Object> info) {
				if (info != null) {
					// Log.i(TAG, info.toString());

					Intent intent = new Intent();
					String username = info.get("screen_name").toString();
					String headPath = info.get("profile_image_url").toString();
					saveUser(username, "", headPath);
					intent.putExtra("local", false);
					setResult(LocalMusicActivity.REQUESTCODE_LOGIN, intent);
					finish();
				}
			}
		});
	}

	

	private void saveUser(String username, String userpasswrod, String headPath) {
		// if(cb_username.isChecked()){
		// userManager.saveUserName(this, username);
		// }
		// if(cb_userpassword.isChecked()){
		// userManager.saveUserName(this, userpasswrod);
		// }
		userManager.setUserBean(new UserBean(username, userpasswrod, headPath));
		userManager.saveUserBean(this, new UserBean(username, userpasswrod,
				headPath));
	}

	private void login(String username, String userpasswrod) {

		if (!isEmpty(username, userpasswrod)) {

			if (isValid(username, userpasswrod)) {
				saveUser(username, userpasswrod, null);
				Intent intent = new Intent();
				intent.putExtra("local", true);
				setResult(LocalMusicActivity.REQUESTCODE_LOGIN, intent);
				finish();
			} else {
				DialogUtil.showToast(this, "�û������������");
			}

		}

	}

	/**
	 * ����Ƿ�Ϊ��
	 * 
	 * @param name
	 * @param password
	 * @return true Ϊ��,false ��Ϊ��
	 */
	private boolean isEmpty(String name, String password) {
		if ("".equals(name)) {
			DialogUtil.showToast(this, "�������û���!");
			return true;
		}
		if ("".equals(password)) {
			DialogUtil.showToast(this, "�������½����!");
			return true;
		}
		return false;
	}

	/**
	 * �������Ч
	 * 
	 * @param name
	 * @param password
	 * @return true ��Ч,false ����Ч
	 */
	private boolean isValid(String name, String password) {
		// if ("����".equals(name) && "123".equals(password)) {
		// return true;
		// }
		return true;
	}
}
