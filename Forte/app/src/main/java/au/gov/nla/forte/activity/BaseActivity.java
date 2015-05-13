package au.gov.nla.forte.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import au.gov.nla.forte.util.Dialog;

import com.actionbarsherlock.app.SherlockActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends SherlockActivity {
	
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
	
}
