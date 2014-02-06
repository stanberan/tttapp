package uk.ac.abdn.t3.trustedtinythings;



import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.abdn.t3.trustedtinythings.OverviewListAdapter.GenericRow;

import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
@SuppressLint("NewApi")
public class MainActivity extends Activity implements AnimationListener {
TextView infoText;
TextView responseText;
ImageView manufacturerLogo;
ImageView ownerLogo;
ImageView consumerLogo;

//NEW API
ImageView deviceImage;
StyledTextView deviceDescription;
ListView capabilityQualityList;
StyledButton accept;
StyledButton cancel;
LinearLayout externalBodies;
LinearLayout collectedData;
StyledTextView details;
Animation animFadein;
ImageView logodetails;
LinearLayout dropdown;
LinearLayout overviewDetailsLayout;

static String  android_id=null;
static String  MD5=null;
static String URL=null;

ArrayList<GenericRow> combinedView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.overview_frag);
		combinedView=new ArrayList<GenericRow>();
		responseText=(TextView)findViewById(R.id.responseText);
		infoText=(TextView)findViewById(R.id.infoText);		
		manufacturerLogo=(ImageView)findViewById(R.id.manufacturerlogo);
		ownerLogo=(ImageView)findViewById(R.id.ownerlogo);
		consumerLogo=(ImageView)findViewById(R.id.consumerlogo);
	
