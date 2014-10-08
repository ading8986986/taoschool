package cn.taoschool;

import android.app.Application;
public class taoSchoolApplication extends Application {
	public static taoSchoolApplication instance = null;
	public String serverIp = "http://218.249.164.245/tdxapi/";
	
	 @Override
	    public void onCreate() {
	        super.onCreate();
	        instance = this;
	    }
	 
	 public static taoSchoolApplication getApplication() {
	        if (instance == null) {
	            synchronized (taoSchoolApplication.class) {
	                if (instance == null) {
	                    instance = new taoSchoolApplication();
	                }
	            }
	        }
	        return instance;
	    }
}
