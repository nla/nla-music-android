package au.gov.nla.forte.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavouritesDB extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "favourites";
	private static final int DATABASE_VERSION = 1;

	public FavouritesDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
			 String sql = "CREATE TABLE IF NOT EXISTS " + FavouritesDBHelper.FAVOURITES_TABLE + 
					 " ( " + FavouritesDBHelper.FAVOURITES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
					 FavouritesDBHelper.FAVOURITES_SCORE + " TEXT, " + 
					 FavouritesDBHelper.FAVOURITES_IDENTIFIER + " TEXT, " + 
					 FavouritesDBHelper.FAVOURITES_CREATOR + " TEXT, " +
					 FavouritesDBHelper.FAVOURITES_PUBLISHER + " TEXT, " +
					 FavouritesDBHelper.FAVOURITES_TITLE + " TEXT, " + 
					 FavouritesDBHelper.FAVOURITES_DATE + " TEXT, " +
					 FavouritesDBHelper.FAVOURITES_DESCRIPTION + " TEXT)";
			 db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTable(db);
		onCreate(db);
	}
	
	public void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + FavouritesDBHelper.FAVOURITES_TABLE);
	}
	
}