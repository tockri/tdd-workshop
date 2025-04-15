package com.example.tdd_workshop.schedule;


import com.example.tdd_workshop.schedule.db.ScheduleRecord;
import com.example.tdd_workshop.schedule.db.ScheduleRepository;
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
                    new ScheduleRecord(1L, "title1", "desc1", dt1_2_3_4, dt1_2_5_6),
                    new ScheduleRecord(2L, "title2", "desc2", dt2_3_4_5, dt2_3_6_7)
            ));

            // Act
            var schedules = scheduleService.getAllSchedules();

            // Assert
            assertEquals(2, schedules.size());
            assertEquals(1L, schedules.get(0).id());
            assertEquals("title1", schedules.get(0).title());
            assertEquals("desc1", schedules.get(0).description());
            assertEquals(2L, schedules.get(1).id());
            assertEquals("title2", schedules.get(1).title());
            assertEquals("desc2", schedules.get(1).description());
        }
    }
}
