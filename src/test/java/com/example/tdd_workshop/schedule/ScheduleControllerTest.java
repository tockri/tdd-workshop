package com.example.tdd_workshop.schedule;

import com.example.tdd_workshop.schedule.schema.Schedule;
import com.example.tdd_workshop.schedule.schema.ScheduleCreateInput;
import com.example.tdd_workshop.schedule.schema.ScheduleUpdateInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(ScheduleController.class)
@DisplayName("ScheduleController")
public class ScheduleControllerTest {
    @Autowired
    private MockMvcTester mvcTester;

    @MockitoBean
    private ScheduleService scheduleService;

    @Nested
    @DisplayName("GET /schedules")
    class GetSchedulesTest {
        @Test
        @DisplayName("ServiceがScheduleのリストを返したらJSON形式で返す")
        void willReturnSchedules() {
            // Arrange
            var schedules = List.of(
                    new Schedule(1L, "title1", "desc1", LocalDateTime.of(2025, 1, 2, 3, 4), LocalDateTime.of(2025, 1, 2, 5, 6)),
                    new Schedule(2L, "title2", "desc2", LocalDateTime.of(2025, 2, 3, 4, 5), LocalDateTime.of(2025, 2, 3, 6, 7))
            );
            when(scheduleService.getAllSchedules()).thenReturn(schedules);

            // Act
            var response = mvcTester.get().uri("/schedules").accept("application/json").exchange().getResponse();

            // Assert
            verify(scheduleService, times(1)).getAllSchedules();
            assertEquals(200, response.getStatus());
            assertDoesNotThrow(() -> JSONAssert.assertEquals("""
                    [
                      {
                        "id": 1,
                        "title": "title1",
                        "description": "desc1",
                        "startTime": "2025-01-02T03:04:00",
                        "endTime": "2025-01-02T05:06:00"
                      },
                      {
                        "id": 2,
                        "title": "title2",
                        "description": "desc2",
                        "startTime": "2025-02-03T04:05:00",
                        "endTime": "2025-02-03T06:07:00"
                      }
                    ]
                    """, response.getContentAsString(), false));
        }
    }

    @Nested
    @DisplayName("GET /schedules/{id}")
    class GetScheduleByIdTest {
        @Test
        @DisplayName("ServiceがScheduleを返したらJSON形式で返す")
        void willReturnSchedule() {
            // Arrange
            var schedule = new Schedule(1L, "title1", "desc1", LocalDateTime.of(2025, 1, 2, 3, 4), LocalDateTime.of(2025, 1, 2, 5, 6));
            when(scheduleService.getScheduleById(1L)).thenReturn(schedule);

            // Act
            var response = mvcTester.get().uri("/schedules/1").accept("application/json").exchange().getResponse();

            // Assert
            verify(scheduleService, times(1)).getScheduleById(1L);
            assertEquals(200, response.getStatus());
            assertDoesNotThrow(() -> JSONAssert.assertEquals("""
                    {
                      "id": 1,
                      "title": "title1",
                      "description": "desc1",
                      "startTime": "2025-01-02T03:04:00",
                      "endTime": "2025-01-02T05:06:00"
                    }
                    """, response.getContentAsString(), false));
        }

        @Test
        @DisplayName("ServiceがScheduleを返さなかったら404を返す")
        void willReturnNotFound() {
            // Arrange
            when(scheduleService.getScheduleById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

            // Act
            var response = mvcTester.get().uri("/schedules/1").accept("application/json").exchange().getResponse();

            // Assert
            verify(scheduleService, times(1)).getScheduleById(1L);
            assertEquals(404, response.getStatus());
        }

        @Test
        @DisplayName("idが数値以外の場合400を返す")
        void willReturnBadRequest() {
            // Act
            var response = mvcTester.get().uri("/schedules/abc").accept("application/json").exchange().getResponse();

            // Assert
            assertEquals(400, response.getStatus());
        }
    }

    @Nested
    @DisplayName("POST /schedules")
    class CreateScheduleTest {
        private static final Schedule GARBAGE_SCHEDULE = new Schedule(1L, "", "", LocalDateTime.MIN, LocalDateTime.MAX);
        private static final String GARBAGE_REQUEST = """
                {
                  "title": "garbage title",
                  "description": "garbage description",
                  "startTime": "2025-01-02T03:04:00",
                  "endTime": "2025-01-02T05:06:00"
                }
                """;

        @Test
        @DisplayName("リクエストボディからScheduleCreateInputを生成し、Serviceに渡す")
        void willCallService() {
            // Arrange
            when(scheduleService.createSchedule(any())).thenReturn(GARBAGE_SCHEDULE);

            // Act
            mvcTester.post().uri("/schedules")
                    .contentType("application/json")
                    .content("""
                            {
                              "title": "title1",
                              "description": "desc1",
                              "startTime": "2025-01-02T03:04:00",
                              "endTime": "2025-01-02T05:06:00"
                            }
                            """)
                    .accept("application/json")
                    .exchange();

            // Assert
            verify(scheduleService, times(1)).createSchedule(eq(new ScheduleCreateInput(
                    "title1",
                    "desc1",
                    LocalDateTime.of(2025, 1, 2, 3, 4),
                    LocalDateTime.of(2025, 1, 2, 5, 6)
            )));
        }

