package cn.taoschool.ui;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import cn.taoschool.adapter.MainViewPagerAdapter;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.controller.DownLoadAPKThread;
import cn.taoschool.controller.DownLoadNotification;
import cn.taoschool.controller.DownLoadService;
import cn.taoschool.controller.HttpController;
import cn.taoschool.ui.fragment.BasicMainFragment;
import cn.taoschool.ui.fragment.MainMajorFragment;
import cn.taoschool.ui.fragment.MainSchoolFragment;
import cn.taoschool.ui.fragment.MyTaoFragment;


import cn.taoschool.listener.IMainActivityReqListener;
import cn.taoschool.R;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements IMainActivityReqListener,
OnPageChangeListener,OnClickListener
{

	private final String TAG = MainActivity.class.getName();
	private ViewPager viewPager;
	private TextView schoolTitle;
	private TextView majorTitle;
	private TextView myTitle;
	private BasicMainFragment schoolFragment,majorFragment,myTaoFragment;//各个页卡 
	private List<BasicMainFragment> mFragments;
	private int mCurPage = 0;// 当前页卡编号

	private MainViewPagerAdapter adapter_viewpager;
	private ProgressDialog mProgressDialog;
	private HttpController mController;
	private Handler mHandler;
	private DownLoadNotification myNotification;
	//通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String filePath = this.getFilesDir().getAbsolutePath();
		Log.i(TAG,"filePath+"+filePath);
		initView();
		initData();
		checkForUpdate();
		initViewPager();						
	}
	
	
	private void initView(){
		schoolTitle = (TextView) findViewById(R.id.title_lable1);  
		majorTitle = (TextView) findViewById(R.id.title_lable2);  
		myTitle = (TextView) findViewById(R.id.title_lable3); 		
		schoolTitle.setOnClickListener(this);
		majorTitle.setOnClickListener(this);
		myTitle.setOnClickListener(this);
	}


	private void initViewPager() {  
	    viewPager=(ViewPager) findViewById(R.id.vp_content);  
	    mFragments=new ArrayList<BasicMainFragment>();  
	    schoolFragment = new MainSchoolFragment();
	    majorFragment = new MainMajorFragment();
	    myTaoFragment = new MyTaoFragment();
	    mFragments.add(schoolFragment);  
	    mFragments.add(majorFragment);  
	    mFragments.add(myTaoFragment);  
	    adapter_viewpager = new MainViewPagerAdapter(getSupportFragmentManager(),mFragments);
	    viewPager.setAdapter(adapter_viewpager);  
	    viewPager.setCurrentItem(0);  
	    viewPager.setOnPageChangeListener(this);  
	    updateTitleSel();	
	}


	private void initData(){
		mHandler = new Handler(){  
			    @Override  
			    public void handleMessage(final Message msg) {  
			        super.handleMessage(msg);  
			        dissmissProgressDialog();			       
			        
			        switch (msg.what) {  			   
			        case DownLoadAPKThread.DOWNLOAD_START://需要更新
			        	 final String downLoadAPKUrl = ((JSONObject)msg.obj).optString("versionurl");
			        	AlertDialog.Builder alBuilder = new AlertDialog.Builder(MainActivity.this);
			        	alBuilder.setTitle("软件升级")
			        	.setMessage("发现新版本，建议立即更新")
			        	.setPositiveButton("更新",  new DialogInterface.OnClickListener(){
	
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								//开启服务
					        	Intent intent = new Intent(MainActivity.this,DownLoadService.class);
					        	intent.putExtra("downLoadAPKUrl", downLoadAPKUrl);
					        	startService(intent);
					        	Log.i(TAG,"startService_in MainActicity");
							}})
			        	.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						})
			        	.create().show();				        
			        	break;
			     
			        default:  
			            break;  
			        }  
			    }  
			      
			};
		if( null == mController) mController = HttpController.getInstance(this);		
	}


	private void updateTitleSel(){
		schoolTitle.setSelected(false);
		majorTitle.setSelected(false);
		myTitle.setSelected(false);
		if (mCurPage == 0) {
			schoolTitle.setSelected(true);
		} else if (mCurPage == 1) {
			majorTitle.setSelected(true);
		} else if (mCurPage == 2) {
			myTitle.setSelected(true);
		}
	}
	
	private void checkForUpdate(){
		Log.i(TAG,"Current version:" + HttpController.getVersionCode(this));
		mController.checkVersion(this, this);
		/*JSONObject result  = new JSONObject();
		try {
			result.put("versionurl","http://218.249.164.245/tdxapi/apifile/v1-1.apk");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Message msg = mHandler.obtainMessage();
			msg.what = DownLoadAPKThread.DOWNLOAD_START;
			msg.obj = result;
			mHandler.sendMessage(msg);*/
	}
	
	
	/**
	 * 显示进度框
	 */
	private void showProgressDialog(String content) {
		if(mProgressDialog == null)
			mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage(content);
		mProgressDialog.show();
	}
	
	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
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
	public void onPageSelected(int CurPage) {
		// TODO Auto-generated method stub
		mCurPage = CurPage;
		updateTitleSel();
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


	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case(R.id.title_lable1):
				mCurPage = 0;
				updateTitleSel();	
				viewPager.setCurrentItem(0);
				break;
			case(R.id.title_lable2):
				mCurPage = 1;
				updateTitleSel();
				viewPager.setCurrentItem(1);
				break;
			case(R.id.title_lable3):
				mCurPage = 2;
				updateTitleSel();
				viewPager.setCurrentItem(2);
				break;
			default:
				break;
		}
		
	}
	


	@Override
	public void onCheckForUpdateObtained(boolean update, JSONObject result) {
		Log.i(TAG,update + "  ");		
		if(update&&result!=null){
			Log.i(TAG,update + "  "+result.toString());
			Message msg = mHandler.obtainMessage();
			msg.what = DownLoadAPKThread.DOWNLOAD_START;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
		
	}


	@Override
	public void onSchoolListObtained(boolean isSuccess, List<SchoolItem> result) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void OnImageInitialized(Object result) {
		// TODO Auto-generated method stub
		
	}
}
