package model;

public class User {
	private String username;
	private String password;
	private String preferred_name;

	public User() {
	}
	
	public User(String username, String password, String preferred_name) {
		this.username = username;
		this.password = password;
		this.preferred_name = preferred_name;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPreferred_name(String preferred_name) {  this.preferred_name = preferred_name; }

	public String getPreferred_name() { return preferred_name; }
}
