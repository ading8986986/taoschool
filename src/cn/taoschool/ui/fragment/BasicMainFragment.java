package cn.taoschool.ui.fragment;

import org.json.JSONObject;

import cn.taoschool.listener.IMainActivityReqListener;
import cn.taoschool.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import android.support.v4.app.Fragment;
import android.view.View;
import android.app.ProgressDialog;
import android.content.Context;


public abstract class BasicMainFragment extends Fragment  implements IMainActivityReqListener,
OnRefreshListener{


	protected ProgressDialog mProgressDialog;
	protected Context mContext;
	protected JSONObject filter_params;
	protected View mLoadingView;
	protected int mCurSubTitle = 0;////当前的那个选项Subtitle,0代表全都隐藏
	abstract public void  getData();
	abstract protected void getMoreData();
	abstract public void doSearch();
	abstract public void setFilterParam();
	abstract public boolean onReturnAction();
	abstract public void initSubtitleMenu();
	public void initSubtitleMenu(int mCurSubTitle){
		this.mCurSubTitle = mCurSubTitle;
		initSubtitleMenu();
	}
	
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
			mLoadingView.setVisibility(View.GONE);	}
	
	
}

