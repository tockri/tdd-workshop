package com.example.tdd_workshop.schedule;

import lombok.NonNull;

import java.time.LocalDateTime;

public record Schedule(@NonNull Long id, @NonNull String title, @NonNull String description,
                       @NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime) {
}
