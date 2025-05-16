package model;

import java.sql.SQLException;

import dao.EventDao;
import dao.UserDao;
import dao.UserDaoImpl;

public class Model {
	private UserDao userDao;
	private User currentUser;
	private EventDao eventDao;
	
	public Model() {
		userDao = new UserDaoImpl();
		eventDao = new EventDao();
	}
	
	public void setup() throws SQLException {
		userDao.setup();
		//eventDao.setup();
	}
	public UserDao getUserDao() {
		return userDao;
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
	}
}
