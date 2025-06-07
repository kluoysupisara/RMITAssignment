package model;

import dao.ShoppingCartDao;
import dao.ShoppingCartDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ShoppingCart {
    private final ObservableList<CartItems> items = FXCollections.observableArrayList();
    private final ShoppingCartDao dao;
    private final String userName;

    public ShoppingCart(String userName, ShoppingCartDao dao) {
        this.userName = userName;
        this.dao = dao;
    }

    public ObservableList<CartItems> getItems() {
        return items;
    }

    public void addItem(Event event, int quantityToAdd) throws SQLException {
        //ShoppingCartDao dao = model.getshoppingCartDao();
        for (CartItems item : items) {
            if (item.getEvent().getId() == event.getId()) {
                int newQuantity = item.getQuantity() + quantityToAdd;
                item.setQuantity(newQuantity); // update in memory
                dao.saveOrUpdateItem(userName, event.getId(), newQuantity ); // auto-save update
                return;
            }
        }
        items.add(new CartItems(event, quantityToAdd)); // add to memory
        dao.saveOrUpdateItem(userName, event.getId(), quantityToAdd); // add to DB
    }


    public void removeItem(Event event) throws SQLException {
        items.removeIf(item -> item.getEvent().getId() == event.getId());
        dao.removeItem(userName, event.getId()); // auto-remove from DB

    }
    // clear all cart and DB
    public void clear() throws SQLException {
        items.clear();
        dao.clearCart(userName); // auto-clear from DB
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
