import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

public class Chat extends JFrame {
	private JButton sendButton;
	private JScrollPane chatScrollPane;
	JScrollPane messScrollPane;
	private JTextField textField;
	private final int WIDTH = 1500;
	private final int HEIGHT = 900;
	private ArrayList<Message> messages;
	MySQLAccess access = new MySQLAccess();
	
	JList<String> list;
	DefaultListModel<String> model;
	
	public static void main(String[] args) throws SQLException{
		new Chat();
	}
	
	public void initComponents(){
		sendButton = new JButton();
		chatScrollPane = new JScrollPane();
		textField = new JTextField();
		
		textField.setText("Write a message");
		textField.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		textField.addMouseListener(new MouseListener(textField));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		.addGroup(layout.createSequentialGroup()
				.addComponent(chatScrollPane, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 1000, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(sendButton,GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup()
								.addComponent(messScrollPane)
								.addContainerGap())))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(chatScrollPane)
				.addGroup(layout.createSequentialGroup()
					.addComponent(messScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
		);
		pack();
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
		model = new DefaultListModel<>();
		list = new JList<String>(model);
		for(String m : messageList){
			model.addElement(m);
		}
		list.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		messScrollPane = new JScrollPane(list);
		
		initComponents();
		
		this.setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Update update = new Update(this);
		update.run();
	}
	
	public void addMessage(){
		
	}
}
