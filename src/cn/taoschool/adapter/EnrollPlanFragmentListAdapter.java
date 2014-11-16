package cn.taoschool.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.w3c.dom.Text;

import cn.taoschool.R;
import cn.taoschool.R.color;
import cn.taoschool.bean.EnrollEntity;
import cn.taoschool.bean.EnrollPlanEntity;
import cn.taoschool.chart.ScoreChart;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EnrollPlanFragmentListAdapter extends BaseAdapter {
	private List<EnrollPlanEntity> testEnrollPlanList = new ArrayList<EnrollPlanEntity>();
	private List<EnrollPlanEntity> enrollPlanList;
	private Context mContext;
	
	public EnrollPlanFragmentListAdapter(Context ctx) {
		this.mContext = ctx;
		testEnrollPlanList.add(new EnrollPlanEntity("文科试验1", 4, "非定向", "理科", "本科"));
		testEnrollPlanList.add(new EnrollPlanEntity("文科试验2", 4, "非定向", "理科", "本科"));
		testEnrollPlanList.add(new EnrollPlanEntity("文科试验3", 4, "非定向", "理科", "本科"));
		testEnrollPlanList.add(new EnrollPlanEntity("文科试验4", 4, "非定向", "理科", "本科"));
		testEnrollPlanList.add(new EnrollPlanEntity("文科试验5", 4, "非定向", "理科", "本科"));
	}
	
	public void setEnrollDataList(List<EnrollPlanEntity> list){
		this.enrollPlanList = list;
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		if(null == enrollPlanList) 
			{
				enrollPlanList = testEnrollPlanList;
			}
		return enrollPlanList.size();
	}

	@Override
	public EnrollPlanEntity getItem(int position) {
		if(null != enrollPlanList) 
			return enrollPlanList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null == convertView){
			
			convertView = LinearLayout.inflate(mContext, R.layout.list_item_enroll_plan, null);
			ViewHolder viewHolder = new ViewHolder(convertView);
			viewHolder.majorName.setText(getItem(position).getMajorName());
			viewHolder.branch.setText(getItem(position).getBranch());
			viewHolder.level.setText(getItem(position).getLevel()+"");
			viewHolder.system.setText(getItem(position).getSystem()+"");
			viewHolder.planType.setText(getItem(position).getPlanType()+"");
			return convertView;
			
		}
		return convertView;
	}		
	

	
	private class ViewHolder{
		TextView majorName;
		TextView branch;
		TextView system;
		TextView planType;
		TextView level;
		public ViewHolder(View convertView){
			majorName = (TextView) convertView.findViewById(R.id.tv_plan_name);
			level = (TextView) convertView.findViewById(R.id.tv_level);
			system = (TextView) convertView.findViewById(R.id.tv_system);
			planType = (TextView) convertView.findViewById(R.id.tv_plan_type);
			branch = (TextView) convertView.findViewById(R.id.tv_branch);
		}
	}

}
