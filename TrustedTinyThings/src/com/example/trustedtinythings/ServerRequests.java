package com.example.trustedtinythings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class ServerRequests {
	

	public InformationHolder setHolder(String response){
		InformationHolder holder=new InformationHolder();
		if(response==null){
		//	infoText.setText("This device was not recognized in our system.\nScan another tag.");
				//TO DO -- REGISTER DEVICE BUTTON. - need deviceID and phoneID
			return null;
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
			return holder;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		//	infoText.setText("JSON ERROR in setholder() method");
			e.printStackTrace();
			return null;
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
	
}
	}
}
	
