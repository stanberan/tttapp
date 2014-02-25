package uk.ac.abdn.t3.trustedtinythings;

import android.os.Parcel;
import android.os.Parcelable;

public class Capability implements OverviewListAdapter.GenericRow,Parcelable {

	public Capability(){}
	public Capability(Parcel in){
		readFromParcel(in);
	}
	
	
	public String getConsumer() {
		return consumer;
	}
	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}
	public String getConsumerURL() {
		return consumerURL;
	}
	public void setConsumerURL(String consumerURL) {
		this.consumerURL = consumerURL;
	}
	public String getConsumes() {
		return consumes;
	}
	public void setConsumes(String consumes) {
		this.consumes = consumes;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getConsumerLogo() {
		return consumerLogo;
	}
	public void setConsumerLogo(String consumerLogo) {
		this.consumerLogo = consumerLogo;
	}
public String getCapabilityType() {
		return capabilityType;
	}
	public void setCapabilityType(String capabilityType) {
		this.capabilityType = capabilityType;
	}
	//commit to remote
	String consumer;
	String consumerURL;
	String consumes;
	String purpose;
	String name;
	String consumerLogo;
	String capabilityType;
	@Override
	public String toString() {
		return "Capability [consumer=" + consumer + ", consumerURL="
				+ consumerURL + ", consumes=" + consumes + ", purpose="
				+ purpose + ", name=" + name + "]";
	}
	@Override
	public String getTitle() {
	
		return name;
	}
	@Override
	public String getDescription() {
		// TODO Get description from server
		return "Data is collected from your mobile phone: "+consumes;
	}
	@Override
	public String getLetter() {
	
		return "C";
	}
	@Override
	public int describeContents() {
		
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(consumer);
		dest.writeString(consumerURL);
		dest.writeString(consumes);
		dest.writeString(purpose);
		dest.writeString(name);
		dest.writeString(consumerLogo);
		dest.writeString(capabilityType);
		
	}
	private void readFromParcel(Parcel in) {  
		consumer=in.readString();
		consumerURL=in.readString();
		consumes=in.readString();
		purpose=in.readString();
		name=in.readString();
		consumerLogo=in.readString();
		capabilityType=in.readString();
	}
	public static final Parcelable.Creator CREATOR =
		    new Parcelable.Creator() {
		        public Capability createFromParcel(Parcel in) {
		            return new Capability(in);
		        }

		        public Capability[] newArray(int size) {
		            return new Capability[size];
		        }
		    };
	
	
}

