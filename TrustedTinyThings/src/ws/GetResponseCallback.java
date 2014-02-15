package ws;

import java.util.List;

public abstract class GetResponseCallback{

    /**
     * Called when the response data for the REST call is ready. <br/>
     * This method is guaranteed to execute on the UI thread.
     * 
     * @param profile The {@code Profile} that was received from the server.
     */
   public abstract void onListDataReceived(List<GenericListResponse> response);
  public abstract void onFailure(String message);
    /*
     * Additional methods like onPreGet() or onFailure() can be added with default implementations.
     * This is why this has been made and abstract class rather than Interface.
     */
}
