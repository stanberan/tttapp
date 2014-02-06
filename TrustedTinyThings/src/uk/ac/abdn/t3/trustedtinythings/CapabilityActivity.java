package uk.ac.abdn.t3.trustedtinythings;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CapabilityActivity extends Activity {
ListView listView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capability);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		listView=(ListView)findViewById(R.id.details_list);
		Bundle extra=getIntent().getExtras();
		Parcelable[] parcels=extra.getParcelableArray("uk.ac.abdn.t3.trustedtinythings.capabilities");
		ArrayList<Capability>capabilities=new ArrayList<Capability>();
	//	ArrayList<Capability> capab=(ArrayList<Capability>)Arrays.asList(capabilities);
		for(int i=0; i<parcels.length;i++){
			capabilities.add((Capability)parcels[i]);
		}
		
		
		DetailsAdapter adapter = new DetailsAdapter(this,
		        R.layout.details_capability_row, capabilities);
		    listView.setAdapter(adapter);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    
	    case R.id.help_bar:
	        Intent i=new Intent(this, LegendActivity.class);
	        startActivity(i);
	        return true;
	    
	    case R.id.info_bar:
	    	Helpers.alertDialog("About", getResources().getString(R.string.about), this);
	    }	
	    return super.onOptionsItemSelected(item);
	}

}
