package au.gov.nla.forte.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import au.gov.nla.forte.R;
import au.gov.nla.forte.adapter.CustomListAdapter;
import au.gov.nla.forte.db.ForteDBHelper;
import au.gov.nla.forte.db.Score;

//https://github.com/nostra13/Android-Universal-Image-Loader - This looks really good.
//http://javatechig.com/android/asynchronous-image-loader-in-android-listview/
//ALSO http://android-developers.blogspot.com.au/2010/07/multithreading-for-performance.html
public class CoverDisplayActivity extends GlobalActivity {
	
	public static final String YEAR = "year";
	public static final String DEFAULT_YEAR = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        String year = getIntent().getExtras().getString(YEAR);
        if (year.equals(DEFAULT_YEAR)) {
        	setContentView(R.layout.activity_start);
        } else {
        	displayYearList(year);
        	addNavigationYearButtons();
        }
        
    }
    
    private void addNavigationYearButtons() {
    	
    	for (int i = 1800; i <= 1970; i=i+10) {
    		LinearLayout ll = (LinearLayout)findViewById(R.id.year_list);
    	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    	            LinearLayout.LayoutParams.MATCH_PARENT,
    	            LinearLayout.LayoutParams.WRAP_CONTENT);
    	    params.setMargins(0, 0, 0, 0);
    	    
    	    Button btn = new Button(this);
    	    btn.setId(i);
    	    final int id_ = btn.getId();
    	    btn.setText(""+id_);
    	    ll.addView(btn, params);
    	    Button btn1 = ((Button) findViewById(id_));
    	    btn1.setOnClickListener(new View.OnClickListener() {
    	    	public void onClick(View view) {
    	    		refreshActivityForYear("" + id_);
    	    	}
    	    });
    	}
    	
    }
    
    private void displayYearList(String year) {
    	setContentView(R.layout.activity_cover_display);
        ArrayList<Score> scores = getScoresForYear(year);
        final ListView lv1 = (ListView) findViewById(R.id.custom_list);
        lv1.setAdapter(new CustomListAdapter(this, scores));
        lv1.setFastScrollEnabled(true);
        lv1.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv1.getItemAtPosition(position);
                Score score = (Score) o;
                goToScoreActivity(score.getId());
            }
 
        });
        
        setTitle(getTitle(year, scores.size()));
    }
    
    private void goToScoreActivity(String id) {
    	Intent i = new Intent(CoverDisplayActivity.this, ScoreActivity.class);
		i.putExtra(ScoreActivity.SCORE_ID, id);
		startActivity(i);
    }
    
    private void refreshActivityForYear(String year) {
		Intent i = new Intent(this, CoverDisplayActivity.class);
		i.putExtra(CoverDisplayActivity.YEAR, year);
		startActivity(i);
		finish();
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