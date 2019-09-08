//dialog window to enter new group details

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class CreateGroupDialog extends JDialog {
	private final JLabel jlblGroupname = new JLabel("Groupname");
	private final JLabel jlblMembers = new JLabel("Members");
	private final JLabel jlblChoose = new JLabel("Choose Members");
	
	private JTextField jtfGroupname = new JTextField(40);
	private JLabel jlblChosen = new JLabel("");
	private JComboBox<User> jcbMembers = new JComboBox<>();
	
	private final JButton jbtCreate = new JButton("Create");
	private final JButton jbtCancel = new JButton("Cancel");
	
	private ArrayList<User> members = new ArrayList<>();
	
	public CreateGroupDialog(final Chat parent, boolean modal, User user, MySQLAccess access) {
		super(parent, modal);
		members.add(user);
		for(User u : user.getFriends()) {
			jcbMembers.addItem(u);
		}
		
		JPanel p1= new JPanel(new GridLayout(3,2));
		p1.add(jlblGroupname);
		p1.add(jtfGroupname);

		p1.add(jlblMembers);
		p1.add(jlblChosen);

		p1.add(jlblChoose);
		p1.add(jcbMembers);
		
		JPanel p3 = new JPanel();
		p3.add(jbtCreate);
		p3.add(jbtCancel);
		
		JPanel p2 = new JPanel();
		p2.add(p3, BorderLayout.CENTER);
		
		add(p1, BorderLayout.CENTER);
		add(p2, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		
		jcbMembers.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				User user = (User) jcbMembers.getSelectedItem();
				members.add(user);
				jlblChosen.setText(jlblChosen.getText().concat(user.getName()).concat(", "));		
			}
		});
		
		jbtCreate.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				Group g = new Group(access.generateId(), jtfGroupname.getText(), members);
				access.addGroup(g);
				System.out.println(g);
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
