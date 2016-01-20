package com.music.view.adapter;

import java.util.ArrayList;
import java.util.List;

import com.music.utils.DeBug;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private static final String TAG = "ViewPagerAdapter";
	List<Fragment> list = new ArrayList<Fragment>();
	public ViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	public Fragment getItem(int arg0) {
		DeBug.d(TAG, "getItem:"+arg0);
		return list.get(arg0);
	}

	public int getCount() {
		return list.size();
	}
//	@Override
//
//	public Object instantiateItem(ViewGroup container,int position) {
//
//	   //�õ������fragment
//
//	    Fragment fragment = (Fragment)super.instantiateItem(container,
//
//	           position);
//
//	   //�õ�tag
//
//	    String fragmentTag = fragment.getTag();         
//
//	 
//
//	   if (fragmentsUpdateFlag[position %fragmentsUpdateFlag.length]) {
//
//	      //������fragment��Ҫ����
//
//	      
//
//	       FragmentTransaction ft =fm.beginTransaction();
//
//	      //�Ƴ��ɵ�fragment
//
//	       ft.remove(fragment);
//
//	      //�����µ�fragment
//
//	       fragment =fragments[position %fragments.length];
//
//	      //�����fragmentʱ������ǰ���õ�tag
//
//	       ft.add(container.getId(), fragment, fragmentTag);
//
//	       ft.attach(fragment);
//
//	       ft.commit();
//
//	      
//
//	      //��λ���±�־
//
//	      fragmentsUpdateFlag[position %fragmentsUpdateFlag.length] =false;
//
//	    }
//
//	 
//
//	   return fragment;
//
//	}
}
