package com.example.tdd_workshop.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        var all = scheduleRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .map(ScheduleService::toModel).toList();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id).map(ScheduleService::toModel);
    }

    public Schedule createSchedule(ScheduleInput input) {
        var dao = scheduleRepository.save(toDao(null, input));
        return toModel(dao);
    }

    public void updateSchedule(Long id, ScheduleInput input) {
        scheduleRepository.save(toDao(id, input));
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
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

    static ScheduleDao toDao(Long id, ScheduleInput input) {
        return new ScheduleDao(
                id,
                input.title(),
                input.description(),
                input.startTime(),
                input.endTime()
        );
    }
}
