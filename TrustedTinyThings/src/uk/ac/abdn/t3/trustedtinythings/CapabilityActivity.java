package uk.ac.abdn.t3.trustedtinythings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import ws.DeviceListHolder;
import ws.GetTask;
import ws.RestTaskCallback;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CapabilityActivity extends Activity {
ListView listView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capability);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
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
		
		
		    listView.setOnItemClickListener(new OnItemClickListener(){
		    	public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
					Object o = listView.getItemAtPosition(position);
					 final Capability cap=(Capability)o;
				
					String url="";
					Helpers.loading(true,CapabilityActivity.this,"Retrieving Company Profile...");
					try {
						url = "http://t3.abdn.ac.uk:8080/t3/1/thing/company/"+URLEncoder.encode(cap.getConsumer(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					new GetTask(url, new RestTaskCallback(){
					
						@Override
						public void onTaskComplete(String result) {
						
					//removeddialogs		Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show(); 
							Helpers.showCompanyDialog(result,cap.getConsumer(),CapabilityActivity.this);
							Helpers.loading(false,CapabilityActivity.this,null);
						}

						@Override
						public void onFailure(String message) {
							Toast.makeText(CapabilityActivity.this, "Unable to retrieve company profile.\nPlease check your internet connection...", Toast.LENGTH_LONG).show();
							Helpers.loading(false,CapabilityActivity.this,null);
							
						}
						
					}).execute();
					
			
					
					
				}
				
				
				
			});
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
