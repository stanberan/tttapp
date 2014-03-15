package uk.ac.abdn.t3.trustedtinythings;

import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

public class DeviceDetailsActivity extends Activity {
ImageView deviceImage;
StyledTextView device_description;
StyledTextView device_manufacturer_title;
ImageView device_details_manufacturer_image;
StyledTextView device_owner_title;
ImageView device_owner_image;
StyledTextView device_type;
StyledTextView thing_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_details);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		deviceImage=(ImageView)findViewById(R.id.details_device_image_view);
		device_description=(StyledTextView)findViewById(R.id.details_device_description);
		device_manufacturer_title=(StyledTextView)findViewById(R.id.details_device_manufacturer_title);
		device_details_manufacturer_image=(ImageView)findViewById(R.id.details_device_manufacturer_image_view);
		device_owner_title=(StyledTextView)findViewById(R.id.details_device_owner_title);
		device_owner_image=(ImageView)findViewById(R.id.details_device_owner_image_view);
		device_type=(StyledTextView) findViewById(R.id.details_device_type);
		thing_name=(StyledTextView) findViewById(R.id.details_thing_name);
		
		populateView();
		
		
		
		
		
	}
	public void populateView(){
		
		Picasso.with(this).load(InformationHolder.holder.manufacturerLogo).into(device_details_manufacturer_image);
		Picasso.with(this).load(InformationHolder.holder.ownerLogo).into(device_owner_image);
		Picasso.with(this).load(InformationHolder.holder.imageURL).into(deviceImage);
		
		device_description.setText(InformationHolder.holder.description);
		device_manufacturer_title.setText(InformationHolder.holder.manufacturer);
		device_owner_title.setText(InformationHolder.holder.owner);
		device_type.setText(InformationHolder.holder.deviceType);
		thing_name.setText(InformationHolder.holder.thingName);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}
	
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
	    	Helpers.alertDialog("About",  getResources().getString(R.string.about), this);
	    }	
	    return super.onOptionsItemSelected(item);
	}

}


