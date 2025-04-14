package com.example.tdd_workshop.schedule;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ScheduleServiceTest {
    @Nested
    class ToModelTest {
        @Test
        @DisplayName("ScheduleEntityのすべての内容をコピーしたScheduleを生成する")
        void makeScheduleSuccessfully() {
            // Arrange
            var entity = new ScheduleDao(
                    100001L,
                    "test title",
                    "test description",
                    LocalDateTime.of(2025, 2, 12, 9, 5),
                    LocalDateTime.of(2025, 2, 12, 10, 5)
            );

            // Act
            var model = ScheduleService.toModel(entity);

            // Assert
            assertEquals(100001L, model.getId());
            assertEquals("test title", model.getTitle());
            assertEquals("test description", model.getDescription());
            assertEquals(LocalDateTime.of(2025, 2, 12, 9, 5), model.getStartTime());
            assertEquals(LocalDateTime.of(2025, 2, 12, 10, 5), model.getEndTime());
        }
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    class GetAllSchedulesTest {
        @Mock
        private ScheduleRepository scheduleRepository;
        @InjectMocks
        private ScheduleService scheduleService;

        @Test
        @DisplayName("Repositoryが返したScheduleEntityのリストをScheduleのリストに変換する")
        void getAllSchedulesSuccessfully() {
            // Arrange
            when(scheduleRepository.findAll()).thenReturn(java.util.List.of(
                    new ScheduleDao(
                            1L,
                            "title1",
                            "desc1",
                            LocalDateTime.of(2025, 1, 2, 3, 4),
                            LocalDateTime.of(2025, 1, 2, 5, 6)
                    ),
                    new ScheduleDao(
                            2L,
                            "title2",
                            "desc2",
                            LocalDateTime.of(2025, 2, 3, 4, 5),
                            LocalDateTime.of(2025, 2, 3, 6, 7)
                    )
            ));

            // Act
            var schedules = scheduleService.getAllSchedules();

            // Assert
            assertEquals(2, schedules.size());
            assertEquals(1L, schedules.get(0).getId());
            assertEquals("title1", schedules.get(0).getTitle());
            assertEquals("desc1", schedules.get(0).getDescription());
            assertEquals(LocalDateTime.of(2025, 1, 2, 3, 4), schedules.get(0).getStartTime());
            assertEquals(LocalDateTime.of(2025, 1, 2, 5, 6), schedules.get(0).getEndTime());
            assertEquals(2L, schedules.get(1).getId());
            assertEquals("title2", schedules.get(1).getTitle());
            assertEquals("desc2", schedules.get(1).getDescription());
            assertEquals(LocalDateTime.of(2025, 2, 3, 4, 5), schedules.get(1).getStartTime());
            assertEquals(LocalDateTime.of(2025, 2, 3, 6, 7), schedules.get(1).getEndTime());
        }
    }
}
