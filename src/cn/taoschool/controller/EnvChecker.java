package cn.taoschool.controller;

import com.android.volley.Request.Method;

import cn.taoschool.util.Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class EnvChecker {
	private Context ctx;
	private SharedPreferences sharedPreferences;
	private static EnvChecker envChecker;
	
	public static EnvChecker getInstance(Context ctx){
		if(envChecker == null){
			synchronized (EnvChecker.class) {
				if(null == envChecker)
				{
					envChecker = new EnvChecker();
					envChecker.ctx = ctx;
				}
			}
		}
		return envChecker;
	}
	
	public boolean isFirstInstalled(){
		boolean result = false;
		sharedPreferences = ctx.getSharedPreferences(
				Constants.APP_ENV, Context.MODE_PRIVATE);
		if("".equals(sharedPreferences.getString(Constants.IS_FIRST_INSTALL, ""))){
			result = true;
			Editor editor = sharedPreferences.edit();
			editor.putString(Constants.IS_FIRST_INSTALL, Constants.IS_FIRST_INSTALL);
			editor.commit();
		}		
		return result;
	}
	
	
	
}
