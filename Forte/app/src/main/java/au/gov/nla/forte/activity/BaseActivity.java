package au.gov.nla.forte.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;

import au.gov.nla.forte.R;
import au.gov.nla.forte.util.Dialog;

import android.support.v7.app.ActionBarActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends ActionBarActivity {
	
	private boolean canGoBack = false;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected String filesDir;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filesDir = getApplicationContext().getFilesDir().getPath();
	}
	
	protected String getFileURI(String fileName) {
		return "file://" + filesDir + "/" + fileName;
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		if (canGoBack && getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	protected boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getActiveNetworkInfo() == null) {
			Dialog.createAlert(this, "Connection failed.", "Please check your internet connection.", true, "Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	

	public void showBackButton() {
		canGoBack = true;
	}

	/**
	 * By default the material design has removed the application logo
	 * This method returns it.
	 */
	protected void addLogoToActionBar() {
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
				ActionBar.DISPLAY_SHOW_TITLE |
				ActionBar.DISPLAY_USE_LOGO);
		getSupportActionBar().setIcon(R.drawable.icon_app);
	}
	
}
