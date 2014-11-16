package cn.taoschool.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.taoschool.R;
import cn.taoschool.ui.fragment.BasicDetailFragment;
import cn.taoschool.ui.fragment.DetailOfSchoolFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BigImageViewPagerAdapter extends PagerAdapter{

	private List<String> mImgUrlList;
	private List<ImageView> mImageViews = new ArrayList<ImageView>();;
	private LinearLayout ImagebottomView;
	private Context context;
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public BigImageViewPagerAdapter(Context context,List<String> list,LinearLayout ImagebottomView) {
		mImgUrlList = list;
		this.context = context;
		this.ImagebottomView = ImagebottomView;
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.pic_default)
		.showImageOnFail(R.drawable.pic_default).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(10))
		.build();
		
		for(int i = 0;i<getCount();i++){
			ImageView view = new ImageView(context);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageLoader.displayImage(mImgUrlList.get(i), view, options);
			mImageViews.add(view);
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImgUrlList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(mImageViews.get(position));
		return mImageViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mImageViews.get(position));
	}
}
