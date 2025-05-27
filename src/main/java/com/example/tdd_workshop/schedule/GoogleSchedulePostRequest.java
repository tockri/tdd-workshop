package com.example.tdd_workshop.schedule;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class GoogleSchedulePostRequest {
    public final String title;
    public final LocalDateTime startTime;
    public final LocalDateTime endTime;
}
