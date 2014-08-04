package com.example.rssreader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RSSDatabaseHandler extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static String DATABASE_PATH ;
	public static final String DATABASE_NAME = "rss.db";

	public static final String TABLE_INDIA_RSS = "india_rss";
	public static final String TABLE_ENTERTAINMENT = "entertainment";
	public static final String TABLE_TECHNOLOGY = "technology";
	public static final String TABLE_SPORTS = "sports";

	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_LINK = "link";
	public static final String KEY_RSS_LINK = "rss_link";
	public static final String KEY_DESCRIPTION = "description";
	
	public String TAG="test.";

	private SQLiteDatabase rssDatabase;
	public final Context rssContext;

	public RSSDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.rssContext = context;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		Log.i(TAG,"create database");
		if (dbExist) {
		}
		else {
			this.getReadableDatabase();
			try {
				copyDataBase();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Check if the database already exist to "avoid re-copying" the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		File dbFile = null;
		Log.i(TAG,"check database");
		try {
			// String myPath = DATABASE_PATH + DATABASE_NAME;
			// checkDB = SQLiteDatabase.openDatabase(myPath, null,
			// SQLiteDatabase.OPEN_READONLY);
			DATABASE_PATH= rssContext.getDatabasePath(DATABASE_NAME).toString();
			dbFile = new File(DATABASE_PATH /*+ DATABASE_NAME*/);
			return dbFile.exists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*if(checkDB != null){
		 checkDB.close();}
		 return checkDB != null ? true : false;*/
		return dbFile != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		InputStream myInput = rssContext.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH /*+ DATABASE_NAME*/;
		Log.d(TAG, "path: " + outFileName);
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		
		Log.i(TAG,"copy database");
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {

		String myPath = DATABASE_PATH /*+ DATABASE_NAME*/;
		Log.i(TAG,"open database, dbPath: " + myPath);
		//String myPath = "/data/data/com.rssnews/databases/rss";
		rssDatabase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		close();
	}

	@Override
	public synchronized void close() {
		if (rssDatabase != null && rssDatabase.isOpen()) {
			rssDatabase.close();
			super.close();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* String CREATE_INDIA_RSS_TABLE = "CREATE TABLE " + TABLE_INDIA_RSS +
		 * "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," +
		 * KEY_LINK + " TEXT," + KEY_RSS_LINK + " TEXT," + KEY_DESCRIPTION +
		 * " TEXT" + ")";
		 * String CREATE_ENTERTAINMENT_TABLE = "CREATE TABLE " +
		 * TABLE_ENTERTAINMENT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
		 * + " TEXT," + KEY_LINK + " TEXT," + KEY_RSS_LINK + " TEXT," +
		 * KEY_DESCRIPTION + " TEXT" + ")";
		 * String CREATE_TECHNOLOGY_TABLE =
		 * "CREATE TABLE " + TABLE_TECHNOLOGY + "(" + KEY_ID + " INTEGER," +
		 * KEY_TITLE + " TEXT," + KEY_LINK + " TEXT," + KEY_RSS_LINK + " TEXT,"
		 * + KEY_DESCRIPTION + " TEXT" + ")"; 
		 * String CREATE_SPORTS_TABLE
		 * = "CREATE TABLE " + TABLE_SPORTS + "(" + KEY_ID +
		 * " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_LINK + " TEXT,"
		 * + KEY_RSS_LINK + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")";
		 * db.execSQL(CREATE_INDIA_RSS_TABLE);
		 * db.execSQL(CREATE_ENTERTAINMENT_TABLE);
		 * db.execSQL(CREATE_SPORTS_TABLE);
		 * db.execSQL(CREATE_TECHNOLOGY_TABLE); onCreate(db); */
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/* db.execSQL("DROP TABLE IF EXISTS " + TABLE_INDIA_RSS);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTERTAINMENT);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_TECHNOLOGY);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPORTS); // Create
		 * tables again onCreate(db); */
	}

	
	/**
	 * Reading all rows from database
	 * */
	public List<WebSite> getIndiaSites() {
		List<WebSite> siteList = new ArrayList<WebSite>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectIndiaQuery = "SELECT  * FROM " + TABLE_INDIA_RSS
				+ " ORDER BY _id";

		Cursor cursorIndia = db.rawQuery(selectIndiaQuery, null);
		if (cursorIndia.moveToFirst()) {
			do {
				WebSite site = new WebSite();
				site.setId(Integer.parseInt(cursorIndia.getString(0)));
				site.setTitle(cursorIndia.getString(1));
				site.setLink(cursorIndia.getString(2));
				site.setRSSLink(cursorIndia.getString(3));
				site.setDescription(cursorIndia.getString(4));
				siteList.add(site);
			} while (cursorIndia.moveToNext());
		}
		cursorIndia.close();
		db.close();
		return siteList;
	}

	public List<WebSite> getEntertainmentSites() {
		List<WebSite> siteLists = new ArrayList<WebSite>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectWorldQuery = "SELECT  * FROM " + TABLE_ENTERTAINMENT
				+ " ORDER BY _id";

		Cursor cursorWorld = db.rawQuery(selectWorldQuery, null);
		if (cursorWorld.moveToFirst()) {
			do {
				WebSite site = new WebSite();
				site.setId(Integer.parseInt(cursorWorld.getString(0)));
				site.setTitle(cursorWorld.getString(1));
				site.setLink(cursorWorld.getString(2));
				site.setRSSLink(cursorWorld.getString(3));
				site.setDescription(cursorWorld.getString(4));
				siteLists.add(site);
			} while (cursorWorld.moveToNext());
		}
		cursorWorld.close();
		db.close();
		return siteLists;
	}

	public List<WebSite> getTechnologySites() {
		List<WebSite> siteLists = new ArrayList<WebSite>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectFavQuery = "SELECT  * FROM " + TABLE_TECHNOLOGY
				+ " ORDER BY _id DESC";

		Cursor cursorFav = db.rawQuery(selectFavQuery, null);
		if (cursorFav.moveToFirst()) {
			do {
				WebSite site = new WebSite();
				site.setId(Integer.parseInt(cursorFav.getString(0)));
				site.setTitle(cursorFav.getString(1));
				site.setLink(cursorFav.getString(2));
				site.setRSSLink(cursorFav.getString(3));
				site.setDescription(cursorFav.getString(4));
				siteLists.add(site);
			} while (cursorFav.moveToNext());
		}
		cursorFav.close();
		db.close();
		return siteLists;
	}
	
	public List<WebSite> getSportsSites() {
		List<WebSite> siteLists = new ArrayList<WebSite>();
		SQLiteDatabase db = this.getReadableDatabase();
		String selectFavQuery = "SELECT  * FROM " + TABLE_SPORTS
				+ " ORDER BY _id DESC";

		Cursor cursorFav = db.rawQuery(selectFavQuery, null);
		if (cursorFav.moveToFirst()) {
			do {
				WebSite site = new WebSite();
				site.setId(Integer.parseInt(cursorFav.getString(0)));
				site.setTitle(cursorFav.getString(1));
				site.setLink(cursorFav.getString(2));
				site.setRSSLink(cursorFav.getString(3));
				site.setDescription(cursorFav.getString(4));
				siteLists.add(site);
			} while (cursorFav.moveToNext());
		}
		cursorFav.close();
		db.close();
		return siteLists;
	}

	

	

	/**
	 * Reading a row (website) row is identified by row id
	 * */
	public WebSite getIndiaRow(int _id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(
				TABLE_INDIA_RSS,
				new String[] { KEY_ID, KEY_TITLE, KEY_LINK, KEY_RSS_LINK,
						KEY_DESCRIPTION }, KEY_ID + "=?",
				new String[] { String.valueOf(_id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		WebSite site = new WebSite(cursor.getString(1), cursor.getString(2),
				cursor.getString(3), cursor.getString(4));
		site.setId(Integer.parseInt(cursor.getString(0)));
		site.setTitle(cursor.getString(1));
		site.setLink(cursor.getString(2));
		site.setRSSLink(cursor.getString(3));
		site.setDescription(cursor.getString(4));
		cursor.close();
		db.close();
		return site;
	}

	public WebSite getEntertainmentRow(int _id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorworld = db.query(TABLE_ENTERTAINMENT, new String[] { KEY_ID,
				KEY_TITLE, KEY_LINK, KEY_RSS_LINK, KEY_DESCRIPTION }, KEY_ID
				+ "=?", new String[] { String.valueOf(_id) }, null, null, null,
				null);
		if (cursorworld != null)
			cursorworld.moveToFirst();

		WebSite site = new WebSite(cursorworld.getString(1),
				cursorworld.getString(2), cursorworld.getString(3),
				cursorworld.getString(4));
		site.setId(Integer.parseInt(cursorworld.getString(0)));
		site.setTitle(cursorworld.getString(1));
		site.setLink(cursorworld.getString(2));
		site.setRSSLink(cursorworld.getString(3));
		site.setDescription(cursorworld.getString(4));
		cursorworld.close();
		db.close();
		return site;
	}

	public WebSite getTechnologyRow(int _id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorfav = db.query(TABLE_TECHNOLOGY, new String[] { KEY_ID,
				KEY_TITLE, KEY_LINK, KEY_RSS_LINK, KEY_DESCRIPTION }, KEY_ID
				+ "=?", new String[] { String.valueOf(_id) }, null, null, null,
				null);
		if (cursorfav != null)
			cursorfav.moveToFirst();

		WebSite site = new WebSite(cursorfav.getString(1),
				cursorfav.getString(2), cursorfav.getString(3),
				cursorfav.getString(4));
		site.setId(Integer.parseInt(cursorfav.getString(0)));
		site.setTitle(cursorfav.getString(1));
		site.setLink(cursorfav.getString(2));
		site.setRSSLink(cursorfav.getString(3));
		site.setDescription(cursorfav.getString(4));
		cursorfav.close();
		db.close();
		return site;
	}

	public WebSite getSportsRow(int _id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorrt = db.query(TABLE_SPORTS, new String[] { KEY_ID,
				KEY_TITLE, KEY_LINK, KEY_RSS_LINK, KEY_DESCRIPTION }, KEY_ID
				+ "=?", new String[] { String.valueOf(_id) }, null, null, null,
				null);
		if (cursorrt != null)
			cursorrt.moveToFirst();

		WebSite site = new WebSite(cursorrt.getString(1),
				cursorrt.getString(2), cursorrt.getString(3),
				cursorrt.getString(4));

		site.setId(Integer.parseInt(cursorrt.getString(0)));
		site.setTitle(cursorrt.getString(1));
		site.setLink(cursorrt.getString(2));
		site.setRSSLink(cursorrt.getString(3));
		site.setDescription(cursorrt.getString(4));
		cursorrt.close();
		db.close();
		return site;
	}

			
}