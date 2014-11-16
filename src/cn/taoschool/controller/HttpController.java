package cn.taoschool.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import android.R.integer;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.util.Log;

import cn.taoschool.R;
import cn.taoschool.taoSchoolApplication;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.cache.AsyncImageLoader;
import cn.taoschool.cache.AsyncImageLoader.ImageCallback;
import cn.taoschool.listener.IDetailActivityFragmentListener;
import cn.taoschool.listener.IMainActivityReqListener;
import cn.taoschool.pulltorefresh.library.internal.Utils;
import cn.taoschool.util.Constants;
import cn.taoschool.util.UtilToast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

public class HttpController {
	private static final String TAG = HttpController.class.getSimpleName();

	public static Context ctx;
	public static String url_school_profile = "schoollist";
	public static String url_school_detail = "getcolinf";
	
	private taoSchoolApplication app;
	private static HttpController mController;
	private AsyncHttpClient mClient = null;	
	private List<SchoolItem> schoolList;
	private AsyncImageLoader loader; 
	private static int downloadImgCount = 0;
	
	
	public static HttpController getInstance(Context context) {
		if(null == ctx )ctx = context;
		if (null == mController) {
			synchronized (HttpController.class) {
				if (null == mController) {
					mController = new HttpController();
				}
			}
		}
		return mController;
	}
	

	public AsyncHttpClient getHttpClient() {
		if (null == mClient) {
			mClient = new AsyncHttpClient();
		}
		return mClient;
	}
	
	
	
	
	public void getSchoolProfileList(final Context context, final IMainActivityReqListener listener,JSONObject params) {	
		
		String url = Constants.SERVER_IP + url_school_profile;
		Log.i(TAG,url);
		JsonObjectRequest getSchoolProfileReq = new JsonObjectRequest(Method.POST, url,
                params,new Listener<JSONObject>() {
			
					private void processFailed() {
						if(null != listener) 
						listener.onSchoolListObtained(false, null);
						return;
					}
					@Override
					public void onResponse(JSONObject response) {
						if (null != response) {		
							if(response.optInt("respCode") == 200){
								//int length = response.optInt("listLength");	
								schoolList = new ArrayList<SchoolItem>();
								JSONArray data = response.optJSONArray("schoolList");
								int length = 0;
								if(data!=null) { length = data.length();}
								else {
									if(listener!=null)
										listener.onSchoolListObtained(true, (ArrayList<SchoolItem>) schoolList);
									return;
								}
								for(int i = 0;i<length;i++){
									SchoolItem item = new SchoolItem();
									JSONObject obj = data.optJSONObject(i);
									try {
										item.setName(obj.getString("colname"));
										item.setYXLX(obj.getInt("yxlxid"));
										item.setBXLX(obj.getInt("bxlxid"));
										item.setXLCC(obj.getInt("xlccid"));
										item.setId(obj.getInt("colid"));
										item.setTel(obj.getString("zbtel"));
										item.setAddress(obj.getString("coladdress"));
										item.setMail(obj.getString("zbmail"));
										item.setWeb( obj.getString("colweb"));
										item.setIs211(obj.getBoolean("is211"));
										item.setIs985( obj.getBoolean("is985"));
										schoolList.add(item);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										processFailed();
									}								
								}
								listener.onSchoolListObtained(true, schoolList);
							}
							else{//404 schoolList==null
								processFailed();
								return;
							}
						}
						else{
							processFailed();
							return;
						}
					}
				},new ErrorListener(){

					@Override
					public void onErrorResponse(VolleyError response) {
						// TODO Auto-generated method stub						
						Log.d(TAG,  response.toString());
						if(null != listener) 
							listener.onSchoolListObtained(false, null);
						return;
					}
				});
		RequestManager.getInstance(context.getApplicationContext())
        .addToRequestQueue(getSchoolProfileReq, TAG);
		
	}
		
	public void getSchoolProfileImages(final IMainActivityReqListener listener,final List<SchoolItem> list){
		
		if( null == loader ){
			loader = new AsyncImageLoader(ctx);
			//将图片缓存至外部文件中  
		    loader.setCache2File(true); //false  
		    //设置外部缓存文件夹  
		    loader.setCachedDir(ctx.getCacheDir().getAbsolutePath());
		}
		downloadImgCount = 0;
		for(int i = 0;i<list.size();i++){
			int id = list.get(i).getId();
			String imgUrl = Constants.PROFILE_IMG_PATH + id + ".jpg";
			//下载图片，第二个参数是否缓存至内存中  
			loader.downloadImage(imgUrl, false,new ImageCallback() {
				@Override
				public void onImageLoaded(Bitmap bitmap, String imageUrl) {
					// TODO Auto-generated method stub
					downloadImgCount++;
					if(bitmap != null){ 
			    		String id =  imageUrl.substring(imageUrl.trim().lastIndexOf("/")+1, imageUrl.trim().indexOf(".jpg"));
			    		Log.i("TAG","id="+id);
			    		for(int j= 0;j<list.size();j++){
			        		if(id.equals(list.get(j).getId()+"")){
			        			list.get(j).setBitmap(bitmap);
			        			break;
			        		}
			        	}
			        }else{  
			            //下载失败，设置默认图片 
			        }
			        if(downloadImgCount==list.size()){
			        	Log.i("TAG","currentThread"+Thread.currentThread().getId());
			        	listener.OnImageInitialized(list);
			        }
				}
			}); 
		}
	}
	
