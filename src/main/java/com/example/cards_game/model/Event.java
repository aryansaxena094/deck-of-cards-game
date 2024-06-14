package com.example.cards_game.model;

import java.time.LocalDateTime;

public class Event {
    private String entityId;
    private String eventType;
    private String description;
    private LocalDateTime timestamp;

    public Event(String entityId, String eventType, String description) {
        this.entityId = entityId;
        this.eventType = eventType;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return "Event{" +
                "entityId='" + entityId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
