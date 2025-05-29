package model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Event, Integer> items = new HashMap<>();

    public void add(Event event, int quantity) {
        items.put(event, items.getOrDefault(event, 0) + quantity);
    }

    public Map<Event, Integer> getItems() {
        return items;
    }
}

