package uk.ac.abdn.t3.trustedtinythings;

public class Capability implements OverviewListAdapter.GenericRow {

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
		// TODO Auto-generated method stub
		return name;
	}
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "This device is collecting your data";
	}
	@Override
	public String getLetter() {
		// TODO Auto-generated method stub
		return "C";
	}
	
	
	
}