//		infoText.setClickable(true);
	/*	infoText.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
				Picasso.with(MainActivity.this).load(InformationHolder.holder.manufacturerLogo).resize(200,200).into(manufacturerLogo);
				Picasso.with(MainActivity.this).load(InformationHolder.holder.ownerLogo).into(ownerLogo);
				Picasso.with(MainActivity.this).load(InformationHolder.holder.capabilities[0].consumerLogo).into(consumerLogo);
				
			}
		});
		
		*/
		
		//NEW
		overviewDetailsLayout=(LinearLayout)findViewById(R.id.overview_details_layout);
		dropdown=(LinearLayout)findViewById(R.id.dropdown_layout);
		deviceImage=(ImageView)findViewById(R.id.device_image_view);
		deviceDescription=(StyledTextView)findViewById(R.id.device_description_view);
		capabilityQualityList=(ListView)findViewById(R.id.capability_quality_list_view);
		externalBodies=(LinearLayout)findViewById(R.id.external_bodies_view);
		collectedData=(LinearLayout)findViewById(R.id.data_collected_view);
		details=(StyledTextView)findViewById(R.id.details_textview);
		logodetails=(ImageView)findViewById(R.id.details_imageview);
	
		
		overviewDetailsLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i =new Intent(MainActivity.this, DeviceDetailsActivity.class);
				startActivity(i);
				
			}
			
			
			
			
		});
		
		
		
		
		
		accept=(StyledButton)findViewById(R.id.accept_button);
		accept.setOnClickListener(new OnClickListener(){

			
			
			
			
			@Override
			public void onClick(View v) {
				 Toast.makeText(getApplicationContext(),"Device id : "+android_id +"Iot ID:"+MD5, Toast.LENGTH_LONG).show();
				new AcceptResponse().execute(new String[]{android_id,MD5,URL});
				
			}
			
		});
		
		cancel=(StyledButton)findViewById(R.id.cancel_button);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this,"Exiting app and canceling forwarding to service",Toast.LENGTH_LONG).show();
				finish();
				
			}
			
		});
		
		
		
		
		Bundle extra=getIntent().getExtras();
		if(extra!=null){
			String response=extra.getString("response");
			
		if(response!=null){
		android_id=extra.getString("android_id");
		URL=extra.getString("URL");
		MD5=extra.getString("MD5");
		setHolder(response);
		Toast.makeText(this, "Data to populate view set", Toast.LENGTH_LONG).show();
		populateView(InformationHolder.holder);
		}
		
		}
		else{
			populateView(InformationHolder.holder);
		//	Toast.makeText(this, "Something went wrong with NFC Activity!", Toast.LENGTH_LONG).show();
		}
		
		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
	             R.anim.slide_up_left);
	      
	     // set animation listener
	     animFadein.setAnimationListener(this);
		
	}
		

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}
	static int lastid=-1;
	static int click=0;
	
	private void populateView(final InformationHolder hol){
		
		
		ArrayList<Company> companies=new ArrayList<Company>();
		Company c=new Company();
		c.logo=hol.ownerLogo;
		c.name=hol.owner;
		Company d=new Company();
		d.logo=hol.manufacturerLogo;
		d.name=hol.manufacturer;
		companies.add(c);
		companies.add(d);
		Company e=new Company();
		e.name=hol.capabilities[0].consumer;
		e.logo=hol.capabilities[0].consumerLogo;
		companies.add(e);
		hol.companies=new Company[companies.size()];
		hol.companies=companies.toArray(hol.companies);
		
		
		final OnClickListener companylistener = new OnClickListener() {
	        @SuppressLint("NewApi")
			public void onClick(final View v) {
	            //Inform the user the button has been clicked
	        	ImageView v1=(ImageView)v;
	            int id=v1.getId();
	            logodetails.setVisibility(View.VISIBLE);
	            if(lastid!=-1){
	            ImageView lastview=(ImageView)findViewById(lastid);
	            if(lastview.getId()==v1.getId()){
	            	if(click==1){
	            		lastid=-1;
	            		click=0;
	            	}
	          		dropdown.clearAnimation();
	            	dropdown.setVisibility(View.GONE);
	          
	                  v1.setBackground(null);
	                  click++;
	            }
	            else if(lastview.getId()!=v1.getId()){
	            	Picasso.with(MainActivity.this).load(InformationHolder.holder.companies[id-100].logo).into(logodetails);
	            	details.setText(InformationHolder.holder.companies[id-100].name);
	            	dropdown.setVisibility(View.VISIBLE);
	            	dropdown.startAnimation(animFadein);	
	            	
	            	v1.setBackgroundColor(getResources().getColor(R.color.yellow));
	            	lastview.setBackground(null);
	            	lastid=v1.getId();
	            }

	    }
	            else{
	              	details.setText(InformationHolder.holder.companies[id-100].name);
	            	dropdown.setVisibility(View.VISIBLE);
	            	dropdown.startAnimation(animFadein);

	            	v1.setBackgroundColor(getResources().getColor(R.color.yellow));
	            	lastid=v1.getId();
	            }
	        }
		};
		
		
		
		
		
		final OnClickListener dataListener = new OnClickListener() {
	        @SuppressLint("NewApi")
			public void onClick(final View v) {
	            //Inform the user the button has been clicked
	        	logodetails.setVisibility(View.GONE);
	        	ImageView v1=(ImageView)v;
	            int id=v1.getId();
	            if(lastid!=-1){
	            ImageView lastview=(ImageView)findViewById(lastid);
	            if(lastview.getId()==v1.getId()){
	            	if(click==1){
	            		lastid=-1;
	            		click=0;
	            	}
	            	dropdown.clearAnimation();
	            	dropdown.setVisibility(View.GONE);
	                  v1.setBackground(null);
	                  click++;
	            }
	            else if(lastview.getId()!=v1.getId()){
	            	details.setText(InformationHolder.holder.capabilities[id].consumes);
	            	dropdown.setVisibility(View.VISIBLE);
	            	dropdown.startAnimation(animFadein);
	            	details.setText(InformationHolder.holder.capabilities[id].consumes);
	            
	            	v1.setBackgroundColor(getResources().getColor(R.color.yellow));
	            	lastview.setBackground(null);
	            	lastid=v1.getId();
	            }

	    }
	            else{
	            	details.setText(InformationHolder.holder.capabilities[id].consumes);
	            	dropdown.setVisibility(View.VISIBLE);
	            	dropdown.startAnimation(animFadein);
	            	
	            	v1.setBackgroundColor(getResources().getColor(R.color.yellow));
	            	lastid=v1.getId();
	            }
	        }
		};
		
	
		
		
		Picasso.with(MainActivity.this).load(InformationHolder.holder.imageURL).into(deviceImage);
		deviceDescription.setText(hol.description);
		
		
		//INFLATE DATA
		int pixels =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                5, getResources().getDisplayMetrics());
		for(int i=0 ;i<hol.capabilities.length ;i++){
			ImageView im=new ImageView(this);
			im.setId(i);
			im.setOnClickListener(dataListener);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, pixels, 0);
			im.setLayoutParams(params);
			im.setImageDrawable(getResources().getDrawable(R.drawable.data));
			collectedData.addView(im);
		
		}
		//INFLATE COMPANIES
		
		for(int i=0 ;i<companies.size() ;i++){
			ImageView im=new ImageView(this);
			im.setPadding(2, 2, 2, 2);
			im.setId(i+100);
			im.setOnClickListener(companylistener);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, pixels, 0);
			im.setLayoutParams(params);
			im.setImageDrawable(getResources().getDrawable(R.drawable.organisation));
			externalBodies.addView(im);
		
		}
		
		
		ArrayList<GenericRow> ada=new ArrayList<GenericRow>(Arrays.asList(hol.capabilities));
		ArrayList<GenericRow> qual=new ArrayList<GenericRow>(Arrays.asList(hol.qualities));
		combinedView.addAll(ada);
		combinedView.addAll(qual);
		capabilityQualityList.setScrollContainer(false);
		capabilityQualityList.setAdapter(new OverviewListAdapter(this,R.layout.capability_row,combinedView));
		
		capabilityQualityList.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {

	      Object o = capabilityQualityList.getItemAtPosition(position);
	       GenericRow row=(GenericRow)o;//As you are using Default String Adapter
	      // Toast.makeText(getBaseContext(),row.getTitle(),Toast.LENGTH_SHORT).show();
	       
	       if(row instanceof Capability){
	    	   Capability cap=(Capability)row;
	    	   Toast.makeText(getBaseContext(),cap.toString(),Toast.LENGTH_LONG).show();
	    	 
	    	   Intent i=new Intent(MainActivity.this,CapabilityActivity.class);	
	    	   i.putExtra("uk.ac.abdn.t3.trustedtinythings.capabilities",hol.capabilities);
	    	   startActivity(i);
	    	   
	       }
	       
	       
	       
	       
	        }
	    });
		
	}
	
	
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case R.id.help_bar:
	        Intent i=new Intent(this, LegendActivity.class);
	        startActivity(i);
	        return true;
	    
	    case R.id.info_bar:
	    	Helpers.alertDialog("About",  getResources().getString(R.string.about), this);
	    }	
	    return super.onOptionsItemSelected(item);
	}

				
	public void setHolder(String response){
		
		if(response==null){
		//	infoText.setText("This device was not recognized in our system.\nScan another tag.");
				//TO DO -- REGISTER DEVICE BUTTON. - need deviceID and phoneID
			Toast.makeText(this, response, Toast.LENGTH_LONG).show();
		}
		else{
		InformationHolder.holder =new InformationHolder();
		try {
			JSONObject root=new JSONObject(response);
			JSONArray capabilities=root.getJSONArray("capabilities");
			JSONArray features =root.getJSONArray("features");
			InformationHolder.holder.capabilities=getCapabilities(capabilities);
			InformationHolder.holder.qualities=getQualities(features);
			InformationHolder.holder.imageURL=root.getString("picture");
			InformationHolder.holder.owner=root.getString("owner");
			InformationHolder.holder.ownerURL=root.getString("ownerURL");
			InformationHolder.holder.manufacturer=root.getString("manufacturer");
			InformationHolder.holder.manufacturerURL=root.getString("manufacturerURL");
			InformationHolder.holder.description=root.getString("deviceDescription");
			InformationHolder.holder.deviceType=root.getString("deviceType");
			InformationHolder.holder.ownerLogo=root.getString("ownerLogo");
			InformationHolder.holder.manufacturerLogo=root.getString("manufacturerLogo");
			
		//	responseText.setText(InformationHolder.holder.toString());
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		//	infoText.setText("JSON ERROR in setInformationHolder.holder() method");
			e.printStackTrace();
		
		}
		}
		
		
		
		
	}
	

	
