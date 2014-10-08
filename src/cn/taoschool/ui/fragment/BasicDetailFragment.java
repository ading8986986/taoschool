package cn.taoschool.ui.fragment;

import cn.taoschool.listener.IDetailActivityReqListener;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;


public abstract class BasicDetailFragment extends Fragment  implements IDetailActivityReqListener{


	protected ProgressDialog mProgressDialog;
	protected Context mContext = getActivity();
	abstract public void  getData();
	abstract protected void getMoreData();
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
