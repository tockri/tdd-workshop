package com.example.tdd_workshop.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@Slf4j
public class GoogleScheduleService {
    private final String googleScheduleUrl;
    private final RestTemplate restTemplate;

    public GoogleScheduleService(RestTemplateBuilder restTemplateBuilder, @Value("${google.schedule.url:http://example.com}") String googleScheduleUrl) {
        restTemplate = restTemplateBuilder.build();
        this.googleScheduleUrl = googleScheduleUrl;
    }


    /**
     * Sends a new schedule to Google Schedule.
     *
     * @param title the title of the schedule
     * @param startTime the start time of the schedule
     * @param endTime the end time of the schedule
     */
    @Retryable(retryFor = RestClientException.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public void postNewSchedule(String title, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("start send");
        var request = new GoogleSchedulePostRequest(title, startTime, endTime);
        var response = restTemplate.postForEntity(googleScheduleUrl + "/schedules", request, Void.class);
        log.info("send success: {}", response);
    }


}



