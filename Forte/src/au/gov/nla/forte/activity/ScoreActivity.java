package au.gov.nla.forte.activity;

import java.util.ArrayList;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import au.gov.nla.forte.R;
import au.gov.nla.forte.adapter.ImagePagerAdapter;
import au.gov.nla.forte.db.ForteDBHelper;
import au.gov.nla.forte.db.Page;

public class ScoreActivity extends GlobalActivity {
	
	public static final String SCORE_ID = "SCORE_ID";
	
	private String scoreId;
	private LayoutInflater layoutInflater;
	private LinearLayout myGallery;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackButton();
        setContentView(R.layout.activity_score_image_pager);
        scoreId = getIntent().getExtras().getString(SCORE_ID);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<Page> pages = getPagesOfScore(scoreId);
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, pages);
        viewPager.setAdapter(adapter);
        setTitle("(" + pages.size() + " pages)");
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
		} else if (id == R.id.menu_information) {
			// Popup something
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

// Old Way using horizontal view thing
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        setContentView(R.layout.activity_score);
//        myGallery = (LinearLayout)findViewById(R.id.mygallery);
//        scoreId = getIntent().getExtras().getString(SCORE_ID);
//        layoutInflater = LayoutInflater.from(this);
//        
//        ArrayList<Page> pages = getPagesOfScore(scoreId);
//        for (Page page : pages){
//        	System.out.println("******** identifier=" + page.getIdentifier() + " score=" + page.getScore() + " number="+page.getNumber());
//        	myGallery.addView(insertPage(page));
//        }
//    }
//    
//    private View insertPage(Page page){    	
//    	ImageView view = (ImageView) layoutInflater.inflate(R.layout.score_image, null);    	
//    	new ImageDownloaderTask(view).execute(page.getImageUrl());    	
//    	return view;
//    }
    
    private ArrayList<Page> getPagesOfScore(String scoreId) {
    	ForteDBHelper db = new ForteDBHelper(this);
    	return db.findPagesByScoreIdOrderByNumber(scoreId);
    }
    
}
