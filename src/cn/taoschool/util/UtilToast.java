package cn.taoschool.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class UtilToast {
	public static void showLong(Context context,int content) {
		Toast t = Toast.makeText(context, content, Toast.LENGTH_LONG);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static void showLong(Context context,String content) {
		Toast t = Toast.makeText(context, content, Toast.LENGTH_LONG);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static void showShort(Context context,int content) {
		Toast t = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public static void showShort(Context context,String content) {
		Toast t = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	
}
