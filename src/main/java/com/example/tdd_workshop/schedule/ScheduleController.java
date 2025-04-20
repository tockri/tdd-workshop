package com.example.tdd_workshop.schedule;

import com.example.tdd_workshop.schedule.schema.Schedule;
import com.example.tdd_workshop.schedule.schema.ScheduleCreateInput;
import com.example.tdd_workshop.schedule.schema.ScheduleUpdateInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;


    @Operation(
            summary = "スケジュール一覧の取得",
            description = "設定されているスケジュールをすべて返します。"
    )
    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<List<Schedule>> getSchedules() {
        var schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }


    @Operation(
            summary = "スケジュールの取得",
            description = "指定したIDのスケジュールを返します。"
    )
    @GetMapping(path = "/{id}", produces = "application/json")
    public Schedule getScheduleById(@Validated @PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }


    @Operation(
            summary = "スケジュールの作成",
            description = "新しいスケジュールを作成します。",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "スケジュールの作成に成功した場合"
                    )
            }
    )
    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Schedule> createSchedule(@Validated @RequestBody ScheduleCreateInput input) {
        var created = scheduleService.createSchedule(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @Operation(
            summary = "スケジュールの更新",
            description = "指定したIDのスケジュールを更新します。",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "スケジュールの更新に成功した場合"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "指定したIDのスケジュールが存在しない場合"
                    )
            }
    )
    @PatchMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateSchedule(@Validated @PathVariable Long id,
                                               @Validated @RequestBody ScheduleUpdateInput input) {
        scheduleService.updateSchedule(id, input);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "スケジュールの削除",
            description = "指定したIDのスケジュールを削除します。",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "スケジュールの削除に成功した場合"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "指定したIDのスケジュールが存在しない場合"
                    )
            }
    )
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Void> deleteSchedule(@Validated @PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

}
