package cn.taoschool.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.text.format.Time;
import android.widget.ListView;

/**
 * 将省，专业，院校等字符串转成int的编码
 **/
public class FinalDataUitl {
	public static final List<String> provinceEncode = new ArrayList<String>(){
		{
			add(0,"全部");add(1,"北京");add(2,"天津");add(3,"辽宁");add(4,"吉林");add(5,"黑龙江");
			add(6,"上海");add(7,"江苏");add(8,"浙江");add(9,"安徽");add(10,"福建");
			add(11,"山东");add(12,"湖北");add(13,"湖南");add(14,"广东");add(15,"重庆");
			add(16,"四川");add(17,"陕西");add(18,"甘肃");add(19,"河北");add(20,"山西");
			add(21,"内蒙古");add(22,"河南");add(23,"海南");add(24,"广西");add(25,"贵州");
			add(26,"云南");add(27,"西藏");add(28,"青海");add(29,"宁夏");add(30,"新疆");
			add(31,"江西");add(32,"香港");add(33,"澳门");add(34,"台湾");
		}
	};
	
	public static final List<String> xylxEncode = new ArrayList<String>(){
		{
			add(0, "全部");add(1,"综合");add(2,"工科");add(3,"财经");add(4,"农业");add(5,"林业");
			add(6,"医药");add(7,"师范");add(8,"体育");add(9,"语言");add(10,"政法");
			add(11,"艺术");add(12,"民族");add(13,"军事");add(14,"党政");
		}
	};
	public static final List<String> bxlxEncode = new ArrayList<String>(){
		{
			add(0, "全部");add(1,"大学");add(2,"学院");add(3,"高等职业技术学校");add(4,"高等专科学校");add(5,"独立学院");
			add(6,"高等学校分校");add(7,"短期职业大学");add(8,"成人高等学校");add(9,"管理干部学院");add(10,"教育学院");		
		}
	};
	public static final List<String> xlccEncode = new ArrayList<String>(){
		{
			add(0, "全部");add(1,"本科");add(2,"本科/高职(专科)");add(3,"高职(专科)");add(4,"未知");
		}
	};
	
	public static final List<String> firstLevelMajor = new ArrayList<String>(){
		{
			add("全部");
			add("哲学");
			add("经济学");
			add("法学");
			add("教育学");
			add("文学");
			add("历史学");
			add("理学");
			add("工学");
			add("农学");			
			add("医学");
			add("管理学");
			add("艺术学");
		}
	};
	
	public static final List<String[]> secondLevelMajor = new ArrayList<String[]>(){
		{
			add(new String[]{"全部"});
			add(new String[]{"全部","哲学类"});
			add(new String[]{"全部","经济学类","财政学类","金融学类","经济与贸易类",});
			add(new String[]{"全部","法学类","政治学类","社会学类","民族学类","马克思主义理论类","公安学类"});
			add(new String[]{"全部","教育学类","体育学类"});
			add(new String[]{"全部","中国语言文学类","外国语言文学类","新闻传播学类"});
			add(new String[]{"全部","历史学类"});
			add(new String[]{"全部","数学类","物理学类","化学类","天文学类","地理学科学类","大气科学类","海洋科学类",
					"地球物理学类","地质学类","生物科学类","心理学学类","统计学类"});
			add(new String[]{"全部","力学类","机械类","仪器类","材料类","能源动力类","电气类","电子信息类",
					"自动化类","计算机类","土木类","水利类","测绘类","化工与制药类","地质类",
					"矿业类","纺织类","经济学类","轻工类","交通运输类","海洋工程类","航空航天类",
					"兵器类","农业工程类","林业工程类","环境科学与工程类","生物医学工程类","食品科学与工程类","建筑类",
					"安全科学与工程类","生物工程类","公安技术类"});
			add(new String[]{"全部","植物生产类","自然保护与环境生态类","动物生产类","动物医学类","林学类","水产类","草学类"});
			add(new String[]{"全部","基础医学类","临床医学类","口腔医学类","公共卫生与预防医学类","中医学类","中西医结合类",
					"药学类","中药学类","法医学类","医学技术类","护理学类"});
			add(new String[]{"全部","管理科学与工程类","工商管理类","农业经济管理类","公共管理类",
					"图书情报与档案管理类","物流管理与工程类","工业工程类","电子商务类","旅游管理类"});
			add(new String[]{"全部","艺术学理论类","音乐与舞蹈学类","戏剧与影视学类","美术学类","设计学类"});
		}
	};
	
	public static List<String> branchList = new ArrayList<String>(){
		{
			add("文科");
			add("理科");
			add("综合");
		}
	};
	
	public static String getXLCCByid(int id){
		return xlccEncode.get(id);
	}
	
	public static String getProvinceByid(int id){
		return provinceEncode.get(id);
	}
	
	public static String getYXLXByid(int id){
		return xylxEncode.get(id);
	}
	
	public static String getBXLXByid(int id){
		return bxlxEncode.get(id);
	}
	
	public static String getBranchByid(int id){
		return branchList.get(id);
	}
	
	public static List getAllProvice(){
		
		return provinceEncode;
	}
	
	public static List getAllProvice(boolean removeFirstItem){
		List<String> list = new ArrayList<String>();
		list.addAll(provinceEncode);
		list.remove(0);
		return list;
	}
	
	public static List getAllXYLX(){
		
		return xylxEncode;
	}
	
	public static List getAllBXLX(){
		
		return bxlxEncode;
	}
	
	public static List getAllXLCC(){
		
		return xlccEncode;
	}
	
	public static List<String> getFirstLevelMajorList(){
		return firstLevelMajor;
	}
	
	public static List<String> getSecondLevelMajorList(int position){
		List<String> list =  new ArrayList<String>();
		String [] array = secondLevelMajor.get(position);
		for(int i = 0;i<array.length;i++)
			list.add(array[i]);
		return list;
	}
	
	public static  List<String> getYearsList(){
		List<String> list =  new ArrayList<String>();
		int thisYear = Integer.parseInt(getCurrentYear());
		for(int i = 0;i<=10;i++){
			list.add(thisYear-i+"");
		}
		return list;
	}
	
	public static  List<String> getBranchList(){		
		return branchList;
	}
	
	public static String getCurrentYear(){
		Time time = new Time();
		time.setToNow();
		return new SimpleDateFormat("yyyy").format(new java.util.Date(time.toMillis(false)));
	}
}
