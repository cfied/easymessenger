import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

public class Chat extends JFrame {
	private final int WIDTH = 1500;
	private final int HEIGHT = 900;
	private ArrayList<Message> messages;
	MySQLAccess access = new MySQLAccess();
	
	public static void main(String[] args) throws SQLException{
		new Chat();
	}
	
	public Chat() throws SQLException{
		//precreated user
		access.connectToMysql("127.0.0.1:3306", "easymessenger", "testuser", "testpassword");
		messages = access.executeQuery("select * from messages");
		this.setTitle("EasyChat");
		String[] messageList = new String[messages.size()];
		for(int i = 0; i < messages.size(); i++) {
			messageList[i] = messages.get(i).getText();
		}
		JList<String> list = new JList<>(messageList);
		list.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		JScrollPane scrollList = new JScrollPane(list);

		
		this.getContentPane().add(scrollList, BorderLayout.CENTER);
		
		this.setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addMessage(){
		
	}
}
