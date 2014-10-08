package cn.taoschool.util;

import java.lang.reflect.Field;
import java.util.List;

import cn.taoschool.R;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.NoCopySpan.Concrete;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DialogOfListStyle extends Dialog{

	private String[] telNumbers;
	private Context context;
	
	public DialogOfListStyle(Context context,String[] telNumbers) {
		super(context);
		this.context =context;
		this.telNumbers = telNumbers;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			Field mAlert = DialogOfListStyle.class.getDeclaredField("mAlert");
			Object alertController = mAlert.get(this);
			Field mTitleView = alertController.getClass().getDeclaredField("mTitleView");  
			mTitleView.setAccessible(true); 
			TextView title = (TextView) mTitleView.get(alertController);  
		    title.setBackgroundColor(Color.GRAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle(R.string.call_hint);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_number_list);
		ListView lvTelNumber = (ListView)findViewById(R.id.lv_tel_number);
		lvTelNumber.setAdapter(new DiaLogAdapter(context, telNumbers));
		lvTelNumber.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cancel();
				// 拨打电话
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+telNumbers[position]));					
				context.startActivity(intent);
			}
		});
	}
	
	private class DiaLogAdapter extends BaseAdapter{

		private String[] telNumbers;
		private Context context;
		
		public DiaLogAdapter(Context context,String[] telNumbers) {
			this.context =context;
			this.telNumbers = telNumbers;
		}
		
		@Override
		public int getCount() {
			if(telNumbers == null ) return 0;
			else return telNumbers.length;
		}

		@Override
		public String getItem(int position) {
			if(telNumbers == null ) return null;
			else return telNumbers[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (null == convertView)
				convertView = LinearLayout.inflate(context, R.layout.list_item_callnumber, null);
			((TextView)convertView.findViewById(R.id.tv_number)).setText(getItem(position));
			return convertView;
		}
	}
}
