package com.example.rssreader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Technology extends Activity{
	
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> rssFeedList;
	RSSParser rssParser= new RSSParser();
	RSSFeed rssFeed;
	String[] sqliteIds;
	ListView lv;
	
	public static String TAG_ID="_id";
	public static String TAG_TITLE="title";
	public static String TAG_LINK= "link";
	
	private static final String TAG = "Test.";
	
	
	@Override
	protected void onCreate(Bundle savedInstancestate){
		super.onCreate(savedInstancestate);
		setContentView(R.layout.site_list);

		Log.i(TAG,"onCreate");
		rssFeedList = new ArrayList<HashMap<String, String>>();
		/*Background thread will load all websites stored in sqlite database*/
		
		new loadStoreSites().execute();
		lv =(ListView) findViewById(R.id.listView1);
		
		ListAdapter adapter= new SimpleAdapter(this, rssFeedList, R.layout.site_list_row, new String[] { TAG_ID, TAG_TITLE, TAG_LINK },
				new int[] { R.id.sqlite_id, R.id.title, R.id.link });
		lv.setAdapter(adapter);
		
	
		//1. get the item clicked i.e, india, entertainment
		//2. switch case --to get all the arralist of data
		//3. create a list adapter (arraylistadapter)
		//4. set that adapter to lv
	
	 lv.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			// getting values from selected ListItem
            String sqlite_id = ((TextView)view.findViewById(R.id.sqlite_id)).getText().toString();
            // Starting new intent
            Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity_T.class);
            // passing sqlite row id
            in.putExtra(TAG_ID, sqlite_id);
            startActivity(in);
			
		}
     });
      
	}
	
	
	class loadStoreSites extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute(){
			
		}

		@Override
		protected String doInBackground(String... arg0) {
			runOnUiThread(new Runnable(){

				@Override
				public void run() {
					Log.i(TAG,"invoking createDataBase");
					RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getApplicationContext());
					try{
						rssDb.createDataBase();
					}
					catch(IOException ioe){
						System.out.println("Unable to create a database");
						Log.i(TAG,"FAIL invoking createDataBase");
					}
					try{
						Log.i(TAG,"invoking openDataBase");
						rssDb.openDataBase();
					}
					catch(SQLException sqle){
						Log.i(TAG,"FAIL invoking openDataBase");
						System.out.println(sqle);
					}
					
					List<WebSite> siteList =rssDb.getTechnologySites();
					sqliteIds = new String [siteList.size()];
					
					for(int i=0;i<siteList.size(); i++){
						WebSite s= siteList.get(i);
						HashMap<String, String> map= new HashMap<String, String>();
						map.put(TAG_ID, s.getId().toString());
						map.put(TAG_TITLE, s.getTitle());
						map.put(TAG_LINK, s.getLink());
						Log.i(TAG,"website: "+s.getLink());
						rssFeedList.add(map);

						sqliteIds[i] = s.getId().toString();
					}
				}});
			return null;
		}
	
		protected void onPostExecute(){
			pDialog.dismiss();
		}
		
	}
		@Override
		public void onBackPressed(){
			finish();
		}
	

}

