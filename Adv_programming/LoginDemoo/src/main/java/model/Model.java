package model;

import java.sql.SQLException;
import java.util.List;

import dao.ShoppingCartDao;
import dao.EventDao;
import dao.UserDao;
import dao.UserDaoImpl;
import dao.OrderDao;

public class Model {
	private UserDao userDao;
	private User currentUser;
	private EventDao eventDao;
	private ShoppingCartDao shoppingCartDao;
	private ShoppingCart shoppingCart;
	private OrderDao orderDao;
	private boolean isAdmin = false;
	
	public Model() {
		userDao = new UserDaoImpl();
		eventDao = new EventDao();
		shoppingCartDao = new ShoppingCartDao();
		orderDao = new OrderDao();
	}
	
	public void setup() throws SQLException {
		userDao.setup();
		eventDao.setup();
		shoppingCartDao.createTable();
		orderDao.createTables();
	}
	public UserDao getUserDao() {
		return userDao;
	}

	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void setCurrentUser(User user) {
		currentUser = user;
		// ------------------ [backup]-----------------------
//		shoppingCartDao = new ShoppingCartDao();
//
//		// Create new cart instance for the user
//		this.shoppingCart = new ShoppingCart(user.getUsername(), shoppingCartDao);
//
//		//load shopping_cart from database
//		List<CartItems> items = shoppingCartDao.loadCartItems(user.getUsername());
//		//add all loaded items to shopping Cart's ObservableList
//		shoppingCart.getItems().addAll(items);
//
//		// ✅ Optional: print loaded cart items
//		// Print loaded items (or say it's empty)
//		System.out.println("Loaded cart items for user: " + user.getUsername());
//		if (items.isEmpty()) {
//			System.out.println("- Cart is currently empty.");
//		} else {
//			for (CartItems item : items) {
//				System.out.printf("- %s x%d%n", item.getEvent().getEventName(), item.getQuantity());
//			}
//		}
		if (!isAdmin) {
			shoppingCartDao = new ShoppingCartDao();
			this.shoppingCart = new ShoppingCart(user.getUsername(), shoppingCartDao);
			List<CartItems> items = shoppingCartDao.loadCartItems(user.getUsername());
			shoppingCart.getItems().addAll(items);
			// Log
			System.out.println("Loaded cart items for user: " + user.getUsername());
			items.forEach(item -> System.out.printf("- %s x%d%n", item.getEvent().getEventName(), item.getQuantity()));
		} else {
			this.shoppingCart = null;
			System.out.println("Admin mode enabled.");
		}
	}
	public ShoppingCartDao getshoppingCartDao() {
		return shoppingCartDao;
	}
	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}
	public void logout() {
		currentUser = null;
		shoppingCart = null;
	}
	public EventDao getEventDao() {
		return eventDao;
	}
	public OrderDao getOrderDao() {
		return orderDao;
	}
}