        @Test
        @DisplayName("ServiceがScheduleを返したらJSON形式で返す")
        void willReturnCreatedSchedule() {
            // Arrange
            var schedule = new Schedule(1L, "title1", "desc1", LocalDateTime.of(2025, 1, 2, 3, 4), LocalDateTime.of(2025, 1, 2, 5, 6));
            when(scheduleService.createSchedule(any())).thenReturn(schedule);

            // Act
            var response = mvcTester.post().uri("/schedules")
                    .contentType("application/json")
                    .content(GARBAGE_REQUEST)
                    .accept("application/json")
                    .exchange()
                    .getResponse();

            // Assert
            verify(scheduleService, times(1)).createSchedule(any());
            assertEquals(201, response.getStatus());
            assertDoesNotThrow(() -> JSONAssert.assertEquals("""
                    {
                      "id": 1,
                      "title": "title1",
                      "description": "desc1",
                      "startTime": "2025-01-02T03:04:00",
                      "endTime": "2025-01-02T05:06:00"
                    }
                    """, response.getContentAsString(), false));
        }

        private static Stream<String> badRequestBodies() {
            return Stream.of(
                    """
                            {
                              "title": "",
                              "description": "",
                              "startTime": "",
                              "endTime": ""
                            }
                            """,
                    """
                            {
                              "title": "title1",
                              "description": "desc1",
                              "startTime": "2025-01-02T03:04:00",
                              "endTime": "2025-01"
                            }
                            """,
                    """
                            {
                              "description": "desc1",
                              "startTime": "2025-01-02T03:04:00",
                              "endTime": "not a date"
                            }
                            """,
                    """
                            {
                              "title": "",
                              "startTime": "2025-01-02T03:04:00",
                              "endTime": "2025-01-02T05:06:00"
                            }
                            """);
        }

        @ParameterizedTest
        @DisplayName("リクエストボディが不正な場合400を返す")
        @MethodSource("badRequestBodies")
        void willReturnBadRequest(String requestBody) {
            // Act
            var response = mvcTester.post().uri("/schedules")
                    .contentType("application/json")
                    .content(requestBody)
                    .accept("application/json")
                    .exchange()
                    .getResponse();

            // Assert
            assertEquals(400, response.getStatus());
        }
    }

    @Nested
    @DisplayName("PATCH /schedules/{id}")
    class UpdateScheduleTest {
        private record Param(String body, ScheduleUpdateInput expected) {
        }

        /**
         * willCallServiceのバリエーション
         */
        private static Stream<Param> willCallServiceArgsSource() {
            return Stream.of(
                    new Param("""
                            {
                              "title": "title1",
                              "description": "desc1",
                              "startTime": "2025-01-02T03:04:00",
                              "endTime": "2025-01-02T05:06:00"
                            }
                            """,
                            new ScheduleUpdateInput(
                                    Optional.of("title1"),
                                    Optional.of("desc1"),
                                    Optional.of(LocalDateTime.of(2025, 1, 2, 3, 4)),
                                    Optional.of(LocalDateTime.of(2025, 1, 2, 5, 6))
                            )
                    ),
                    new Param("""
                            {
                              "description": "desc1",
                              "startTime": "2025-01-02T03:04:00"
                            }
                            """,
                            new ScheduleUpdateInput(
                                    Optional.empty(),
                                    Optional.of("desc1"),
                                    Optional.of(LocalDateTime.of(2025, 1, 2, 3, 4)),
                                    Optional.empty()
                            )
                    ),
                    new Param("""
                            {
                              "endTime": "2025-01-02T05:06:00"
                            }
                            """,
                            new ScheduleUpdateInput(
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.of(LocalDateTime.of(2025, 1, 2, 5, 6))
                            )
                    )
            );
        }

        @ParameterizedTest
        @DisplayName("リクエストボディからScheduleUpdateInputを生成し、Serviceに渡す")
        @MethodSource("willCallServiceArgsSource")
        void willCallService(Param param) {
            // Arrange
            doNothing().when(scheduleService).updateSchedule(any(), any());

            // Act
            mvcTester.patch().uri("/schedules/1")
                    .contentType("application/json")
                    .content(param.body)
                    .accept("application/json")
                    .exchange();

            // Assert
            verify(scheduleService, times(1)).updateSchedule(eq(1L), eq(param.expected));
        }


        @Test
        @DisplayName("Service#updateScheduleが成功したら204を返す")
        void willSuccess() {
            // Arrange
            doNothing().when(scheduleService).updateSchedule(any(), any());

            // Act
            var response = mvcTester.patch().uri("/schedules/1")
                    .contentType("application/json")
                    .content("""
                            {
                              "title": "title1",
                            }
                            """)
                    .accept("application/json")
                    .exchange()
                    .getResponse();

            // Assert
            verify(scheduleService, times(1)).updateSchedule(any(), any());
            assertEquals(204, response.getStatus());
        }

        @Test
        @DisplayName("リクエストボディが不正な場合400を返す")
        void willReturnBadRequest() {
            // Act
            var response = mvcTester.patch().uri("/schedules/1")
                    .contentType("application/json")
                    .content("""
                            {
                              "startTime": "not a date"
                            }
                            """)
                    .accept("application/json")
                    .exchange()
                    .getResponse();

            // Assert
            assertEquals(400, response.getStatus());
        }

        @Test
        @DisplayName("idが数値以外の場合400を返す")
        void willReturnBadRequestForInvalidId() {
            // Act
            var response = mvcTester.patch().uri("/schedules/abc")
                    .contentType("application/json")
                    .content("""
                            {
                              "startTime": "2025-01-02T03:04:00"
                            }
                            """)
                    .accept("application/json")
                    .exchange()
                    .getResponse();

            // Assert
            assertEquals(400, response.getStatus());
        }
    }
}
