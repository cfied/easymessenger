import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JList;

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
		oldMessages = access.executeQuery("select * from messages");
		
		
	}
	
	//@Override
	public void run() {
		while(true) {
			newMessages = access.executeQuery("select * from messages");
			
			int oldSize = oldMessages.size();
			int newSize = newMessages.size();
			if(newSize > oldSize){
				for(int i = oldSize; i<newSize; i++) {
					chat.model.addElement(newMessages.get(i).getText());
				}
			}
		
			
			oldMessages = newMessages;

		}
	}
	
}
