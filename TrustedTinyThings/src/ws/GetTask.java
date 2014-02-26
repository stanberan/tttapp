package ws;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import uk.ac.abdn.t3.trustedtinythings.Helpers;
import android.os.AsyncTask;

public class GetTask extends AsyncTask<String, String, String>{

    private String mRestUrl;
    private RestTaskCallback mCallback;

    /**
     * Creates a new instance of GetTask with the specified URL and callback.
     * 
     * @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     * 
     */
    public GetTask(String restUrl, RestTaskCallback callback){
        this.mRestUrl = restUrl;
        this.mCallback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
       
      try{
        HttpClient httpclient= new DefaultHttpClient();
      HttpParams par=httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(par, 6000);
HttpConnectionParams.setSoTimeout(par, 6000);
        
    	HttpGet httpget = new HttpGet(mRestUrl);
    	 HttpResponse responseHttp=httpclient.execute(httpget);
    	
    	
    	response=EntityUtils.toString(responseHttp.getEntity());
    	return response;
      }
      catch(Exception e){
    	  e.printStackTrace();
    	  return "exception:"+e.getMessage();
      }
    }

    @Override
    protected void onPostExecute(String result) {
    	if(result.startsWith("exception")){
    		mCallback.onFailure(result);
    	}
    	else{
        mCallback.onTaskComplete(result);
    	}
        super.onPostExecute(result);
    }
}

   