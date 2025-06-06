package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;
import util.PasswordUtils;

public class UserDaoImpl implements UserDao {
	private final String TABLE_NAME = "users";

	public UserDaoImpl() {
	}

	@Override
	public void setup() throws SQLException {
		try (Connection connection = Database.getConnection();
				Statement stmt = connection.createStatement();) {
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (username VARCHAR(10) NOT NULL,"
					+ "password VARCHAR(8) NOT NULL," + "preferred_name VARCHAR(10) NOT NULL," + "PRIMARY KEY (username))";
			stmt.executeUpdate(sql);
		} 
	}

	@Override
	public User getUser(String username, String plainPassword) throws SQLException {
		String password =  PasswordUtils.encrypt(plainPassword);
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE username = ? AND password = ?";
		try (Connection connection = Database.getConnection(); 
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					User user = new User();
					user.setUsername(rs.getString("username"));
					user.setPassword(rs.getString("password"));
					user.setPreferred_name(rs.getString("preferred_name"));
					return user;
				}
				return null;
			} 
		}
	}

	@Override
	public User createUser(String username, String password, String preferred_name) throws SQLException {
		String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)";
		try (Connection connection = Database.getConnection();
				PreparedStatement stmt = connection.prepareStatement(sql);) {
			stmt.setString(1, username);
			stmt.setString(2, PasswordUtils.encrypt(password)); //  Encrypt before saving
			stmt.setString(3, preferred_name);


			stmt.executeUpdate();
			return new User(username, password, preferred_name);
		} 
	}
	@Override
	public void updatePassword(String username, String encryptedPassword) throws SQLException {
		String sql = "UPDATE users SET password = ? WHERE username = ?";
		try (Connection con = Database.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, encryptedPassword);
			stmt.setString(2, username);
			stmt.executeUpdate();
			System.out.println(stmt.toString());
		}
	}
}
