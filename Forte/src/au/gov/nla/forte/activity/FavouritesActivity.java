package au.gov.nla.forte.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import au.gov.nla.forte.R;
import au.gov.nla.forte.constant.Nla;
import au.gov.nla.forte.db.FavouritesDBHelper;
import au.gov.nla.forte.model.Favourite;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Displays the list of favourites that have been stored offline.
 */
public class FavouritesActivity extends BaseActivity  {
	
	private final int SCORE_REQUEST_CODE = 1;
	
	private GridView gridView;
	private CustomGridViewAdapter customGridAdapter;
	private ArrayList<Favourite> list;
	private DisplayImageOptions displayImageOptions;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        
        showBackButton();      
        setTitle("Favourites");
                
        displayImageOptions = new DisplayImageOptions.Builder()
		//.showImageOnLoading(R.drawable.image_thumbnail_placeholder)
		.showImageForEmptyUri(R.drawable.image_placeholder)
		.showImageOnFail(R.drawable.image_placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.build();
        
        filesDir = getApplicationContext().getFilesDir().getPath();
        
        list = getFavourites();
        
        gridView = (GridView) findViewById(R.id.grid_view);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.thumbnail_grid_item, list);
        gridView.setAdapter(customGridAdapter);        
        gridView.setOnItemClickListener(new OnItemClickListener() {			
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
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
		i.putExtra(ScoreActivity.FAVOURITE, "true");
		startActivityForResult(i, SCORE_REQUEST_CODE);
    }
    
    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData) {
    	// On return refresh the gridview as an item may have been deleted.
    	list = getFavourites();
    	customGridAdapter.notifyDataSetChanged();
    	customGridAdapter = new CustomGridViewAdapter(this, R.layout.thumbnail_grid_item, list);
    	gridView.invalidateViews();
    	gridView.setAdapter(customGridAdapter);
    }
    
    public class CustomGridViewAdapter extends ArrayAdapter<Favourite> {
    	
    	private Context context;
    	private ArrayList<Favourite> listData = new ArrayList<Favourite>();

    	public CustomGridViewAdapter(Context context, int layoutResourceId, 
    			ArrayList<Favourite> data) {
    		super(context, layoutResourceId, data);
    		this.context = context;
    		this.listData = data;
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View row = convertView;
    		GridItem gridItem = null;

    		if (row == null) {
    			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
    			row = inflater.inflate(R.layout.thumbnail_grid_item, parent, false);
    			gridItem = new GridItem();
    			gridItem.label = (TextView) row.findViewById(R.id.grid_item_label);
    			gridItem.image = (ImageView) row.findViewById(R.id.grid_item_image);
    			row.setTag(gridItem);
    		} else {
    			gridItem = (GridItem) row.getTag();
    		}

    		Favourite item = listData.get(position);
    		gridItem.label.setText(item.getScoreMetadata().getTitle());    		
    		imageLoader.displayImage(getFileURI(item.getIdentifier() + Nla.THUMBNAIL), gridItem.image, displayImageOptions); 

    		return row;
    	}

    	class GridItem {
    		TextView label;
    		ImageView image;
    	}

    }

}
