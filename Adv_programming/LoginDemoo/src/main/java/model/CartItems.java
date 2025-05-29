package model;

public class CartItems {
    private Event event;
    private int quantity;

    public CartItems(Event event, int quantity) {
        this.event = event;
        this.quantity = quantity;
    }

    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

