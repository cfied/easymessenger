//dialog window to enter new group details
/*
 * TO-DO: display friends in combobox, think about identifier, maybe add user class
 * call the add-group method
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
	
	public CreateGroupDialog(User user) {
		this(null, true, user);
	}
	
	public CreateGroupDialog(final Chat parent, boolean modal, User user) {
		super(parent, modal);
		for(User u : user.getFriends()) {
			jcbMembers.addItem(u);
		}
		
		JPanel p2 = new JPanel(new GridLayout(3,2));
		p2.add(jlblGroupname);
		p2.add(jtfGroupname);

		p2.add(jlblMembers);
		p2.add(jlblChosen);

		p2.add(jlblChoose);
		p2.add(jcbMembers);
		
		add(p2, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		
		jcbMembers.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				User user = (User) jcbMembers.getSelectedItem();
				members.add(user);
				jlblChosen.setText(jlblChosen.getText().concat(user.getName()).concat(", "));
				System.out.println("Chosen");
				
			}
			
		});
		
		setVisible(true);
	}
}
