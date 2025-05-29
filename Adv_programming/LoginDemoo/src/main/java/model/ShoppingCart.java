package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShoppingCart {
    private final ObservableList<CartItems> items = FXCollections.observableArrayList();

    public ObservableList<CartItems> getItems() {
        return items;
    }

    public void addItem(Event event, int quantityToAdd) {
        for (CartItems item : items) {
            if (item.getEvent().getId() == event.getId()) {
                item.setQuantity(item.getQuantity() + quantityToAdd);
                return;
            }
        }
        items.add(new CartItems(event, quantityToAdd));
    }


    public void removeItem(Event event) {
        items.removeIf(item -> item.getEvent().getId() == event.getId());
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