	public void getSchoolDetail(Context ctx,final IDetailActivityFragmentListener listener,int schoolID){
		//mlistener = listener;
		
		String url = Constants.SERVER_IP + url_school_detail + "?colid=" + schoolID;
		JsonObjectRequest getSchoolDetailReq = new JsonObjectRequest(Method.GET, url, null, getDetailSuccess(listener), getDetailInfoFail(listener));
		RequestManager.getInstance(ctx.getApplicationContext())
        .addToRequestQueue(getSchoolDetailReq, TAG);
	}
	
	private Listener<JSONObject> getDetailSuccess(final IDetailActivityFragmentListener listener){
		Listener<JSONObject> successListener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject json) {
				// TODO Auto-generated method stub
				if(json!=null){
					int respCode = json.optInt("respCode");
					if(200 == respCode){
						try {
							JSONObject detailJsonObject = (JSONObject) json.opt("colinf");
							if(listener!=null){
								listener.OnGetDetailInfo(true, detailJsonObject);
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
							if(listener!=null){
								listener.OnGetDetailInfo(true, null);
								return;
							}
						}
						
					}
					else{
						if(listener != null ){
							listener.OnGetDetailInfo(false,null);
							return;
						}
					}
				}
				else{
					if(listener != null ){
						listener.OnGetDetailInfo(false,null);
						return;
					}
				}
			}
		};
		return successListener;
	}
	
	/**
	 * 获取详情页历年记录信息
	 * **/
	public void getDetailEnrollInfo(Context ctx,final IDetailActivityFragmentListener listener,Map param){
		
		String url = Constants.DETAIL_ENROLL_INFO + 
				"?province=" + param.get("province")+
				"&colid=" + param.get("colid")+
				"&klid=" + param.get("klid")+
				"&year=" + param.get("year")+
				"&pcid=" + param.get("pcid");
				
		JsonObjectRequest getDetailEnrollInfoReq = new JsonObjectRequest(Method.GET, url, null, getDetailEnrollInfoSuccess(listener), 
				getDetailInfoFail(listener));
		RequestManager.getInstance(ctx.getApplicationContext())
	    .addToRequestQueue(getDetailEnrollInfoReq, TAG);
	}
	
	private Listener<JSONObject> getDetailEnrollInfoSuccess(final IDetailActivityFragmentListener listener){
		Listener<JSONObject> successListener = new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject json) {
				// TODO Auto-generated method stub
				if(json!=null){
					int respCode = json.optInt("respCode");
					if(200 == respCode){
						try {
							JSONArray jsonList = json.optJSONArray("lqxxlist");
							if(listener!=null){
								listener.OnGetDetailInfo(true, jsonList);
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
							if(listener!=null){
								listener.OnGetDetailInfo(true, null);
								return;
							}
						}
						
					}
					else{
						if(listener != null ){
							listener.OnGetDetailInfo(false,null);
							return;
						}
					}
				}
				else{
					if(listener != null ){
						listener.OnGetDetailInfo(false,null);
						return;
					}
				}
			}
		};
		return successListener;
	}


	
	/**
	 * 获取详情页专业录取信息
	 * **/
public void getDetailMajorEnroll(Context ctx,final IDetailActivityFragmentListener listener,Map param){
		
		String url = Constants.DETAIL_MAJOR_ENROLL + 
				"?province=" + param.get("province")+
				"&colid=" + param.get("colid")+
				"&klid=" + param.get("klid")+
				"&year=" + param.get("year");
				
		JsonObjectRequest getDetailMajorEnrollReq = new JsonObjectRequest(Method.GET, url, null, getDetailMajorEnrollSuccess(listener), getDetailInfoFail(listener));
		RequestManager.getInstance(ctx.getApplicationContext())
	    .addToRequestQueue(getDetailMajorEnrollReq, TAG);
	}

