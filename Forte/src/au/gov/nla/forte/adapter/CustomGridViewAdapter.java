package au.gov.nla.forte.adapter;

import java.util.ArrayList;

import au.gov.nla.forte.R;
import au.gov.nla.forte.model.Favourite;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		//gridItem.image.setImageBitmap(item.getImage());

		return row;
	}

	static class GridItem {
		TextView label;
		ImageView image;
	}

}