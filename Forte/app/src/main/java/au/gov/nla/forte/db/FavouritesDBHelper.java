package au.gov.nla.forte.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import au.gov.nla.forte.model.Favourite;
import au.gov.nla.forte.model.Score;
import au.gov.nla.forte.model.ScoreMetadata;

public class FavouritesDBHelper {
	
	public final static String FAVOURITES_TABLE = "FAVOURITES";
	public final static String FAVOURITES_ID = "_id";
	public final static String FAVOURITES_SCORE = "SCORE";
	public final static String FAVOURITES_IDENTIFIER = "IDENTIFIER";
	public final static String FAVOURITES_CREATOR = "CREATOR";
	public final static String FAVOURITES_PUBLISHER = "PUBLISHER";
	public final static String FAVOURITES_TITLE = "TITLE";
	public final static String FAVOURITES_DATE = "DATE";
	public final static String FAVOURITES_DESCRIPTION = "DESCRIPTION";
	
	private SQLiteDatabase db;
	private Context ctx;

	public FavouritesDBHelper(Context ctx) {
		this.ctx = ctx;
	}
	
	public long create(Favourite fav) {
		openDatabase(false);
		long id = db.insert(FAVOURITES_TABLE, null, getContentValues(fav));
		closeDatabase();
		return id;
	}
	
	public boolean delete(Favourite fav) {
		String where = FAVOURITES_ID + " = " + fav.getId();
		openDatabase(false);
		int rowsDeleted = db.delete(FAVOURITES_TABLE, where, null);
		closeDatabase();
		return rowsDeleted > 0;
	}
	
	public boolean deleteByScore(String score) {
		String where = FAVOURITES_SCORE + " = '" +score + "'";
		openDatabase(false);
		int rowsDeleted = db.delete(FAVOURITES_TABLE, where, null);
		closeDatabase();
		return rowsDeleted > 0;
	}
	
	public int countAllFavourites() {
		return executeCountQuery("SELECT count(*) FROM " + FAVOURITES_TABLE);
	}
	
	public ArrayList<Favourite> findAllFavouritesOrderByTitle() {
		return executeFavouritesSelectQuery("SELECT * FROM " + FAVOURITES_TABLE + 
				" ORDER BY " + FAVOURITES_TITLE + " COLLATE NOCASE");
	}
	
	public Favourite findByScore(String score) {
		ArrayList<Favourite> list = executeFavouritesSelectQuery("SELECT * FROM " + FAVOURITES_TABLE + 
				" WHERE " + FAVOURITES_SCORE + " = '" + score + "'");
		
		if (list.size() > 0) 
			return list.get(0);
		else
			return null;
	}
	
	private int executeCountQuery(String sql) {
		int count = 0;
		
		openDatabase(true);
		Cursor cursor = db.rawQuery(sql, null);
		
		try {
			if (cursor != null && cursor.getCount() >= 1) {
				cursor.moveToFirst();
				count = cursor.getInt(0);
			}
		} finally {
			cursor.close();
			closeDatabase();
		}
		
		return count;
	}
	
	private ArrayList<Favourite> executeFavouritesSelectQuery(String sql) {
		openDatabase(true);
		Cursor cursor = db.rawQuery(sql, null);
		
		final int idIndex = cursor.getColumnIndexOrThrow(FAVOURITES_ID);
		final int scoreIndex = cursor.getColumnIndexOrThrow(FAVOURITES_SCORE);
		final int identifierIndex = cursor.getColumnIndexOrThrow(FAVOURITES_IDENTIFIER);
		final int creatorIndex = cursor.getColumnIndexOrThrow(FAVOURITES_CREATOR);
		final int publisherIndex = cursor.getColumnIndexOrThrow(FAVOURITES_PUBLISHER);
		final int titleIndex = cursor.getColumnIndexOrThrow(FAVOURITES_TITLE);
		final int dateIndex = cursor.getColumnIndexOrThrow(FAVOURITES_DATE);
		final int descriptionIndex = cursor.getColumnIndexOrThrow(FAVOURITES_DESCRIPTION);
	
		ArrayList<Favourite> list = new ArrayList<Favourite>();
		try {
			if (cursor != null && cursor.getCount() >= 1) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					
					Favourite fav = new Favourite();
					fav.setId(""+cursor.getInt(idIndex));
					fav.setScore(cursor.getString(scoreIndex));
					fav.setIdentifier(cursor.getString(identifierIndex));
					
					ScoreMetadata scoreMetadata = new ScoreMetadata();
					scoreMetadata.setCreator(cursor.getString(creatorIndex));
					scoreMetadata.setPublisher(cursor.getString(publisherIndex));
					scoreMetadata.setTitle(cursor.getString(titleIndex));
					scoreMetadata.setDate(cursor.getString(dateIndex));
					scoreMetadata.setDescription(cursor.getString(descriptionIndex));
					scoreMetadata.setIdentifier(fav.getIdentifier());
					
					fav.setScoreMetadata(scoreMetadata);
					
					list.add(fav);
					cursor.moveToNext();
					if (cursor.isAfterLast()) {
						break;
					}
				}
			}
		} finally {
			cursor.close();
			closeDatabase();
		}
		return list;
	}
	
	private ContentValues getContentValues(Favourite fav) {
		ContentValues values = new ContentValues();
		
		values.put(FAVOURITES_SCORE, fav.getScore());
		values.put(FAVOURITES_IDENTIFIER, fav.getIdentifier());
	
		values.put(FAVOURITES_CREATOR, fav.getScoreMetadata().getCreator());
		values.put(FAVOURITES_PUBLISHER, fav.getScoreMetadata().getPublisher());
		values.put(FAVOURITES_TITLE, fav.getScoreMetadata().getTitle());
		values.put(FAVOURITES_DATE, fav.getScoreMetadata().getDate());
		values.put(FAVOURITES_DESCRIPTION, fav.getScoreMetadata().getDescription());
				
		return values;
	}

	private void openDatabase(boolean readonly) {
		closeDatabase();
		if (readonly) {
			db = (new FavouritesDB(ctx)).getReadableDatabase();
		} else {
			db = (new FavouritesDB(ctx)).getWritableDatabase();
		}
	}

	private void closeDatabase() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

}
