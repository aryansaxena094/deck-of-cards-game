package com.example.cards_game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cards_game.model.Event;
import com.example.cards_game.service.EventLogger;

@RestController
@RequestMapping("/events")
public class EventController {
    
    @Autowired
    private EventLogger eventLogger;

    @GetMapping
    public ResponseEntity<List<Event>> getEvents() {
        List<Event> events = eventLogger.getEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{entityId}")
    public ResponseEntity<List<Event>> getEventsByEntityId(@PathVariable String entityId) {
        List<Event> events = eventLogger.getEventsByEntityId(entityId);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
