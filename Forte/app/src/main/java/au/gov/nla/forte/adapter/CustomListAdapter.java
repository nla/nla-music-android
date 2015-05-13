package au.gov.nla.forte.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import au.gov.nla.forte.R;
import au.gov.nla.forte.constant.Nla;
import au.gov.nla.forte.model.Score;
import au.gov.nla.forte.task.ImageDownloaderTask;
import au.gov.nla.forte.woozzu.StringMatcher;
 
public class CustomListAdapter extends BaseAdapter implements SectionIndexer {
	
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private ArrayList<Score> listData;
    private LayoutInflater layoutInflater;
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader;
 
    public CustomListAdapter(Context context, ArrayList<Score> listData, 
    		DisplayImageOptions displayImageOptions, ImageLoader imageLoader) {
        this.listData = listData;
        this.layoutInflater = LayoutInflater.from(context);
        this.displayImageOptions = displayImageOptions;
        this.imageLoader = imageLoader;
    }
    
    // START ALPHABET THING
    @Override
	public int getPositionForSection(int section) {
		// If there is no item for current section, previous section will be selected
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				Score score = (Score) getItem(j);
				String sortString = score.getSorttitle();
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(String.valueOf(sortString.charAt(0)), String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(String.valueOf(sortString.charAt(0)), String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}
    
    @Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}
	//////// END 
 
    @Override
    public int getCount() {
        return listData.size();
    }
 
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.title);
            holder.creatorView = (TextView) convertView.findViewById(R.id.creator);
            holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
            convertView.setTag(holder);
            
        } else {        	
            holder = (ViewHolder) convertView.getTag();            
        }
        
        Score score = (Score) listData.get(position);
 
        holder.titleView.setText(score.getTitle());
        holder.creatorView.setText(score.getCreator());

        System.out.println("******* loading image from " + Nla.getThumbnailUrl(score.getIdentifier()));
        
        imageLoader.displayImage(Nla.getThumbnailUrl(score.getIdentifier()), holder.imageView, displayImageOptions);
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView titleView;
        TextView creatorView;
        ImageView imageView;
    }
}
