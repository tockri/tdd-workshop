package com.example.tdd_workshop.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(
        components = GoogleScheduleService.class,
        properties = "google.schedule.url=http://localhost"
)
@EnableRetry
class GoogleScheduleServiceTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    GoogleScheduleService service;

    @Test
    @DisplayName("postNewScheduleがRestClientを使ってリクエストを送信する")
    void postNewSchedule_sendsRequest() {
        // Arrange
        // /schedulesエンドポイントに対するPOSTリクエストをモック
        server.expect(requestTo("http://localhost/schedules"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Schedule"))
                .andExpect(jsonPath("$.startTime").value("2023-10-01T10:00:00"))
                .andExpect(jsonPath("$.endTime").value("2023-10-01T11:00:00"))
                .andRespond(withSuccess());
        // Act
        service.postNewSchedule("Test Schedule", LocalDateTime.of(2023, 10, 1, 10, 0), LocalDateTime.of(2023, 10, 1, 11, 0));
        // Assert
        server.verify();

    }

    @Test
    @DisplayName("/schedulesエンドポイントが500エラーの場合、リトライする")
    void postNewSchedule_throwsOnRestClientError() {
        // Arrange
        for (int i = 0; i < 5; i++) {
            // /schedulesエンドポイントに対するPOSTリクエストをモック
            server.expect(requestTo("http://localhost/schedules"))
                    .andExpect(method(HttpMethod.POST))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.title").value("Test Schedule"))
                    .andExpect(jsonPath("$.startTime").value("2023-10-01T10:00:00"))
                    .andExpect(jsonPath("$.endTime").value("2023-10-01T11:00:00"))
                    .andRespond(withServerError().body("Internal Server Error").contentType(MediaType.TEXT_PLAIN));
        }

        // Act & Assert
        var ex = assertThrows(Exception.class, () -> {
            service.postNewSchedule("Test Schedule", LocalDateTime.of(2023, 10, 1, 10, 0), LocalDateTime.of(2023, 10, 1, 11, 0));
        });

        // Verify that the server was called
        assert ex.getMessage().contains("Internal Server Error");
        server.verify();

    }
}