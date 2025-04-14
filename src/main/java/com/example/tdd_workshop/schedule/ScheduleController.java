package com.example.tdd_workshop.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;


    @GetMapping("")
    public ResponseEntity<List<Schedule>> getSchedules() {
        var schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public Schedule getScheduleById(@Validated @PathVariable Long id) {
        var found = scheduleService.getScheduleById(id);
        if (found.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "schedule %d not found".formatted(id));
        }
        return found.get();
    }
}
