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


    public Event(int id, String eventName, String venue, String day, double price, int soldTickets, int totalTickets, int availableTickets) {
        this.id = id;
        this.eventName = eventName;
        this.venue = venue;
        this.day = day;
        this.price = price;
        this.soldTickets = soldTickets;
        this.totalTickets = totalTickets;
        this.availableTickets = availableTickets;
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
}
