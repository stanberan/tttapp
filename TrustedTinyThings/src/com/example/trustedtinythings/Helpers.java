package com.example.trustedtinythings;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Helpers {

	public static DefaultHttpClient createHttpClient() { 
		final int TIMEOUT=15;
		final SchemeRegistry supportedSchemes = new SchemeRegistry();
		supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80)); 
	//	supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443)); 
		final HttpParams params = new BasicHttpParams(); HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000); 
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpClientParams.setRedirecting(params, false); 
		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, supportedSchemes); 
		return new DefaultHttpClient(ccm, params);}
	
	
	public static String convertInputToString(InputStream input){
    	BufferedReader r = new BufferedReader(new InputStreamReader(input));
    	StringBuilder total= new StringBuilder();
    	String line=null;
    	try{
    	while((line=r.readLine())!=null){
    		total.append(line);
    	}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return total.toString();
    	}
	
	
	public static String getMD5(byte[]message,byte[]id){
		MessageDigest messag;
		try{
			messag=MessageDigest.getInstance("MD5");
			messag.update(message);
			messag.update(id);
			byte[]output=messag.digest();
			StringBuffer sb =new StringBuffer();
			for(int i=0;i<output.length; i++){
				sb.append(Integer.toString((output[i] & 0xff) + 0x100,16).substring(1));
			}
				return sb.toString();
					
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return "";		
		
	}
	
	
	
	public static void alertDialog(String title, String message, Context c){
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
						// if this button is clicked, close
						// current activity
						
					}
				  })
				;
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			}
	
	
	
	
	
	
}
