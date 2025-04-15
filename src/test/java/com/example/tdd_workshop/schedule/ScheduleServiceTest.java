package com.example.tdd_workshop.schedule;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ScheduleServiceTest {
    private static final LocalDateTime dt1_2_3_4 = LocalDateTime.of(2025, 1, 2, 3, 4);
    private static final LocalDateTime dt1_2_5_6 = LocalDateTime.of(2025, 1, 2, 5, 6);
    private static final LocalDateTime dt2_3_4_5 = LocalDateTime.of(2025, 2, 3, 4, 5);
    private static final LocalDateTime dt2_3_6_7 = LocalDateTime.of(2025, 2, 3, 6, 7);

    @Nested
    class ToModelTest {
        @Test
        @DisplayName("ScheduleEntityのすべての内容をコピーしたScheduleを生成する")
        void makeScheduleSuccessfully() {
            // Arrange
            var dao = new ScheduleDao(100001L, "test title", "test description", dt1_2_3_4, dt1_2_5_6);

            // Act
            var model = ScheduleService.toModel(dao);

            // Assert
            assertEquals(100001L, model.id());
            assertEquals("test title", model.title());
            assertEquals("test description", model.description());
            assertEquals(dt1_2_3_4, model.startTime());
            assertEquals(dt1_2_5_6, model.endTime());
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
            when(scheduleRepository.findAll()).thenReturn(List.of(
                    new ScheduleDao(1L, "title1", "desc1", dt1_2_3_4, dt1_2_5_6),
                    new ScheduleDao(2L, "title2", "desc2", dt2_3_4_5, dt2_3_6_7)
            ));

            // Act
            var schedules = scheduleService.getAllSchedules();

            // Assert
            assertEquals(2, schedules.size());
            assertEquals(1L, schedules.get(0).id());
            assertEquals("title1", schedules.get(0).title());
            assertEquals("desc1", schedules.get(0).description());
            assertEquals(dt1_2_3_4, schedules.get(0).startTime());
            assertEquals(dt1_2_5_6, schedules.get(0).endTime());
            assertEquals(2L, schedules.get(1).id());
            assertEquals("title2", schedules.get(1).title());
            assertEquals("desc2", schedules.get(1).description());
            assertEquals(dt2_3_4_5, schedules.get(1).startTime());
            assertEquals(dt2_3_6_7, schedules.get(1).endTime());
        }
    }
}
