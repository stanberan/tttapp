package uk.ac.abdn.t3.trustedtinythings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import ws.GenericListResponse;
import ws.GetResponseCallback;
import ws.RestUtils;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NFCActivity extends FragmentActivity {
	ProgressBar progress;
	static ScanATagFragment scan = null;
	static String info = "noinfo";
	static String MD5 = null;
	boolean first=false;
	String urlAction=null;
	String uid;
	SharedPreferences prefs;
	int busStop=0;
	static boolean connected=true;
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
	
	
		setContentView(R.layout.scan_activity);
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		 uid=tm.getDeviceId();	 
		
		
		
		scan=(ScanATagFragment)getSupportFragmentManager().findFragmentById(R.id.scan_a_tag);
		/*prefs= PreferenceManager.getDefaultSharedPreferences(this); //MOVED
		if(!prefs.getBoolean("EULA_ACCEPTED", false)) {
		    showEula();
		    // Determine if EULA was accepted this time
		  
		}
		else{
			
			*/
			
		handleIntent(getIntent());
		
		//}
	
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onResume() {
		
		super.onResume();
		
	}

	@SuppressLint("NewApi")
	public void handleIntent(Intent i) {
	
		
		String action = i.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			scan.setProgress(true);
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
				//CHECK FOR BUSSTOP
				if (urlAction.contains("deps.at")) {
					 busStop=1;
				}
					MD5 = Helpers.getMD5(rawMessage, idTag);
				
				
				info = "URI" + urlAction+ "Size:"
						+ ndefMessage.getRecords().length + "\nTYPE:" + type;
				Helpers.loading(true, this, "Retrieving device information and capabilities...");
				new ServerResponse().execute(new String[]{MD5});
				
			}
		}
		else{
			scan.setCatalogue();
		}
	}
	
	
	
	
private class ServerResponse extends AsyncTask<String, String, String> {

		
		
		
	
	public String accepted(String deviceId){
		
		
		 String urlRequest="http://t3.abdn.ac.uk:8080/t3/1/user/accepted/"+uid+"/"+deviceId;
		 Log.e("UID:",uid+"   dev:"+deviceId);
		HttpClient httpclient= new DefaultHttpClient();
		HttpParams par=httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(par, 6000);
HttpConnectionParams.setSoTimeout(par, 6000);
	HttpGet httpget = new HttpGet(urlRequest);
	HttpResponse response;
	try{
		response =httpclient.execute(httpget);
		HttpEntity entity= response.getEntity();
		
		if(response.getStatusLine().getStatusCode()==200){
			String answer =EntityUtils.toString(entity);
			JSONObject root=new JSONObject(answer);
			String time=root.getString("accepted");
			
			return time;
			
			
			
			
		}
	}
	catch(Exception e){
		e.printStackTrace();
		Helpers.loading(false, NFCActivity.this, "Retrieving device data...");
		
	}

		return null;
	}
	
	
	
	
		
		
		
       @Override
       protected String doInBackground(String... params) {
      
       		String accepted=accepted(params[0]);
       		if(accepted!=null){
       			return accepted;
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
       		scan.setProgress(false);
       		Helpers.loading(false, NFCActivity.this, null);
       		startActivity(i);
       		
       		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
       	    try {
					Date parsedDate = dateFormat.parse(result);
					Calendar myCal = new GregorianCalendar();
					myCal.setTime(parsedDate);
					Toast.makeText(getApplicationContext(), "You trusted this device on "+parsedDate,Toast.LENGTH_LONG).show();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
       		
       		
       		//end our application
       		//TODO TIME WHEN TRUSTED
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
    	
    	//wait for acceptance
    	prefs= PreferenceManager.getDefaultSharedPreferences(NFCActivity.this);
		while(!prefs.getBoolean("EULA_ACCEPTED", false)) {}
    	
    	String urlRequest="http://t3.abdn.ac.uk:8080/t3/1/thing/"+params[0]+"/"+uid+"/information?busstop="+busStop;
    	HttpClient httpclient= Helpers.createHttpClient();
    	
    	HttpParams par=httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(par, 6000);
HttpConnectionParams.setSoTimeout(par, 6000);
    	
    	
    	publishProgress("Http Client created...");
    	HttpGet httpget = new HttpGet(urlRequest);
    	Log.d("busstop",""+ urlRequest);
    	httpget.getParams().setParameter("busstop", ""+busStop);
    	//setCredentials(httpGet)
    	//addHeaders(httpGet)
    	httpget.addHeader("accept", "application/json");
    	
    	
    	HttpResponse response;
    	try{
    		//first network call
    		connected=true;
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
    		connected=false;
    		
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
    		//Toast.makeText(NFCActivity.this, result, Toast.LENGTH_SHORT).show();
    Intent s=new Intent(NFCActivity.this, MainActivity.class);
    getActionBar().hide();
    s.putExtra("response", result);
    s.putExtra("android_id", uid);
    s.putExtra("MD5", MD5);
    s.putExtra("URL",urlAction);
    s.putExtra("accept", false);    //used only for logic where the request of device is coming from (Accept vs. Delete)
    scan.setProgress(false);
	Helpers.loading(false, NFCActivity.this, null);
    startActivity(s);
    finish();
    	}
  
    	else{
    		Helpers.loading(false, NFCActivity.this, null);
    		scan.setProgress(false);
    		if(connected){
    		alertDialog("WARNING!","I am sorry, but this device has not been registered with Trusted Tiny Things service.\n Would you like to proceed anyway?",NFCActivity.this);
    	}
    		else{
    			Toast.makeText(NFCActivity.this, "Seems there is an issue with your internet connection.",Toast.LENGTH_LONG).show();
    		}
    	
    	
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
			.setCancelable(false).setIcon(getResources().getDrawable(R.drawable.notfound))
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
						scan.setCatalogue();
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


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


