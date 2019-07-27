import java.util.ArrayList;

public class Update extends Thread{
	private MySQLAccess access = new MySQLAccess();
	private ArrayList<Message> oldMessages;
	private ArrayList<Message> newMessages;
	private Chat chat;
	
	//@Override
	public Update(Chat chat) {
		super();
		this.chat = chat;
		access.connectToMysql("127.0.0.1:3306", "easymessenger", "testuser", "testpassword");
		oldMessages = access.getMessages();
	}
	
	//@Override
	public void run() {
		while(true) {
			newMessages = access.getMessages();
			
			int oldSize = oldMessages.size();
			int newSize = newMessages.size();
			if(newSize > oldSize){
				for(int i = oldSize; i<newSize; i++) {
					chat.messageModel.addElement(newMessages.get(i).getText());
				}
			}else if(newSize < oldSize){
				for(int i = 0; i < oldMessages.size(); i++){
					if(!newMessages.contains(oldMessages.get(i))){
						//should messages be renumbered?
						chat.messageModel.set(Integer.parseInt(oldMessages.get(i).getID()), "This message has been removed");
					}
				}
			}
			oldMessages = newMessages;
		}
	}
}
