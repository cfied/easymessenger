
public class Message {
	private final String OWNERID;
	private final String ID;
	private String text;
	
	public Message(String ownerID, String id, String text){
		this.OWNERID = ownerID;
		this.ID = id;
		this.text = text;
	}
	
	public String getownerID(){
		return OWNERID;
	}
	
	public String getID(){
		return ID;
	}
	
	public String getText(){
		return text;
	}
	
}
