package cn.taoschool.listener;

import java.util.List;

public interface IDetailActivityFragmentListener {
	public void OnGetImgUrl(List<String> imgUrls);
	public void OnGetDetailInfo(boolean isSuccess, Object result);
}
