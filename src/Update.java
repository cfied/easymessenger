import java.util.ArrayList;

import javax.swing.SwingUtilities;

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
		oldGroups = access.getGroups(chat.user.getId());
	}
	
	//@Override
	public void run() {
		while(true) {
			try {
				sleep(1);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			newMessages = access.getMessages();
			newGroups = access.getGroups(chat.user.getId());
			
			int oldSizeM = oldMessages.size();
			int newSizeM = newMessages.size();
			int oldSizeG = oldGroups.size();
			int newSizeG = newGroups.size();
			
			//add new Messages
			if(newSizeM > oldSizeM){
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						Message m;
						for(int i = oldSizeM; i<newSizeM; i++) {
							m = newMessages.get(i);
							if(chat.selectedGroupId.equals(m.getReceiverID()))
							chat.messageModel.addElement(m);
						}
					}
				});	
			}else if(newSizeM < oldSizeM){
				
				//should messages be renumbered?
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						for(int i = 0; i < oldMessages.size(); i++){
							if(!newMessages.contains(oldMessages.get(i))){
								chat.messageModel.get(Integer.parseInt(oldMessages.get(i).getID())).setText("This message has been removed");
							}
						}
					}
				});
			}
			
			//remove deleted elements
			if(newSizeG > oldSizeG){
				System.out.println("New groups");
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						for(int i = oldSizeG; i<newSizeG; i++) {
							chat.chatModel.addElement(newGroups.get(i));
						}
					}
				});
			}else if(newSizeG < oldSizeG){
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						for(int i = 0; i < oldGroups.size(); i++){
							if(!newGroups.contains(oldGroups.get(i))){
								chat.chatModel.removeElement((oldGroups).get(i));
							}
						}
					}
				});
			}
			oldMessages = newMessages;
			oldGroups = newGroups;
		}
	}
}
