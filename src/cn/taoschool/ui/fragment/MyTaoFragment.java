package cn.taoschool.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import cn.taoschool.R;
import cn.taoschool.bean.SchoolItem;
import cn.taoschool.pulltorefresh.library.PullToRefreshBase;
import cn.taoschool.ui.ChoiceListActivity;
import cn.taoschool.util.UtilToast;

public class MyTaoFragment extends BasicMainFragment implements OnClickListener{
	private View rootView;
	private TextView tvStudentLocation, tvSchoolLocation;
	private TextView tvSchoolType,tvSchoolSystem,tvMajor;
	private TextView tvDeviation,tvBranch;
	private TextView tvConfirm;
	private EditText etStudentScore;
	private RadioButton rbSchool,rbMajor;
	private RadioGroup rbPreference;
	
	private String[] deviations = new String[]{"0","±5","±10","±15"};
	private String[] branchs = new String[]{"文科","理科"};
	
	//需要传递给server的参数
	private String studentProvince;
	private String schoolProvince="0";
	private String schoolType="0";
	private String schoolSystem="0";
	private String firstLevelMajor="0";
	private String secondLevelMajor="0";
	private String preference="0";
	private String studentScore;
	private int scoreDeviation = 5;
	private int branch = 0;
	private int beginAt = 0;
	
