import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MySQLAccess {
	Connection con;
	
	public void connectToMysql(String host, String database, String user, String passwd) throws SQLException{
		try {	
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+database+"?autoReconnect=true&verifyServerCertificate=false&useSSL=true", user, passwd);
			System.out.println("Connected to database " + database + ":");
		}catch(ClassNotFoundException e){
			System.out.println(e);
		}
	}
	
	public boolean checkPassword(String username, String password) {
		try {	
			PreparedStatement stmt = con.prepareStatement("select password from users where user_name = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1).equals(password);
			
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public User getUser(String username) {
		try {	
			PreparedStatement stmt = con.prepareStatement("select user_id from users where user_name = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			//hardcoded friends
			ArrayList<User> friends = new ArrayList<>();
			friends.add(new User("00000005","Paul", null, null));
			friends.add(new User("00000006","Johann", null, null));
			return new User(rs.getString(1), username, friends, null);
			
		}catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public boolean addUser(String username, String password){
		try {
			PreparedStatement stmt = con.prepareStatement("select user_name from users where user_name = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				return false;
			}else{
				stmt = con.prepareStatement("insert into users values (?, ?, ?)");
				stmt.setString(1, String.format("%08d", getMessages().size()));
				stmt.setString(2, username);
				stmt.setString(3, password);
				stmt.executeUpdate();
				return true;
			}
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
		
	}
	
	public ArrayList<Message> getMessages(){
		ArrayList<Message> results = new ArrayList<>();
		String r1, r2, r3, r4;
		boolean r5;
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from messages");
			while(rs.next()){
				r1 = rs.getString(1);
				r2 = rs.getString(2);
				r3 = rs.getString(3);
				r4 = rs.getString(4);
				r5 = rs.getBoolean(5);

				results.add(new Message(r1, r2, r3, r4, r5));
			}
			return results;
		}catch(Exception e){ System.out.println(e);
			return null;
		}
	}
	
	public HashMap<String,String> getUsers(){
		HashMap<String, String> results = new HashMap<>();
		String r1, r2;
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users");
			while(rs.next()){
				r1 = rs.getString(1);
				r2 = rs.getString(2);
				results.put(r1, r2);
			}
			return results;
		}catch(Exception e){ 
			System.out.println(e);
			return null;
		}
	}
	
	public ArrayList<Group> getGroups(String userId){
		ArrayList<Group> results = new ArrayList<>();
		ArrayList<String> ids = new ArrayList<>();
		//get all group ids
		try{
			PreparedStatement stmt = con.prepareStatement("select distinct group_id from user_groups where user_id = ?");
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ids.add(rs.getString(1));				
			}
			//for every group id query all members
			ArrayList<String> members;
			String name;
			PreparedStatement prepStmt = con.prepareStatement("select user_id from user_groups where group_id = ?");
			PreparedStatement prepStmt2 = con.prepareStatement("select distinct group_name from user_groups where group_id = ?");
			for(String id : ids) {
				members = new ArrayList<>();
				prepStmt.setString(1, id);
				prepStmt2.setString(1, id);
				rs = prepStmt2.executeQuery();
				rs.next();
				name = rs.getString(1);
				rs = prepStmt.executeQuery();
				while(rs.next()){
					members.add(rs.getString(1));
				}
				results.add(new Group(id,name,members));		
			}
			return results;
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}
	
	//TO-DO: check if user exists when creating group
	public void addGroup(Group g) {
		try{
			PreparedStatement stmt = con.prepareStatement("insert into user_groups(group_name, group_id, user_id) values (?, ?, ?)");
			stmt.setString(1, g.getName());
			stmt.setString(2, g.getId());
			for(String member : g.getMembers()) {
				stmt.setString(3, member);
				stmt.executeUpdate();
			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	
	public void addMessage(Message m) {
		try{
			PreparedStatement stmt = con.prepareStatement("insert into messages(id, sender, receiver, text, is_group) values (?, ?, ?, ?, ?)");
			stmt.setString(1, m.getID());
			stmt.setString(2, m.getSenderID());
			stmt.setString(3, m.getReceiverID());
			stmt.setString(4, m.getText());
			stmt.setBoolean(5, m.isGroup());
			
			stmt.executeUpdate();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void deleteMessage(String messageID) {
		try{
			PreparedStatement stmt = con.prepareStatement("delete from messages where id = ?");
			stmt.setString(1, messageID);
			stmt.executeUpdate();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
