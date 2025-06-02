package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private int orderId;
    private String orderNumber;
    private String user_name;
    private LocalDateTime orderDate;
    private List<CartItems> items;
    private double orderPrice;

    public Order(int orderId, String orderNumber, String user_name, LocalDateTime orderDate, List<CartItems> items, double orderPrice) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.user_name = user_name;
        this.orderDate = orderDate;
        this.items = items;
        this.orderPrice = orderPrice;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    public List<CartItems> getItems() {
        return items;
    }
    public void setItems(List<CartItems> items) {
        this.items = items;
    }
    public double getOrderPrice() {
        return orderPrice;
    }
    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }
    // Helper method to format all events in the order as a summary string
    public String getEventSummary() {
        return items.stream()
                .map(item -> "- " + item.getEvent().getEventName() + " x" + item.getQuantity())
                .collect(Collectors.joining("\n"));
    }
}
