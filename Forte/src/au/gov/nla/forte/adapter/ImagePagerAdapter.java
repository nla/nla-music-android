package au.gov.nla.forte.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import au.gov.nla.forte.R;
import au.gov.nla.forte.db.Page;
import au.gov.nla.forte.task.ImageDownloaderTask;

public class ImagePagerAdapter extends PagerAdapter {
	
	private ArrayList<Page> mImages;
	private LayoutInflater layoutInflater;
    
    public ImagePagerAdapter(Context context, ArrayList<Page> mImages) {
    	this.mImages = mImages;
    	this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
      return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.score_image, null);
    	Page page = mImages.get(position);
    	new ImageDownloaderTask(imageView).execute(page.getImageUrl());
    	((ViewPager) container).addView(imageView, 0);
    	return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      ((ViewPager) container).removeView((ImageView) object);
    }
 }
