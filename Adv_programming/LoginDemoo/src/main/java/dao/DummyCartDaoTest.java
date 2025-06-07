package model;

import dao.ShoppingCartDao;
import model.CartItems;

import java.util.Collections;
import java.util.List;

public class DummyCartDaoTest implements ShoppingCartDao {

    @Override
    public void createTable() {
        // No operation needed for test
    }

    @Override
    public void saveOrUpdateItem(String username, int eventId, int quantity) {
        System.out.printf("Simulated saveOrUpdateItem: %s, eventId=%d, qty=%d%n", username, eventId, quantity);
    }

    @Override
    public void removeItem(String username, int eventId) {
        System.out.printf("Simulated removeItem: %s, eventId=%d%n", username, eventId);
    }

    @Override
    public void clearCart(String username) {
        System.out.println("Simulated clearCart for: " + username);
    }

    @Override
    public List<CartItems> loadCartItems(String username) {
        System.out.println("Simulated loadCartItems for: " + username);
        return Collections.emptyList(); // return an empty cart for test
    }
}