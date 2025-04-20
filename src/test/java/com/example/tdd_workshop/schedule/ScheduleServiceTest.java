package com.example.tdd_workshop.schedule;


import com.example.tdd_workshop.schedule.db.ScheduleRecord;
import com.example.tdd_workshop.schedule.db.ScheduleRepository;
import com.example.tdd_workshop.schedule.schema.ScheduleCreateInput;
import com.example.tdd_workshop.schedule.schema.ScheduleUpdateInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

            assertEquals(2L, schedules.get(1).id());
            assertEquals("title2", schedules.get(1).title());
        }
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
        @DisplayName("指定したIDのScheduleをDBから取得できた場合、Scheduleインスタンスを返す")
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

            // Act
            var exception = assertThrows(
                    ResponseStatusException.class,
                    () -> scheduleService.getScheduleById(1L)
            );

            // Assert
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

        private static final ScheduleRecord mockRecord = new ScheduleRecord(
                1L,
                "oldTitle",
                "oldDesc",
                dt1_2_3_4,
                dt1_2_5_6
        );

        record Param(ScheduleUpdateInput input, ScheduleRecord expected) {
        }

        private static Stream<Param> testParams() {
            return Stream.of(
                    new Param(
                            new ScheduleUpdateInput(
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty()
                            ),
                            mockRecord
                    ),
                    new Param(
                            new ScheduleUpdateInput(
                                    Optional.of("title1"),
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty()
                            ),
                            new ScheduleRecord(
                                    1L,
                                    "title1",
                                    "oldDesc",
                                    dt1_2_3_4,
                                    dt1_2_5_6
                            )
                    ),
                    new Param(
                            new ScheduleUpdateInput(
                                    Optional.empty(),
                                    Optional.of("desc1"),
                                    Optional.empty(),
                                    Optional.empty()
                            ),
                            new ScheduleRecord(
                                    1L,
                                    "oldTitle",
                                    "desc1",
                                    dt1_2_3_4,
                                    dt1_2_5_6
                            )
                    ),
                    new Param(
                            new ScheduleUpdateInput(
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.of(dt2_3_4_5),
                                    Optional.empty()
                            ),
                            new ScheduleRecord(
                                    1L,
                                    "oldTitle",
                                    "oldDesc",
                                    dt2_3_4_5,
                                    dt1_2_5_6
                            )
                    ),
                    new Param(
                            new ScheduleUpdateInput(
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.empty(),
                                    Optional.of(dt2_3_6_7)
                            ),
                            new ScheduleRecord(
                                    1L,
                                    "oldTitle",
                                    "oldDesc",
                                    dt1_2_3_4,
                                    dt2_3_6_7
                            )
                    )
            );
        }

        @ParameterizedTest
        @DisplayName("指定したIDのScheduleが存在する場合に、指定レコードの引数で渡されたフィールドだけを更新する")
        @MethodSource("testParams")
        void updateScheduleSuccessfully(Param param) {
            // Arrange
            when(scheduleRepository.findById(1L)).thenReturn(Optional.of(mockRecord));

            // Act
            scheduleService.updateSchedule(1L, param.input);

            // Assert
            verify(scheduleRepository, times(1)).save(param.expected);
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


    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("createSchedule()")
    class CreateScheduleTest {
        @Mock
        private ScheduleRepository scheduleRepository;
        @InjectMocks
        private ScheduleService scheduleService;

        private static final ScheduleRecord mockRecord = new ScheduleRecord(
                1L,
                "title1",
                "desc1",
                dt1_2_3_4,
                dt1_2_5_6
        );

        @Test
        @DisplayName("ScheduleCreateInputを元にScheduleRecordを生成し、Repositoryに保存する")
        void createScheduleSuccessfully() {
            // Arrange
            when(scheduleRepository.save(any())).thenReturn(mockRecord);

            // Act
            scheduleService.createSchedule(new ScheduleCreateInput(
                    "title1",
                    "desc1",
                    dt1_2_3_4,
                    dt1_2_5_6
            ));

            // Assert
            verify(scheduleRepository, times(1)).save(new ScheduleRecord(
                    null,
                    "title1",
                    "desc1",
                    dt1_2_3_4,
                    dt1_2_5_6
            ));
        }
    }
}