	private ChoiceListViewOnclickListener mListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = LinearLayout.inflate(getActivity(), R.layout.main_viewpager3, null);
		initData();
		initView();
		return rootView;
	}
	
	private void initData(){
		mListener = new ChoiceListViewOnclickListener();
	}
	
	private void initView(){
		etStudentScore = (EditText) rootView.findViewById(R.id.et_score);
		tvStudentLocation = (TextView) rootView.findViewById(R.id.tv_location);
		tvSchoolLocation = (TextView) rootView.findViewById(R.id.tv_school_location);
		tvSchoolType = (TextView) rootView.findViewById(R.id.tv_school_type);
		tvSchoolSystem = (TextView) rootView.findViewById(R.id.tv_school_system);
		tvMajor = (TextView) rootView.findViewById(R.id.tv_major);
		tvDeviation = (TextView) rootView.findViewById(R.id.tv_deviation);
		tvBranch = (TextView) rootView.findViewById(R.id.tv_branch);
		rbPreference = (RadioGroup) rootView.findViewById(R.id.rg_preference);
		tvConfirm = (TextView) rootView.findViewById(R.id.tv_confirm);
		tvMajor.setOnClickListener(mListener);
		tvStudentLocation.setOnClickListener(mListener);
		tvSchoolLocation.setOnClickListener(mListener);
		tvSchoolType.setOnClickListener(mListener);
		tvSchoolSystem.setOnClickListener(mListener);
		tvDeviation.setOnClickListener(this);
		tvBranch.setOnClickListener(this);
		tvConfirm.setOnClickListener(this);
		rbPreference.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (group.getCheckedRadioButtonId()) {
				case R.id.rb_major:
					preference = "1";
					break;
				case R.id.rb_school:
					preference = "0";
					break;
				default:
					break;
				}
			}
		});
	}
	

	@Override
	public void onSchoolListObtained(boolean isSuccess, List<SchoolItem> result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnImageInitialized(Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckForUpdateObtained(boolean update, JSONObject result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getMoreData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSearch() {
//		getAllParams();
		if(TextUtils.isEmpty(etStudentScore.getText())){
			UtilToast.showShort(getActivity(), R.string.no_score_hint);
			return;
		}
		if(TextUtils.isEmpty(this.studentProvince)||"0".equals(this.studentProvince)){
			UtilToast.showShort(getActivity(), R.string.no_province_hint);
			return;
		}
		setFilterParam();
	}
	
	private void getAllParams(){
		 studentProvince = tvStudentLocation.getText().toString();
		 schoolProvince = tvSchoolLocation.getText().toString();
		 schoolType = tvSchoolType.getText().toString();
		 schoolSystem = tvSchoolSystem.getText().toString();
		 studentScore=etStudentScore.getText().toString();
	}

	@Override
	public void setFilterParam() {
		filter_params = new JSONObject();
		try {
			filter_params.put("studentProvince", this.studentProvince);
			filter_params.put("schoolProvince",this.schoolProvince);
			filter_params.put("schoolType", this.schoolType);
			filter_params.put("schoolSystem",this.schoolSystem);
			filter_params.put("firstLevelMajor", this.firstLevelMajor);
			filter_params.put("secondLevelMajor", this.secondLevelMajor);
			filter_params.put("preference", this.preference);
			filter_params.put("studentScore", this.studentScore);
			filter_params.put("scoreDeviation", this.scoreDeviation);
			filter_params.put("branch", this.branch);
			filter_params.put("beginAt", this.beginAt);
			Log.i("TAG","fileterParam="+filter_params.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Override
	public boolean onReturnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	public class ChoiceListViewOnclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),ChoiceListActivity.class);
			switch (v.getId()) {
			case R.id.tv_location:
				intent.putExtra("curListView", ChoiceListActivity.SHOW_STUDENT_PROVINCE);
				startActivityForResult(intent, ChoiceListActivity.SHOW_STUDENT_PROVINCE);
				break;
			case R.id.tv_school_location:
				intent.putExtra("curListView", ChoiceListActivity.SHOW_SCHOOL_PROVINCE);
				startActivityForResult(intent, ChoiceListActivity.SHOW_SCHOOL_PROVINCE);
				break;
			case R.id.tv_school_type:
				intent.putExtra("curListView", ChoiceListActivity.SHOW_SCHOOL_TYPE);
				startActivityForResult(intent, ChoiceListActivity.SHOW_SCHOOL_TYPE);
				break;
			case R.id.tv_school_system:
				intent.putExtra("curListView", ChoiceListActivity.SHOW_SCHOOL_SYSTEM);
				startActivityForResult(intent, ChoiceListActivity.SHOW_SCHOOL_SYSTEM);
				break;
			case R.id.tv_major:
				intent.putExtra("curListView", ChoiceListActivity.SHOW_MAJOR);
				startActivityForResult(intent, ChoiceListActivity.SHOW_MAJOR);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case ChoiceListActivity.SHOW_STUDENT_PROVINCE:
			tvStudentLocation.setText(data.getStringExtra("provinceName"));
			studentProvince = data.getStringExtra("provinceCode");
			break;
		case ChoiceListActivity.SHOW_SCHOOL_PROVINCE:
			tvSchoolLocation.setText(data.getStringExtra("provinceName"));
			schoolProvince = data.getStringExtra("provinceCode");
			break;
		case ChoiceListActivity.SHOW_SCHOOL_SYSTEM:
			tvSchoolSystem.setText(data.getStringExtra("schoolSystemName"));
			schoolSystem = data.getStringExtra("schoolSystemCode");
			break;
		case ChoiceListActivity.SHOW_SCHOOL_TYPE:
			tvSchoolType.setText(data.getStringExtra("schoolTypeName"));
			schoolType = data.getStringExtra("schoolTypeCode");
			break;
		case ChoiceListActivity.SHOW_MAJOR:
			firstLevelMajor = data.getStringExtra("firstLevelMajorCode");
			secondLevelMajor = data.getStringExtra("secondLevelMajorCode");
			if("0".equals(secondLevelMajor))
				tvMajor.setText(data.getStringExtra("firstLevelMajorName"));
			else
				tvMajor.setText(data.getStringExtra("secondLevelMajorName"));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_deviation:
			new AlertDialog.Builder(getActivity()).setTitle(R.string.score_deviation).
			setItems(deviations, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					scoreDeviation = which*5;
					tvDeviation.setText(deviations[which]);
					dialog.cancel();
				}
			})
			.setNegativeButton(
			"取消",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show();

			break;
		case R.id.tv_branch:
			new AlertDialog.Builder(getActivity()).setTitle(R.string.student_branch)
			.setSingleChoiceItems(branchs, branch,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					branch = which;
					tvBranch.setText(branchs[which]);
					dialog.cancel();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show();
			break;
		case R.id.tv_confirm:
			doSearch();
			break;
		default:
			break;
		}
	}

	@Override
	public void initSubtitleMenu() {
		// TODO Auto-generated method stub
		
	}

}