public Capability[] getCapabilities(JSONArray capabilities){
    	
    	Capability[] capabilityAr=new Capability[capabilities.length()];
    	for(int i=0; i<capabilities.length();i++){
    	try{
    		Capability c=new Capability();
    		JSONObject row= capabilities.getJSONObject(i);
    		c.setConsumer(row.getString("consumer"));
    		c.setConsumerURL(row.getString("consumerURL"));
    		c.setName(row.getString("descriptionOfCapability"));
    		c.setConsumes(row.getString("consumes"));
    		c.setPurpose(row.getString("purpose"));
    		c.setConsumerLogo(row.getString("consumerLogo"));
    		capabilityAr[i]=c;   		
    	}
    	catch(Exception e){e.printStackTrace();}
    	}
    	return capabilityAr;
    	
    	
    	
    }
 public Quality[] getQualities(JSONArray qualities){
    	
    	Quality[] qualityAr=new Quality[qualities.length()];
    	for(int i=0; i<qualities.length();i++){
    	try{
    		Quality c=new Quality();
    		JSONObject row= qualities.getJSONObject(i);
    		c.setDescription(row.getString("description"));
    		c.setProvider(row.getString("provider"));
    		qualityAr[i]=c;   		
    	}
    	catch(Exception e){e.printStackTrace();}
    	}
    	return qualityAr;
    	
    	
    	
    }


 
 private class AcceptResponse extends AsyncTask<String, Boolean, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
	
		 String urlRequest="http://t3.abdn.ac.uk:8080/t3/1/user/accept/"+params[0]+"/"+params[1];
	    	HttpClient httpclient= new DefaultHttpClient();
	    	HttpGet httpget = new HttpGet(urlRequest);
	    	HttpResponse response;
	    	try{
	    		response =httpclient.execute(httpget);
	    		Log.e("STATUS", response.getStatusLine().getStatusCode()+"");
	    		if(response.getStatusLine().getStatusCode()==201){
	    			
	    			 return true;
	    		}
	    		else{
	    		//	 Toast.makeText(getApplicationContext(), "Server Timeout????Device not added to your list" , Toast.LENGTH_LONG).show();
	    			 return false;
	    		}
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
		return null;
	}
	protected void onPostExecute(Boolean b){
	if(b.booleanValue()){
		 Toast.makeText(getApplicationContext(), "This IOT Device was succesfully added to your list of accepted devices" , Toast.LENGTH_LONG).show();
		 Intent i= new Intent(Intent.ACTION_VIEW);
		 i.setData(Uri.parse(URL));
		 startActivity(i);
		 finish();
	}
		
	}
	 
	 
	 
	 
	 
	 
	 
	 
 }
 



	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	

	}
	

    
   