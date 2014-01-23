package com.example.trustedtinythings;



import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.trustedtinythings.OverviewListAdapter.GenericRow;
import com.squareup.picasso.Picasso;

import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
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

static InformationHolder holder=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.overview_frag);
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
				Picasso.with(MainActivity.this).load(holder.manufacturerLogo).resize(200,200).into(manufacturerLogo);
				Picasso.with(MainActivity.this).load(holder.ownerLogo).into(ownerLogo);
				Picasso.with(MainActivity.this).load(holder.capabilities[0].consumerLogo).into(consumerLogo);
				
			}
		});
		
		*/
		
		//NEW 
		
		deviceImage=(ImageView)findViewById(R.id.device_image_view);
		deviceDescription=(StyledTextView)findViewById(R.id.device_description_view);
		capabilityQualityList=(ListView)findViewById(R.id.capability_quality_list_view);
		externalBodies=(LinearLayout)findViewById(R.id.external_bodies_view);
		collectedData=(LinearLayout)findViewById(R.id.data_collected_view);
		
		new ServerResponse().execute(new String[]{"MD5Hash"});
		
		
	}
		
		
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}
	
	private void populateView(InformationHolder hol){
		ArrayList<GenericRow> ada=new ArrayList<GenericRow>(Arrays.asList(hol.capabilities));
		ArrayList<GenericRow> qual=new ArrayList<GenericRow>(Arrays.asList(hol.qualities));
		ada.addAll(qual);
		capabilityQualityList.setScrollContainer(false);
		capabilityQualityList.setAdapter(new OverviewListAdapter(this,R.layout.capability_row,ada));
		
		
		
	}
				
	public void setHolder(String response){
		
		if(response==null){
		//	infoText.setText("This device was not recognized in our system.\nScan another tag.");
				//TO DO -- REGISTER DEVICE BUTTON. - need deviceID and phoneID
			
		}
		else{
		holder =new InformationHolder();
		try {
			JSONObject root=new JSONObject(response);
			JSONArray capabilities=root.getJSONArray("capabilities");
			JSONArray features =root.getJSONArray("features");
			holder.capabilities=getCapabilities(capabilities);
			holder.qualities=getQualities(features);
			holder.imageURL=root.getString("picture");
			holder.owner=root.getString("owner");
			holder.ownerURL=root.getString("ownerURL");
			holder.manufacturer=root.getString("manufacturer");
			holder.manufacturerURL=root.getString("manufacturerURL");
			holder.description=root.getString("deviceDescription");
			holder.deviceType=root.getString("deviceType");
			holder.ownerLogo=root.getString("ownerLogo");
			holder.manufacturerLogo=root.getString("manufacturerLogo");
			
		//	responseText.setText(holder.toString());
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		//	infoText.setText("JSON ERROR in setholder() method");
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

	
	private class ServerResponse extends AsyncTask<String, String, String> {

		
		
		
		
		
		
        @Override
        protected String doInBackground(String... params) {
        	publishProgress("Creating Http Client...");
        	String urlRequest="http://t3.abdn.ac.uk:8080/t3/1/thing/"+params[0]+"/information";
        	HttpClient httpclient= Helpers.createHttpClient();
        	publishProgress("Http Client created...");
        	HttpGet httpget = new HttpGet(urlRequest);
        	//setCredentials(httpGet)
        	//addHeaders(httpGet)
        	httpget.addHeader("accept", "application/json");
        	
        	HttpResponse response;
        	try{
        		publishProgress("Getting response from server");
        		response =httpclient.execute(httpget);
        		if(response.getStatusLine().getStatusCode()!=200){
        			return null;
        		}
        		HttpEntity entity= response.getEntity();
        		publishProgress("Converting entity to String");
        		String responseContent=EntityUtils.toString(response.getEntity());
        		return responseContent;
        	}
        	catch(Exception e){
        		e.printStackTrace();
        		return null;
        	}
       
        }
        
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        // infoText.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
        	if(result!=null){
        //	responseText.setText(result);
        	}
        	setHolder(result);
        	populateView(holder);
	
}
	}
	
	
}
    
   