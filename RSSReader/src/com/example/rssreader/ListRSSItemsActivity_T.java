package com.example.rssreader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.rssreader.ListRSSItemsActivity.loadRSSFeedItems;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListRSSItemsActivity_T extends Activity {
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String, String>>();
	RSSParser rssParser = new RSSParser();
	List<RSSItem> rssItems = new ArrayList<RSSItem>();
	String rss_link;
	RSSFeed rssFeed;
	ListView lv;
	private static final String TAG = "tag";

	private static String TAG_TITLE = "title";
	private static String TAG_LINK = "link";
	private static String TAG_DESRIPTION = "description";
	private static String TAG_PUB_DATE = "pubDate";
	// private static String TAG_GUID = "guid"; // not used

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_item_list);
		
		
		
		
		Log.i(TAG,"intent, extra: " + (getIntent().getStringExtra(India_News.TAG_ID)));
		Intent i = getIntent();
		Integer site_id = Integer.parseInt(i.getStringExtra(India_News.TAG_ID));
		RSSDatabaseHandler rssDB = new RSSDatabaseHandler(getApplicationContext());

		Log.d(TAG,"website");
		WebSite site = rssDB.getTechnologyRow(site_id); //apply switch case for each of the India,Entertainment,Sports,Technology!!! <--later-->
		rss_link = site.getRSSLink();
		
		 //Calling a background thread will loads recent articles of a website
		 Log.i(TAG,"getRSSItems");
		new loadRSSFeedItems().execute(rss_link);
		
		lv =(ListView) findViewById(R.id.list);
		ListAdapter adapter = new SimpleAdapter(
				ListRSSItemsActivity_T.this, rssItemList,
				R.layout.rss_item_list_row, new String[] {
						TAG_LINK, TAG_TITLE, TAG_PUB_DATE,
						TAG_DESRIPTION }, new int[] {
						R.id.page_url, R.id.title, R.id.pub_date,
						R.id.link });
		lv.setAdapter(adapter);

		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent in = new Intent(getApplicationContext(),
						DisPlayWebPageActivity.class);
				String page_url = ((TextView) view.findViewById(R.id.page_url))
						.getText().toString();
				in.putExtra("page_url", page_url);
				startActivity(in);
			}
		});
	}
	
	/* Background AsyncTask to get RSS Feed Items data from URL*/
	
	class loadRSSFeedItems extends AsyncTask<String, String, String> {
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			Log.i(TAG,"RSSItemFeedActivity");
			super.onPreExecute();
			pDialog = new ProgressDialog(ListRSSItemsActivity_T.this);
			pDialog.setMessage("Loading recent articles...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		/**
		 * getting all recent articles and showing them in listview
		 * */
		@Override
		protected String doInBackground(String... args) {
			Log.i(TAG,"inBackground");
			String rss_url = args[0];
			rssItems = rssParser.getRSSFeedItems(rss_url);
			for (RSSItem item : rssItems) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_TITLE, item.getTitle());
				map.put(TAG_LINK, item.getLink());
				map.put(TAG_PUB_DATE, item.getPubdate());

				String description = item.getDescription();
				if (description.length() > 100) {
					description = description.substring(0, 97) + "..";
				}
				map.put(TAG_DESRIPTION, description);
				rssItemList.add(map);
				Log.i(TAG,"Background");
			}

			runOnUiThread(new Runnable() {
				public void run() {
					
					//Updating parsed items into listview
					 
					Log.i(TAG,"Thread");
					ListAdapter adapter = new SimpleAdapter(
							ListRSSItemsActivity_T.this, rssItemList,
							R.layout.rss_item_list_row, new String[] {
									TAG_LINK, TAG_TITLE, TAG_PUB_DATE,
									TAG_DESRIPTION }, new int[] {
									R.id.page_url, R.id.title, R.id.pub_date,
									R.id.link });
					lv.setAdapter(adapter);
					
				}
			});
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String args) {
			pDialog.dismiss();
		}
	}
	

}
