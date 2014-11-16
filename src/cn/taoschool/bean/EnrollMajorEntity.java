package cn.taoschool.bean;

import org.json.JSONObject;

import cn.taoschool.R.string;
import cn.taoschool.util.FinalDataUitl;

public class EnrollMajorEntity {
	private String majorName;
	private int minScore;
	private int maxScore;
	private int averageScore;
	private int part;
	private String branch;
	
	public EnrollMajorEntity(){
		
	}
	
	public EnrollMajorEntity(String majorName,int minScore,
			int maxScore,int averageScore,int part,String branch){
		this.majorName =  majorName;
		this.minScore = minScore;
		this.maxScore = maxScore;
		this.averageScore = averageScore;
		this.part = part;
		this.branch = branch;
	}
	
	public EnrollMajorEntity(JSONObject object){	
		this.minScore = object.optInt("minscore");
		this.maxScore = object.optInt("maxscore");
		this.averageScore = object.optInt("avgscore");
		this.majorName = object.optString("zyname");
		this.branch = object.optString("kb");
	}
	
	
	
	public String getMajorName(){
		return majorName;
	}
	
	public String getBranch(){
		return branch;
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

	
	public void setMajorName(String MajorName){
		this.majorName =  majorName;
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
	
	
	public void setpart(int part){
		this.part = part;
	}
	
	public void setBranch(String branch){
		 this.branch = branch;
	}
	
	
	
}
