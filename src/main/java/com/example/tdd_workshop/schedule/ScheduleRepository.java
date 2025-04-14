package com.example.tdd_workshop.schedule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleDao, Long> {
}
