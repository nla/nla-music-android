package au.gov.nla.forte.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

//http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/
//http://stackoverflow.com/questions/7458281/how-do-i-copy-a-pre-existing-sqlite-database-file-from-assets-to-database-folder


/**
 * @see https://github.com/jgilfelt/android-sqlite-asset-helper
 * for more details on the SQLiteAssetHelper
 */
public class ForteDB extends SQLiteAssetHelper {
	
	private static final String DATABASE_NAME = "forte";
	private static final int DATABASE_VERSION = 1;

	public ForteDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	}
	
	/**
	 * Call this method for SQLiteAssetHelper to copy the
	 * zipped forte.zip database into the correct Android
	 * location.
	 * 
	 * Call this only once when setting up the application.
	 */
	public void copyDatabase() {
		getReadableDatabase();
		close();
	}

}
