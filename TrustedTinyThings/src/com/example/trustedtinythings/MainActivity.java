package com.example.trustedtinythings;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
TextView infoText;
TextView responseText;
ImageView manufacturerLogo;
ImageView ownerLogo;
ImageView consumerLogo;
static InformationHolder holder=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main);
		responseText=(TextView)findViewById(R.id.responseText);
		infoText=(TextView)findViewById(R.id.infoText);
		
		manufacturerLogo=(ImageView)findViewById(R.id.manufacturerlogo);
		ownerLogo=(ImageView)findViewById(R.id.ownerlogo);
		consumerLogo=(ImageView)findViewById(R.id.consumerlogo);
		
	
		infoText.setClickable(true);
		infoText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
				Picasso.with(MainActivity.this).load(holder.manufacturerLogo).resize(200,200).into(manufacturerLogo);
				Picasso.with(MainActivity.this).load(holder.ownerLogo).into(ownerLogo);
				Picasso.with(MainActivity.this).load(holder.capabilities[0].consumerLogo).into(consumerLogo);
				
			}
		});
		
		
		
		Intent i=getIntent();
		String id=i.getStringExtra("id");	
		
		
		if(id==null){
			getDeviceInfo("MD5Hash");
		}
		else{
		getDeviceInfo(id);
		}
		infoText.setText("Show me the frickin companies who are behind this device :P");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void setHolder(String response){
		if(response==null){
			infoText.setText("This device was not recognized in our system.\nScan another tag.");
				//TO DO -- REGISTER DEVICE BUTTON. - need deviceID and phoneID
			return;
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
			
			responseText.setText(holder.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			infoText.setText("JSON ERROR in setholder() method");
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
	

	
	
	
	public void getDeviceInfo(String id){
    	try{
    	new ServerResponse().execute(new String[]{id});
			 /*
			 if(200!=response.getStatusLine().getStatusCode()){
    			
    		//	info.setText("This device was not recognized in our system.");
    		//	acceptContinue.setClickable(false);
    		//	registerButton.setVisibility(View.VISIBLE);
    			return;
    		}
    		
    		if(entity!=null){
    		//	acceptContinue.setText("Accept & Continue");
    			InputStream instream= entity.getContent();
    			String result= Helpers.convertInputToString(instream);
    			Log.e("JSON", result);
    		//	json.setText(result);
    			JSONObject json= new JSONObject(result);
    			JSONArray capabilities=json.getJSONArray("capabilities");
    			JSONArray features =json.getJSONArray("features");
    			Log.e("Features:",features.toString(2));
    			Log.e("Capabilities:",capabilities.toString(2));
    			
    			String picture =json.getString("picture");
    			String infoString="Device Type: "+json.getString("deviceType")+"\n"+
    			"Owner:"+json.getString("owner")+"\n"+
    			"Manufacturer: "+json.getString("manufacturer");
    			//info.setText(infoString);
    		//	description.setText(json.getString("deviceDescription")); 
    			
    		//	CapabilityAdapter capAdapter=new CapabilityAdapter(this,getCapabilities(capabilities));
    		//	capabilityList.setAdapter(capAdapter);
    			
    		//	QualityAdapter qualAdapter=new QualityAdapter(this,getQualities(features));
    		//	qualityList.setAdapter(qualAdapter);
    			Log.e("Bus Image URL", picture);
    		//	loadImage(picture);
    		//	instream.close();
    			
    		
    			
    		}
    		else{
    	//		json.setText("ID not Found. Try Again!");
    		}
    		*/
    	}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    		
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
         infoText.setText(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
        	if(result!=null){
        	responseText.setText(result);
        	}
        	setHolder(result);
        	
        	// Toast.makeText(getApplicationContext(),responseContent , Toast.LENGTH_LONG).show();
        
          //  TextView txt = (TextView) findViewById(R.id.output);
          //  txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }
	
	
        	}
	
	
}
    
   