package cn.taoschool.bean;

import org.json.JSONObject;

import cn.taoschool.R.string;
import cn.taoschool.util.FinalDataUitl;

public class EnrollPlanEntity {
	private String majorName;
	private int system;//学制
	private String planType;
	private String branch;
	private String level;//学历层次
	
	public EnrollPlanEntity(){
		
	}
	
	public EnrollPlanEntity(String majorName,int system,String planType,String branch,String level){
		this.majorName =  majorName;
		this.system = system;
		this.planType = planType;
		this.level = level;
		this.branch = branch;
	}
	
	public EnrollPlanEntity(JSONObject object){	
		this.system = object.optInt("xz");
		this.planType = object.optString("jhlx");
		this.level = object.optString("xlcc");
		this.majorName = object.optString("zymc");
		this.branch = object.optString("kl");
	}
	
	
	
	public String getMajorName(){
		return majorName;
	}
	
	public String getBranch(){
		return branch;
	}
	
	public int getSystem(){
		return system;
	}
	
	public String getLevel(){
		return level;
	}
	
	public String getPlanType(){
		return planType;
	}

	
	
	public void setMajorName(String MajorName){
		this.majorName =  majorName;
	}
	
	public void setSystem(int system){
		this.system = system;
	}
	
	public void setPlanType(String planType){
		this.planType = planType;
	}
	
	public void setLevel(String level){
		this.level = level;
	}
	
	
	public void setBranch(String branch){
		 this.branch = branch;
	}
	
	
	
}
