package uk.ac.abdn.t3.trustedtinythings;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScanATagFragment extends Fragment{
	
	private static StyledTextView scan_a_tag=null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View result=inflater.inflate(R.layout.scan_tag_frag,container,false);		
		//	scan_a_tag=(StyledTextView)result.findViewById(R.id.info_view);	
			return result;
			
}
	public void onActivityCreated(Bundle savedInstanceState) { 
	super.onActivityCreated(savedInstanceState);
	setRetainInstance(true);
	setHasOptionsMenu(true);
	}
	
	
	
	
	public void setInfo(String text){
	//scan_a_tag=(TextView) getView().findViewById(R.id.scan_a_tag_view);
		//scan_a_tag.setText(text);
		
	}
}

