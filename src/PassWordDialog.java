import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

public class PassWordDialog extends JDialog {
	private final JLabel jlblUsername = new JLabel("Username");
	private final JLabel jlblPassword = new JLabel("Password");
	
	private final JTextField jtfUsername = new JTextField(20);
	private final JPasswordField jpfPassword = new JPasswordField();
	
	private final JButton jbtSignIn = new JButton("Sign in");
	private final JButton jbtSignUp = new JButton("Sign up");
	
	private final JLabel jlblStatus = new JLabel(" ");
	
	public PassWordDialog(final Chat parent, boolean modal) {
		super(parent, modal);
		
		JPanel p3 = new JPanel(new GridLayout(2,1));
		p3.add(jlblUsername);
		p3.add(jlblPassword);
		
		JPanel p4 = new JPanel(new GridLayout(2,1));
		p4.add(jtfUsername);
		p4.add(jpfPassword);
		
		JPanel p1 = new JPanel();
		p1.add(p3);
		p1.add(p4);
		
		JPanel p2 = new JPanel();
		p2.add(jbtSignIn);
		p2.add(jbtSignUp);
		
		JPanel p5 = new JPanel(new BorderLayout());
		p5.add(p2, BorderLayout.CENTER);
		p5.add(jlblStatus, BorderLayout.NORTH);
		jlblStatus.setForeground(Color.RED);
		jlblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		setLayout(new BorderLayout());
		add(p1, BorderLayout.CENTER);
		add(p5, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		jbtSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//check password
				try{
					MySQLAccess access = new MySQLAccess();
					String username = jtfUsername.getText();
					String password = jpfPassword.getText();
					access.connectToMysql("127.0.0.1:3306", "easymessenger", "testuser", "testpassword");
					//TO-DO Hashing and Salting, use char[]
					if(access.checkPassword(username, password)){
						parent.setUser(access.getUser(username, Identifier.NAME));
						parent.access = access;
						setVisible(false);
					}else{
						jlblStatus.setText("Invalid username or password");
						jtfUsername.setText("");
						jpfPassword.setText("");
					}
					
					
					//clear password
					/*for(int i = 0; i < password.length; i++){
						password[i] = '0';
					}*/

				} catch(SQLException ex) {
					System.out.println(ex);
				}
			}
		});
		
		jbtSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					MySQLAccess access = new MySQLAccess();
					String username = jtfUsername.getText();
					String password = jpfPassword.getText();
					access.connectToMysql("127.0.0.1:3306", "easymessenger", "testuser", "testpassword");
					if(access.addUser(username, password)){
						parent.setUser(access.getUser(username, Identifier.NAME));
						parent.access = access;
						setVisible(false);
					}else{
						jlblStatus.setText("Username is already in use");
						jtfUsername.setText("");
						jpfPassword.setText("");
					}
				} catch(SQLException ex) {
					System.out.println(ex);
				}
			}
		});
		
		
		
	}
}