private Listener<JSONObject> getDetailMajorEnrollSuccess(final IDetailActivityFragmentListener listener){
	Listener<JSONObject> successListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject json) {
			// TODO Auto-generated method stub
			if(json!=null){
				int respCode = json.optInt("respCode");
				if(200 == respCode){
					try {
						JSONArray jsonList = json.optJSONArray("zylqxxlist");
						if(listener!=null){
							listener.OnGetDetailInfo(true, jsonList);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						if(listener!=null){
							listener.OnGetDetailInfo(true, null);
							return;
						}
					}
					
				}
				else{
					if(listener != null ){
						listener.OnGetDetailInfo(false,null);
						return;
					}
				}
			}
			else{
				if(listener != null ){
					listener.OnGetDetailInfo(false,null);
					return;
				}
			}
		}
	};
	return successListener;
}


/**
 * 获取详情招生计划信息
 * **/
public void getDetailEnrollPlan(Context ctx,final IDetailActivityFragmentListener listener,Map param){
	
	String url = Constants.DETAIL_ENROLL_PLAN + 
			"?province=" + param.get("province")+
			"&colid=" + param.get("colid")+
			"&year=" + param.get("year");
			
	JsonObjectRequest getDetailEnrollPlanReq = new JsonObjectRequest(Method.GET, url, null, getDetailEnrollPlanSuccess(listener), getDetailInfoFail(listener));
	RequestManager.getInstance(ctx.getApplicationContext())
    .addToRequestQueue(getDetailEnrollPlanReq, TAG);
}

private Listener<JSONObject> getDetailEnrollPlanSuccess(final IDetailActivityFragmentListener listener){
	Listener<JSONObject> successListener = new Listener<JSONObject>() {

		@Override
		public void onResponse(JSONObject json) {
			// TODO Auto-generated method stub
			if(json!=null){
				int respCode = json.optInt("respCode");
				if(200 == respCode){
					try {
						JSONArray jsonList = json.optJSONArray("zsjhlist");
						if(listener!=null){
							listener.OnGetDetailInfo(true, jsonList);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						if(listener!=null){
							listener.OnGetDetailInfo(true, null);
							return;
						}
					}
					
				}
				else{
					if(listener != null ){
						listener.OnGetDetailInfo(false,null);
						return;
					}
				}
			}
			else{
				if(listener != null ){
					listener.OnGetDetailInfo(false,null);
					return;
				}
			}
		}
	};
	return successListener;
}


private ErrorListener getDetailInfoFail(final IDetailActivityFragmentListener listener){
	ErrorListener failListener = new ErrorListener(){

		@Override
		public void onErrorResponse(VolleyError response) {
			// TODO Auto-generated method stub
			if(null != listener){
				listener.OnGetDetailInfo(false, null);
				Log.d(TAG,response.toString());
				return;
			}
		}
		
	};
	return failListener;			
}
	
	
	public void checkVersion(final Context context, final IMainActivityReqListener listener){
		
		String url = Constants.DOWNLOAD_PATH;
		JsonObjectRequest checkReq = new JsonObjectRequest(url,null, 
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject result) {
						// TODO Auto-generated method stub
						if(null != result){
							if(200 == result.optInt("respCode")){
								JSONObject apiObject = (JSONObject) result.opt("latestapi");
								listener.onCheckForUpdateObtained(checkForUpdate(apiObject), apiObject);									
							}
							else
								listener.onCheckForUpdateObtained(false, null);							
						}
						else{
							listener.onCheckForUpdateObtained(false, null);
						}
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						
					}
				});
		
		RequestManager.getInstance(ctx.getApplicationContext())
        .addToRequestQueue(checkReq, TAG);
	}
	
	private boolean checkForUpdate(JSONObject result){
		boolean update = false;
		String serverVersionName = result.optString("versionname").substring(2, result.optString("versionname").length());
		String[] serverVersion = serverVersionName.split("\\.");
		String[] localVersion = getVersionCode(ctx).split("\\.");
		Log.i(TAG,"serverVersion  "+serverVersionName);
		Log.i(TAG,"localVersion  "+getVersionCode(ctx));
		int length = serverVersion.length<localVersion.length?serverVersion.length:localVersion.length;
		for(int i = 0;i<length;i++){
			if(Integer.parseInt(localVersion[i])<Integer.parseInt(serverVersion[i])){
				update = true;
			}
		}
		
		return update;
	}
	
	
	/** 
     * 获取软件版本号 
     *  
     * @param context 
     * @return 
     */  
    public static  String getVersionCode(Context context)  
    {  
        String versionCode = "";  
        try  
        {  
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode  
            versionCode = context.getPackageManager().getPackageInfo("cn.taoschool", 0).versionName;  
        } catch (NameNotFoundException e)  
        {  
            e.printStackTrace();  
        }  
        return versionCode;  
    } 
}
