package cn.taoschool.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.taoschool.R;
import cn.taoschool.adapter.EnrollFragmentListAdapter;
import cn.taoschool.adapter.EnrollMajorFragmentListAdapter;
import cn.taoschool.adapter.ExpandMenuAdapter;
import cn.taoschool.bean.EnrollEntity;
import cn.taoschool.bean.EnrollMajorEntity;
import cn.taoschool.controller.HttpController;
import cn.taoschool.ui.SchoolDetailActivity;
import cn.taoschool.util.FinalDataUitl;
import cn.taoschool.util.UtilToast;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DetailOfMajorEnrollFragment extends BasicDetailFragment implements OnClickListener, OnItemClickListener {
	
	private String TAG = DetailOfMajorEnrollFragment.class.getName();
	private TextView tvProvince,tvYear,tvBranch;
	private LinearLayout llProvince,llYear,llBranch;
	private TextView tvProvinceMenu,tvYearMenu,tvBranchMenu;
	private ImageView ivTrendChar;
	private ListView lvFilter;//筛选条件时弹出的下拉菜单
	private ListView mListView;
	private HttpController mController;
	private Map<String, Integer> reqParamMap;
	private EnrollMajorFragmentListAdapter mAdapter;
	private ExpandMenuAdapter filterProvinceAdapter;
	private ExpandMenuAdapter filterBranchAdapter;//筛选条件时弹出的下拉菜单
	private ExpandMenuAdapter filterYearAdapter;//筛选条件时弹出的下拉菜单
	private List<EnrollMajorEntity> enrollMajorList;
	private Handler mHandler;
	
	private int mCurFilter = 0;
	private int province = 0;
	private int klid = 0;//科类型（1 文 2 理 3综合）
	private int year = Integer.parseInt(FinalDataUitl.getCurrentYear());
	private List<String> provinceList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG,"MajorEnrollFragment onCreatView");
		View mView = View.inflate(getActivity(), R.layout.fragment_enroll_major, null);
		initView(mView);
		initData();
		getData();
		return mView;
	}
	
	private void initView(View mView){
		tvProvince = (TextView)mView.findViewById(R.id.tv_province);
		tvYear = (TextView)mView.findViewById(R.id.tv_year);
		tvBranch = (TextView)mView.findViewById(R.id.tv_branch);
		tvProvinceMenu = (TextView)mView.findViewById(R.id.tv_province_menu);
		tvYearMenu = (TextView)mView.findViewById(R.id.tv_year_menu);
		tvBranchMenu = (TextView)mView.findViewById(R.id.tv_branch_menu);
		llProvince = (LinearLayout)mView.findViewById(R.id.ll_province);
		llYear = (LinearLayout)mView.findViewById(R.id.ll_year);
		llBranch = (LinearLayout)mView.findViewById(R.id.ll_branch);
		ivTrendChar = (ImageView)mView.findViewById(R.id.iv_view_chart);
		mListView = (ListView)mView.findViewById(R.id.lv_enroll_major);
		lvFilter = (ListView)mView.findViewById(R.id.lv_filter);
		mLoadingView = mView.findViewById(R.id.ll_loading);
		ivTrendChar.setVisibility(View.GONE);
		llProvince.setOnClickListener(this);
		llYear.setOnClickListener(this);
		llBranch.setOnClickListener(this);
		lvFilter.setOnItemClickListener(this);
	}
	
	public void initData() {	
		provinceList = FinalDataUitl.getAllProvice(true);
		//provinceList.remove(0);//按省份筛选，此时不应该有“全部”选项	
		tvProvince.setText(provinceList.get(0));
		tvYear.setText(FinalDataUitl.getCurrentYear()+"年");
		tvBranch.setText(FinalDataUitl.getBranchList().get(0));
		filterProvinceAdapter = new ExpandMenuAdapter(getActivity(), provinceList);
		filterYearAdapter = new ExpandMenuAdapter(getActivity(), FinalDataUitl.getYearsList());
		filterBranchAdapter = new ExpandMenuAdapter(getActivity(), FinalDataUitl.getBranchList());
		if( null == mController) mController = HttpController.getInstance(getActivity());			
		mAdapter = new EnrollMajorFragmentListAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if( 0 == msg.what)
					mAdapter.setEnrollDataList(enrollMajorList);
			}
		};
	}
	
	
	@Override
	public void getData() {
		showProgressDialog();
		setFilterParam();
		mAdapter.setEnrollDataList(null);
		mController.getDetailMajorEnroll(getActivity(), this,reqParamMap );		
	}
	
	public void setFilterParam(){
		reqParamMap = new HashMap<String, Integer>();
		reqParamMap.put("province", province);
		reqParamMap.put("colid", SchoolDetailActivity.getSchoolID());
		reqParamMap.put("klid", klid);//科类型（1 文 2 理 3综合）
		reqParamMap.put("year", year);
	}

	@Override
	protected void getMoreData() {
				
	}
	

	@Override
	public void OnGetImgUrl(List<String> imgUrls) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnGetDetailInfo(boolean isSuccess, Object result) {
		this.dissmissProgressDialog();
		if(isSuccess){
			JSONArray resultList = ((JSONArray)result);
			if(resultList!=null && resultList.length()>0){
				enrollMajorList = new ArrayList<EnrollMajorEntity>();
				for(int i = 0;i<resultList.length();i++){
					EnrollMajorEntity entity;
					try {
						entity = new EnrollMajorEntity(resultList.getJSONObject(i));
					} catch (JSONException e) {
						e.printStackTrace();
						continue;
					}
					enrollMajorList.add(entity);
				}
				mHandler.sendEmptyMessage(0);
			}
		}
		else{
			UtilToast.showLong(getActivity(), R.string.net_cannot_used);
		}
	}
	
	private void setFilterListView(){
		lvFilter.setVisibility(View.GONE);
		if(0 == mCurFilter){
			return;
		}
		lvFilter.setVisibility(View.VISIBLE);
		switch (mCurFilter) {
		case 1:
			lvFilter.setAdapter(filterProvinceAdapter);
			break;
		case 2:
			lvFilter.setAdapter(filterYearAdapter);
			break;
		case 3:
			lvFilter.setAdapter(filterBranchAdapter);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (mCurFilter) {
		case 1:
			province = position+1;
			tvProvince.setText(provinceList.get(position));
			filterProvinceAdapter.setItemSelect(position, true);
			break;
		case 2:
			tvYear.setText((String)filterYearAdapter.getItem(position)+"年");
			year = Integer.parseInt((String) filterYearAdapter.getItem(position));
			filterYearAdapter.setItemSelect(position, true);
			break;
		case 3:
			tvBranch.setText(FinalDataUitl.getBranchByid(position));
			klid = position;
			filterBranchAdapter.setItemSelect(position, true);
			break;
		default:
			break;
		}
		mCurFilter = 0;
		setFilterListView();
		getData();
	}

	@Override
	public boolean onReturnAction() {
		// TODO Auto-generated method stub
		if(0 != mCurFilter){
			mCurFilter =0;
			setFilterListView();
			return true;
		}
		else return false;
	}
	
	@Override
	public void onPause() {
		Log.i(TAG, "DetailOfEnrollFragment onPause");
		// TODO Auto-generated method stub
		mCurFilter =0;
		setFilterListView();
		super.onPause();
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_province:
			mCurFilter = mCurFilter == 1?0:1;	
			setFilterListView();
			break;
		case R.id.ll_year:
			mCurFilter = mCurFilter == 2?0:2;
			setFilterListView();
			break;
		case R.id.ll_branch:
			mCurFilter = mCurFilter == 3?0:3;
			setFilterListView();
			break;
		default:
			break;
		}		
	}
	

}
