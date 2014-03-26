package uk.ac.abdn.t3.trustedtinythings;

public class GenericRowView implements OverviewListAdapter.GenericRow{
String title;
String description;
String letter;
String image;
	
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public String getLetter() {
		// TODO Auto-generated method stub
		return letter;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	@Override
	public String getImage() {

		return image;
	}
	public void setImage(String image){
		this.image=image;
	}

	
	
	
}
