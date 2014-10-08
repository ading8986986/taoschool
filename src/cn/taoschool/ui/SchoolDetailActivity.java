package cn.taoschool.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import cn.taoschool.R;
import cn.taoschool.R.layout;
import cn.taoschool.adapter.DetailActivityViewPagerAdapter;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.cache.AsyncImageLoader;
import cn.taoschool.controller.HttpController;
import cn.taoschool.listener.IDetailActivityReqListener;
import cn.taoschool.listener.IMainActivityReqListener;
import cn.taoschool.ui.fragment.BasicDetailFragment;
import cn.taoschool.ui.fragment.DetailOfEnrollFragment;
import cn.taoschool.ui.fragment.DetailOfEnrollPlanFragment;
import cn.taoschool.ui.fragment.DetailOfMajorEnrollFragment;
import cn.taoschool.ui.fragment.DetailOfSchoolFragment;
import cn.taoschool.util.Constants;
import cn.taoschool.util.UtilToast;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class SchoolDetailActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener
,IDetailActivityReqListener{

	private String TAG = SchoolDetailActivity.class.getName();
	private int mCurPage = 0;// 当前页卡编号
	private int mSubTitle = 0;
	private List<BasicDetailFragment> mFragments;
	
	private TextView tv_schoolname;
	private ImageView iv_iscomfirm,iv_is985,iv_is211;
	private ImageView iv_big_image;
	private ViewPager mViewPager;
	private DetailActivityViewPagerAdapter mAdapter;
	private ImageView iv_back;
	private TextView tv_sub_title1,tv_sub_title2,tv_sub_title3,tv_sub_title4;
	private TextView tv_more;
	
	private DetailOfSchoolFragment detailOfSchoolProfileFragment;
	private AsyncImageLoader loader; 
	private Handler mHandler;
	private static int schoolID;
	private SchoolItem curSchoolItem;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_detail);
		initView();
		initData();
		initViewPager();
		updateSubTitleSel();
		
	}
	
	

	private void initView(){
		tv_schoolname = (TextView)findViewById(R.id.tv_schoolname);
		iv_iscomfirm = (ImageView)findViewById(R.id.iv_iscomfirm);
		iv_is985 = (ImageView)findViewById(R.id.iv_is985);
		iv_is211 = (ImageView)findViewById(R.id.iv_is211);
		iv_big_image = (ImageView)findViewById(R.id.iv_big_image);
		tv_sub_title1 = (TextView)findViewById(R.id.tv_sub_title1);
		tv_sub_title2 = (TextView)findViewById(R.id.tv_sub_title2);
		tv_sub_title3 = (TextView)findViewById(R.id.tv_sub_title3);
		tv_sub_title4 = (TextView)findViewById(R.id.tv_sub_title4);
		tv_more = (TextView)findViewById(R.id.tv_more);
		mViewPager = (ViewPager)findViewById(R.id.vp_content);
		iv_back = (ImageView)findViewById(R.id.iv_back);
		tv_sub_title1.setOnClickListener(this);
		tv_sub_title2.setOnClickListener(this);
		tv_sub_title3.setOnClickListener(this);
		tv_sub_title4.setOnClickListener(this);
		tv_more.setOnClickListener(this);
	}

	private void initData(){
		mHandler = new Handler(){
			 @SuppressLint("NewApi")
			@Override
		        public void handleMessage(Message msg) {
				if(msg.what == 0){//图片下载失败
					iv_big_image.setBackgroundResource(R.drawable.detail1);
				}
				else if(msg.what == 1){
					BitmapDrawable drawable = new BitmapDrawable(getResources(),(Bitmap) msg.obj);
					iv_big_image.setBackground(drawable);
				}
			 }
		};
		mCurPage = getIntent().getIntExtra("mCurPage",0);	
		schoolID = getIntent().getIntExtra("schoolID",0);
	}
	
	private void updateSubTitleSel(){
		tv_sub_title1.setTextColor(getResources().getColor(R.color.detail_activity_gray2));
		tv_sub_title2.setTextColor(getResources().getColor(R.color.detail_activity_gray2));
		tv_sub_title3.setTextColor(getResources().getColor(R.color.detail_activity_gray2));
		tv_sub_title4.setTextColor(getResources().getColor(R.color.detail_activity_gray2));
		if (mSubTitle == 0) {
			tv_sub_title1.setTextColor(getResources().getColor(R.color.detail_activity_green_more));
		} else if (mSubTitle == 1) {
			tv_sub_title2.setTextColor(getResources().getColor(R.color.detail_activity_green_more));
		} else if (mSubTitle == 2) {
			tv_sub_title3.setTextColor(getResources().getColor(R.color.detail_activity_green_more));
		}
		else if (mSubTitle == 3) {
			tv_sub_title4.setTextColor(getResources().getColor(R.color.detail_activity_green_more));
		}
	}
	
	public void initViewPager(){
		mFragments = new ArrayList<BasicDetailFragment>();
		DetailOfSchoolFragment fragmentDetail = new DetailOfSchoolFragment(this);
		DetailOfEnrollFragment fragmentEnrollHistory = new DetailOfEnrollFragment();
		DetailOfEnrollPlanFragment fragmentEnrollPlan = new DetailOfEnrollPlanFragment();
		DetailOfMajorEnrollFragment fragmentMajorEnroll = new DetailOfMajorEnrollFragment();
		mFragments.add(fragmentDetail);
		mFragments.add(fragmentEnrollPlan);
		mFragments.add(fragmentEnrollHistory);
		mFragments.add(fragmentMajorEnroll);
		mAdapter = new DetailActivityViewPagerAdapter(getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0); 
		mViewPager.setOnPageChangeListener(this);
		updateSubTitleSel();
	}
	
	public static int getSchoolID(){
		return schoolID;
	}
	
	private void getImages(String[] imageNames,int id){
		if( null == loader ){
			loader = new AsyncImageLoader(this);
			}
			//将图片缓存至外部文件中  
		    loader.setCache2File(false); //false  
		    //设置外部缓存文件夹  
		    loader.setCachedDir(getCacheDir().getAbsolutePath());
		    //String imgUrl = Constants.detailImgPath + schoolID + "/" + schoolID +".jpg";
		    String imgUrl = Constants.DETAIL_IMG_PATH + "/1.jpg";
		    Log.i("TAG",imgUrl);
		    loader.downloadImage(imgUrl, false, new AsyncImageLoader.ImageCallback() {
				
				@SuppressLint("NewApi")
				@Override
				public void onImageLoaded(Bitmap bitmap, String imageUrl) {
					// TODO Auto-generated method stub		
					/*int width = iv_big_image.getWidth();
					LayoutParams para = iv_big_image.getLayoutParams();
					para.height = width*250/640;
					iv_big_image.setLayoutParams(para);*/
					if(bitmap!=null){
						Message msg = mHandler.obtainMessage();
						msg.what = 1;
						msg.obj = bitmap;
						mHandler.sendMessage(msg);						
					}
					else{
						mHandler.sendEmptyMessage(0);
						iv_big_image.setBackgroundResource(R.drawable.detail1);
					}
				}
			});
		
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.tv_sub_title1:
				mSubTitle = 0;
				mCurPage = 0;
				updateSubTitleSel();
				mViewPager.setCurrentItem(0);
			break;
			case R.id.tv_sub_title2:
				mSubTitle = 1;
				mCurPage = 1;
				updateSubTitleSel();
				mViewPager.setCurrentItem(1);
			break;
			case R.id.tv_sub_title3:
				mSubTitle = 2;
				mCurPage = 2;
				updateSubTitleSel();
				mViewPager.setCurrentItem(2);
			break;
			case R.id.tv_sub_title4:
				mSubTitle = 3;
				mCurPage = 3;
				updateSubTitleSel();
				mViewPager.setCurrentItem(3);
			break;
			case R.id.tv_more:
			break;
			default:
			break;
		}
	}



	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPageSelected(int page) {
		mSubTitle = page;
    	updateSubTitleSel();
    	Log.i(TAG,"onpage:"+page);
		//mFragments.get(page).getData();
	}



	@Override
	public void OnGetImgUrl(String[] imgUrls) {
		// TODO Auto-generated method stub
		getImages(imgUrls,schoolID);
	}

	@Override
	public void OnGetDetailInfo(boolean isSuccess, Object result) {
		// TODO Auto-generated method stub
		tv_schoolname.setText(((JSONObject) result).optString("colname"));
		if(!((JSONObject) result).optBoolean("is211")) iv_is211.setVisibility(View.GONE);
		if(!((JSONObject) result).optBoolean("is985")) iv_is985.setVisibility(View.GONE);
		if(!((JSONObject) result).optBoolean("isyan")) iv_iscomfirm.setVisibility(View.GONE);
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
        	if(mFragments.get(mCurPage).onReturnAction())
        		return false;
        	else
        		return super.onKeyDown(keyCode, event);  
        } else  
            return super.onKeyDown(keyCode, event);  
    }




}
