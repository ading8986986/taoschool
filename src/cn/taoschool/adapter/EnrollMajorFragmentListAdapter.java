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
import cn.taoschool.bean.EnrollMajorEntity;
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

public class EnrollMajorFragmentListAdapter extends BaseAdapter {
	private List<EnrollMajorEntity> testEnrollMajorList = new ArrayList<EnrollMajorEntity>();
	private List<EnrollMajorEntity> enrollMajorList;
	private Context mContext;
	
	public EnrollMajorFragmentListAdapter(Context ctx) {
		this.mContext = ctx;
		testEnrollMajorList.add(new EnrollMajorEntity("环境科学",550,600,575,1,"理科"));
		testEnrollMajorList.add(new EnrollMajorEntity("机械制造",520,530,525,1,"理科"));
		testEnrollMajorList.add(new EnrollMajorEntity("计算机",550,600,575,1,"综合"));
		testEnrollMajorList.add(new EnrollMajorEntity("城市建设",550,600,575,1,"文科"));
		testEnrollMajorList.add(new EnrollMajorEntity("环境科学",550,600,575,1,"理科"));
		testEnrollMajorList.add(new EnrollMajorEntity("机械制造",520,530,525,1,"理科"));
		testEnrollMajorList.add(new EnrollMajorEntity("计算机",550,600,575,1,"综合"));
		testEnrollMajorList.add(new EnrollMajorEntity("城市建设",550,600,575,1,"文科"));
	}
	
	public void setEnrollDataList(List<EnrollMajorEntity> list){
		this.enrollMajorList = list;
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		if(null == enrollMajorList) 
			{
				enrollMajorList = testEnrollMajorList;
			}
		return enrollMajorList.size();
	}

	@Override
	public EnrollMajorEntity getItem(int position) {
		if(null != enrollMajorList) 
			return enrollMajorList.get(position);
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
			
			convertView = LinearLayout.inflate(mContext, R.layout.list_item_enroll_major, null);
			ViewHolder viewHolder = new ViewHolder(convertView);
			viewHolder.majorName.setText(getItem(position).getMajorName());
			viewHolder.branch.setText(getItem(position).getBranch());
			viewHolder.minScore.setText(getItem(position).getMinScore()+"");
			viewHolder.maxScore.setText(getItem(position).getMaxScore()+"");
			viewHolder.averageScore.setText(getItem(position).getAverageScore()+"");
			viewHolder.part.setText(getItem(position).getpart()+"");
			return convertView;
			
		}
		return convertView;
	}		
	

	
	private class ViewHolder{
		TextView majorName;
		TextView branch;
		TextView minScore;
		TextView maxScore;
		TextView averageScore;
		TextView part;
		
		public ViewHolder(View convertView){
			majorName = (TextView) convertView.findViewById(R.id.tv_major_name);
			minScore = (TextView) convertView.findViewById(R.id.tv_min_score);
			maxScore = (TextView) convertView.findViewById(R.id.tv_max_score);
			averageScore = (TextView) convertView.findViewById(R.id.tv_avg_score);
			branch = (TextView) convertView.findViewById(R.id.tv_branch);
			part = (TextView) convertView.findViewById(R.id.tv_part);
		}
	}

}
