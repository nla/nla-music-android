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
import au.gov.nla.forte.model.Favourite;
import au.gov.nla.forte.model.Page;
import au.gov.nla.forte.model.ScoreMetadata;
import au.gov.nla.forte.task.ImageDownloaderTask;
import au.gov.nla.forte.task.XmlOaiDownloaderTask;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class ScoreActivity extends GlobalActivity {
	
	public static final String SCORE_ID = "SCORE_ID";
	public static final String SCORE_IDENTIFIER = "SCORE_IDENTIFIER";
	
	private String scoreId;
	private String pid;
	private int totalPages;
	private boolean isActionBarShowing;
	private ScoreMetadata scoreMetadata;
 
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
        ArrayList<Page> pages = getPagesOfScore(scoreId);
        totalPages = pages.size();
        
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
        
        updateTitleWithCurrentPageNumber("1");
        getScoreMetadata(pid);
    }
    
    private void getScoreMetadata(String pid) {
    	scoreMetadata = new ScoreMetadata();
    	new XmlOaiDownloaderTask(scoreMetadata).execute(Nla.getOaiUrl(pid));
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
	    // Do as Task
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
        	
        	new ImageDownloaderTask(imageView).execute(Nla.getImageUrl(page.getIdentifier()));
        	((ViewPager) container).addView(imageView, 0);
        	return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
          ((ViewPager) container).removeView((ImageView) object);
        }
     }

 // Old Way using horizontal view thing
//  @Override
//  public void onCreate(Bundle savedInstanceState) {
//      super.onCreate(savedInstanceState);
//      
//      setContentView(R.layout.activity_score);
//      myGallery = (LinearLayout)findViewById(R.id.mygallery);
//      scoreId = getIntent().getExtras().getString(SCORE_ID);
//      layoutInflater = LayoutInflater.from(this);
//      
//      ArrayList<Page> pages = getPagesOfScore(scoreId);
//      for (Page page : pages){
//      	System.out.println("******** identifier=" + page.getIdentifier() + " score=" + page.getScore() + " number="+page.getNumber());
//      	myGallery.addView(insertPage(page));
//      }
//  }
//  
//  private View insertPage(Page page){    	
//  	ImageView view = (ImageView) layoutInflater.inflate(R.layout.score_image, null);    	
//  	new ImageDownloaderTask(view).execute(page.getImageUrl());    	
//  	return view;
//  }
    
}
