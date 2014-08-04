package com.example.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


public class MainActivity extends Activity {


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        GridView gridview = (GridView) findViewById(R.id.gridView1);
        gridview.setAdapter(new ImageAdapter(this));
        
       
       
     
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        		
        		switch(position){
        		
        		case 0: Intent a= new Intent(v.getContext(),India_News.class);
        				v.getContext().startActivity(a);
        				break;
        				
        		case 1: Intent b= new Intent(v.getContext(),Entertainment.class);
        				v.getContext().startActivity(b);
        				break;
        				
        		case 2: Intent c= new Intent(v.getContext(),Technology.class);
						v.getContext().startActivity(c);
						break;
						
        		case 3: Intent d= new Intent(v.getContext(),Sports.class);
						v.getContext().startActivity(d);
						break;
        				
        				
        		}
        		
        	}
        });
    	   
       }
       

       /* if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
   /* public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/


