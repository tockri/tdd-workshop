package com.example.tdd_workshop.schedule;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
@Table(name = "SCHEDULE")
class ScheduleDao {
    @Id
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