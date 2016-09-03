package ru.doublebyte.availabilitymonitor.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.doublebyte.availabilitymonitor.types.TestResultDifference;

import java.util.List;

public interface TestResultDifferenceRepository extends CrudRepository<TestResultDifference, Long> {

    List<TestResultDifference> findByResultMonitoringIdOrderByResultCreatedAtDesc(Long monitoringId, Pageable pageable);

}
