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
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Chat extends JFrame {
	private JButton sendButton;
	private JScrollPane chatScrollPane;
	private JScrollPane messScrollPane;
	private JTextField textField;
	private JMenuBar menuBar;
	private JMenu createGroupMenu;
	private JMenu addFriendMenu;
	
	private final int WIDTH = 1500;
	private final int HEIGHT = 900;
	
	private ArrayList<Message> messages;
	private ArrayList<Group> groups;
	MySQLAccess access = new MySQLAccess();
	
	JList<Message> messageList;
	DefaultListModel<Message> messageModel;
	JList<Group> chatList;
	DefaultListModel<Group> chatModel;
	
	//change to user object
	User user;
	String selectedGroupId;
	
	
	/*TO DO
	 * 
	 * deal with no selectedGroupId
	 * add foreign keys and not null constraints in database
	 * add create group option
	 * add password checking, hashing
	 * selecting messages
	 * 
	 * */
	
	public static void main(String[] args) throws SQLException{
		new Chat();
	}
	
	public void initComponents(){
		
		menuBar = new JMenuBar();
		createGroupMenu = new JMenu();
		createGroupMenu.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		createGroupMenu.setText("Create Group");
		createGroupMenu.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				new CreateGroupDialog(Chat.this, true, user, access);
				
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
		menuBar.add(createGroupMenu);
		addFriendMenu = new JMenu();
		addFriendMenu.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		addFriendMenu.setText("Add friend");
		//TO-DO: add addFriend option
		addFriendMenu.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				new AddFriendDialog(Chat.this, true, user, access);
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		menuBar.add(addFriendMenu);
		
		setJMenuBar(menuBar);
		
		chatScrollPane.getViewport().getView().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				try{
					int i = chatList.getSelectedIndex();
					selectedGroupId = chatModel.get(i).getId();
					updateMessages();
				}catch(java.lang.ArrayIndexOutOfBoundsException e){
					
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
		
		sendButton = new JButton();
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//messageid depends on number of messages in db
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
		PassWordDialog passDialog = new PassWordDialog(this, true);
		passDialog.setVisible(true);

		this.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		this.setTitle("EasyChat");
		
		//list of groups
		groups = access.getGroups(user.getId());
		if(groups.isEmpty()){
			selectedGroupId = null;
		}else{
			selectedGroupId = groups.get(0).getId();
		}
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
			try{
				if(m.getReceiverID().equals(selectedGroupId)) {
					messageModel.addElement(m);
				}
			}catch(NullPointerException ex){
				System.out.println("No groups");
			}
		}
		messageList.setFont(new Font("Comic Sans Ms", Font.PLAIN, 20));
		messScrollPane = new JScrollPane(messageList);
		messageList.setCellRenderer(new MyCellRenderer(user.getId(), messageModel));
		
		initComponents();
		
		this.setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Update update = new Update(this);
		update.run();
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	private void sendMessage(){
		if(selectedGroupId == null){ return;}
		String messageid, text;		
		messageid = String.format("%08d", access.getMessages().size());
		text = textField.getText();
		Message m = new Message(messageid, user.getId(), selectedGroupId, text, false);
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
