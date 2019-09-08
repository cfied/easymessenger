import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AddFriendDialog extends JDialog {
	private final JLabel jlblId = new JLabel("Enter Id");
	private final JLabel jlblName = new JLabel("Or enter Name");
	
	private JTextField jtfId = new JTextField(8);
	private JTextField jtfName = new JTextField(20);
	
	private final JButton jbtAdd = new JButton("Add");
	private final JButton jbtCancel = new JButton("Cancel");
	
	private User newFriend;
	
	public AddFriendDialog(final Chat parent, boolean modal, User user, MySQLAccess access){
		super(parent, modal);
		
		JPanel p1 = new JPanel(new GridLayout(2,2));
		p1.add(jlblId);
		p1.add(jtfId);
		
		p1.add(jlblName);
		p1.add(jtfName);
		
		JPanel p3 = new JPanel();
		p3.add(jbtAdd);
		p3.add(jbtCancel);
		
		JPanel p2 = new JPanel();
		p2.add(p3, BorderLayout.CENTER);
		
		add(p1, BorderLayout.CENTER);
		add(p2, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		
		jtfId.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				update();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				update();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				update();
			}
			
			public void update(){
				String id = jtfId.getText();
				newFriend = access.getUser(id, Identifier.ID);
				if(newFriend != null){
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							jtfName.setText(newFriend.getName());
						}
					});
				}else if(id.length() == 8){
					System.out.println("No user with this id");
				}
			}
		});
		
		
		jtfName.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				update();
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				update();				
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				update();
			}
			
			public void update(){
				String name = jtfName.getText();
				newFriend = access.getUser(name, Identifier.NAME);
				if(newFriend != null){
					SwingUtilities.invokeLater(new Runnable(){
						public void run(){
							jtfId.setText(newFriend.getId());
						}
					});
				}
		}
	});
		
		jbtAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				user.addFriend(newFriend);
				dispose();
			}
		});
		
		
		jbtCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		setVisible(true);
		
	}
	
	
	
	
	
}
