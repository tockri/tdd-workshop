package com.example.tdd_workshop.schedule;

import com.example.tdd_workshop.schedule.db.ScheduleRecord;
import com.example.tdd_workshop.schedule.db.ScheduleRepository;
import com.example.tdd_workshop.schedule.schema.Schedule;
import com.example.tdd_workshop.schedule.schema.ScheduleCreateInput;
import com.example.tdd_workshop.schedule.schema.ScheduleUpdateInput;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleService::toSchema)
                .toList();
    }

    public Schedule getScheduleById(Long id) {
        var record = findRecord(id);
        return toSchema(record);
    }

    public Schedule createSchedule(ScheduleCreateInput input) {
        throw new NotImplementedException("Not implemented yet.");
    }

    @Transactional
    public void updateSchedule(Long id, ScheduleUpdateInput input) {
        var record = findRecord(id);
        scheduleRepository.save(toUpdateRecord(record, input));
    }

    @Transactional
    public void deleteSchedule(Long id) {
        var record = findRecord(id);
        scheduleRepository.deleteById(record.id());
    }

    private ScheduleRecord findRecord(Long id) {
        var recordOpt = scheduleRepository.findById(id);
        if (recordOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "schedule %d is not found.".formatted(id));
        }
        return recordOpt.get();
    }

    private static Schedule toSchema(ScheduleRecord dao) {
        return new Schedule(
                dao.id(),
                dao.title(),
                dao.description(),
                dao.endTime(),
                dao.startTime()
        );
    }

    private static ScheduleRecord toUpdateRecord(ScheduleRecord orig, ScheduleUpdateInput input) {
        return new ScheduleRecord(
                orig.id(),
                input.title().orElse(orig.title()),
                input.description().orElse(orig.description()),
                input.startTime().orElse(orig.startTime()),
                input.endTime().orElse(orig.endTime())
        );
    }
}
