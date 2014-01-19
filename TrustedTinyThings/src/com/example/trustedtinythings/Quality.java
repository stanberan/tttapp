package com.example.trustedtinythings;

public class Quality {
String provider;
String description;
public String getProvider() {
	return provider;
}
public void setProvider(String provider) {
	this.provider = provider;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
@Override
public String toString() {
	return "Quality [provider=" + provider + ", description=" + description
			+ "]";
}
	
}
