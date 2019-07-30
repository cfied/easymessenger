import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class Chat extends JFrame {
	private JButton sendButton;
	private JScrollPane chatScrollPane;
	private JScrollPane messScrollPane;
	private JTextField textField;
	private JMenuBar menuBar;
	private JMenu menu;
	
	private final int WIDTH = 1500;
	private final int HEIGHT = 900;
	
	private ArrayList<Message> messages;
	private ArrayList<Group> groups;
	MySQLAccess access = new MySQLAccess();
	
	JList<Message> messageList;
	DefaultListModel<Message> messageModel;
	JList<Group> chatList;
	DefaultListModel<Group> chatModel;
	
	private String username;
	private final String USERID;
	//todo only display messages of selected group
	String selectedGroupId;
	
	public static void main(String[] args) throws SQLException{
		new Chat();
	}
	
	public void initComponents(){
		
		menuBar = new JMenuBar();
		menu = new JMenu();
		menu.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		menu.setText("Create Group");
		menuBar.add(menu);
		setJMenuBar(menuBar);
		
		chatScrollPane.getViewport().getView().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				int i = chatList.getSelectedIndex();
				selectedGroupId = chatModel.get(i).getId();
				updateMessages();
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		sendButton = new JButton();
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//hardcoded user, messageid depends on number of messages in db
				//hence pay attention to deleted messages
				sendMessage();
			}
		});
		
		textField = new JTextField();
		
		textField.setText("Write a message");
		textField.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		textField.addMouseListener(new MouseListener(){
			//@Override
			public void mouseClicked(MouseEvent arg0) {
				if(textField.getText().equals("Write a message")){
					textField.setText("");
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		textField.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
					sendMessage();
				}else if(textField.getText().equals("Write a message")){
					textField.setText("");
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, 3)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING))
					.addGroup(layout.createSequentialGroup()
						.addComponent(chatScrollPane, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, 3)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
									.addComponent(textField, 300, 1000, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
									.addComponent(sendButton, 50, 50, GroupLayout.PREFERRED_SIZE))
								.addGroup(layout.createSequentialGroup()
									.addComponent(messScrollPane))))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, 3)
				
		);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, 3)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
						.addComponent(chatScrollPane))
					.addGroup(layout.createSequentialGroup()
						.addComponent(messScrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, 3)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(sendButton, 50, 50, GroupLayout.PREFERRED_SIZE)
							.addComponent(textField, 50, 50, GroupLayout.PREFERRED_SIZE))))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, 3)
		);
		pack();
	}
	
	
	public Chat() throws SQLException{
		//precreated user
		PassWordDialog passDialog = new PassWordDialog(this, true);
		passDialog.setVisible(true);
		
		USERID = access.getUserId(username);
		this.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		this.setTitle("EasyChat");
		
		//list of groups
		groups = access.getGroups();
		selectedGroupId = groups.get(1).getId();
		chatModel = new DefaultListModel<>();
		chatList = new JList<Group>(chatModel);
		for(Group m : groups){
			chatModel.addElement(m);
		}
		chatList.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		chatScrollPane = new JScrollPane(chatList);
		
		//list of messages
		messages = access.getMessages();
		messageModel = new DefaultListModel<>();
		messageList = new JList<Message>(messageModel);
		for(Message m : messages){
			if(m.getReceiverID().equals(selectedGroupId)) {
				messageModel.addElement(m);
			}
		}
		messageList.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		messScrollPane = new JScrollPane(messageList);

		
		initComponents();
		
		this.setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Update update = new Update(this);
		update.run();
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	private void sendMessage(){
		String messageid, text, receiverid;
		//TO-DO: connect groups and messages
		receiverid = "00000001";		
		messageid = String.format("%08d", access.getMessages().size());
		text = textField.getText();
		Message m = new Message(messageid, USERID, receiverid, text, false);
		access.addMessage(m);
		textField.setText("Write a message");
	}
	
	private void updateMessages(){
		messages = access.getMessages();
		messageList = new JList<Message>(messageModel);
		messageModel.clear();
		for(Message m : messages){
			if(m.getReceiverID().equals(selectedGroupId)) {
				messageModel.addElement(m);
			}
		}

	
	}
}
