package uk.ac.abdn.t3.trustedtinythings;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.trustedtinythings.R;

import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class NFCActivity extends FragmentActivity {
	static ScanATagFragment scan = null;
	static String info = "noinfo";
	static String MD5 = null;
	boolean first=false;
	String urlAction=null;
	String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
/*
		if (!NfcAdapter.getDefaultAdapter(this).isEnabled()) {
			Toast.makeText(getApplicationContext(),
					"Please Enable NFC before using this app",
					Toast.LENGTH_LONG).show();
			finish();
		}
*/
		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			scan = new ScanATagFragment();
			first=true;
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(android.R.anim.fade_in,
							android.R.anim.fade_out)
					.add(android.R.id.content, scan).commit();
		}

		handleIntent(getIntent());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

	public void onResume() {
		super.onResume();
		scan.setInfo(info);
	}

	@SuppressLint("NewApi")
	public void handleIntent(Intent i) {
		String action = i.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			Log.e("NFC", "Discovered NFC with NDEF");
			String scheme = i.getScheme();
			if (scheme.equals("http")) {
				Tag tag = i.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				Ndef ndef = Ndef.get(tag);
				if (ndef == null) {
					// not supported;
					Log.d("Tag:Ndef", "Not supported");
				}
				NdefMessage ndefMessage = ndef.getCachedNdefMessage();
				byte[] rawMessage = ndefMessage.toByteArray();
				byte[] idTag = tag.getId();
				NdefRecord ndefrecord = ndefMessage.getRecords()[0];
				
				String type = new String(ndefrecord.getType());
				byte[] payloadurl = ndefMessage.getRecords()[0].getPayload();
				String urltemp = "http://";
				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					try {
						NdefMessage nestedMessage = new NdefMessage(payloadurl);
						byte[] nestedPayload = nestedMessage.getRecords()[0]
								.getPayload();
						for (int s = 1; s < nestedPayload.length; s++) {
							urltemp += (char) nestedPayload[s];
						}
						urlAction=urltemp;
					} catch (FormatException e) {
						Toast.makeText(getApplicationContext(),
								"No wrapper around NDEF Tag!",
								Toast.LENGTH_LONG).show();
					}

				}
				else{
				String u = ndefrecord.toUri().toString();
				urlAction=u;
				}
				
				if (urlAction.contains("deps.at")) {
					MD5 = "MD5Hash";
				} else {
					MD5 = Helpers.getMD5(rawMessage, idTag);
					
				}
				
				info = "URI" + urlAction+ "Size:"
						+ ndefMessage.getRecords().length + "\nTYPE:" + type;
				
				new ServerResponse().execute(new String[]{MD5});
				
			}
		}
	}
	
	
	
	
private class ServerResponse extends AsyncTask<String, String, String> {

		
		
		
	
	public boolean accepted(String deviceId){
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		 uid=tm.getDeviceId();	 	 
		 String urlRequest="http://t3.abdn.ac.uk:8080/t3/1/user/accepted/"+uid+"/"+deviceId;
		 Log.e("UID:",uid+"   dev:"+deviceId);
		HttpClient httpclient= new DefaultHttpClient();
	HttpGet httpget = new HttpGet(urlRequest);
	HttpResponse response;
	try{
		response =httpclient.execute(httpget);
		HttpEntity entity= response.getEntity();
		if(response.getStatusLine().getStatusCode()==200){
			return true;
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
		return false;
	}
	
	
	
	
		
		
		
        @Override
        protected String doInBackground(String... params) {
       
        		boolean accepted=accepted(params[0]);
        		if(accepted){
        			return "1";
        		}
        		else{
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
        		Intent i = new Intent(Intent.ACTION_VIEW);
        		i.setData(Uri.parse(urlAction));
        		startActivity(i);
        		//end our application
        		finish();
        	}
        	else{
 
        		new DeviceExist().execute(new String[]{MD5});
        		
        	}
}
	}





private class DeviceExist extends AsyncTask<String, String, String> {

	

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
    Intent s=new Intent(NFCActivity.this, MainActivity.class);
    s.putExtra("response", result);
    s.putExtra("android_id", uid);
    s.putExtra("MD5", MD5);
    s.putExtra("URL",urlAction);
    startActivity(s);
    finish();
    	}
  
    	else{
    		alertDialog("WARNING!","I am sorry, but this device was not recognized in our system. Would you still like to execute its intent?",NFCActivity.this);
    	}
}
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
					Intent i = new Intent(Intent.ACTION_VIEW);
	        		i.setData(Uri.parse(urlAction));
	        		startActivity(i);
	        		//end our application
	        		finish();
					
				}
			  }).setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				})
			;

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		}

}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


