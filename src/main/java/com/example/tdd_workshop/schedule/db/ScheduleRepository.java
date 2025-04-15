package com.example.tdd_workshop.schedule.db;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ScheduleテーブルのRepository
 */
@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleRecord, Long> {
    @NonNull
    List<ScheduleRecord> findAll();
}
