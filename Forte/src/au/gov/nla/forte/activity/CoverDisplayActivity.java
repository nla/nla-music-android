package au.gov.nla.forte.activity;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import au.gov.nla.forte.R;
import au.gov.nla.forte.adapter.CustomListAdapter;
import au.gov.nla.forte.constant.ScoreCount;
import au.gov.nla.forte.db.ForteDBHelper;
import au.gov.nla.forte.model.Score;

//https://github.com/nostra13/Android-Universal-Image-Loader - This looks really good.
//http://javatechig.com/android/asynchronous-image-loader-in-android-listview/
//ALSO http://android-developers.blogspot.com.au/2010/07/multithreading-for-performance.html

/**
 * Displays the covers for the scores for the decades.
 */
public class CoverDisplayActivity extends BaseActivity {
	
	public static final String YEAR = "year";
	public static final String DEFAULT_YEAR = "0";
	
	private DisplayImageOptions displayImageOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_display);
        
        displayImageOptions = new DisplayImageOptions.Builder()
				//.showImageOnLoading(R.drawable.image_thumbnail_placeholder)
				.showImageForEmptyUri(R.drawable.image_thumbnail_placeholder)
				.showImageOnFail(R.drawable.image_thumbnail_placeholder)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				//.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
      
        String year = getIntent().getExtras().getString(YEAR);
        if (year.equals(DEFAULT_YEAR)) {
        	findViewById(R.id.custom_list).setVisibility(View.INVISIBLE);
        } else {
        	findViewById(R.id.nla_logo).setVisibility(View.INVISIBLE);
        	displayYearList(year);
        }
              
        addNavigationButtons();
    }
    
    private void addNavigationButtons() {
    	
    	findViewById(R.id.favourites).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				goToFavouritesActivity();
			}
		});
    	
    	ScoreCount scoreCount = new ScoreCount();
    	final int minButtonHeight = (int) (getResources().getDimension(R.dimen.fav_nav_height));
    	
    	for (int i = 1800; i <= 1970; i=i+10) {    		
    		int height = scoreCount.calculateHeight(i, minButtonHeight);
    		
    		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.year_list);
    		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
    	    params.setMargins(0, 0, 0, 0);
    	    
    	    LayoutInflater.from(this).inflate(R.layout.year_button, null);
    	    
    	    final int buttonId = i; 
    	    Button button = (Button)LayoutInflater.from(this).inflate(R.layout.year_button, null);
    	    button.setId(i);
    	    button.setText(""+i);
    	    linearLayout.addView(button, params);
    	    button.setOnClickListener(new View.OnClickListener() {
    	    	public void onClick(View view) {
    	    		refreshActivityForYear("" + buttonId);
    	    	}
    	    });
    	}
    	
    }
    
    private void displayYearList(String year) {
    	
        ArrayList<Score> scores = getScoresForYear(year);
        final ListView lv1 = (ListView) findViewById(R.id.custom_list);
        lv1.setAdapter(new CustomListAdapter(this, scores, displayImageOptions, imageLoader));
        lv1.setFastScrollEnabled(true);
        lv1.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                Score score = (Score) o;
                goToScoreActivity(score.getId(), score.getIdentifier());
            }
 
        });
        
        setTitle(getTitle(year, scores.size()));
    }
    
    private void goToScoreActivity(String id, String identifier) {
    	Intent i = new Intent(CoverDisplayActivity.this, ScoreActivity.class);
		i.putExtra(ScoreActivity.SCORE_ID, id);
		i.putExtra(ScoreActivity.SCORE_IDENTIFIER, identifier);
		i.putExtra(ScoreActivity.FAVOURITE, "false");
		startActivity(i);
    }
    
    private void goToFavouritesActivity() {
    	Intent i = new Intent(CoverDisplayActivity.this, FavouritesActivity.class);
		startActivity(i);
    }
    
    private void refreshActivityForYear(String year) {
		Intent i = new Intent(this, CoverDisplayActivity.class);
		i.putExtra(CoverDisplayActivity.YEAR, year);
		startActivity(i);
		finish();
//    	findViewById(R.id.custom_list).setVisibility(View.VISIBLE);
//		findViewById(R.id.nla_logo).setVisibility(View.INVISIBLE);
//		displayYearList(year);
	}
    
    private String getTitle(String year, int total) {
    	int startYear = Integer.parseInt(year);
    	int endYear = startYear + 9;
    	return year + "-" + endYear + " (" + total + " items)";
    }
    
    private ArrayList<Score> getScoresForYear(String year) {
    	ForteDBHelper db = new ForteDBHelper(this);
    	return db.findScoresByYearOrderBySortTitle(year);
    }
 
 }