package cn.taoschool.ui.fragment;

import org.json.JSONObject;

import cn.taoschool.R;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.cache.AsyncImageLoader;
import cn.taoschool.controller.HttpController;
import cn.taoschool.listener.IDetailActivityReqListener;
import cn.taoschool.ui.SchoolDetailActivity;
import cn.taoschool.util.Constants;
import cn.taoschool.util.DialogOfListStyle;
import cn.taoschool.util.FinalDataUitl;
import cn.taoschool.util.UtilToast;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailOfSchoolFragment extends BasicDetailFragment {
	private String TAG = DetailOfSchoolFragment.class.getName();
	private TextView tv_xxlx,tv_yxls,tv_szd,tv_txdz,tv_tel,tv_mail,tv_web;
	private TextView tv_detail;
	private HttpController mController;
	private IDetailActivityReqListener listener;
	public  DetailOfSchoolFragment(IDetailActivityReqListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onPause() {
		Log.i(TAG, "DetailOfSchoolFragment onPause");
		
		super.onPause();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("TAG","DetailOfSchoolFragment onCreatView");
		View mView = LinearLayout.inflate(getActivity(), R.layout.fragment_school_profile, null);
		initView(mView);
		initData();
		getData();
		return mView;
	}

	
	private void initView(View mView){
		
		tv_xxlx = (TextView)mView.findViewById(R.id.tv_xxlx);
		tv_yxls = (TextView)mView.findViewById(R.id.tv_yxls);
		tv_tel = (TextView)mView.findViewById(R.id.tv_tel);
		tv_mail = (TextView)mView.findViewById(R.id.tv_mail);
		tv_szd = (TextView)mView.findViewById(R.id.tv_szd);
		tv_txdz = (TextView)mView.findViewById(R.id.tv_txdz);
		tv_web = (TextView)mView.findViewById(R.id.tv_web);
		tv_detail = (TextView)mView.findViewById(R.id.tv_detail);	
		mLoadingView = mView.findViewById(R.id.ll_loading);
	}

	public void initData() {
		if( null == mController) mController = HttpController.getInstance(getActivity());	
	}
	
	@Override
	public void getData(){
		showProgressDialog();
		mController.getSchoolDetail(getActivity(), this, SchoolDetailActivity.getSchoolID());		
	}

	@Override
	protected void getMoreData() {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void OnGetDetailInfo(boolean isSuccess, Object result) {
		this.dissmissProgressDialog();
		if(isSuccess&&null!=result){
			fillContent((JSONObject) result);
			listener.OnGetDetailInfo(isSuccess, result);
		}
		else{
			UtilToast.showLong(getActivity(), R.string.net_cannot_used);
			getActivity().finish();
		}
	}
	
	private void fillContent(final JSONObject result){
		tv_xxlx.setText(FinalDataUitl.getBXLXByid(result.optInt("bxlxid"))+ "    (" +FinalDataUitl.getXLCCByid(result.optInt("xlccid")) + ")");
		tv_txdz.setText(result.optString("coladdress"));
		tv_web.setText(result.optString("colweb"));
		if(!TextUtils.isEmpty(result.optString("zbtel"))){
			tv_tel.setText( result.optString("zbtel"));
			tv_tel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogOfListStyle dialog = new DialogOfListStyle(getActivity(),result.optString("zbtel").split(","));
					dialog.show();	
				}
			});
		}
		if(!TextUtils.isEmpty(result.optString("zbmail")))
			tv_mail.setText(result.optString("zbmail"));
		tv_yxls.setText(result.optString("yxls"));
		tv_szd.setText(FinalDataUitl.getProvinceByid(result.optInt("proid")));
		tv_detail.setText(result.optString("coldes"));
		String[] big_imageStrings = result.optString("colimg").split(",");
		this.listener.OnGetImgUrl(big_imageStrings);
	}


	@Override
	public void OnGetImgUrl(String[] imgUrls) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public  boolean onReturnAction() {
		// TODO Auto-generated method stub
		return false;
	}


	
	

}
