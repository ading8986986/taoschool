package cn.taoschool.listener;

public interface IDetailActivityReqListener {
	public void OnGetImgUrl(String[] imgUrls);
	public void OnGetDetailInfo(boolean isSuccess, Object result);
}
