package ws;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

public class RestUtils {
	
	
	public void getList(String userName, final GetResponseCallback callback){
		String url=buildListUrl(userName);
		
		new GetTask(url, new RestTaskCallback (){
            @Override
            public void onTaskComplete(String response){
                ArrayList<GenericListResponse> list = parseList(response);
                callback.onListDataReceived(list);
            }

			@Override
			public void onFailure(String message) {
				callback.onFailure(message);
				
			}
        }).execute();
		
		
	}
	
	
	public String buildListUrl(String username){
Uri.Builder b = Uri.parse("http://t3.abdn.ac.uk:8080").buildUpon().
				
				appendPath("t3").appendPath("1").appendPath("thing").appendPath("list").appendPath(username);
return b.build().toString();
	}
	
	
	public ArrayList<GenericListResponse> parseList(String response) {
		ArrayList<GenericListResponse> list=new ArrayList<GenericListResponse>();
		try{
		JSONObject root=new JSONObject(response);
		JSONArray devices=root.getJSONArray("devices");
		for (int i=0; i<devices.length();i++){
		JSONObject device=devices.getJSONObject(i);
		DeviceListHolder dev=new DeviceListHolder();
		dev.id=device.getString("id");
		dev.image_url=device.getString("url");
		dev.nickname=device.getString("nickname");
		list.add(dev);
			
		}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
		
		
	}

}
