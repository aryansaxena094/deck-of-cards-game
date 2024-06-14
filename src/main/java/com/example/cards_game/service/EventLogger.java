package com.example.cards_game.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.cards_game.model.Event;

public class EventLogger {
    private List<Event> events = Collections.synchronizedList(new ArrayList<>());
    
    public void logEvent(String entityType, String eventType, String description) {
        Event event = new Event(entityType, eventType, description);
        events.add(event);
    }
    
    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }
}
