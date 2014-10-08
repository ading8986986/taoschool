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
	public static final HashMap<Integer,String> provinceEncode = new HashMap<Integer,String>(){
		{
			put(0, "全部");put(1,"北京");put(2,"天津");put(3,"辽宁");put(4,"吉林");put(5,"黑龙江");
			put(6,"上海");put(7,"江苏");put(8,"浙江");put(9,"安徽");put(10,"福建");
			put(11,"山东");put(12,"湖北");put(13,"湖南");put(14,"广东");put(15,"重庆");
			put(16,"四川");put(17,"陕西");put(18,"甘肃");put(19,"河北");put(20,"山西");
			put(21,"内蒙古");put(22,"河南");put(23,"海南");put(24,"广西");put(25,"贵州");
			put(26,"云南");put(27,"西藏");put(28,"青海");put(29,"宁夏");put(30,"新疆");
			put(31,"江西");put(32,"香港");put(33,"澳门");put(34,"台湾");
		}
	};
	
	public static final HashMap<Integer,String> xylxEncode = new HashMap<Integer,String>(){
		{
			put(0, "全部");put(1,"综合");put(2,"工科");put(3,"财经");put(4,"农业");put(5,"林业");
			put(6,"医药");put(7,"师范");put(8,"体育");put(9,"语言");put(10,"政法");
			put(11,"艺术");put(12,"民族");put(13,"军事");put(14,"党政");
		}
	};
	public static final HashMap<Integer,String> bxlxEncode = new HashMap<Integer,String>(){
		{
			put(0, "全部");put(1,"大学");put(2,"学院");put(3,"高等职业技术学校");put(4,"高等专科学校");put(5,"独立学院");
			put(6,"高等学校分校");put(7,"短期职业大学");put(8,"成人高等学校");put(9,"管理干部学院");put(10,"教育学院");		
		}
	};
	public static final HashMap<Integer,String> xlccEncode = new HashMap<Integer,String>(){
		{
			put(0, "全部");put(1,"本科");put(2,"本科/高职(专科)");put(3,"高职(专科)");put(4,"未知");
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
		List list = new ArrayList<String>();
		Iterator iterator = provinceEncode.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			list.add(entry.getValue());
		}
		return list;
	}
	
	public static List getAllXYLX(){
		List list = new ArrayList<String>();
		Iterator iterator = xylxEncode.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			list.add(entry.getValue());
		}
		return list;
	}
	
	public static List getAllBXLX(){
		List list = new ArrayList<String>();
		Iterator iterator = bxlxEncode.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			list.add(entry.getValue());
		}
		return list;
	}
	
	public static List getAllXLCC(){
		List list = new ArrayList<String>();
		Iterator iterator = xlccEncode.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry entry = (Map.Entry) iterator.next();
			list.add(entry.getValue());
		}
		return list;
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
