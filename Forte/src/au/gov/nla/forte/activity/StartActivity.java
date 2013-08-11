package au.gov.nla.forte.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import au.gov.nla.forte.R;
import au.gov.nla.forte.db.ForteDB;
import au.gov.nla.forte.util.Preferences;
import au.gov.nla.forte.util.PreferencesEnum;

public class StartActivity extends GlobalActivity {
	
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		int forteDBCopied = Integer.parseInt(Preferences.getPreference(this, PreferencesEnum.FORTE_DATABASE_COPIED, "0"));
		if (forteDBCopied == 0) {
			CopyForteDB task = new CopyForteDB(this);
			task.execute();
		} else {
			goToCoverDisplayActivity();
		}
		
	}
	
	private void goToCoverDisplayActivity() {
		Intent i = new Intent(this, CoverDisplayActivity.class);
		i.putExtra(CoverDisplayActivity.YEAR, CoverDisplayActivity.DEFAULT_YEAR);
		startActivity(i);
		finish();
	}
	
	/**
	 * Background task to copy the Forte database
	 */
	private class CopyForteDB extends AsyncTask<Void, Void, Boolean> {

		private Context ctx;

		public CopyForteDB(Context ctx) {
			this.ctx = ctx;
		}
		
		@Override
	    protected void onPreExecute() {
	        progressDialog = new ProgressDialog(StartActivity.this);
	        progressDialog.setMessage("Configuring database ...");
	        progressDialog.setCancelable(false);
	        progressDialog.show();
	    }

		@Override
		protected Boolean doInBackground(Void... arg0) {		
			ForteDB db = new ForteDB(ctx);
			db.copyDatabase();
			Preferences.putPreference(ctx, PreferencesEnum.FORTE_DATABASE_COPIED, "1");			
			return true;
		}
		
		@Override
	    protected void onPostExecute(Boolean result) {
	        progressDialog.cancel();
	        goToCoverDisplayActivity();
	    }
 
	}
}
