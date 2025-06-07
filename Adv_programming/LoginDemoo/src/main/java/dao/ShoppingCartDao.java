package dao;

import model.CartItems;
import model.ShoppingCart;

import java.sql.SQLException;
import java.util.List;

public interface ShoppingCartDao {
    void createTable() throws SQLException;
    List<CartItems> loadCartItems(String userName) throws SQLException;
    void saveOrUpdateItem(String username, int eventId, int quantity) throws SQLException;
    void removeItem(String username, int eventId) throws SQLException;
    void clearCart(String username) throws SQLException;


}
