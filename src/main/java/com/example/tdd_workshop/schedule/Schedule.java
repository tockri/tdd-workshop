package com.example.tdd_workshop.schedule;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class Schedule {
    @NonNull
    Long id;
    @NonNull
    String title;
    @NonNull
    String description;
    @NonNull
    LocalDateTime startTime;
    @NonNull
    LocalDateTime endTime;
}
