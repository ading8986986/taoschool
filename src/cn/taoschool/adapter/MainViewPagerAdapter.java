package cn.taoschool.adapter;

import java.util.List;

import cn.taoschool.ui.fragment.BasicMainFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MainViewPagerAdapter extends FragmentPagerAdapter{
	
	private List<BasicMainFragment> mListViews;
	
	public MainViewPagerAdapter(FragmentManager fm,List<BasicMainFragment> mListViews) {
		super(fm);
	    this.mListViews = mListViews;  
	}		
	
	@Override
	public int getCount() {
		if(null == mListViews) return 0;
		return mListViews.size();
	}

	@Override
	public Fragment getItem(int position) {
		if(null == mListViews) return null;
		return mListViews.get(position);
	}

}
