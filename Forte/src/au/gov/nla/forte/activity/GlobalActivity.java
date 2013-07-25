package au.gov.nla.forte.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.view.KeyEvent;
import au.gov.nla.forte.util.Dialog;

import com.actionbarsherlock.app.SherlockActivity;

public abstract class GlobalActivity extends SherlockActivity {
	
	private boolean canGoBack = false;

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

	public String getVersionName() {
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}
	
	private boolean isBelowVersion(int sdkVersion) {
		return android.os.Build.VERSION.SDK_INT < sdkVersion;
	}

	protected boolean isBelowIceCreamSandwich() {
		return isBelowVersion(android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH);
	}
	
	protected boolean isBelowHoneyComb() {
		return isBelowVersion(android.os.Build.VERSION_CODES.HONEYCOMB);
	}
	
}
