package com.music.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lu.library.base.BasePresenter;
import com.lu.library.base.IBaseView;
import com.lu.library.log.DebugLog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class BaseMVPFragment<P extends BasePresenter> extends BaseFragment {

	protected P mPersenter;
	/**
	 * 子activity需复写这个方法。。。。
	 * @return
	 */
	protected  P autoCreatePresenter(){
		try {
			return (P)getArgumentClass().newInstance();
		} catch (java.lang.InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	};


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		mPersenter=autoCreatePresenter();
		if (mPersenter!=null){
			try {
				mPersenter.attachView((IBaseView) this);
			}catch (Exception e){
				e.printStackTrace();
			}

		}
		super.onCreate(savedInstanceState);

	}



	//得到父类的泛型类T
	public Class getArgumentClass(){
		//返回表示此 Class 所表示的实体类的 直接父类 的 Type。注意，是直接父类
		//这里type结果是 com.dfsj.generic.GetInstanceUtil<com.dfsj.generic.User>
		Type type = getClass().getGenericSuperclass();
		DebugLog.d(type);
		// 判断 是否泛型
		if (type instanceof ParameterizedType) {
			// 返回表示此类型实际类型参数的Type对象的数组.
			// 当有多个泛型类时，数组的长度就不是1了
			Type[] ptype = ((ParameterizedType) type).getActualTypeArguments();
			Class clazz= (Class) ptype[0];
			DebugLog.d(clazz);
			return clazz;  //将第一个泛型T对应的类返回（这里只有一个）
		} else {
			return Object.class;//若没有给定泛型，则返回Object类
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mPersenter!=null){
			mPersenter.detachView();
		}
	}


}
