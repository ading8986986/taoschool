package cn.taoschool.ui;

import java.util.List;

import cn.taoschool.R;
import cn.taoschool.adapter.ExpandMenuAdapter;
import cn.taoschool.util.FinalDataUitl;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class ChoiceListActivity extends Activity implements OnItemClickListener {

	private ListView lvFirstLevel,lvSecondLevel,lvOneLevelListView;
	private LinearLayout llTwoLevelMenu;
	private TextView tvTitle;
	private List<String> listProvince;
	private List<String> listSchoolType;
	private List<String> listSchoolSystem;
	
	private List<String> listFirstLevelMajor;
	private List<String> listSecondLevelMajor;
	
	private ExpandMenuAdapter provinceAdapter;
	private ExpandMenuAdapter schoolTypeAdapter;
	private ExpandMenuAdapter schoolSystemAdapter;
	private ExpandMenuAdapter firstLevelAdapter;
	private ExpandMenuAdapter secondLevelAdapter;
	
	public static final int SHOW_NOTHING = 0;
	public static final int SHOW_STUDENT_PROVINCE = 1;
	public static final int SHOW_SCHOOL_PROVINCE = 2;
	public static final int SHOW_SCHOOL_TYPE = 3;
	public static final int SHOW_SCHOOL_SYSTEM = 4;
	public static final int SHOW_MAJOR = 5;
	
	private String province;
	private String schoolType;
	private String schoolSystem;
	private String firstLevelMajor;
	private String secondLevelMajor;
	
	private int curListView = SHOW_NOTHING;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choice_list);
		initView();
		initData();
		changeCurListView();
	}
	
	private void initView(){
		tvTitle = (TextView)findViewById(R.id.tv_title);
		llTwoLevelMenu = (LinearLayout)findViewById(R.id.ll_two_level_menu);
		lvOneLevelListView = (ListView) findViewById(R.id.ll_one_level_menu);
		lvFirstLevel = (ListView) findViewById(R.id.lv_first_level);
		lvSecondLevel = (ListView) findViewById(R.id.lv_second_level);
		lvOneLevelListView.setOnItemClickListener(this);
		lvFirstLevel.setOnItemClickListener(this);
		lvSecondLevel.setOnItemClickListener(this);
	}

	private void initData(){
		curListView = getIntent().getIntExtra("curListView", 0);
		switch (curListView) {
		case SHOW_STUDENT_PROVINCE:
			tvTitle.setText(R.string.student_location);
			break;
		case SHOW_SCHOOL_PROVINCE:
			tvTitle.setText(R.string.register_location);
			break;
		case SHOW_SCHOOL_TYPE:
			tvTitle.setText(R.string.register_school_type);
			break;
		case SHOW_SCHOOL_SYSTEM:
			tvTitle.setText(R.string.register_school_system);
			break;
		case SHOW_MAJOR:
			tvTitle.setText(R.string.register_major_type);
			break;
		default:
			break;
		}
		
		listProvince = FinalDataUitl.getAllProvice();
		listFirstLevelMajor = FinalDataUitl.getFirstLevelMajorList();
		listSecondLevelMajor = FinalDataUitl.getSecondLevelMajorList(0) ;
		listSchoolType = FinalDataUitl.getAllXYLX();
		listSchoolSystem = FinalDataUitl.getAllBXLX();
		provinceAdapter = new ExpandMenuAdapter(this, listProvince);
		schoolTypeAdapter = new ExpandMenuAdapter(this, listSchoolType);
		schoolSystemAdapter = new ExpandMenuAdapter(this, listSchoolSystem);
		firstLevelAdapter = new ExpandMenuAdapter(this, listFirstLevelMajor);
		secondLevelAdapter = new ExpandMenuAdapter(this,listSecondLevelMajor);
	}
	
	private void changeCurListView(){
		lvOneLevelListView.setVisibility(View.GONE);
		llTwoLevelMenu.setVisibility(View.GONE);
		if (SHOW_MAJOR == curListView) {
			llTwoLevelMenu.setVisibility(View.VISIBLE);
			lvFirstLevel.setAdapter(firstLevelAdapter);
			lvSecondLevel.setAdapter(secondLevelAdapter);
		}
		else{
			lvOneLevelListView.setVisibility(View.VISIBLE);
			switch (curListView) {
			case SHOW_SCHOOL_TYPE:
				lvOneLevelListView.setAdapter(schoolTypeAdapter);
				break;
			case SHOW_SCHOOL_SYSTEM:
				lvOneLevelListView.setAdapter(schoolSystemAdapter);
				break;
			case SHOW_SCHOOL_PROVINCE:
			case SHOW_STUDENT_PROVINCE:
				lvOneLevelListView.setAdapter(provinceAdapter);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if(parent == lvOneLevelListView){
			switch (curListView) {
			case SHOW_STUDENT_PROVINCE:
				province = position+"";
				provinceAdapter.setItemSelect(position, true);
				tvTitle.setText(listProvince.get(position));
				setResult(SHOW_STUDENT_PROVINCE,
						new Intent().putExtra("provinceCode", province)
									.putExtra("provinceName", listProvince.get(position)));
				break;
			case SHOW_SCHOOL_PROVINCE:
				province = position+"";
				provinceAdapter.setItemSelect(position, true);
				tvTitle.setText(listProvince.get(position));
				setResult(SHOW_SCHOOL_PROVINCE,
						new Intent().putExtra("provinceCode", province)
									.putExtra("provinceName", listProvince.get(position)));
				break;
			case SHOW_SCHOOL_TYPE:
				schoolType = position+"";
				schoolTypeAdapter.setItemSelect(position, true);
				tvTitle.setText(listSchoolType.get(position));
				setResult(SHOW_SCHOOL_TYPE,
						new Intent().putExtra("schoolTypeCode", schoolType)
									.putExtra("schoolTypeName", listSchoolType.get(position)));
				break;
			case SHOW_SCHOOL_SYSTEM:
				schoolSystem = position+"";
				schoolSystemAdapter.setItemSelect(position, true);
				tvTitle.setText(listSchoolSystem.get(position));
				setResult(SHOW_SCHOOL_SYSTEM,
						new Intent().putExtra("schoolSystemCode", schoolType)
									.putExtra("schoolSystemName", listSchoolSystem.get(position)));
				break;
			default:
				break;
			}
			finish();	
		}
		else if(parent == lvFirstLevel){
			firstLevelMajor = position + "";
			firstLevelAdapter.setItemSelect(position, true);
			tvTitle.setText(listFirstLevelMajor.get(position));
			listSecondLevelMajor = FinalDataUitl.getSecondLevelMajorList(position);
			secondLevelAdapter = new ExpandMenuAdapter(this, listSecondLevelMajor);
			lvSecondLevel.setAdapter(secondLevelAdapter);
			return;
		}
		else if(parent == lvSecondLevel){
			secondLevelMajor = position + "";
			tvTitle.setText(listSecondLevelMajor.get(position));
			secondLevelAdapter.setItemSelect(position, true);
			setResult(SHOW_MAJOR,
					new Intent().putExtra("firstLevelMajorCode", firstLevelMajor)
								.putExtra("firstLevelMajorName", listFirstLevelMajor.get(Integer.parseInt(firstLevelMajor)))
								.putExtra("secondLevelMajorCode", secondLevelMajor)
								.putExtra("secondLevelMajorName", listSecondLevelMajor.get(position)));
			
			finish();
		}
	}
	
	
}
