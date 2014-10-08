package cn.taoschool.controller;

import java.io.File;
import java.net.URI;

import cn.taoschool.R;
import cn.taoschool.ui.MainActivity;
import cn.taoschool.util.Constants;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class DownLoadService extends Service{

	private static final String TAG = DownLoadService.class.getName();
	//文件存储
//    private File updateDir = null;
//    private File updateFile = null;
	private String filePath = null;
	private DownLoadAPKThread downLoadAPKThread;
    //通知栏
    private DownLoadNotification downLoadNotification = null;
    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;
    private String downLoadAPKUrl = null;
    private Handler updateHandler = new Handler(){
    	 @Override
         public void handleMessage(Message msg) {
             switch(msg.what){
                 case DownLoadAPKThread.DOWNLOAD_COMPLETE:
                     //点击安装PendingIntent
                	 
                      Uri uri = Uri.fromFile(downLoadAPKThread.getApkFile());
                      Intent installIntent = new Intent(Intent.ACTION_VIEW);
                      installIntent.setDataAndType(uri, "application/vnd.android.package-archive");                                     
                      installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      //updatePendingIntent = PendingIntent.getActivity(DownLoadService.this, 0, installIntent, 0);
                      //downLoadNotification.changeContentIntent(updatePendingIntent);
                      //downLoadNotification.notification.defaults=Notification.DEFAULT_SOUND;//铃声提醒                    
                      //downLoadNotification.changeNotificationText("下载完成，请点击安装！");
                      downLoadNotification.removeNotification();
                      startActivity(installIntent);
                        
                      //停止服务
                      //myNotification.removeNotification();
                     stopSelf();
                     break;
                 case DownLoadAPKThread.DOWNLOAD_FAIL:
                     //下载失败
                     //myNotification.changeProgressStatus(DOWNLOAD_FAIL);  
                	 downLoadNotification.changeNotificationText("文件下载失败！");
                     stopSelf();
                     break;
                 default:  //下载中
                     Log.i(TAG, "default"+msg.what);
         //          myNotification.changeNotificationText(msg.what+"%");
                     downLoadNotification.changeProgressStatus(msg.what);  
             }
         }
    };
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.i(TAG,"onCreate");
        super.onCreate();
    }
 
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i(TAG,"onDestroy");
        if(downLoadAPKThread!=null)
        downLoadAPKThread.interuptThread();
        stopSelf();
        super.onDestroy();
    }
    
    @SuppressWarnings("deprecation")
	@Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        Log.i(TAG,"onStart");
        super.onStart(intent, startId);
    }
    
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i(TAG,"onStartCommand");
        downLoadAPKUrl = intent.getStringExtra("downLoadAPKUrl");
        filePath = getFilesDir() +"/" + Constants.APP_NAME;
		Log.i(TAG,"filePath+"+filePath);

        updateIntent = new Intent(this, MainActivity.class);
        updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        downLoadNotification=new DownLoadNotification(this, updatePendingIntent, 1);
        //myNotification.showDefaultNotification(R.drawable.ic_launcher, "测试", "开始下载");
        downLoadNotification.showCustomizeNotification(R.drawable.ic_launcher, "测试下载", R.layout.download_notify_item);

        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        downLoadAPKThread = new  DownLoadAPKThread(this,updateHandler,downLoadAPKUrl,filePath);
        new Thread(downLoadAPKThread).start();
         
        return super.onStartCommand(intent, flags, startId);
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
