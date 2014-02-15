import java.util.ArrayList;

import uk.ac.abdn.t3.trustedtinythings.Capability;
import uk.ac.abdn.t3.trustedtinythings.R;
import uk.ac.abdn.t3.trustedtinythings.StyledTextView;
import ws.DeviceListHolder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

	public class CatalogueAdapter extends ArrayAdapter<DeviceListHolder> {
		
		
	    public CatalogueAdapter(Context context, int resource) {
			super(context, resource);
			// TODO Auto-generated constructor stub
		}
		private Context context;

	    public CatalogueAdapter(Context context, int textViewResourceId, ArrayList<DeviceListHolder> items) {
	        super(context, textViewResourceId, items);
	        this.context = context;
	    }

		public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.catalogue_card, null);
	        }
	        

	        DeviceListHolder item = getItem(position);
	        if (item!= null) {
	        
	          StyledTextView me=(StyledTextView)view.findViewById(R.id.nick_name_view);
	          ImageView who_image=(ImageView)view.findViewById(R.id.catalogue_image);
	          
	          
	          me.setText(item.getNickname());
	          Picasso.with(context).load(item.getImage_url()).into(who_image);
	       
	        }
	          

	        return view;
	    }
	
		
	}