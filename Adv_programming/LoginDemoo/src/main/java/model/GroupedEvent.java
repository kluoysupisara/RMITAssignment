package model;

import java.util.List;

public class GroupedEvent {
    private String eventName;
    private List<Event> events;
    private Event selectedEvent;

    public GroupedEvent(String eventName, List<Event> events) {
        this.eventName = eventName;
        this.events = events;
        this.selectedEvent = (events != null && !events.isEmpty()) ? events.get(0) : null;
    }

    public String getEventName() { return eventName; }
    public List<Event> getEvent() { return events; }
    public Event getSelectedEvent() { return selectedEvent; }
    public void setSelectedEvent(Event event) { this.selectedEvent = event; }
}
