/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.taoschool.chart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;

/**
 * Average temperature demo chart.
 */
public class ScoreChart extends AbstractDemoChart {
	private Context mContext;
	private List<double[]> yearList;
	private List<double[]> scoreList;
	private String[] titleList;
	public ScoreChart(Context mContexts,String[] titleList,List<double[]> yearList,List<double[]> scoreList){
		this.mContext = mContexts;
		this.scoreList = scoreList;
		this.yearList = yearList;
		this.titleList = titleList;
	}
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   * 
   */
  public String getName() {
    return "Average temperature";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "The average temperature in 4 Greek islands (line chart)";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public Intent execute(Context context) {
	  return null;
  }
  
  public View getCharView(){
	  int[] colors = new int[] { Color.BLUE,Color.RED,Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,
		         PointStyle.SQUARE };
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    int length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
	    }
	    double minYear = Double.parseDouble(
        		new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis()))        	
        		);
	    double maxYear = 0;
	    double maxScore = 0;
	    double minScore = 1000;
	    for(int i = 0;i<yearList.get(0).length;i++){
	    	if(minYear > yearList.get(0)[i]) minYear = yearList.get(0)[i];
	    	if(maxYear < yearList.get(0)[i]) maxYear = yearList.get(0)[i];
	    }
	    for(int i = 0;i<scoreList.get(0).length;i++){
	    	if(minScore > scoreList.get(0)[i]) minScore = scoreList.get(0)[i];
	    	if(maxScore < scoreList.get(1)[i]) maxScore = scoreList.get(1)[i];
	    }
	    setChartSettings(renderer, "分数线趋势图", "年份", "分数", 
	    		minYear-1, maxYear+1, minScore-100, maxScore+100,
	    	Color.rgb(51, 51, 51), 
	        Color.rgb(51, 51, 51),
	        Color.WHITE,
	        Color.rgb(245, 245, 245));
	    renderer.setXLabels(yearList.get(0).length+2);
	    renderer.setYLabels(10);
	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.RIGHT);
	    renderer.setYLabelsAlign(Align.RIGHT);
//	    renderer.setZoomButtonsVisible(true);
//	    renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
//	    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

	    XYMultipleSeriesDataset dataset = buildDataset(titleList, yearList, scoreList);
	    XYSeries series = dataset.getSeriesAt(0);
	    series.addAnnotation("Vacation", 6, 30);
	    GraphicalView  graphView = ChartFactory.getLineChartView(mContext, dataset, renderer); 
		graphView.setLayoutParams(new ListView.LayoutParams(LayoutParams.MATCH_PARENT,300));
		return graphView;
  }

}
