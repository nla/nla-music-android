package au.gov.nla.forte.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import au.gov.nla.forte.R;
import au.gov.nla.forte.db.FavouritesDBHelper;
import au.gov.nla.forte.db.ForteDBHelper;
import au.gov.nla.forte.model.Favourite;
import au.gov.nla.forte.model.Score;
import au.gov.nla.forte.model.ScoreMetadata;
import au.gov.nla.forte.util.Dialog;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class FavouritesActivity extends BaseActivity  {
	
	private GridView gridView;
	private CustomGridViewAdapter customGridAdapter;
	private ArrayList<Favourite> list;
	private DisplayImageOptions displayImageOptions;
	private String filesDir;
	
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
		//.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
        
        filesDir = getApplicationContext().getFilesDir().getPath();
        
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
    
    public class CustomGridViewAdapter extends ArrayAdapter<Favourite> {
    	
    	private Context context;
    	private int layoutResourceId;
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
    		String imageUri = "file://" + filesDir + "/" + item.getIdentifier() + "-t";
    		imageLoader.displayImage(imageUri, gridItem.image, displayImageOptions); 

    		return row;
    	}

    	class GridItem {
    		TextView label;
    		ImageView image;
    	}

    }

}
