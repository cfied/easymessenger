import java.util.ArrayList;

public class User {
	private final String id;
	private String name;
	private ArrayList<User> friends;
	private ArrayList<Group> groups;
	
	public User(String id, String name, ArrayList<User> friends, ArrayList<Group> groups) {
		this.id = id;
		this.name = name;
		this.friends = friends;
		this.groups = groups;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Group> getGroups() {
		return groups;
	}
	
	public void addGroup(Group group) {
		groups.add(group);
	}
	
	public void addFriend(User friend){
		friends.add(friend);
	}
	
	public ArrayList<User> getFriends() {
		return friends;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
