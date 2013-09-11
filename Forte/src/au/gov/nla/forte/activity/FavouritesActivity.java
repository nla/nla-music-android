package au.gov.nla.forte.activity;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;
import au.gov.nla.forte.R;
import au.gov.nla.forte.adapter.CustomGridViewAdapter;
import au.gov.nla.forte.db.FavouritesDBHelper;
import au.gov.nla.forte.db.ForteDBHelper;
import au.gov.nla.forte.model.Favourite;
import au.gov.nla.forte.model.Score;
import au.gov.nla.forte.model.ScoreMetadata;
import au.gov.nla.forte.util.Dialog;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class FavouritesActivity extends GlobalActivity  {
	
	private GridView gridView;
	private CustomGridViewAdapter customGridAdapter;
	private ArrayList<Favourite> list;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        
        showBackButton();      
        setTitle("Favourites");
        
        list = getFavourites();
        
        gridView = (GridView) findViewById(R.id.grid_view);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.thumbnail_grid_item, list);
        gridView.setAdapter(customGridAdapter);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {
			
        	public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
        		//Toast.makeText(getApplicationContext(), list.get(position).getScoreMetadata().getTitle(), Toast.LENGTH_SHORT).show();
        		goToScoreActivity(list.get(position).getScore(), list.get(position).getIdentifier());
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
    
    private ArrayList<Favourite> getFavourites() {
    	FavouritesDBHelper db = new FavouritesDBHelper(this);
    	return db.findAllFavouritesOrderByTitle();
    }
    
    private void goToScoreActivity(String id, String identifier) {
    	Intent i = new Intent(FavouritesActivity.this, ScoreActivity.class);
		i.putExtra(ScoreActivity.SCORE_ID, id);
		i.putExtra(ScoreActivity.SCORE_IDENTIFIER, identifier);
		startActivity(i);
    }

}
