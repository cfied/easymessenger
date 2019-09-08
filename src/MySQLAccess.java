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
	
	public User getUser(String useridentifier, Identifier identifier) {
		try {	
			PreparedStatement stmt;
			if(identifier == Identifier.NAME) {
				stmt = con.prepareStatement("select user_id from users where user_name = ?");
			}else if(identifier == Identifier.ID) {
				stmt = con.prepareStatement("select user_name from users where user_id = ?");
			}else{
				return null;
			}

			stmt.setString(1, useridentifier);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()){
				return null;
			}
			
			//no groups
			ArrayList<User> friends;
			if(identifier == Identifier.NAME){
				friends = getFriends(useridentifier);
				return new User(rs.getString(1), useridentifier, friends, null);
			}else if(identifier == Identifier.ID) {
				String username = rs.getString(1);
				friends = getFriends(username);
				return new User(useridentifier, username, friends, null);
			}else{
				return null;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
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
				stmt.setString(1, String.format("%08d", getUsers().size() + 1));
				stmt.setString(2, username);
				stmt.setString(3, password);
				stmt.executeUpdate();
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
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
			e.printStackTrace();
			return null;
		}
	}
	
	public void addFriend(String userId, String friendId) {
		try {
			PreparedStatement stmt = con.prepareStatement("insert into friends(id1,id2) values(?, ?)");
			stmt.setString(1, userId);
			stmt.setString(2, friendId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<User> getFriends(String UserId) {
		ArrayList<User> friends = new ArrayList<>();
		try {
			PreparedStatement stmt = con.prepareStatement("select id2 from friends where id1 = ? union select id1 from friends where id2 = ?");
			stmt.setString(1,  UserId);
			stmt.setString(2, UserId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				User user = getUser(rs.getString(1), Identifier.ID);
				friends.add(user);
				System.out.println(user);
			}
			return friends;
		} catch (SQLException e) {
			e.printStackTrace();
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
			ResultSet rsUsername;
			while(rs.next()){
				ids.add(rs.getString(1));				
			}
			//for every group id query all members
			ArrayList<User> members;
			String name;
			PreparedStatement prepStmtUserId = con.prepareStatement("select user_id from user_groups where group_id = ?");
			PreparedStatement prepStmtGroupName = con.prepareStatement("select distinct group_name from user_groups where group_id = ?");
			PreparedStatement prepStmtUserName = con.prepareStatement("select user_name from users where user_id = ?");
			for(String id : ids) {
				members = new ArrayList<>();
				prepStmtUserId.setString(1, id);
				prepStmtGroupName.setString(1, id);
				rs = prepStmtGroupName.executeQuery();
				rs.next();
				name = rs.getString(1);
				rs = prepStmtUserId.executeQuery();
				while(rs.next()){
					String userid = rs.getString(1);
					prepStmtUserName.setString(1, userid);
					rsUsername = prepStmtUserName.executeQuery();
					rsUsername.next();
					members.add(new User(userid, rsUsername.getString(1), null, null));
				}
				results.add(new Group(id,name,members));		
			}
			return results;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	//TO-DO: check if user exists when creating group
	public void addGroup(Group g) {
		try{
			PreparedStatement stmt = con.prepareStatement("insert into user_groups(group_name, group_id, user_id) values (?, ?, ?)");
			stmt.setString(1, g.getName());
			stmt.setString(2, g.getId());
			for(User member : g.getMembers()) {
				stmt.setString(3, member.getId());
				stmt.executeUpdate();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String generateId(){
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select max(group_id) from user_groups");
			rs.next();
			String str = rs.getString(1);
			if(str != null){
				System.out.println(str);
				int result = Integer.parseInt(str);
				result++;
				String resultString = String.format("%08d", result);
				System.out.println(resultString);
				return resultString;
			}else{
				return "00000000";
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
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
		}catch(Exception e){ e.printStackTrace();
			return null;
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
			e.printStackTrace();
		}
	}
	
	public void deleteMessage(String messageID) {
		try{
			PreparedStatement stmt = con.prepareStatement("delete from messages where id = ?");
			stmt.setString(1, messageID);
			stmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
