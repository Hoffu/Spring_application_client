package com.example.client;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Consultation {
    @NonNull
    private String id;
    @NonNull
    private String location;
    @NonNull
    private String group;
    @NonNull
    private LocalDateTime date;
    @NonNull
    private String description;
    private LocalDateTime created;
    private LocalDateTime modified;
    private boolean completed;

    public Consultation() {
        LocalDateTime date = LocalDateTime.now();
        this.id = UUID.randomUUID().toString();
        this.created = date;
        this.modified = date;
    }

    public Consultation(String description, String location, String group, LocalDateTime date) {
        this();
        this.description = description;
        this.location = location;
        this.group = group;
        this.date = date;
    }
}

