package uk.ac.abdn.t3.trustedtinythings;



import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.abdn.t3.trustedtinythings.OverviewListAdapter.GenericRow;
import ws.GetTask;
import ws.RestTaskCallback;

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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



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
View buttons;
StyledButton delete;

static String  android_id=null;
static String  MD5=null;
static String URL=null;

static boolean accepted;    //not from nfc but list
ArrayList<GenericRow> combinedView;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.overview_frag);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		getActionBar().setDisplayHomeAsUpEnabled(true);
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
		buttons=(View)findViewById(R.id.accept_cancel_include);
		delete=(StyledButton)findViewById(R.id.delete_button);
		
		logodetails.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String url="";
				Helpers.loading(true,MainActivity.this,"Retrieving Company Profile...");
				try {
					url = "http://t3.abdn.ac.uk:8080/t3/1/thing/company/"+URLEncoder.encode(details.getText().toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				new GetTask(url, new RestTaskCallback(){
				
					@Override
					public void onTaskComplete(String result) {
					
				//removeddialogs		Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show(); 
						Helpers.showCompanyDialog(result,details.getText().toString(),MainActivity.this);
						Helpers.loading(false,MainActivity.this,null);
					}

					@Override
					public void onFailure(String message) {
						Toast.makeText(MainActivity.this, "Unable to retrieve company profile.\nPlease check your internet connection...", Toast.LENGTH_LONG).show();
						Helpers.loading(false,MainActivity.this,null);
						
					}
					
				}).execute();
				
		
				
				
			}
			
			
			
		});
		
		
		
		
		
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				alertDialog("Remove","Are you sure you would like to remove this device from your collection?",MainActivity.this);
					
				
			}
			
			
		});
		
		
		overviewDetailsLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i =new Intent(MainActivity.this, DeviceDetailsActivity.class);
				startActivity(i);
				//stats
				new TrackStats().execute(new String[]{android_id,"From Overview -> To Device Details "});
			}
			
			
			
			
		});
		
		
		
		
		
		accept=(StyledButton)findViewById(R.id.accept_button);
		accept.setOnClickListener(new OnClickListener(){

			
			
			
			
			@Override
			public void onClick(View v) {
				// Toast.makeText(getApplicationContext(),"Device id : "+android_id +"Iot ID:"+MD5, Toast.LENGTH_LONG).show();
				
			
			setNickName(1); //default first case - recursive
				
			}
			
		});
		
		cancel=(StyledButton)findViewById(R.id.cancel_button);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//removeddialogs		Toast.makeText(MainActivity.this,"Exiting app and canceling forwarding to service",Toast.LENGTH_LONG).show();
				new TrackStats().execute(new String[]{android_id,"User Declined Device:"+InformationHolder.holder.thingName+" with iotid:"+MD5});
				finish();
				
			}
			
		});
		
		
		
		
		Bundle extra=getIntent().getExtras();
		if(extra!=null){
			String response=extra.getString("response");
			
		if(response!=null){
			if(extra.getBoolean("accept")){
				accepted=true;
			accept.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			delete.setVisibility(View.VISIBLE);
			
			}
			else{
				accepted=false;
				accept.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.VISIBLE);
				delete.setVisibility(View.GONE);
			}
		
		android_id=extra.getString("android_id");
		URL=extra.getString("URL");
		MD5=extra.getString("MD5");
		setHolder(response);
		//removeddialogs		Toast.makeText(this, "Data to populate view set", Toast.LENGTH_LONG).show();
		populateView(InformationHolder.holder);
		}
		
		}
		else{
			if(accepted==true){
				accept.setVisibility(View.GONE);
				cancel.setVisibility(View.GONE);
				delete.setVisibility(View.VISIBLE);
			}
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
		
		//TODO populateView in MainActivity retrieve involved companies with REST API
		ArrayList<Company> companies=new ArrayList<Company>();
		Company c=new Company();
		c.logo=hol.ownerLogo;
		c.name=hol.owner;
		Company d=new Company();
		d.logo=hol.manufacturerLogo;
		d.name=hol.manufacturer;
		companies.add(c);
		companies.add(d);
		//GET Companies
		
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
	            	//Statistic hook
	            	new TrackStats().execute(new String[]{android_id,"Clicked on company->"+InformationHolder.holder.companies[id-100].name});
	            }

	    }
	            else{
	              	details.setText(InformationHolder.holder.companies[id-100].name);
	            	dropdown.setVisibility(View.VISIBLE);
	            	dropdown.startAnimation(animFadein);

	            	v1.setBackgroundColor(getResources().getColor(R.color.yellow));
	            	lastid=v1.getId();
	            	//stats
	            	new TrackStats().execute(new String[]{android_id,"Clicked on company->"+InformationHolder.holder.companies[id-100].name});
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
	            	//stats hook
	            	new TrackStats().execute(new String[]{android_id,"Clicked on data->"+InformationHolder.holder.capabilities[id].consumes});
	            }

	    }
	            else{
	            	details.setText(InformationHolder.holder.capabilities[id].consumes);
	            	dropdown.setVisibility(View.VISIBLE);
	            	dropdown.startAnimation(animFadein);
	            	
	            	v1.setBackgroundColor(getResources().getColor(R.color.yellow));
	            	lastid=v1.getId();
	            	//Stats hook
	            	new TrackStats().execute(new String[]{android_id,"Clicked on data->"+InformationHolder.holder.capabilities[id].consumes});
	            }
	        }
		};
		
	
		
		
		Picasso.with(MainActivity.this).load(InformationHolder.holder.imageURL).into(deviceImage);
		deviceDescription.setText(hol.description);
		
		
		//INFLATE DATA
		int pixels =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
                5, getResources().getDisplayMetrics());
		ArrayList<String> data=new ArrayList<String>();
		
		for(int i=0 ;i<hol.capabilities.length ;i++){
			if(!data.contains(hol.capabilities[i].consumes)){
				data.add(hol.capabilities[i].consumes);
			ImageView im=new ImageView(this);
			im.setId(i);
			im.setOnClickListener(dataListener);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, pixels, 0);
			im.setLayoutParams(params);
			im.setImageDrawable(getResources().getDrawable(R.drawable.data));
			collectedData.addView(im);
			}
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
		
		final HashMap<String,HashMap<String,ArrayList<GenericRow>>> capabilityarrayholder=new HashMap<String,HashMap<String,ArrayList<GenericRow>>>();
		//all capabilities
		ArrayList<GenericRow> ada=new ArrayList<GenericRow>(Arrays.asList(hol.capabilities));
		//all qualities
		ArrayList<GenericRow> qual=new ArrayList<GenericRow>(Arrays.asList(hol.qualities));	
		//sort capabilities into hashmaps of capability type->associated company
		for(GenericRow row : ada){
		
			Capability cap=(Capability)row;
			if(!capabilityarrayholder.containsKey(cap.name)){
				capabilityarrayholder.put(cap.name,new HashMap<String,ArrayList<GenericRow>>());
			}		
			if(capabilityarrayholder.containsKey(cap.name)){
				HashMap<String,ArrayList<GenericRow>> map=capabilityarrayholder.get(cap.name);
			if(map.containsKey(cap.consumer)){
				map.get(cap.consumer).add(cap);				
			}
			else{
				map.put(cap.consumer, new ArrayList<GenericRow>());
				map.get(cap.consumer).add(cap);
			}	
			}
		
			 
		}
	
		ArrayList<GenericRow> capabilityView=new ArrayList<GenericRow>();
		
		for ( String key : capabilityarrayholder.keySet() ) {
		    //for each capability
			for(String comp : capabilityarrayholder.get(key).keySet()){
				ArrayList<GenericRow> r=capabilityarrayholder.get(key).get(comp);				
				GenericRowView capability= new GenericRowView();
				capability.setTitle(key);
				capability.setLetter("C");	
				capability.setImage(((Capability)r.get(0)).consumerLogo);
				
				capability.setDescription(comp);
				
				capabilityView.add(capability);
			}
		}
		
		
		
		
		
		
		combinedView.addAll(capabilityView);
		combinedView.addAll(qual);
		capabilityQualityList.setScrollContainer(false);
		capabilityQualityList.setAdapter(new OverviewListAdapter(this,R.layout.capability_row,combinedView));
		
		capabilityQualityList.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view,
	                int position, long id) {

	      Object o = capabilityQualityList.getItemAtPosition(position);
	       GenericRow row=(GenericRow)o;//As you are using Default String Adapter
	   
	       
	       if(row instanceof GenericRowView){
	    	   GenericRowView cap=(GenericRowView)row;
	    	ArrayList<GenericRow> capies=capabilityarrayholder.get(cap.getTitle()).get(cap.getDescription());
	    	Capability[] capabilities=new Capability[capies.size()];
	    	for(int i=0;i <capabilities.length ; i++){
	    		capabilities[i]=(Capability) capies.get(i);
	    	}
	    	
	    	
	    	 
	    	   Intent i=new Intent(MainActivity.this,CapabilityActivity.class);	
	    	   i.putExtra("uk.ac.abdn.t3.trustedtinythings.capabilities",capabilities);
	    	   startActivity(i);
	    	   new TrackStats().execute(new String[]{android_id,"From Overview Screen clicked on Capability:"+cap.getTitle()});
	    	   
	       }
	       else if(row instanceof Quality){
	    	   Quality q=(Quality)row;
	    	   //TODO Quality enhancement title type 
	    	   new TrackStats().execute(new String[]{android_id,"From Overview Screen clicked on Quality:"+q.description});
	    	   Toast.makeText(getBaseContext(),q.description,Toast.LENGTH_LONG).show();
	    	   
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
	    	 	return true;
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }	
	    
	    
	    
	    return super.onOptionsItemSelected(item);
	}

				
	public void setHolder(String response){
		
		if(response==null){
		//	infoText.setText("This device was not recognized in our system.\nScan another tag.");
				//TO DO -- REGISTER DEVICE BUTTON. - need deviceID and phoneID
			//removeddialogs		Toast.makeText(this, response, Toast.LENGTH_LONG).show();
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
			InformationHolder.holder.thingName=root.getString("thingName");
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
	
		
	    	try{
	    		 String urlRequest="http://t3.abdn.ac.uk:8080/t3/1/user/accept/"+params[0]+"/"+params[1]+"/"+URLEncoder.encode(params[3],"UTF-8");
	 	    	HttpClient httpclient= new DefaultHttpClient();
	 	    	HttpGet httpget = new HttpGet(urlRequest);
	 	    	HttpResponse response;
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
		Helpers.loading(false, MainActivity.this, null);
	if(b.booleanValue()){
		 Toast.makeText(getApplicationContext(), "This device was succesfully added to your catalogue." , Toast.LENGTH_LONG).show();
		 Intent i= new Intent(Intent.ACTION_VIEW);
		 i.setData(Uri.parse(URL));
		 startActivity(i);
		 finish();
		 
	}
		
	}
	 
	 
	 
	 
	 
	 
	 
	 
 }
 

 private class CheckNickName extends AsyncTask<String, Integer, Integer> {

	 
	 
	 
	 
	@Override
	protected Integer doInBackground(String... params) {
		if(params[1].equals("generate")){
			boolean added=false;
			int dev=1;
			while(!added){			
				Uri.Builder b = Uri.parse("http://t3.abdn.ac.uk:8080").buildUpon().
						
						appendPath("t3").appendPath("1").appendPath("user").appendPath("checknick").appendPath(params[0]).appendPath("device"+dev);	
				
				try{
					  
			    	HttpClient httpclient= new DefaultHttpClient();
			    	
			    	HttpGet httpget = new HttpGet(b.build().toString());
			    	HttpResponse response;

		    		response =httpclient.execute(httpget);
		    		if(response.getStatusLine().getStatusCode()==215){
		    		//
		    		}
		    		else if(response.getStatusLine().getStatusCode()==200){
		    			
		    			added=true;
		    			new AcceptResponse().execute(new  String[]{android_id,MD5,URL,"device"+dev});
		    			return 6;
		    		}
			
			dev++;
		}
			catch(Exception e){
				if(e instanceof UnknownHostException || e instanceof ConnectException){
					added=true;
				
					return -2; //connection issue
				}
				e.printStackTrace();
				//added=true;
    			//new AcceptResponse().execute(new  String[]{android_id,MD5,URL,"device"+dev});
    			dev++;
			}
		
			}
			return -10 ; //should not happen
	
		}
		
		else{
		
		try{
			Uri.Builder b = Uri.parse("http://t3.abdn.ac.uk:8080").buildUpon().
					
					appendPath("t3").appendPath("1").appendPath("user").appendPath("checknick").appendPath(params[0]).appendPath(URLEncoder.encode(params[1], "UTF-8"));
	  
		    	HttpClient httpclient= new DefaultHttpClient();
		    	HttpGet httpget = new HttpGet(b.build().toString());
		    	HttpResponse response;

	    		response =httpclient.execute(httpget);
	    		if(response.getStatusLine().getStatusCode()==215){
	    		
	    			return 2;    //already exist
	    		}
	    		else if(response.getStatusLine().getStatusCode()==200){
	    			new AcceptResponse().execute(new String[]{android_id,MD5,URL,params[1]}); //TODO referencing static variables when accpeting
	    			return 0;   //does not exist create
	    		}
		return null;
		
		
	}
		catch(Exception e){
			e.printStackTrace();
			if(e instanceof UnknownHostException || e instanceof ConnectException){
				
			
				return -2; //connection issue
			}
			return 2;
		}
		}
		
	}
		public void onPostExecute(Integer response){
			if(response!=0){
			Helpers.loading(false, MainActivity.this,null);
			}
			if(response!=null){
				if(response==6){
					//removeddialogs	Toast.makeText(MainActivity.this, "Nick name generated and saved", Toast.LENGTH_LONG).show();
				}
				else if(response==2){
					
					setNickName(2);
				}
				else if (response==-1){
					Toast.makeText(MainActivity.this, "Exception!", Toast.LENGTH_LONG).show();
				}
				else if(response==-2){
					
					Toast.makeText(MainActivity.this, "Device not saved. Check your internet connection.", Toast.LENGTH_LONG).show();
				}
				else if (response ==-10){
					Toast.makeText(MainActivity.this, "Should not happen, please contact s.beran@abdn.ac.uk if you see this message.", Toast.LENGTH_LONG).show();
				}
				
				
				
				
				
				
				
			}
		}
	 
	 
	 
 }
 private class TrackStats extends AsyncTask<String, Boolean, Boolean> {

	@Override
	protected Boolean doInBackground(String... params) {
		Uri.Builder b = Uri.parse("http://t3.abdn.ac.uk:8080").buildUpon().
	
		appendPath("t3").appendPath("1").appendPath("user").appendPath("track").appendPath(params[0]).appendPath(params[1]);

		String myUrl = b.build().toString();
		Log.e("HTTP", myUrl );
	    	try{
	    	//	URI uri =new URI("http","t3.abdn.ac.uk",null,8080,"/t3/1/user/track/"+params[0]+"/"+params[1],null);
	    	//	java.net.URL url=uri.toURL();
	    	//	String urlRequest= uri.toASCIIString();   //"http://t3.abdn.ac.uk:8080/t3/1/user/track/"+params[0]+"/"+params[1];
		    	HttpClient httpclient= new DefaultHttpClient();
		    	HttpGet httpget = new HttpGet(myUrl);
		    	HttpResponse response;
	    		
	    		
	    		response =httpclient.execute(httpget);
	    		Log.e("STATUS", response.getStatusLine().getStatusCode()+"");    	
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
		return null;
	}
	 
	 
	 
 }
 
 public void setNickName(int response){
	Helpers.loading(false, this, null);
	String message="Please provide a nickname so you can refer to it in the future. If left empty the nickname will be provided automatically.";
	if (response ==2){
		message="This nickname already exist, please choose a new one.";
	}
	if (response ==3){
       	Helpers.loading(true,MainActivity.this,"Generating new nickname and saving device ...");
    	new CheckNickName().execute(new String[]{android_id,"generate"});
    	return;
	}
	
	
	final EditText input = new EditText(MainActivity.this);
	 
	 new AlertDialog.Builder(MainActivity.this) 
	    .setTitle("Create NickName")
	    .setMessage(message) //TODO string message for nickname promtp dialog
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	           String text=input.getText().toString();
	           Helpers.loading(true,MainActivity.this,"Saving Device ...");
	           if (text!=null && !text.equals("")){
	            new CheckNickName().execute(new String[]{android_id,text});
	           }
	           else{
	        	   Helpers.loading(false,MainActivity.this,null);
	        	   setNickName(3);
	        	   
	           }
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	dialog.dismiss();
	        	
	        }
	    }).show();
	 
	 
	 
	 
	 
 }
 
 public  void alertDialog(String title, String message, Context c){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				c);

			// set title
			alertDialogBuilder.setTitle(title);

			// set dialog message
			alertDialogBuilder
				.setMessage(message)
				.setCancelable(true)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						String url="http://t3.abdn.ac.uk:8080/t3/1/user/remove/"+android_id+"/"+MD5;
						Helpers.loading(true,MainActivity.this,"Removing this device from your list of accepted devices...");
						new GetTask(url, new RestTaskCallback(){
								
							@Override
							public void onTaskComplete(String result) {
								//removeddialogs		Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
								Helpers.loading(false,MainActivity.this,null);
								Intent i=new Intent(MainActivity.this,NFCActivity.class);
								
								 startActivity(i);
								finish();
								
							}

							@Override
							public void onFailure(String message) {
								Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
								Helpers.loading(false,MainActivity.this,null);
								finish();
								
							}
							
						}).execute();
						
						
					}
				  }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
					  
				  })
				;

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
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
	

    
   