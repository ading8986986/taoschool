package cn.taoschool.ui.fragment;

import cn.taoschool.listener.IDetailActivityFragmentListener;
import android.support.v4.app.Fragment;
import android.view.View;
import android.app.ProgressDialog;
import android.content.Context;


public abstract class BasicDetailFragment extends Fragment  implements IDetailActivityFragmentListener{


	protected ProgressDialog mProgressDialog;
	protected Context mContext = getActivity();
	protected View mLoadingView;
	abstract public void  getData();
	abstract protected void getMoreData();
	abstract public boolean onReturnAction();
	
	
	/**
	 * 显示进度框
	 */
	protected void showProgressDialog() {		
		if(mLoadingView != null)
			mLoadingView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 隐藏进度框
	 */
	protected void dissmissProgressDialog() {
		if(mLoadingView != null)
			mLoadingView.setVisibility(View.GONE);	}}
