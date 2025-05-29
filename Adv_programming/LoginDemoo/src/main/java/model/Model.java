package model;

import java.sql.SQLException;

import dao.EventDao;
import dao.UserDao;
import dao.UserDaoImpl;

public class Model {
	private UserDao userDao;
	private User currentUser;
	private EventDao eventDao;
	private Cart cart;
	
	public Model() {
		userDao = new UserDaoImpl();
		eventDao = new EventDao();
		cart = new Cart();
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
	public Cart getCart() {
		return cart;
	}
}
