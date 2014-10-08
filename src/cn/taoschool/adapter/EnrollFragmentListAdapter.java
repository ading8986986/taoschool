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

public class EnrollFragmentListAdapter extends BaseAdapter {
	
	private List<EnrollEntity> enrollList;
	private Context mContext;
	
	public EnrollFragmentListAdapter(Context ctx) {
		this.mContext = ctx;
	}
	
	public void setEnrollDataList(List<EnrollEntity> enrollList){
		this.enrollList = enrollList;
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		if(null != enrollList) return enrollList.size()+1;
		return 0;
	}

	@Override
	public EnrollEntity getItem(int position) {
		if(null != enrollList && position>=1) 
			return enrollList.get(position-1);
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
			if( 0 ==position)
				return getTitleView();
			else {
				Log.i("TAG","第"+position+"个");
				convertView = LinearLayout.inflate(mContext, R.layout.list_item_enroll_data, null);
				ViewHolder viewHolder = new ViewHolder(convertView);
				viewHolder.year.setText(getItem(position).getYear()+"");
				viewHolder.minScore.setText(getItem(position).getMinScore()+"");
				viewHolder.maxScore.setText(getItem(position).getMaxScore()+"");
				viewHolder.averageScore.setText(getItem(position).getAverageScore()+"");
				viewHolder.count.setText(getItem(position).getCount()+"");
				viewHolder.part.setText(getItem(position).getpart()+"");
				return convertView;
			}
		}
		return convertView;
	}
	
	private View getTitleView(){
		View view = LinearLayout.inflate(mContext, R.layout.list_item_enroll_data, null);
		view.setBackgroundResource(R.color.gray_backgroud);
		return view;
	}
	
	/**
	 * 返回图表View
	 * **/
	private View getChartView(){
		if(null == enrollList) return null;
		String[] titleList = new String[]{"最低分","最高分","平均分"};
		List<double[]> yearList = new ArrayList<double[]>();
		List<double[]> scoreList = new ArrayList<double[]>();
		
		double[] year = new double[enrollList.size()];
		double[] maxScore = new double[enrollList.size()];
		double[] minScore = new double[enrollList.size()];
		double[] averageScore = new double[enrollList.size()];
		for(int i = 0;i<enrollList.size();i++){
			year[i] = enrollList.get(i).getYear();
			maxScore[i] = enrollList.get(i).getMaxScore();
			minScore[i] = enrollList.get(i).getMinScore();
			averageScore[i] = enrollList.get(i).getAverageScore();
		}
		for (int j = 0; j < titleList.length; j++) {	
			yearList.add(year);
		}
		scoreList.add(minScore);
		scoreList.add(maxScore);
		scoreList.add(averageScore);
		ScoreChart scoreChart = new ScoreChart(mContext, titleList, yearList, scoreList);
		return scoreChart.getCharView();
		
	}
	
	private class ViewHolder{
		TextView year;
		TextView minScore;
		TextView maxScore;
		TextView averageScore;
		TextView count;
		TextView part;
		
		public ViewHolder(View convertView){
			year = (TextView) convertView.findViewById(R.id.tv_year);
			minScore = (TextView) convertView.findViewById(R.id.tv_lowscore);
			maxScore = (TextView) convertView.findViewById(R.id.tv_highscore);
			averageScore = (TextView) convertView.findViewById(R.id.tv_averagescore);
			count = (TextView) convertView.findViewById(R.id.tv_count);
			part = (TextView) convertView.findViewById(R.id.tv_part);
		}
	}

}
