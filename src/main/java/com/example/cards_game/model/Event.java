package com.example.cards_game.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Event {

    @NotNull
    private String entityId;

    @NotNull
    private String eventType;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime timestamp;

    public Event(String entityId, String eventType, String description) {
        this.entityId = entityId;
        this.eventType = eventType;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Event{" +
                "entityId='" + entityId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
