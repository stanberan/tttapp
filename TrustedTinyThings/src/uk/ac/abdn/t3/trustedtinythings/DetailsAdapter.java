package uk.ac.abdn.t3.trustedtinythings;


import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

	public class DetailsAdapter extends ArrayAdapter<Capability> {
		
		
	    public DetailsAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}
		private Context context;

	    public DetailsAdapter(Context context, int textViewResourceId, ArrayList<Capability> items) {
	        super(context, textViewResourceId, items);
	        this.context = context;
	    }

		public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.details_capability_row, null);
	        }
	        

	        Capability item = getItem(position);
	        if (item!= null) {
	            // My layout has only one TextView
	          StyledTextView who_name=(StyledTextView)view.findViewById(R.id.who_name);
	          ImageView who_image=(ImageView)view.findViewById(R.id.who_image);
	          StyledTextView why=(StyledTextView)view.findViewById(R.id.why_name);
	          StyledTextView what=(StyledTextView)view.findViewById(R.id.what_name);
	          
	          who_name.setText(item.getConsumer());
	          Picasso.with(context).load(item.getConsumerLogo()).into(who_image);
	          why.setText(item.getPurpose());
	          what.setText(item.getConsumes());
	        }
	          

	        return view;
	    }
	
		
	}
	

