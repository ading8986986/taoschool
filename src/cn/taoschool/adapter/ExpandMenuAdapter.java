package cn.taoschool.adapter;

import java.util.List;

import cn.taoschool.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandMenuAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> list;
	private boolean[] selected;
	
	public ExpandMenuAdapter(Context c,List<String> list){
		context = c;
		this.list = list;
		selected = new boolean[list.size()];
		selected[0] = true;
	}
	
	public void  setItemSelect(int position, boolean selected){
		this.selected = new boolean[list.size()];
		this.selected[position] = selected;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(null == convertView) convertView = LinearLayout.inflate(context, R.layout.list_item_expand_menu, null);
		ViewHolder viewHolder = new ViewHolder(convertView, position);
		viewHolder.tvContent.setText(list.get(position));
		return convertView;
	}
	
	private class ViewHolder{
		public  TextView tvContent;
		public ImageView ivDivider;
		@SuppressLint("NewApi")
		public ViewHolder(View convertView,int position){
			this.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			this.ivDivider = (ImageView) convertView.findViewById(R.id.iv_divider);			
			if(selected[position]){				
				tvContent.setTextColor(context.getResources().getColor(R.color.title_green));
				ivDivider.setBackgroundResource(R.color.title_green);
				convertView.setBackgroundResource(R.color.white);
			}
			else{
				tvContent.setTextColor(context.getResources().getColor(R.color.gray5));
				ivDivider.setBackgroundResource(R.color.gray_divider);
				convertView.setBackground(null);
			}
		}				
	}

}
