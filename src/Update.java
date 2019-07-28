import java.util.ArrayList;

public class Update extends Thread{
	private MySQLAccess access;
	private ArrayList<Message> oldMessages;
	private ArrayList<Message> newMessages;
	private ArrayList<Group> oldGroups;
	private ArrayList<Group> newGroups;
	private Chat chat;
	
	//@Override
	public Update(Chat chat) {
		super();
		this.chat = chat;
		access = chat.access;
		oldMessages = access.getMessages();
		oldGroups = access.getGroups();
	}
	
	//@Override
	public void run() {
		while(true) {
			newMessages = access.getMessages();
			newGroups = access.getGroups();
			
			int oldSizeM = oldMessages.size();
			int newSizeM = newMessages.size();
			int oldSizeG = oldGroups.size();
			int newSizeG = newGroups.size();
			
			if(newSizeM > oldSizeM){
				for(int i = oldSizeM; i<newSizeM; i++) {
					chat.messageModel.addElement(newMessages.get(i).getText());
				}
			}else if(newSizeM < oldSizeM){
				for(int i = 0; i < oldMessages.size(); i++){
					if(!newMessages.contains(oldMessages.get(i))){
						//should messages be renumbered?
						chat.messageModel.set(Integer.parseInt(oldMessages.get(i).getID()), "This message has been removed");
					}
				}
			}
			
			if(newSizeG > oldSizeG){
				for(int i = oldSizeG; i<newSizeG; i++) {
					chat.chatModel.addElement(newGroups.get(i).getName());
				}
			}else if(newSizeG < oldSizeG){
				for(int i = 0; i < oldGroups.size(); i++){
					if(!newGroups.contains(oldGroups.get(i))){
						chat.chatModel.removeElement((oldGroups).get(i));
					}
				}
			}
			oldMessages = newMessages;
			oldGroups = newGroups;
		}
	}
}
