package uk.ac.abdn.t3.trustedtinythings;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

	public class OverviewListAdapter extends ArrayAdapter<OverviewListAdapter.GenericRow> {
		
		
	    public OverviewListAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}
		private Context context;

	    public OverviewListAdapter(Context context, int textViewResourceId, ArrayList<OverviewListAdapter.GenericRow> items) {
	        super(context, textViewResourceId, items);
	        this.context = context;
	    }

		public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.capability_row, null);
	        }
	        

	        GenericRow item = getItem(position);
	        if (item!= null) {
	            // My layout has only one TextView
	            StyledTextView letterView = (StyledTextView) view.findViewById(R.id.capital_letter_view);
	            if (letterView != null) {
	                // do whatever you want with your string and long
	            	String letter=item.getLetter();	            	
	                letterView.setText(letter);
	                if(letter.equals("Q")){
	                	letterView.setBackgroundColor(context.getResources().getColor(R.color.green));
	                }
	                else{
	                	letterView.setBackgroundColor(context.getResources().getColor(R.color.YellowGreen));
	                }
	            }
	            StyledTextView descriptionView=(StyledTextView)view.findViewById(R.id.row_description);
	            descriptionView.setText(item.getDescription());
	            StyledTextView titleView=(StyledTextView)view.findViewById(R.id.row_title);
	            titleView.setText(item.getTitle());
	         }

	        return view;
	    }
		public interface GenericRow{
			public String getTitle();
			public String getDescription();
			public String getLetter();
		}
		
	}
	
