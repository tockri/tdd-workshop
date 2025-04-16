package com.example.tdd_workshop.schedule;


import com.example.tdd_workshop.schedule.db.ScheduleRecord;
import com.example.tdd_workshop.schedule.db.ScheduleRepository;
import com.example.tdd_workshop.schedule.schema.ScheduleUpdateInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("ScheduleService")
public class ScheduleServiceTest {
    private static final LocalDateTime dt1_2_3_4 = LocalDateTime.of(2025, 1, 2, 3, 4);
    private static final LocalDateTime dt1_2_5_6 = LocalDateTime.of(2025, 1, 2, 5, 6);
    private static final LocalDateTime dt2_3_4_5 = LocalDateTime.of(2025, 2, 3, 4, 5);
    private static final LocalDateTime dt2_3_6_7 = LocalDateTime.of(2025, 2, 3, 6, 7);


    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("getAllSchedules()")
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

        @Nested
        @ExtendWith(MockitoExtension.class)
        @DisplayName("getScheduleById()")
        class GetScheduleByIdTest {
            @Mock
            private ScheduleRepository scheduleRepository;
            @InjectMocks
            private ScheduleService scheduleService;

            @Test
            @DisplayName("指定したIDのScheduleを取得する")
            void getScheduleByIdSuccessfully() {
                // Arrange
                when(scheduleRepository.findById(1L)).thenReturn(Optional.of(
                        new ScheduleRecord(1L, "title1", "desc1", dt1_2_3_4, dt1_2_5_6)
                ));

                // Act
                var schedule = scheduleService.getScheduleById(1L);

                // Assert
                assertEquals(1L, schedule.id());
                assertEquals("title1", schedule.title());
                assertEquals("desc1", schedule.description());
            }

            @Test
            @DisplayName("指定したIDのScheduleが存在しない場合、例外をスローする")
            void getScheduleByIdNotFound() {
                // Arrange
                when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                var exception = assertThrows(
                        ResponseStatusException.class,
                        () -> scheduleService.getScheduleById(1L)
                );
                assertEquals("404 NOT_FOUND \"schedule 1 is not found.\"", exception.getMessage());
            }
        }


        @Nested
        @ExtendWith(MockitoExtension.class)
        @DisplayName("updateSchedule()")
        class UpdateScheduleTest {
            @Mock
            private ScheduleRepository scheduleRepository;
            @InjectMocks
            private ScheduleService scheduleService;

            @Test
            @DisplayName("指定したIDのScheduleのtitle, description, startTime, endTimeを更新する")
            void updateScheduleSuccessfully() {
                // Arrange
                var input = new ScheduleUpdateInput(
                        Optional.of("title1"),
                        Optional.of("desc1"),
                        Optional.empty(),
                        Optional.empty());
                when(scheduleRepository.findById(1L)).thenReturn(Optional.of(
                        new ScheduleRecord(
                                1L,
                                "oldTitle",
                                "oldDesc",
                                dt1_2_3_4,
                                dt1_2_5_6
                        )
                ));
                var captor = ArgumentCaptor.forClass(ScheduleRecord.class);

                // Act
                scheduleService.updateSchedule(1L, input);

                // Assert
                verify(scheduleRepository).save(captor.capture());
                var calledArg = captor.getValue();
                assertEquals(1L, calledArg.id());
                assertEquals("title1", calledArg.title());
                assertEquals("desc1", calledArg.description());
                assertEquals(dt1_2_3_4, calledArg.startTime());
                assertEquals(dt1_2_5_6, calledArg.endTime());
            }

            @Test
            @DisplayName("指定したIDのScheduleが存在しない場合、例外をスローする")
            void updateScheduleNotFound() {
                // Arrange
                var input = new ScheduleUpdateInput(
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                );
                when(scheduleRepository.findById(1L)).thenReturn(Optional.empty());

                // Act & Assert
                var exception = assertThrows(
                        ResponseStatusException.class,
                        () -> scheduleService.updateSchedule(1L, input)
                );
                verify(scheduleRepository, never()).save(any());
                assertEquals("404 NOT_FOUND \"schedule 1 is not found.\"", exception.getMessage());
            }
        }
    }

}
