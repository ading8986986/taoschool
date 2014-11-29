package cn.taoschool.ui.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.taoschool.R;
import cn.taoschool.adapter.ExpandMenuAdapter;
import cn.taoschool.adapter.MainActivityListAdapter;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.controller.EnvChecker;
import cn.taoschool.controller.HttpController;
import cn.taoschool.db.DBManager;
import cn.taoschool.pulltorefresh.library.PullToRefreshBase;
import cn.taoschool.pulltorefresh.library.PullToRefreshListView;
import cn.taoschool.pulltorefresh.library.PullToRefreshBase.Mode;
import cn.taoschool.ui.SchoolDetailActivity;
import cn.taoschool.util.EnvUtils;
import cn.taoschool.util.FinalDataUitl;
import cn.taoschool.util.UtilToast;

public class MainMajorFragment extends BasicMainFragment implements OnItemClickListener,TextWatcher,
OnClickListener{
	
	private PullToRefreshListView lvSchoolProfile;
	private ListView actualListView;
	private LinearLayout sub_title1,sub_title2,sub_title3;
	private TextView tv_sub_title1,tv_sub_title2,tv_sub_title3;
	private LinearLayout llTwoLevelMenu,llScore;
	private ListView lvFirstLevel,lvSecondLevel,lvOneLevelListView;
	private ImageView iv_up_arrow1,iv_up_arrow2,iv_up_arrow3;
	private EditText et_search;
	private ImageView iv_cancle_search;
	private TextView tv_search;
	private EditText etLowScore,etHighScore;
	private TextView tvScoreCommit;
	
	private List<String> list_province;
	private List<String> listFirstLevelMajor;
	private List<String> listSecondLevelMajor;
	private ExpandMenuAdapter provinceAdapter;
	private ExpandMenuAdapter firstLevelAdapter;
	private ExpandMenuAdapter secondLevelAdapter;
	//private int mCurSubTitle = 0;//当前的那个选项Subtitle,0代表全都隐藏
	
	
	private boolean isTaskCancled = false;//网络任务没有被用户手动cancle
	private DBManager dbManager;
	private HttpController mController;
	private int downloadImgCount;//已下载图片数
	private final int[] imgs = {R.drawable.img1,R.drawable.img2,
			R.drawable.img3,R.drawable.img4,R.drawable.img5,
			R.drawable.img6,R.drawable.img7,R.drawable.img8,
			R.drawable.img9,R.drawable.img10};
	
	private String province = "0"; //省的帅选条件
	private String firstLevelMajor = "0";
	private String secondLevelMajor = "0";
	private int lowScore = 0;
	private int highScore = 0;
	private String keyword = "";
	
	private int beginAt;
	private int loadOperation;//0->重新查询，1->加载更多
	
	private List<SchoolItem> schoolList;
	private MainActivityListAdapter mAdapter;
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("TAG","MainMajorFragment onCreateView");
		View parentView = View.inflate(getActivity(),R.layout.main_viewpager2, null);
		initView(parentView);
		initSubtitleMenu();
		initData();
		getData();
		return parentView;
	}
	
	private void initView(View parentView){
		lvSchoolProfile = (PullToRefreshListView) parentView.findViewById(R.id.refresh_school_list);
		lvSchoolProfile.setOnRefreshListener(this);
		lvSchoolProfile.setMode(Mode.PULL_FROM_END);
		actualListView = lvSchoolProfile.getRefreshableView();
		et_search = (EditText) parentView.findViewById(R.id.et_search);
		iv_cancle_search = (ImageView) parentView.findViewById(R.id.iv_cancel_search);
		tv_search = (TextView) parentView.findViewById(R.id.iv_search);
		mLoadingView = parentView.findViewById(R.id.ll_loading);
//		mLoadingView.setBackgroundColor(Color.RED);
//		mLoadingView.setVisibility(View.VISIBLE);
		sub_title1 = (LinearLayout) parentView.findViewById(R.id.sub_title_lable1);
		sub_title2 = (LinearLayout) parentView.findViewById(R.id.sub_title_lable2);
		sub_title3 = (LinearLayout) parentView.findViewById(R.id.sub_title_lable3);
		tv_sub_title1 = (TextView) parentView.findViewById(R.id.tv_sub_title1);
		tv_sub_title2 = (TextView) parentView.findViewById(R.id.tv_sub_title2);
		tv_sub_title3 = (TextView) parentView.findViewById(R.id.tv_sub_title3);
		llTwoLevelMenu = (LinearLayout)parentView.findViewById(R.id.ll_two_level_menu);
		llScore =  (LinearLayout)parentView.findViewById(R.id.ll_score);
		lvOneLevelListView = (ListView) parentView.findViewById(R.id.ll_one_level_menu);
		lvFirstLevel = (ListView) parentView.findViewById(R.id.lv_first_level);
		lvSecondLevel = (ListView) parentView.findViewById(R.id.lv_second_level);
		iv_up_arrow1 = (ImageView)parentView.findViewById(R.id.iv_up_arrow1);
		iv_up_arrow2 = (ImageView)parentView.findViewById(R.id.iv_up_arrow2);
		iv_up_arrow3 = (ImageView)parentView.findViewById(R.id.iv_up_arrow3);
		etHighScore  = (EditText) parentView.findViewById(R.id.et_high_score);
		etLowScore = (EditText) parentView.findViewById(R.id.et_low_score);
		tvScoreCommit = (TextView) parentView.findViewById(R.id.tv_score_commit);
		et_search.addTextChangedListener(this);
		lvOneLevelListView.setOnItemClickListener(this);
		actualListView.setOnItemClickListener(this);
		lvFirstLevel.setOnItemClickListener(this);
		lvSecondLevel.setOnItemClickListener(this);
		sub_title1.setOnClickListener(this);
		sub_title2.setOnClickListener(this);
		sub_title3.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		iv_cancle_search.setOnClickListener(this);
		tvScoreCommit.setOnClickListener(this);
	}
	

	@Override
	public  void initSubtitleMenu(){
		iv_up_arrow1.setVisibility(View.GONE);
		iv_up_arrow2.setVisibility(View.GONE);
		iv_up_arrow3.setVisibility(View.GONE);
		lvOneLevelListView.setVisibility(View.GONE);
		llTwoLevelMenu.setVisibility(View.GONE);
		llScore.setVisibility(View.GONE);
		switch (mCurSubTitle) {
		case 1:
			iv_up_arrow1.setVisibility(View.VISIBLE);
			lvOneLevelListView.setVisibility(View.VISIBLE);
			lvOneLevelListView.setAdapter(provinceAdapter);
			break;
		case 2:
			iv_up_arrow2.setVisibility(View.VISIBLE);
			llTwoLevelMenu.setVisibility(View.VISIBLE);
			lvOneLevelListView.setAdapter(provinceAdapter);
			lvFirstLevel.setAdapter(firstLevelAdapter);
			lvSecondLevel.setAdapter(secondLevelAdapter);
			break;
		case 3:
			iv_up_arrow3.setVisibility(View.VISIBLE);
			llScore.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	private void initData(){
		list_province = FinalDataUitl.getAllProvice();
		listFirstLevelMajor = FinalDataUitl.getFirstLevelMajorList();
		listSecondLevelMajor = FinalDataUitl.getSecondLevelMajorList(0) ;
		provinceAdapter = new ExpandMenuAdapter(getActivity(), list_province);
		firstLevelAdapter = new ExpandMenuAdapter(getActivity(), listFirstLevelMajor);
		secondLevelAdapter = new ExpandMenuAdapter(getActivity(),listSecondLevelMajor);
		if( null == mController) mController = HttpController.getInstance(getActivity());
		if (null == dbManager) dbManager = DBManager.getInstance(getActivity());
		mHandler = new Handler(){  
			  
		    @Override  
		    public void handleMessage(final Message msg) {  
		        super.handleMessage(msg);  
		        dissmissProgressDialog();			       
		        if(lvSchoolProfile!=null)
		        	lvSchoolProfile.onRefreshComplete();
		        if(!isTaskCancled){
			        switch (msg.what) {  
			        
			        case -1://网络问题
			        	UtilToast.showShort(getActivity(), R.string.net_cannot_used);
			        case 0://加载schoolList->fail 
			        	//UtilToast.showLong(getActivity(), "fail");
			        	break;	
			        case 1:  //加载schoolList->success
			        	//UtilToast.showLong(getActivity(), "success");		       
			        	mAdapter.setDataList(schoolList);
			            break;
			        case 2://加载schoolList-> 空
			        	if(0 == loadOperation)
			        		UtilToast.showShort(getActivity(), "查询结果为空");
			        	else 
			        		UtilToast.showShort(getActivity(), "没有更多");
			        	break;
			        default:  
			            break;  
			        } 
		        }
		    }
		};
	}

	@Override
	public void onSchoolListObtained(boolean isSuccess, List<SchoolItem> result) {
		if(isSuccess) {
			if(result.size() == 0){
				mHandler.sendEmptyMessage(2);
			}
			else{						
				mController.getSchoolProfileImages(this, result);
			}
		}
		else 
			mHandler.sendEmptyMessage(0);
		
	}

	@Override
	public void OnImageInitialized(Object result) {
		schoolList.addAll((List<SchoolItem>) result);
		beginAt = beginAt+ ((List<SchoolItem>) result).size();
		mHandler.sendEmptyMessage(1);
	}

	@Override
	public void onCheckForUpdateObtained(boolean update, JSONObject result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
//		if(!EnvUtils.isNetworkConnected(getActivity())){
//			Message msg = mHandler.obtainMessage(-1);
//			msg.sendToTarget();
//			return;
//		}
		getMoreData();
	}

	@Override
	public void getData() {
		//从本地拿10个大学的数据
		schoolList = dbManager.getSchoolItems();
		try {
			for(int i = 0;i<imgs.length;i++){
				schoolList.get(i).setBitmap(BitmapFactory
						.decodeResource(getResources(),imgs[i]));
			}
		} catch (Exception e) {			
			Log.e("TAG","count of item less than count of imgs");
		}
		beginAt = 10;//在没联网的情况下先加载10条本地的出来
		mAdapter = new MainActivityListAdapter(getActivity(), schoolList);
		actualListView.setAdapter(mAdapter);
	}

	@Override
	protected void getMoreData() {
		if(!EnvUtils.isNetworkConnected(getActivity())){
			Message msg = mHandler.obtainMessage(-1);
			msg.sendToTarget();
			return;
		}
		isTaskCancled = false;
		loadOperation = 1;
		lvSchoolProfile.getLoadingLayoutProxy().setLastUpdatedLabel("正在加载。。。。");
		getSchoolItemsFromServer();
	}

	@Override
	public void doSearch() {
		if(!EnvUtils.isNetworkConnected(getActivity())){
			UtilToast.showShort(getActivity(), R.string.net_cannot_used);
			return;
		}
		showProgressDialog();
		isTaskCancled = false;
		loadOperation = 0;
		beginAt = 0;
		if( null != schoolList) schoolList.clear();
		mAdapter.setDataList(schoolList);
		getSchoolItemsFromServer();
	}

	@Override
	public void setFilterParam() {
		filter_params = new JSONObject();
		try {
			filter_params.put("province", this.province);
			filter_params.put("firstLevelMajor",this.firstLevelMajor);
			filter_params.put("secondLevelMajor", this.secondLevelMajor);
			filter_params.put("lowScore",this.lowScore);
			filter_params.put("highScore",this.highScore);
			filter_params.put("keyword", this.keyword);
			filter_params.put("beginAt", this.beginAt);
			Log.i("TAG","fileterParam="+filter_params.toString());
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @param 
	 * more=1 下拉加载更多
	 * **/
	private void getSchoolItemsFromServer(){
		
		setFilterParam();
		mController.getSchoolProfileList(getActivity(), this, filter_params);			
	}

	/**
	 * 按下返回键
	 * **/
	@Override
	public boolean onReturnAction() {
		if( 0 != mCurSubTitle){
			mCurSubTitle = 0;
			initSubtitleMenu();
			return true;
		}
		else if((mLoadingView!=null && mLoadingView.getVisibility() == View.VISIBLE) ||
				lvSchoolProfile.isRefreshing()){
			//在加载数据
			isTaskCancled = true;
			Message msg = mHandler.obtainMessage(-1);
			msg.sendToTarget();
			return true;
		}
		else
			return false;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		mCurSubTitle = 0;
		initSubtitleMenu();
		super.onStop();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		try {
			this.keyword = new String(s.toString().trim().getBytes(),"UTF-8");
			if(!("".equals(s.toString().trim()))){
				//this.keyword = new String(s.toString().trim().getBytes(),"UTF-8");
				iv_cancle_search.setVisibility(View.VISIBLE);
				tv_search.setSelected(true);
			}
			else{
				this.keyword = "";
				iv_cancle_search.setVisibility(View.GONE);
				tv_search.setSelected(false);
				doSearch();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent == actualListView){
			//下拉菜单未收缩
			if(mCurSubTitle!=0){
				mCurSubTitle = 0;
				initSubtitleMenu();
				return;
			}
			if(!EnvUtils.isNetworkConnected(getActivity())){
				UtilToast.showShort(getActivity(), R.string.net_cannot_used);
				return;
			}
			Intent intent = new Intent(getActivity(),SchoolDetailActivity.class);
			intent.putExtra("schoolID", ((SchoolItem)mAdapter.getItem(position-1)).getId());
			startActivity(intent);
		}
		else if(parent == lvOneLevelListView){
			province = position+"";
			tv_sub_title1.setText(list_province.get(position));	
			provinceAdapter.setItemSelect(position, true);
			doSearch();
		}
		else if(parent == lvFirstLevel){
			firstLevelMajor = position + "";
			tv_sub_title2.setText(listFirstLevelMajor.get(position));
			firstLevelAdapter.setItemSelect(position, true);
			listSecondLevelMajor = FinalDataUitl.getSecondLevelMajorList(position);
			secondLevelAdapter = new ExpandMenuAdapter(getActivity(), listSecondLevelMajor);
			lvSecondLevel.setAdapter(secondLevelAdapter);
			return;
		}
		else if(parent == lvSecondLevel){
			secondLevelMajor = position + "";
			tv_sub_title2.setText(listSecondLevelMajor.get(position));
			secondLevelAdapter.setItemSelect(position, true);
			doSearch();
		}
		mCurSubTitle = 0;
		initSubtitleMenu();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sub_title_lable1:
			mCurSubTitle = mCurSubTitle == 1?0:1;
			initSubtitleMenu();
			break;
		case(R.id.sub_title_lable2):
			mCurSubTitle = mCurSubTitle == 2?0:2;
			initSubtitleMenu();
			break;
		case(R.id.sub_title_lable3):
			mCurSubTitle = mCurSubTitle == 3?0:3;
			initSubtitleMenu();			
			break;
		case R.id.iv_cancel_search:
			et_search.setText("");
			break;
		case R.id.iv_search:
			if("".equals(keyword))
				UtilToast.showShort(getActivity(), "请输入关键字");			
			else
				doSearch();
			break;
		case R.id.tv_score_commit:
				if(TextUtils.isEmpty(etLowScore.getText().toString()) || TextUtils.isEmpty(etLowScore.getText().toString())){
					UtilToast.showLong(getActivity(), R.string.score_hint);
				}
				else{
					lowScore = Integer.parseInt(etLowScore.getText().toString().trim());
					highScore = Integer.parseInt(etHighScore.getText().toString().trim());
					mCurSubTitle = 0;
					initSubtitleMenu();
					doSearch();					
				}
			break;
		default:
			break;
		}
		
	}

}
