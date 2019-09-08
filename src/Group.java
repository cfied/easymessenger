import java.util.ArrayList;

public class Group {
	private final String ID;
	private String name;
	private ArrayList<User> members;

	public Group(String ID, String name, ArrayList<User> members) {
		this.ID = ID;
		this.name = name;
		this.members = members;
	}

	public String getId() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public ArrayList<User> getMembers() {
		return members;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addMember(User member) {
		this.members.add(member);
	}
	
	public String toString() {
		return name;
	}
	
	public boolean equals(Group g) {
		return this.ID.equals(g.ID) && this.name.equals(g.name) && this.members.equals(g.members);
	}
}
