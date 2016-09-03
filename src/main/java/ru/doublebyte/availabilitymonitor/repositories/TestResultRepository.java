package ru.doublebyte.availabilitymonitor.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.doublebyte.availabilitymonitor.types.TestResult;

public interface TestResultRepository extends CrudRepository<TestResult, Long> {

}
