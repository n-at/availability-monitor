package ru.doublebyte.availabilitymonitor.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.doublebyte.availabilitymonitor.entities.TestResult;

import java.time.LocalDateTime;
import java.util.List;

public interface TestResultRepository extends CrudRepository<TestResult, Long> {

    TestResult findFirstByMonitoringIdOrderByCreatedAtDesc(Long monitoringId);

    List<TestResult> findByMonitoringIdOrderByCreatedAtDesc(Long monitoringId, Pageable page);

    @Transactional
    @Modifying
    @Query("delete from TestResult t where t.createdAt < ?1")
    int deleteOlder(LocalDateTime localDateTime);

}
