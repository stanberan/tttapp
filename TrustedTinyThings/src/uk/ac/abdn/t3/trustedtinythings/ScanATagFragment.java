package uk.ac.abdn.t3.trustedtinythings;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ScanATagFragment extends Fragment{
	
	
	private static ProgressBar progress=null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View result=inflater.inflate(R.layout.scan_tag_frag,container,false);		
			progress=(ProgressBar)result.findViewById(R.id.progressBar1);	
			return result;
			
}
	public void onActivityCreated(Bundle savedInstanceState) { 
	super.onActivityCreated(savedInstanceState);
	setRetainInstance(true);
	setHasOptionsMenu(true);
	}
	
	
	
	
	public void setProgress(boolean p){
		if(p){
			progress.setVisibility(View.VISIBLE);
		}
		else{
			progress.setVisibility(View.GONE);
		}

		
	}
}

