package com.music.ui.activity;

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
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
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

//	private UMSocialService mController = UMServiceFactory
//			.getUMSocialService(DESCRIPTOR);

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		userManager = UserManager.getInstance();
		userBean = userManager.getUserBean(this);

		setTitle(R.string.title_login);

		initWidget();

		addQZoneQQPlatform();

	}

	private void addQZoneQQPlatform() {
//		String appId = "1104335219";
//		String appKey = "J68iUn08AUZwHWrJ";
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
//		qqSsoHandler.setTargetUrl("http://www.umeng.com");
//		qqSsoHandler.addToSocialSDK();
//
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
//				appKey);
//		qZoneSsoHandler.addToSocialSDK();
		UMShareAPI umShareAPI=UMShareAPI.get(this);
		umShareAPI.getPlatformInfo(this, SHARE_MEDIA.SINA, new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA share_media) {

			}

			@Override
			public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

			}

			@Override
			public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

			}

			@Override
			public void onCancel(SHARE_MEDIA share_media, int i) {

			}
		});
	}

	private void initWidget() {


		et_username.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		umShareAPI=UMShareAPI.get(this);
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
			DialogUtil.showToast(this, "...");
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
	UMShareAPI umShareAPI
			;
	/**
	 */
	private void login(final SHARE_MEDIA platform) {

		umShareAPI.doOauthVerify(this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
				DialogUtil.showToast(getApplicationContext(), "mm..");
			}

			@Override
			public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
				String uid = map.get("uid");
				if (!TextUtils.isEmpty(uid)) {
					getUserInfo(platform);
				} else {
					Toast.makeText(LoginActivity.this, ",,,...",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

			}

			@Override
			public void onCancel(SHARE_MEDIA share_media, int i) {

			}

		});
	}

	/**
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		umShareAPI.getPlatformInfo(this, platform, new UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA share_media) {

			}

			@Override
			public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
				if (map != null) {
					// Log.i(TAG, info.toString());

					Intent intent = new Intent();
					String username = map.get("screen_name").toString();
					String headPath = map.get("profile_image_url").toString();
					saveUser(username, "", headPath);
					intent.putExtra("local", false);
					setResult(LocalMusicActivity.REQUESTCODE_LOGIN, intent);
					finish();
				}
			}

			@Override
			public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

			}

			@Override
			public void onCancel(SHARE_MEDIA share_media, int i) {

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
				DialogUtil.showToast(this, "");
			}

		}

	}

	/**
	 *
	 * @param name
	 * @param password
	 */
	private boolean isEmpty(String name, String password) {
		if ("".equals(name)) {
			DialogUtil.showToast(this, "fff!");
			return true;
		}
		if ("".equals(password)) {
			DialogUtil.showToast(this, "fff!");
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param name
	 * @param password
	 */
	private boolean isValid(String name, String password) {
		// return true;
		// }
		return true;
	}
}
