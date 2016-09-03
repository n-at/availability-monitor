package ru.doublebyte.availabilitymonitor.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.doublebyte.availabilitymonitor.entities.TestResult;

import java.util.List;

public interface TestResultRepository extends CrudRepository<TestResult, Long> {

    TestResult findFirstByMonitoringIdOrderByCreatedAtDesc(Long monitoringId);

    List<TestResult> findByMonitoringIdOrderByCreatedAtDesc(Long monitoringId, Pageable page);
}
