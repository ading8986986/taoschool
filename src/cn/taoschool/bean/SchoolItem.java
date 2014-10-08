package cn.taoschool.bean;

import java.io.Serializable;
import java.util.HashMap;

import cn.taoschool.cache.LoaderImpl;
import cn.taoschool.util.FinalDataUitl;

import android.R.bool;
import android.R.integer;
import android.graphics.Bitmap;

public class SchoolItem implements Serializable {
	private  int id;
	private String name;
	private int yxlx;
	private int bxlx;
	private int xlcc;//学历层次
	private String web;
	private String tel;
	private String mail;
	private String address;
	private boolean is985;
	private boolean is211;
	private Bitmap bitmap;
	
	public SchoolItem(){
		
	}
	
	public SchoolItem(int id,String name,int yxlx,String web,String tel,String mail,String address,
			int is985,int is211){
		this.id = id;
		this.name = name;
		this.yxlx = yxlx;
		this.web = web;
		this.mail = mail;
		this.tel = tel;
		this.address = address;
		this.is211 = is211 == 1?true:false;
		this.is985 = is985 == 1?true:false;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public int getId(){
		return id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setAddress(String address){
		this.address=address;
	}
	
	public String getAddress(){
		return address;
	}
	
	public void setTel(String tel){
		this.tel=tel;
	}
	
	public String getTel(){
		return tel;
	}
	
	public void setMail(String mail){
		this.mail=mail;
	}
	
	public String getMail(){
		return mail;
	}
	
	
	public void setWeb(String web){
		this.web=web;
	}
	
	public String getWeb(){
		return web;
	}
	
	
	public void setIs985(boolean is985){
		this.is985=is985;
	}
	
	public boolean getIs985(){
		return is985;
	}
	
	public void setIs211(boolean is211){
		this.is211=is211;
	}
	
	public boolean getIs211(){
		return this.is211;
	}
	
	public void setYXLX(int id){
		this.yxlx = id;
	}
	
	public String getYXLX(){
		return FinalDataUitl.getYXLXByid(yxlx);
	}
	
	public int getYXLXId(){
		return this.yxlx;
	}
	
	public void setBXLX(int id){
		this.bxlx = id;
	}
	
	public String getBXLX(){
		return FinalDataUitl.getBXLXByid(bxlx);
	}
	
	public void setXLCC(int id){
		this.xlcc = id;
	}
	
	public String getXLCC(){
		return FinalDataUitl.getXLCCByid(xlcc);
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
}
