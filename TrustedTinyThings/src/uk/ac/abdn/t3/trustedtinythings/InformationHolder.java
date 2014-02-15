package uk.ac.abdn.t3.trustedtinythings;

import java.util.Arrays;

import uk.ac.abdn.t3.trustedtinythings.Capability;
import uk.ac.abdn.t3.trustedtinythings.Quality;

public class InformationHolder {

	static InformationHolder holder=null;
	
String manufacturer;
String manufacturerURL;
String manufacturerLogo;
String ownerLogo;
String owner;
String ownerURL;
String description;
String deviceType;
String imageURL;
String thingName;


Company[]companies;
DeviceData[] data;
Capability[] capabilities;
Quality[] qualities;
@Override
public String toString() {
	return "InformationHolder [manufacturer=" + manufacturer
			+ ", manufacturerURL=" + manufacturerURL + ", owner=" + owner
			+ ", ownerURL=" + ownerURL + ", description=" + description
			+ ", deviceType=" + deviceType + ", imageURL=" + imageURL
			+ ", capabilities=" + Arrays.toString(capabilities)
			+ ", qualities=" + Arrays.toString(qualities) + "]";
	

	
}
public void setCompanies(){
	
}






}
