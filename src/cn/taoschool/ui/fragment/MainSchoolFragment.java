package cn.taoschool.ui.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
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
import cn.taoschool.pulltorefresh.library.PullToRefreshBase.Mode;
import cn.taoschool.pulltorefresh.library.PullToRefreshListView;
import cn.taoschool.ui.SchoolDetailActivity;
import cn.taoschool.util.FinalDataUitl;
import cn.taoschool.util.UtilToast;

public class MainSchoolFragment extends BasicMainFragment implements TextWatcher,
OnClickListener,OnItemClickListener{

	private PullToRefreshListView lvSchoolProfile;
	private ListView actualListView;
	private LinearLayout sub_title1,sub_title2,sub_title3,sub_title4;
	private TextView tv_sub_title1,tv_sub_title2,tv_sub_title3,tv_sub_title4;
	private ListView llMenuListView;
	private ImageView iv_up_arrow1,iv_up_arrow2,iv_up_arrow3,iv_up_arrow4;
	private EditText et_search;
	private ImageView iv_cancle_search;
	private TextView tv_search;

	
	
	private DBManager dbManager;
	private HttpController mController;
	private EnvChecker envChecker;
	private int downloadImgCount;//已下载图片数
	private final int[] imgs = {R.drawable.img1,R.drawable.img2,
			R.drawable.img3,R.drawable.img4,R.drawable.img5,
			R.drawable.img6,R.drawable.img7,R.drawable.img8,
			R.drawable.img9,R.drawable.img10};
	
	private String province = "0"; //省的帅选条件
	private String yxlxid = "0";
	private String bxlxid = "0";
	private String xlccid = "0";
	private String keyword = "";
	
	
	private int beginAt;
	private int loadOperation;//0->重新查询，1->加载更多
	
	private List<SchoolItem> schoolList;
	private MainActivityListAdapter mAdapter;
	private int mCurSubTitle = 0;//当前的那个选项Subtitle,0代表全都隐藏
	private List<String> list_province;
	private List<String> list_xylx;
	private List<String> list_bxlx;
	private List<String> list_xlcc;
	private ExpandMenuAdapter provinceAdapter;
	private ExpandMenuAdapter yxlxAdapter;
	private ExpandMenuAdapter bxlxAdapter;
	private ExpandMenuAdapter xlccAdapter;
	private Handler mHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("TAG","MainSchoolFragment onCreateView");
		View parentView = View.inflate(getActivity(),R.layout.main_viewpager1, null);
		initView(parentView);
		initData();
		initSubtitleMenu();
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
				
		sub_title1 = (LinearLayout) parentView.findViewById(R.id.sub_title_lable1);
		sub_title2 = (LinearLayout) parentView.findViewById(R.id.sub_title_lable2);
		sub_title3 = (LinearLayout) parentView.findViewById(R.id.sub_title_lable3);
		sub_title4 = (LinearLayout) parentView.findViewById(R.id.sub_title_lable4);
		tv_sub_title1 = (TextView) parentView.findViewById(R.id.tv_sub_title1);
		tv_sub_title2 = (TextView) parentView.findViewById(R.id.tv_sub_title2);
		tv_sub_title3 = (TextView) parentView.findViewById(R.id.tv_sub_title3);
		tv_sub_title4 = (TextView) parentView.findViewById(R.id.tv_sub_title4);
		llMenuListView = (ListView) parentView.findViewById(R.id.lv_menulist);
		iv_up_arrow1 = (ImageView)parentView.findViewById(R.id.iv_up_arrow1);
		iv_up_arrow2 = (ImageView)parentView.findViewById(R.id.iv_up_arrow2);
		iv_up_arrow3 = (ImageView)parentView.findViewById(R.id.iv_up_arrow3);
		iv_up_arrow4 = (ImageView)parentView.findViewById(R.id.iv_up_arrow4);
		et_search.addTextChangedListener(this);
		llMenuListView.setOnItemClickListener(this);
		actualListView.setOnItemClickListener(this);
		sub_title1.setOnClickListener(this);
		sub_title2.setOnClickListener(this);
		sub_title3.setOnClickListener(this);
		sub_title4.setOnClickListener(this);
		tv_search.setOnClickListener(this);
		iv_cancle_search.setOnClickListener(this);
	}
	
	private void initSubtitleMenu(){
		iv_up_arrow1.setVisibility(View.GONE);
		iv_up_arrow2.setVisibility(View.GONE);
		iv_up_arrow3.setVisibility(View.GONE);
		iv_up_arrow4.setVisibility(View.GONE);
		if( 0 == mCurSubTitle) llMenuListView.setVisibility(View.GONE);
		else llMenuListView.setVisibility(View.VISIBLE);
		
		if(mCurSubTitle == 1){
			iv_up_arrow1.setVisibility(View.VISIBLE);
			llMenuListView.setAdapter(provinceAdapter);
		}
		if(mCurSubTitle == 2){
			iv_up_arrow2.setVisibility(View.VISIBLE);
			llMenuListView.setAdapter(yxlxAdapter);
		}
		if(mCurSubTitle == 3){
			iv_up_arrow3.setVisibility(View.VISIBLE);
			llMenuListView.setAdapter(bxlxAdapter);
		}
		if(mCurSubTitle == 4){
			iv_up_arrow4.setVisibility(View.VISIBLE);
			llMenuListView.setAdapter(xlccAdapter);
		}
	}
	
	private void initData(){
		mHandler = new Handler(){  
			  
		    @Override  
		    public void handleMessage(final Message msg) {  
		        super.handleMessage(msg);  
		        dissmissProgressDialog();			       
		        if(lvSchoolProfile!=null)
		        	lvSchoolProfile.onRefreshComplete();
		        
		        switch (msg.what) {  
		        case 0://加载schoolList->fail 
		        	UtilToast.showLong(getActivity(), "fail");
		        	break;	
		        case 1:  //加载schoolList->success
		        	UtilToast.showLong(getActivity(), "success");		       
		        	mAdapter.setDataList(schoolList);
		            break;
		        case 2://加载schoolList-> 空
		        	if(0 == loadOperation)
		        		UtilToast.showLong(getActivity(), "没有更多");
		        	else 
		        		UtilToast.showLong(getActivity(), "查询结果为空");
		        	break;
		        default:  
		            break;  
		        } 
		    }
		};
		if( null == mController) mController = HttpController.getInstance(getActivity());
		if (null == envChecker ) envChecker = EnvChecker.getInstance(getActivity());
		if (null == dbManager) dbManager = DBManager.getInstance(getActivity());	
		list_province = FinalDataUitl.getAllProvice();
		list_xylx = FinalDataUitl.getAllXYLX();
		list_bxlx = FinalDataUitl.getAllBXLX();
		list_xlcc =  FinalDataUitl.getAllXLCC();		
		provinceAdapter = new ExpandMenuAdapter(getActivity(),list_province );
		yxlxAdapter = new ExpandMenuAdapter(getActivity(),list_xylx );
		bxlxAdapter = new ExpandMenuAdapter(getActivity(),list_bxlx );
		xlccAdapter = new ExpandMenuAdapter(getActivity(), list_xlcc);
	}
	
	
	@Override
	public void getData() {
		if(envChecker.isFirstInstalled()){
			Log.i("TAG","first installed");
			dbManager.InitTabel();
		}
		//从本地拿10个大学的数据
		schoolList = dbManager.getSchoolItems();
		try {
			for(int i = 0;i<imgs.length;i++){
				schoolList.get(i).setBitmap(BitmapFactory
						.decodeResource(getResources(),imgs[i]));
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("TAG","count of item less than count of imgs");
		}
		beginAt = 10;//在没联网的情况下先加载10条本地的出来
		mAdapter = new MainActivityListAdapter(getActivity(), schoolList);
		actualListView.setAdapter(mAdapter);
	}

	@Override
	protected void getMoreData() {
		loadOperation = 1;
		lvSchoolProfile.getLoadingLayoutProxy().setLastUpdatedLabel("正在加载。。。。");
		getSchoolItemsFromServer();
	}

	@Override
	public void doSearch() {
		loadOperation = 0;
		beginAt = 0;
		if( null != schoolList) schoolList.clear();
		mAdapter.setDataList(schoolList);
		getSchoolItemsFromServer();
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
	public void onRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		getMoreData();
	}

	@Override
	public void onCheckForUpdateObtained(boolean update, JSONObject result) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param 
	 * more=1 下拉加载更多
	 * **/
	private void getSchoolItemsFromServer(){
		showProgressDialog("请求中...");
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
		else
			return false;
	}
	

	@Override
	public void setFilterParam() {
		filter_params = new JSONObject();
		try {
			filter_params.put("province", this.province);
			filter_params.put("yxlxid",this.yxlxid);
			filter_params.put("bxlxid", this.bxlxid);
			filter_params.put("xlcxid",this.xlccid);
			filter_params.put("keyword", this.keyword);
			filter_params.put("beginAt", this.beginAt);
			Log.i("TAG","fileterParam="+filter_params.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			if(!("".equals(s.toString().trim()))){
				this.keyword = new String(s.toString().trim().getBytes(),"UTF-8");
				iv_cancle_search.setVisibility(View.VISIBLE);
				tv_search.setSelected(true);
			}
			else{
				this.keyword = "";
				iv_cancle_search.setVisibility(View.GONE);
				tv_search.setSelected(false);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent == actualListView){
			if(mCurSubTitle!=0){
				mCurSubTitle = 0;
				initSubtitleMenu();
				return;
			}
			Intent intent = new Intent(getActivity(),SchoolDetailActivity.class);
			intent.putExtra("schoolID", ((SchoolItem)mAdapter.getItem(position-1)).getId());
			startActivity(intent);
		}
		else{			
			switch(mCurSubTitle){
			case 1:
				province = position+"";
				tv_sub_title1.setText(list_province.get(position));	
				provinceAdapter.setItemSelect(position, true);
				doSearch();
				break;
			case 2:
				yxlxid = position+"";
				tv_sub_title2.setText(list_xylx.get(position));
				yxlxAdapter.setItemSelect(position, true);
				doSearch();
				break;
			case 3:
				bxlxid = position+"";
				tv_sub_title3.setText(list_bxlx.get(position));
				bxlxAdapter.setItemSelect(position, true);
				doSearch();
				break;
			case 4:
				xlccid = position+"";
				tv_sub_title4.setText(list_xlcc.get(position));
				xlccAdapter.setItemSelect(position, true);
				doSearch();
				break;
			default :
				break;
			}
			mCurSubTitle = 0;
			initSubtitleMenu();
		}
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
		case(R.id.sub_title_lable4):
			mCurSubTitle = mCurSubTitle == 4?0:4;
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
		default:
			break;
		}
	}

}
