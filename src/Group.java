import java.util.ArrayList;

public class Group {
	private final String ID;
	private String name;
	private ArrayList<String> members;

	public Group(String ID, String name, ArrayList<String> members) {
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

	public ArrayList<String> getMembers() {
		return members;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addMember(String member) {
		this.members.add(member);
	}
	
	

}
