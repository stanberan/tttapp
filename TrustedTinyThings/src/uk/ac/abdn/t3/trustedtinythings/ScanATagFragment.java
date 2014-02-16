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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
							Helpers.loading(false,getActivity(),"Removing this device from your list of accepted devices...");
							Toast.makeText(getActivity(), "Could not retrieve device information!(Check your mobile data and network connection?)", Toast.LENGTH_LONG).show();
							
						}
			        }).execute();
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
				}
				
			});
			
			
			return result;
	}
	public void onActivityCreated(Bundle savedInstanceState) { 
	super.onActivityCreated(savedInstanceState);
	
	setHasOptionsMenu(true);
	setCatalogue();
	}
	
	
	public void setCatalogue(){
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
				
					progress_catalogue.setText("No accepted devices.");
				}
			}
			}
			@Override
			public void onFailure(String message) {
			Helpers.loading(false,getActivity(),null);
			Toast.makeText(getActivity(), "Could not retrieve your device list.\n Check your internet connection", Toast.LENGTH_LONG).show();
				if(!isNFC){
				progress.setVisibility(View.GONE);
				}
				progress_catalogue.setVisibility(View.VISIBLE);
				progress_catalogue.setText(message);
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
}

