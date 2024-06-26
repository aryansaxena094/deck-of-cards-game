package com.example.cards_game.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cards_game.model.Event;

@Service
public class EventLogger {
    private final List<Event> events = Collections.synchronizedList(new ArrayList<>());
    
    public void logEvent(String entityType, String eventType, String description) {
        Event event = new Event(entityType, eventType, description);
        events.add(event);
    }
    
    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public List<Event> getEventsByEntityId(String entityId) {
        List<Event> entityEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getEntityId().equals(entityId)) {
                entityEvents.add(event);
            }
        }
        return entityEvents;
    }
}
