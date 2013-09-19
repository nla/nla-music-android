package au.gov.nla.forte.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import au.gov.nla.forte.R;
import au.gov.nla.forte.constant.Nla;
import au.gov.nla.forte.db.FavouritesDBHelper;
import au.gov.nla.forte.db.ForteDBHelper;
import au.gov.nla.forte.layout.SimpleViewPagerIndicator;
import au.gov.nla.forte.model.Favourite;
import au.gov.nla.forte.model.Page;
import au.gov.nla.forte.model.ScoreMetadata;
import au.gov.nla.forte.task.DeleteImageTask;
import au.gov.nla.forte.task.DownloadAndSaveImageTask;
import au.gov.nla.forte.task.XmlOaiDownloaderTask;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ScoreActivity extends BaseActivity {
	
	public static final String SCORE_ID = "SCORE_ID";
	public static final String SCORE_IDENTIFIER = "SCORE_IDENTIFIER";
	public static final String FAVOURITE = "FAVOURITE";
	
	private String scoreId;
	private String pid;
	private int totalPages;
	private ArrayList<Page> pages;
	private boolean isActionBarShowing;
	private ScoreMetadata scoreMetadata;
	private DisplayImageOptions displayImageOptions;
	
	private boolean isFromFavourites;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        showBackButton();
        getSupportActionBar().hide();
		isActionBarShowing = false;
		
        setContentView(R.layout.activity_score_image_pager);
        findViewById(R.id.score_metadata).setVisibility(View.INVISIBLE);
        
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.semi_transparent_background));
        
        scoreId = getIntent().getExtras().getString(SCORE_ID);
        pid = getIntent().getExtras().getString(SCORE_IDENTIFIER);
        isFromFavourites = Boolean.parseBoolean(getIntent().getExtras().getString(FAVOURITE));
        pages = getPagesOfScore(scoreId);
        totalPages = pages.size();
        
        displayImageOptions = new DisplayImageOptions.Builder()
				//.showImageOnLoading(R.drawable.image_thumbnail_placeholder)
				.showImageForEmptyUri(R.drawable.image_placeholder)
				.showImageOnFail(R.drawable.image_placeholder)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.build();
        
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
            	updateTitleWithCurrentPageNumber(""+(position+1));
            }
        });
        
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, pages);
        viewPager.setAdapter(adapter);
        
        SimpleViewPagerIndicator pageIndicator = (SimpleViewPagerIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setViewPager(viewPager);
        pageIndicator.notifyDataSetChanged();
        
        updateTitleWithCurrentPageNumber("1");
        setScoreMetadata();
    }
    
    private void setScoreMetadata() {
    	scoreMetadata = new ScoreMetadata();
    	if (isFromFavourites) {
    		FavouritesDBHelper db = new FavouritesDBHelper(this);
    		Favourite fav = db.findByScore(scoreId);
    		if (fav != null) scoreMetadata = fav.getScoreMetadata();
    	} else {    		
    		new XmlOaiDownloaderTask(scoreMetadata).execute(Nla.getOaiUrl(pid));
    	}
    }
    
    private void updateMetadataView() {
    	((TextView)findViewById(R.id.metadata_title)).setText(scoreMetadata.getTitle());
    	((TextView)findViewById(R.id.metadata_creator)).setText(scoreMetadata.getCreator());
    	((TextView)findViewById(R.id.metadata_description)).setText(scoreMetadata.getDescription());
    	((TextView)findViewById(R.id.metadata_date)).setText(scoreMetadata.getDate());
    	((TextView)findViewById(R.id.metadata_publisher)).setText(scoreMetadata.getPublisher());
    	
    	// Open item in browser
    	findViewById(R.id.view_nla_website).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Nla.getItemUrl(pid)));
				startActivity(browserIntent);				
			}
		});
    	
    	if (isScoreAFavourite()) {
    		ImageView img = (ImageView)findViewById(R.id.icon_score_fav);
    		img.setImageResource(R.drawable.icon_action_done);
    	}	
    	
    	// Save to Favourites
    	findViewById(R.id.icon_score_fav).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				ImageView img = (ImageView)findViewById(R.id.icon_score_fav);
				
				if (isScoreAFavourite()) {
					deleteFromFavourites();					
					img.setImageResource(R.drawable.icon_star_white);
					showToastMessageCentred("Removed from Favourites.");
				} else {
					addToFavourites();				
					img.setImageResource(R.drawable.icon_action_done);
					showToastMessageCentred("Added to Favourites.");
				}								
			}
		});
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_score, menu);
		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		} 
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	private void showOrHideActionBar() {		
		if (isActionBarShowing) {
			getSupportActionBar().hide();
			findViewById(R.id.score_metadata).setVisibility(View.INVISIBLE);
        	isActionBarShowing = false;                      
        } else {
        	getSupportActionBar().show();
        	updateMetadataView();
        	findViewById(R.id.score_metadata).setVisibility(View.VISIBLE);
        	isActionBarShowing = true;
        }			
	}
	
	private boolean isScoreAFavourite() {
		FavouritesDBHelper db = new FavouritesDBHelper(this);
		if (db.findByScore(this.scoreId) != null)
			return true;
		else
			return false;
	}
	
	private void deleteFromFavourites() {		
		// Remove from Database
		FavouritesDBHelper db = new FavouritesDBHelper(this);
		db.deleteByScore(this.scoreId);
		
		// Delete Files on device
		// Do as Task
		// Thumbnail
	    new DeleteImageTask().execute(filesDir, (pid + Nla.THUMBNAIL));
	    
	    // Pages
	    for (Page page:pages) {
	    	new DeleteImageTask().execute(filesDir, (page.getIdentifier() + Nla.EXAMINATION_COPY));
	    }
	}
	
	private void addToFavourites() {		
		// Add to Database
		FavouritesDBHelper db = new FavouritesDBHelper(this);	    	
	    Favourite f = new Favourite();
	    f.setIdentifier(this.pid);
	    f.setScore(this.scoreId);
	    f.setScoreMetadata(this.scoreMetadata);	    	
	    db.create(f);
	    
	    // Download and Save files    
	    // Thumbnail
	    new DownloadAndSaveImageTask(filesDir, (pid + Nla.THUMBNAIL)).execute(Nla.getThumbnailUrl(pid));
	    
	    // Pages
	    for (Page page:pages) {
	    	new DownloadAndSaveImageTask(filesDir, (page.getIdentifier() + Nla.EXAMINATION_COPY)).execute(Nla.getImageUrl(page.getIdentifier()));
	    }
	}
	
	private void showToastMessageCentred(String msg) {
		Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);  
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	private void updateTitleWithCurrentPageNumber(String num) {
		setTitle(num + " of " + totalPages);
	}
    
    private ArrayList<Page> getPagesOfScore(String scoreId) {
    	ForteDBHelper db = new ForteDBHelper(this);
    	return db.findPagesByScoreIdOrderByNumber(scoreId);
    }
    
    /**
     * PageAdpater that displays the individual pages 
     */
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
        	imageView.findViewById(R.id.scorePage).setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					showOrHideActionBar();					
				}
			});
        	
        	Page page = mImages.get(position);  
        	String imageURI;
        	if (isFromFavourites) {
        		imageURI = getFileURI(page.getIdentifier() + Nla.EXAMINATION_COPY);
        	} else {
        		imageURI = Nla.getImageUrl(page.getIdentifier());
        	}
        	imageLoader.displayImage(imageURI, imageView, displayImageOptions);
        	((ViewPager) container).addView(imageView, 0);
        	return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
          ((ViewPager) container).removeView((ImageView) object);
        }
     }

}
