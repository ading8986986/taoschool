package cn.taoschool.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.taoschool.R;
import cn.taoschool.R.layout;
import cn.taoschool.adapter.BigImageViewPagerAdapter;
import cn.taoschool.adapter.DetailActivityViewPagerAdapter;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.cache.AsyncImageLoader;
import cn.taoschool.controller.HttpController;
import cn.taoschool.listener.IDetailActivityFragmentListener;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class SchoolDetailActivity extends FragmentActivity implements OnClickListener, OnPageChangeListener
,IDetailActivityFragmentListener{

	private String TAG = SchoolDetailActivity.class.getName();
	private int mCurPage = 0;// 当前页卡编号
	private int mSubTitle = 0;
	private List<BasicDetailFragment> mFragments;
	
	private TextView tv_schoolname;
	private ImageView iv_iscomfirm,iv_is985,iv_is211;
	private ViewPager vpBigImages;
	private ViewPager mViewPager;
	private DetailActivityViewPagerAdapter mAdapter;
	private BigImageViewPagerAdapter mImageViewPagerAdapter;//顶部图像的image切换
	private ImageView iv_back;
	private TextView tv_sub_title1,tv_sub_title2,tv_sub_title3,tv_sub_title4;
	private TextView tv_more;
	private LinearLayout ImagebottomView;//
	
	//private AsyncImageLoader loader; 
	private Handler mHandler;
	private static int schoolID;
	private List<String> imageUrlList;
	
	private SchoolItem curSchoolItem;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_detail);
		initView();
		initData();
		initMainViewPager();
		updateSubTitleSel();
	}
	
	

	private void initView(){
		tv_schoolname = (TextView)findViewById(R.id.tv_schoolname);
		iv_iscomfirm = (ImageView)findViewById(R.id.iv_iscomfirm);
		iv_is985 = (ImageView)findViewById(R.id.iv_is985);
		iv_is211 = (ImageView)findViewById(R.id.iv_is211);
		vpBigImages = (ViewPager)findViewById(R.id.vp_big_img);
		ImagebottomView = (LinearLayout)findViewById(R.id.ll_bottom_circle);
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
		iv_back.setOnClickListener(this);
	}

	private void initData(){
		
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
	
	public void initTopImageViewPager(List<String> imageUrlList){
		//this.imageUrlList = imageUrlList;
		this.imageUrlList = new ArrayList<String>();
		String imgUrl1 = Constants.DETAIL_IMG_PATH + schoolID + "/" + 1 +".jpg";
		String imgUrl2 = Constants.DETAIL_IMG_PATH + schoolID + "/" + 2 +".jpg";
		String imgUrl3 = Constants.DETAIL_IMG_PATH + schoolID + "/" + 3 +".jpg";
		Log.i(TAG, "imgUrl1="+imgUrl1);
		this.imageUrlList.add(imgUrl1);
		this.imageUrlList.add(imgUrl2);
		this.imageUrlList.add(imgUrl3);
		mImageViewPagerAdapter = new BigImageViewPagerAdapter(this, this.imageUrlList, ImagebottomView);
		vpBigImages.setAdapter(mImageViewPagerAdapter);
		setBottomCircle(0);
		vpBigImages.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				setBottomCircle(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public void initMainViewPager(){
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
		mViewPager.setOffscreenPageLimit(0);
		updateSubTitleSel();
	}
	
	public static int getSchoolID(){
		return schoolID;
	}
	
	/**
	 * 处理小圆点
	 * **/
	public void setBottomCircle(int position){
		ImagebottomView.removeAllViews();
		if(imageUrlList.size() == 1){//大于1才需要小圆点示意切换 
			ImagebottomView.setVisibility(View.GONE);
		}
		else {
			for(int i = 0;i<imageUrlList.size();i++){
				ImageView circle = new ImageView(this);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				param.setMargins(5, 0, 5, 0);
				circle.setLayoutParams(param);
				circle.setBackgroundResource(R.drawable.bottom_circle);		
				if(i == position) circle.setSelected(true);
				ImagebottomView.addView(circle);
			}	
		}
	}
	
//	private void getImages(String[] imageNames,int id){
//		if( null == loader ){
//			loader = new AsyncImageLoader(this);
//			}
//			//将图片缓存至外部文件中  
//		    loader.setCache2File(false); //false  
//		    //设置外部缓存文件夹  
//		    loader.setCachedDir(getCacheDir().getAbsolutePath());
//		    //String imgUrl = Constants.detailImgPath + schoolID + "/" + schoolID +".jpg";
//		    String imgUrl = Constants.DETAIL_IMG_PATH + "/1.jpg";
//		    Log.i("TAG",imgUrl);
//		    loader.downloadImage(imgUrl, false, new AsyncImageLoader.ImageCallback() {
//				
//				@SuppressLint("NewApi")
//				@Override
//				public void onImageLoaded(Bitmap bitmap, String imageUrl) {
//					// TODO Auto-generated method stub		
//					/*int width = iv_big_image.getWidth();
//					LayoutParams para = iv_big_image.getLayoutParams();
//					para.height = width*250/640;
//					iv_big_image.setLayoutParams(para);*/
//					if(bitmap!=null){
//						Message msg = mHandler.obtainMessage();
//						msg.what = 1;
//						msg.obj = bitmap;
//						mHandler.sendMessage(msg);						
//					}
//					else{
//						mHandler.sendEmptyMessage(0);
//						iv_big_image.setBackgroundResource(R.drawable.detail1);
//					}
//				}
//			});
		
		
//	}

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
			case R.id.iv_back:
				if(!onReturnAction())
					finish();
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
	public void OnGetImgUrl(List<String> imgUrls) {
		// TODO Auto-generated method stub
		initTopImageViewPager(imgUrls);
	}

	@Override
	public void OnGetDetailInfo(boolean isSuccess, Object result) {
		if(null == result) return;

		tv_schoolname.setText(((JSONObject) result).optString("colname"));
		if(!((JSONObject) result).optBoolean("is211")) iv_is211.setVisibility(View.GONE);
		if(!((JSONObject) result).optBoolean("is985")) iv_is985.setVisibility(View.GONE);
		if(!((JSONObject) result).optBoolean("isyan")) iv_iscomfirm.setVisibility(View.GONE);
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
        	if(onReturnAction())
        		return true;
        	else
        		return super.onKeyDown(keyCode, event);  
        } else  
            return super.onKeyDown(keyCode, event);  
    }
	
	private boolean onReturnAction(){
		if(mFragments.get(mCurPage).onReturnAction())
    		return true;
		else
			return false;
	}




}
