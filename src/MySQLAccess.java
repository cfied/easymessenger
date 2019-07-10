import java.sql.*;
import java.util.ArrayList;

public class MySQLAccess {
	Connection con;
	public static void main(String[] args) throws SQLException{
		MySQLAccess access = new MySQLAccess();
		
		access.connectToMysql("127.0.0.1:3306", "easymessenger", "testuser", "testpassword");
		access.executeQuery("select * from messages");
		//access.addMessage(new Message("00000005","00000004","Hey, nice to meet you!"));
		access.deleteMessage("00000004");
		access.executeQuery("select * from messages");
	}
		
		
	public boolean connectToMysql(String host, String database, String user, String passwd){
		try{	
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"+host+"/"+database+"?autoReconnect=true&verifyServerCertificate=false&useSSL=true", user, passwd);
			System.out.println("Connected to database " + database + ":");
			return true;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	//only valid for result with three columns
	public ArrayList<Message> executeQuery(String query){
		ArrayList<Message> results = new ArrayList<>();
		String r1, r2, r3;
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				r1 = rs.getString(1);
				r2 = rs.getString(2);
				r3 = rs.getString(3);
				
				System.out.println(r1 + " " + r2 + " " + r3);
				results.add(new Message(r1, r2, r3));
			}
			return results;
		}catch(Exception e){ System.out.println(e);
			return null;
		}
	}
	
	public void addMessage(Message m) {
		try{
			PreparedStatement stmt = con.prepareStatement("insert into messages(userid, messageid, text) values (?, ?, ?)");
			stmt.setString(1, m.getownerID());
			stmt.setString(2, m.getID());
			stmt.setString(3, m.getText());
			
			stmt.executeUpdate();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void deleteMessage(String messageID) {
		try{
			PreparedStatement stmt = con.prepareStatement("delete from messages where messageid = ?");
			stmt.setString(1, messageID);
			stmt.executeUpdate();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
