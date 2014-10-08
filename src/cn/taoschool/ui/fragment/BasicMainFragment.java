package cn.taoschool.ui.fragment;

import org.json.JSONObject;

import cn.taoschool.listener.IMainActivityReqListener;
import cn.taoschool.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;


public abstract class BasicMainFragment extends Fragment  implements IMainActivityReqListener,
OnRefreshListener{


	protected ProgressDialog mProgressDialog;
	protected Context mContext;
	protected JSONObject filter_params;
	abstract public void  getData();
	abstract protected void getMoreData();
	abstract public void doSearch();
	abstract public void setFilterParam();
	abstract public boolean onReturnAction();
	
	/**
	 * 显示进度框
	 */
	protected void showProgressDialog(String content) {		
		if(mProgressDialog == null)
			mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(content);
		mProgressDialog.show();
	}
	
	/**
	 * 隐藏进度框
	 */
	protected void dissmissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
}

