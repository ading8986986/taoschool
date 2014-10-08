package cn.taoschool.db;

import java.util.ArrayList;
import java.util.List;

import cn.taoschool.bean.SchoolItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class DBManager extends SQLiteOpenHelper {

	private static final String DB_NAME = "cn-taoschool-db";
	private static final String TABLE_NAME = "myTable";
	private static int VERSION = 1;
	private SQLiteDatabase db;
	private static DBManager mInstance;
	
	public DBManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		db = this.getWritableDatabase();
	}
	
	public static DBManager getInstance(Context context){
		if(mInstance == null){
			synchronized (DBManager.class) {
				if(null == mInstance){
					mInstance = new DBManager(context,DB_NAME,null,VERSION);
				}
			}
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE IF NOT EXISTS "		
					+ "'" + TABLE_NAME + "' (" + 
			                "'id' INTEGER AUTOCREMENT PRIMARY KEY," + // 0: id
			                "'school_id' INTEGER NOT NULL, " +
			                "'name' TEXT NOT NULL, " +
			                "'web' TEXT, " +
			                "'tel' TEXT, " +
			                "'mail' TEXT, " +
			                "'address' TEXT, " +
			                "'yxlx' INTEGER, " +
			                "'is211' INTEGER, " +
			                "'is985' INTEGER, " +
			                "'imgurl' TEXT );";
		db.execSQL(sql);
	}
	
	/**
	 * 初始化table中的10条数据
	 * **/
	public void InitTabel(){
		List<SchoolItem> list = new ArrayList<SchoolItem>();
		SchoolItem item1 = new SchoolItem(1, "北京大学", 1, "http://www.pku.edu.cn/", 
										"010-62751407", "bdzsb@pku.edu.cn", 
										"北京市海淀区颐和园路5号", 1, 1);
		insertSchoolItem(item1);
		SchoolItem item2 = new SchoolItem(2, "中国人民大学", 1,  "http://www.ruc.edu.cn",
										"010-62511340;010-62511156", "zsb@ruc.edu.cn", 
										"北京市海淀区中关村大街59号", 1, 1);
		insertSchoolItem(item2);
		
		SchoolItem item3 = new SchoolItem(3, "清华大学", 2,  "http://www.tsinghua.edu.cn",
				"010-62770334,62782051", "zsb@mail.tsinghua.edu.cn", 
				"北京市海淀区中关村大街59号", 1, 1);
		insertSchoolItem(item3);
		
		SchoolItem item4 = new SchoolItem(4, "北京航空航天大学", 2,  "http://www.buaa.edu.cn",
				"010-82330003,82330002", "zsbgs@buaa.edu.cn", 
				"北京市海淀区学院路37号", 1, 1);
		insertSchoolItem(item4);
		
		SchoolItem item5 = new SchoolItem(5, "北京理工大学", 2,  "http://www.bit.edu.cn",
				"010-68913345,68949926", "admission@bit.edu.cn", 
				"北京市海淀区中关村大街59号", 1, 1);
		insertSchoolItem(item5);
		
		SchoolItem item6 = new SchoolItem(6, "中国农业大学", 4,  "http://www.cau.edu.cn",
				"010-62737682", "zsb@cau.edu.cn", 
				"北京市海淀区清华东路17号", 1, 1);
		insertSchoolItem(item6);
		
		SchoolItem item7 = new SchoolItem(7, "北京师范大学", 7,  "http://www.bnu.edu.cn",
				"010-58807962", "zsb@bnu.edu.cn", 
				"北京市海淀区新街口外大街19号", 1, 1);
		insertSchoolItem(item7);
		
		SchoolItem item8 = new SchoolItem(8, "中央民族大学", 12,  "http://www.muc.edu.cn",
				"010-68932902;010-68933922", "cun3922@126.com", 
				"北京市海淀区中关村南大街27号", 1, 1);
		insertSchoolItem(item8
				);
		SchoolItem item9 = new SchoolItem(9, "南开大学", 1,  "http://www.ruc.nankai.cn",
				"022-23504845", "zhshb@nankai.edu.cn", 
				"天津市南开区卫津路94号", 1, 1);
		insertSchoolItem(item9);
		
		SchoolItem item10 = new SchoolItem(10, "天津大学", 2,  "http://www.tju.edu.cn",
				"022-27405486", "tdzb@tju.edu.cn", 
				"天津市南开区卫津路92号", 1, 1);
		insertSchoolItem(item10);
	}
	
	
	
	
	public List<SchoolItem> getSchoolItems(){
		//String sql = "select * from " + TABLE_NAME;
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		List<SchoolItem> list = new ArrayList<>();
		while(cursor.moveToNext()){
			SchoolItem schoolItem = new SchoolItem(
					cursor.getInt(cursor.getColumnIndex("school_id")),
					cursor.getString(cursor.getColumnIndex("name")),
					cursor.getInt(cursor.getColumnIndex("yxlx")),
					cursor.getString(cursor.getColumnIndex("web")),
					cursor.getString(cursor.getColumnIndex("tel")),
					cursor.getString(cursor.getColumnIndex("mail")),
					cursor.getString(cursor.getColumnIndex("address")),
					cursor.getInt(cursor.getColumnIndex("is985")),
					cursor.getInt(cursor.getColumnIndex("is211"))
					);
			list.add(schoolItem);
		}
		return list;
	}

	public void insertSchoolItem(SchoolItem schoolItem){
		ContentValues values = new ContentValues();
		values.put("school_id", schoolItem.getId());
		values.put("name", schoolItem.getName());
		values.put("web", schoolItem.getWeb());
		values.put("tel", schoolItem.getTel());
		values.put("mail", schoolItem.getMail());
		values.put("address", schoolItem.getAddress());
		values.put("yxlx", schoolItem.getYXLXId());
		values.put("is985", schoolItem.getIs985()?1:0);
		values.put("is211", schoolItem.getIs211()?1:0);
		db.insert(TABLE_NAME, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
