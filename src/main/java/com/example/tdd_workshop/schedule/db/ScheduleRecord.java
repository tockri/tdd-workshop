package com.example.tdd_workshop.schedule.db;

import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * ScheduleテーブルのDAO
 */
@Table(name = "SCHEDULE")
public record ScheduleRecord(
        @Id
        Long id,
        @NonNull
        String title,
        @NonNull
        String description,
        @NonNull
        @Column("start_time")
        LocalDateTime startTime,
        @NonNull
        @Column("end_time")
        LocalDateTime endTime
) {
}