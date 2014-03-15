package uk.ac.abdn.t3.trustedtinythings;

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
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Helpers {
static ProgressDialog mDialog;
	public static DefaultHttpClient createHttpClient() { 
		final int TIMEOUT=30;
		final SchemeRegistry supportedSchemes = new SchemeRegistry();
		supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80)); 
	//	supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443)); 
		final HttpParams params = new BasicHttpParams(); HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000); 
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		HttpClientParams.setRedirecting(params, true); 
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
						
						
					}
				  })
				;
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			}
	
	
	//used when loading data from server
	public static void loading(boolean loading, Context context, String message){
		 if(loading){
			 mDialog = ProgressDialog.show(context,"Please wait...", message, true);
		 }
		 else{
			 mDialog.dismiss();
		 }
		 
		 
	 }
	 

	 public static void showCompanyDialog(String result,String title,Context c){
		
	
		 try{
		 JSONObject res= new JSONObject(result);
		 final Dialog dialog = new Dialog(c);
			dialog.setContentView(R.layout.company_layout);
			dialog.setTitle(title);
			dialog.setCanceledOnTouchOutside(true);
			

			TextView address = (TextView) dialog.findViewById(R.id.address);
			
			address.setText(res.getString("address"));
			StyledTextView email=(StyledTextView)dialog.findViewById(R.id.company_email);
			if(!res.getString("email").equals("null")){
			email.setText(res.getString("email"));
			email.setVisibility(View.VISIBLE);
			dialog.findViewById(R.id.label_email).setVisibility(View.VISIBLE);
			}
			StyledTextView url=(StyledTextView)dialog.findViewById(R.id.company_website);
			if(!res.getString("url").equals("null")){
			url.setText(res.getString("url"));
			url.setVisibility(View.VISIBLE);
			dialog.findViewById(R.id.label_website).setVisibility(View.VISIBLE);
			}
			StyledTextView phone=(StyledTextView)dialog.findViewById(R.id.company_phone);
			if(!res.getString("telNumber").equals("null")){
			phone.setText(res.getString("telNumber"));
			phone.setVisibility(View.VISIBLE);
			dialog.findViewById(R.id.label_phone).setVisibility(View.VISIBLE);
			}
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			Picasso.with(c).load(res.getString("logo")).into(image);
	
			
		 dialog.show();
		 }
		 catch(Exception e){
			 Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG).show();
		 }
		 
		 
		 
	 }
	
	
	
	
}
