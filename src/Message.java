
public class Message {
	private final String SENDER_ID;
	private final String ID;
	private String receiver_id;
	private String text;
	private boolean isGroup;
	
	public Message(String id, String senderID, String receiverId, String text, boolean isGroup){
		this.SENDER_ID = senderID;
		this.receiver_id = receiverId;
		this.ID = id;
		this.text = text;
		this.isGroup = isGroup;
	}
	
	public String getSenderID(){
		return SENDER_ID;
	}
	
	public String getReceiverID(){
		return receiver_id;
	}
	
	public String getID(){
		return ID;
	}
	
	public String getText(){
		return text;
	}
	
	public boolean isGroup(){
		return isGroup;
	}
	
	public boolean equals(Message m){
		return this.SENDER_ID.equals(m.SENDER_ID) && this.receiver_id.equals(m.receiver_id) 
			&& this.ID.equals(m.ID) && this.text.equals(m.text) && this.isGroup == m.isGroup;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String toString() {
		return text;
	}
	
	
}
