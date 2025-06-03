package model;

public class Event {
    private int id;
    private String eventName;
    private String venue;
    private String day;
    private double price;
    private int soldTickets;
    private int totalTickets;
    private int availableTickets;
    private boolean enabled;


    public Event(int id, String eventName, String venue, String day, double price, int soldTickets, int totalTickets, int availableTickets, boolean enabled) {
        this.id = id;
        this.eventName = eventName;
        this.venue = venue;
        this.day = day;
        this.price = price;
        this.soldTickets = soldTickets;
        this.totalTickets = totalTickets;
        this.availableTickets = availableTickets;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }
    public String getEventName() {
        return eventName;
    }
    public String getVenue() {
        return venue;
    }
    public String getDay() {
        return day;
    }
    public double getPrice() {
        return price;
    }
    public int getSoldTickets() {
        return soldTickets;
    }
    public int getTotalTickets() {
        return totalTickets;
    }
    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
    public boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEventName(String text) {
        this.eventName = text;
    }

    public void setVenue(String text) {
        this.venue = text;
    }

    public void setDay(String value) {
        this.day = value;
    }

    public void setPrice(double v) {
        this.price = v;
    }
}
