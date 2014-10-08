package cn.taoschool.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cn.taoschool.R;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.cache.AsyncImageLoader;
import cn.taoschool.util.Constants;
import cn.taoschool.util.DialogOfListStyle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivityListAdapter extends BaseAdapter {
	
	private Context context;
	private List<SchoolItem> schoolList;
	
	
	public MainActivityListAdapter(Context context,List<SchoolItem> schoolList){
		this.context = context;
		
//		this.schoolList = new ArrayList<>();
//		for(int i = 0;i<schoolList2.size();i++){
//			this.schoolList.add(schoolList2.get(i));
//		}
		this.schoolList = schoolList;
	}
	
	public void setDataList(List<SchoolItem> schoolList){
		this.schoolList = schoolList;
		notifyDataSetChanged();
	}

	
	@Override
	public int getCount() {
		if(null == schoolList) return 0;
		return schoolList.size();
	}

	@Override
	public SchoolItem getItem(int position) {
		if(null == schoolList) return null;
		return schoolList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i("TAG","position="+position);
		if(null == convertView) convertView = (RelativeLayout)RelativeLayout.inflate(context,R.layout.list_item_school_profile,null);
		ViewHolder viewHolder = new ViewHolder(convertView);
		viewHolder.is211.setVisibility(View.VISIBLE);
		viewHolder.is985.setVisibility(View.VISIBLE);
		if(!getItem(position).getIs211()) viewHolder.is211.setVisibility(View.GONE);
		if(!getItem(position).getIs985()) viewHolder.is985.setVisibility(View.GONE);
		if(getItem(position).getBitmap()!=null) 
			viewHolder.school_img.setImageBitmap(schoolList.get(position).getBitmap());
		else{
			viewHolder.school_img.setImageResource(R.drawable.ic_launcher);
		}
		viewHolder.school_name.setText(getItem(position).getName());
		viewHolder.school_address.setText(getItem(position).getAddress());
		viewHolder.school_type.setText(getItem(position).getYXLX());		
		
		return convertView;
	}
	
	public void addItem(SchoolItem item){
		schoolList.add(item);
	}
	
	public void setImage(int i,Bitmap bitmap){
		getItem(i).setBitmap(bitmap);
	}
	
	public void clearData(){
		schoolList.clear();
	}
	
	public class ViewHolder{
		public ImageView school_img,is211,is985;	
		public TextView school_name,school_type,school_address;
		public ViewHolder(View convertView){
			school_img = (ImageView)convertView.findViewById(R.id.school_img);
			is211 = (ImageView)convertView.findViewById(R.id.is211);
			is985 = (ImageView)convertView.findViewById(R.id.is985);
			school_address = (TextView)convertView.findViewById(R.id.school_address);
			school_type = (TextView)convertView.findViewById(R.id.school_type);
			school_name = (TextView)convertView.findViewById(R.id.school_name);
			school_img = (ImageView) convertView.findViewById(R.id.school_img);
		}
	}

}
