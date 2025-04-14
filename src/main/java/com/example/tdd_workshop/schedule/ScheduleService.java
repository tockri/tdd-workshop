package com.example.tdd_workshop.schedule;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAllSchedules() {
        var all = scheduleRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .map(ScheduleService::toModel).toList();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id).map(ScheduleService::toModel);
    }

    static Schedule toModel(ScheduleDao dao) {
        return new Schedule(
                dao.getId(),
                dao.getTitle(),
                dao.getDescription(),
                dao.getStartTime(),
                dao.getEndTime()
        );
    }

    static ScheduleDao toDao(Schedule schedule) {
        return new ScheduleDao(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getDescription(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}
