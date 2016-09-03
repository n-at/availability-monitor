package ru.doublebyte.availabilitymonitor.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.availabilitymonitor.repositories.TestResultDifferenceRepository;
import ru.doublebyte.availabilitymonitor.types.TestResultDifference;

public class TestResultDifferenceManager {

    private static final Logger logger = LoggerFactory.getLogger(TestResultDifferenceManager.class);

    private TestResultDifferenceRepository testResultDifferenceRepository;

    ///////////////////////////////////////////////////////////////////////////

    public TestResultDifferenceManager(
            TestResultDifferenceRepository testResultDifferenceRepository
    ) {
        this.testResultDifferenceRepository = testResultDifferenceRepository;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Add new test result difference
     * @param testResultDifference
     * @return Status of operation
     */
    public boolean add(TestResultDifference testResultDifference) {
        try {
            testResultDifference = testResultDifferenceRepository.save(testResultDifference);
            logger.info("Added test result difference with id {}", testResultDifference.getId());
        } catch (Exception e) {
            logger.error("An error occurred while adding test result difference", e);
            return false;
        }
        return true;
    }

}
