package uk.ac.abdn.t3.trustedtinythings;



import java.util.ArrayList;
import java.util.List;

import uk.ac.abdn.t3.trustedtinythings.OverviewListAdapter.GenericRow;
import ws.DeviceListHolder;
import ws.GenericListResponse;
import ws.GetResponseCallback;
import ws.GetTask;
import ws.RestTaskCallback;
import ws.RestUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ScanATagFragment extends Fragment{
	
	private static ListView catalogue;
	private static ProgressBar progress=null;
	private static TextView progress_catalogue;
	private static String uid;
	private static boolean isNFC=false;
	public static boolean isRetrievingCat=false;
	private boolean first=true;
	private SharedPreferences prefs;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final TelephonyManager tm = (TelephonyManager) getActivity().getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		 uid=tm.getDeviceId();	 
			View result=inflater.inflate(R.layout.scan_tag_frag,container,false);		
			progress=(ProgressBar)result.findViewById(R.id.progressBar1);	
			catalogue=(ListView)result.findViewById(R.id.catalogue_list_view);
			View header = View.inflate(getActivity(), R.layout.catalogue_list_header, null);
			catalogue.addHeaderView(header, null, false);
			
			progress_catalogue=(TextView)result.findViewById(R.id.catalogue_progress);
			progress_catalogue.setVisibility(View.GONE);
			catalogue.setOnItemClickListener(new OnItemClickListener(){
           
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
					if(catalogue.getAdapter().isEmpty()){
						setCatalogue();
						catalogue.invalidate();
						return;
					}
					Object o = catalogue.getItemAtPosition(position);
					 final DeviceListHolder device=(DeviceListHolder)o;
					
					
				//	Toast.makeText(getActivity(), device.getId(), Toast.LENGTH_SHORT).show();
					//TODO     populate main activity view check for errors!!
					
					
					 Helpers.loading(true,getActivity(),"Retrieving information about this device...");
String url="http://t3.abdn.ac.uk:8080/t3/1/thing/"+device.getId()+"/"+uid+"/information";
					
					new GetTask(url, new RestTaskCallback (){
						
			            @Override
			            public void onTaskComplete(String response){
			            	Helpers.loading(false,getActivity(),null);
			          String devicedata=response;
			          Intent i=new Intent(getActivity(), MainActivity.class);
			          i.putExtra("android_id", uid);
			          i.putExtra("MD5", device.getId());
			          i.putExtra("response", devicedata);
			          i.putExtra("accept", true);
			          startActivity(i);
			            	
			            }

						@Override
						public void onFailure(String message) {
							Helpers.loading(false,getActivity(),null);
							Toast.makeText(getActivity(), "Could not retrieve device information!(Check your mobile data and network connection?)", Toast.LENGTH_LONG).show();
							
						}
			        }).execute();
					
				}
				
			});
			
			catalogue.setAdapter(null);
			/*prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
			if(!prefs.getBoolean("EULA_ACCEPTED", false)) {
			    Helpers.showEula(getActivity());
			    // Determine if EULA was accepted this time
			  
			}*/
			return result;
	}
	public void onActivityCreated(Bundle savedInstanceState) { 
	super.onActivityCreated(savedInstanceState);
	
	setHasOptionsMenu(true);
	//setCatalogue();
	}
	
	
	public void setCatalogue(){
		isRetrievingCat=true;
		Helpers.loading(true,getActivity(),"Retrieving your device catalogue list from server ...");
		RestUtils utils =new RestUtils();
		progress.setVisibility(View.VISIBLE);
		utils.getList(uid, new GetResponseCallback(){

			@Override
			public void onListDataReceived(List<GenericListResponse> response) {
				Helpers.loading(false,getActivity(),null);
				if(getActivity()!=null && response!=null){
				
			CatalogueAdapter ad=new CatalogueAdapter(getActivity().getApplicationContext(),R.layout.catalogue_card,(ArrayList<DeviceListHolder>)(List<?>) response);
				catalogue.setAdapter(ad);
				if(!isNFC){
				progress.setVisibility(View.GONE);
				}
				if(response.size()==0){
					progress_catalogue.setVisibility(View.VISIBLE);
				
					progress_catalogue.setText("No devices in your collection.");
					
				}
			}
				isRetrievingCat=false;
			}
			@Override
			public void onFailure(String message) {
			Helpers.loading(false,getActivity(),null);
			Toast.makeText(getActivity(), "Could not retrieve your device collection.\n Check your internet connection", Toast.LENGTH_LONG).show();
				if(!isNFC){
				progress.setVisibility(View.GONE);
				}
				progress_catalogue.setVisibility(View.VISIBLE);
				progress_catalogue.setText(message);
				isRetrievingCat=false;
			}
			
		});
	}

	

	public void setProgress(boolean p){
		if(p){
			progress.setVisibility(View.VISIBLE);
			isNFC=true;
		}
		else{
			progress.setVisibility(View.GONE);
			isNFC=false;
		}

		
	}
	
	
	public void showEula(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
String raw=getResources().getText(R.string.terms_conditions).toString();

		final SpannableString s = 
	               new SpannableString(raw);
	  Linkify.addLinks(s, Linkify.WEB_URLS);
		
			// set title
			alertDialogBuilder.setTitle("Terms & Conditions");

			// set dialog message
			alertDialogBuilder
				.setMessage(s)
				.setCancelable(false)
				.setPositiveButton("I Agree to terms and conditions",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						  prefs.edit().putBoolean("EULA_ACCEPTED", true).commit();
					}
				  }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
							getActivity().finish();
						}
					})
				;

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
			
				// show it
				alertDialog.show();
				((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
			}
	 
}

