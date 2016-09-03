package ru.doublebyte.availabilitymonitor.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.doublebyte.availabilitymonitor.types.TestResultDifference;

public interface TestResultDifferenceRepository extends CrudRepository<TestResultDifference, Long> {

}
