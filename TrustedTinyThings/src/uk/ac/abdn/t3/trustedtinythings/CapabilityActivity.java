package uk.ac.abdn.t3.trustedtinythings;

import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ListActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class CapabilityActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extra=getIntent().getExtras();
		Parcelable[] parcels=extra.getParcelableArray("uk.ac.abdn.t3.trustedtinythings.capabilities");
		Capability[] capabilities=new Capability[parcels.length];
	//	ArrayList<Capability> capab=(ArrayList<Capability>)Arrays.asList(capabilities);
		for(int i=0; i<parcels.length;i++){
			capabilities[i]=(Capability)parcels[i];
		}
		
		
		ArrayAdapter<Capability> adapter = new ArrayAdapter<Capability>(this,
		        android.R.layout.simple_list_item_1, capabilities);
		    setListAdapter(adapter);
		
		
		
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
	    }
	    return super.onOptionsItemSelected(item);
	}

}
