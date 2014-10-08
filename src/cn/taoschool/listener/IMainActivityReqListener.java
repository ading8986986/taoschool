package cn.taoschool.listener;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import cn.taoschool.bean.SchoolItem;

public interface IMainActivityReqListener {
	void onSchoolListObtained(boolean isSuccess, List<SchoolItem> result);
	void OnImageInitialized(Object result);
	void onCheckForUpdateObtained(boolean update,JSONObject result);
}
