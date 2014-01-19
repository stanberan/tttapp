package com.example.trustedtinythings;

import java.util.Arrays;

import com.example.trustedtinythings.Capability;
import com.example.trustedtinythings.Quality;

public class InformationHolder {

String manufacturer;
String manufacturerURL;
String manufacturerLogo;
String ownerLogo;
String owner;
String ownerURL;
String description;
String deviceType;
String imageURL;

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






}
