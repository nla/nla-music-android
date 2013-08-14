package au.gov.nla.forte.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import au.gov.nla.forte.model.Page;
import au.gov.nla.forte.model.Score;

public class ForteDBHelper {
	
	private final static String PAGE_TABLE = "ZPAGE";
	private final static String PAGE_ID = "_id";
	private final static String PAGE_NUMBER = "ZNUMBER";
	private final static String PAGE_SCORE = "ZSCORE";
	private final static String PAGE_IDENTIFIER = "ZIDENTIFIER";
	
	private final static String SCORE_TABLE = "ZSCORE";
	private final static String SCORE_ID = "_id";
	private final static String SCORE_CREATOR = "ZCREATOR";
	private final static String SCORE_IDENTIFIER = "ZIDENTIFIER";
	private final static String SCORE_PUBLISHER = "ZPUBLISHER";
	private final static String SCORE_TITLE = "ZTITLE";
	private final static String SCORE_SORTTITLE = "ZSORTTITLE";
	private final static String SCORE_DATE = "ZDATE";
	
	private SQLiteDatabase db;
	private Context ctx;

	public ForteDBHelper(Context ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * Counts all the scores in the SCORE_TABLE
	 */
	public int countAllScores() {
		return executeCountQuery("SELECT count(*) FROM " + SCORE_TABLE);
	}
	
	/**
	 * Counts all the scores in the SCORE_TABLE by year
	 */
	public int countScoresByYear(String year) {
		return executeCountQuery("SELECT count(*) FROM " + SCORE_TABLE + " WHERE " + SCORE_DATE + " = '" + year + "'");
	}

	/**
	 *  Find All Scores By Year Order By Sort Title ASC
	 */
	public ArrayList<Score> findScoresByYearOrderBySortTitle(String year) {
		return executeScoreSelectQuery("SELECT * FROM " + SCORE_TABLE + 
				" WHERE " + SCORE_DATE + " = '" + year + "'" +
				" ORDER BY " + SCORE_SORTTITLE + " COLLATE NOCASE");
	}
	
	/**
	 * Find Pages for a Score
	 */
	public ArrayList<Page> findPagesByScoreIdOrderByNumber(String scoreId) {
			return executePageSelectQuery("SELECT * FROM " + PAGE_TABLE + 
					" WHERE " + PAGE_SCORE + " = '" + scoreId + "'" +
					" ORDER BY " + PAGE_NUMBER);
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
	
	private ArrayList<Score> executeScoreSelectQuery(String sql) {
		openDatabase(true);
		Cursor cursor = db.rawQuery(sql, null);
		
		final int idIndex = cursor.getColumnIndexOrThrow(SCORE_ID);
		final int creatorIndex = cursor.getColumnIndexOrThrow(SCORE_CREATOR);
		final int identifierIndex = cursor.getColumnIndexOrThrow(SCORE_IDENTIFIER);
		final int publisherIndex = cursor.getColumnIndexOrThrow(SCORE_PUBLISHER);
		final int titleIndex = cursor.getColumnIndexOrThrow(SCORE_TITLE);
		final int sortTitleIndex = cursor.getColumnIndexOrThrow(SCORE_SORTTITLE);
		final int dateIndex = cursor.getColumnIndexOrThrow(SCORE_DATE);

		ArrayList<Score> list = new ArrayList<Score>();
		try {
			if (cursor != null && cursor.getCount() >= 1) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					Score score = new Score();
					score.setId(""+cursor.getInt(idIndex));
					score.setCreator(cursor.getString(creatorIndex));
					score.setIdentifier(cursor.getString(identifierIndex));
					score.setPublisher(cursor.getString(publisherIndex));
					score.setTitle(cursor.getString(titleIndex));
					score.setSorttitle(cursor.getString(sortTitleIndex));
					score.setDate(cursor.getString(dateIndex));
					list.add(score);
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
	
	private ArrayList<Page> executePageSelectQuery(String sql) {
		openDatabase(true);
		Cursor cursor = db.rawQuery(sql, null);
		
		final int idIndex = cursor.getColumnIndexOrThrow(PAGE_ID);					
		final int numberIndex = cursor.getColumnIndexOrThrow(PAGE_NUMBER);
		final int scoreIndex = cursor.getColumnIndexOrThrow(PAGE_SCORE);
		final int identifierIndex = cursor.getColumnIndexOrThrow(PAGE_IDENTIFIER);
		
		ArrayList<Page> list = new ArrayList<Page>();
		try {
			if (cursor != null && cursor.getCount() >= 1) {
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					
					Page page = new Page();
					page.setId(""+cursor.getInt(idIndex));
					page.setNumber(cursor.getString(numberIndex));
					page.setScore(cursor.getString(scoreIndex));
					page.setIdentifier(cursor.getString(identifierIndex));										
					list.add(page);
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


	private void openDatabase(boolean readonly) {
		closeDatabase();
		if (readonly) {
			db = (new ForteDB(ctx)).getReadableDatabase();
		} else {
			db = (new ForteDB(ctx)).getWritableDatabase();
		}
	}

	private void closeDatabase() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

}
