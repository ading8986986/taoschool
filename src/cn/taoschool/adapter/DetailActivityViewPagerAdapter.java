package cn.taoschool.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.taoschool.ui.fragment.BasicDetailFragment;
import cn.taoschool.ui.fragment.DetailOfSchoolFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DetailActivityViewPagerAdapter extends FragmentPagerAdapter{

	private List<BasicDetailFragment> mList;
	
	public DetailActivityViewPagerAdapter(FragmentManager fm,List<BasicDetailFragment> list) {
		super(fm);
		mList = list;
	}

	@Override
	public Fragment getItem(int position) {
		if(null!=mList && position < mList.size()){
			return mList.get(position);
		}
		return null;
	}

	@Override
	public int getCount() {
		if(null!=mList){
			return mList.size();
		}
		return 0;
	}
}
