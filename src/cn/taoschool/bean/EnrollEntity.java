package cn.taoschool.bean;

import org.json.JSONObject;

import cn.taoschool.util.FinalDataUitl;

public class EnrollEntity {
	private int province;
	private int year;
	private int minScore;
	private int maxScore;
	private int averageScore;
	private int count;
	private int part;
	private int branch;
	
	public EnrollEntity(){
		
	}
	
	public EnrollEntity(JSONObject object){	
		this.year = object.optInt("year");
		this.minScore = object.optInt("minscore");
		this.maxScore = object.optInt("maxscore");
		this.averageScore = object.optInt("avgscore");
		this.count = object.optInt("lqrs");
	}
	
	
	
	public String getProvince(){
		return FinalDataUitl.getProvinceByid(province);
	}
	
	public int getYear(){
		return year;
	}
	
	public int getMaxScore(){
		return maxScore;
	}
	
	public int getMinScore(){
		return minScore;
	}
	
	public int getAverageScore(){
		return averageScore;
	}
	
	public int getCount(){
		return count;
	}
	
	public String getpart(){
		switch (part) {
		case 1:
			return "第一批";
		case 2:
			return "第二批";
		case 3:
			return "第三批";
		default:
			break;
		}
		return "";
	}
	
	public int getBranch(){
		return this.branch;
	}
	
	public void setProvince(int province){
		this.province =  province;
	}
	
	public void setYear(int year){
		this.year = year;
	}
	
	public void setMinScore(int minScore){
		this.minScore = minScore;
	}
	
	public void setMaxScore(int maxScore){
		this.maxScore = maxScore;
	}
	
	public void setAverageScore(int averageScore){
		this.averageScore = averageScore;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public void setpart(int part){
		this.part = part;
	}
	
	public void setBranch(int branch){
		 this.branch = branch;
	}
	
	
	
}
